package server;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import obradaSlika.SlikeIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerKontroler implements Initializable {

    @FXML TextField ime,naucnoIme,temperatura,ph,temperament,ishrana,cena,putanjaSlike,velicinaRibe,velicinaAkvarijuma;
    @FXML Label labelIme, labelAdresa, labelTelefon, labelPrOcena, labelPosecenost, labelTrPosecenost, labelaZarada;
    @FXML Button potvrdiNovaRiba, buttonBrisiSaListe;
    @FXML TextArea textAreaNaruceno;
    @FXML ListView<Kupac> listaKupaca;
    @FXML ListView<Riba> top5Jef;
    @FXML ListView<String> top5Pop;
    @FXML Pane paneDodajSliku;

    public void azurirajListuKupaca(){
        listaKupaca.getItems().setAll(Kupac.getListaKupaca());
    }

    public void azurirajStatistiku(){
        Platform.runLater(() -> {
            labelPosecenost.setText(Statistika.getPosecenost() + "");
            labelPrOcena.setText(Statistika.getProsecnaOcena() + "");
            labelTrPosecenost.setText(Statistika.getTrenutnaPosecenost() + "");
            labelaZarada.setText(Statistika.getUkupnaZarada() + "");
            top5Jef.getItems().setAll(Statistika.getTop5Najjeftiniji());
            top5Pop.getItems().setAll(Statistika.getTop5Najprodavaniji());
        });
    }

    public void dodajSliku(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        File file= dragboard.getFiles().get(0);
        dragboard.getFiles().clear();
        try {
            BufferedImage image = ImageIO.read(file);
            ImageIO.write(image,"jpg",new File("src\\server\\slikeGalerija\\" + file.getName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert obavestenje = new Alert(Alert.AlertType.CONFIRMATION);
        obavestenje.setHeaderText(file.getName());
        obavestenje.setTitle("Obavestenje");
        obavestenje.setContentText("Slika je ubacena u galeriju");
        obavestenje.showAndWait();
    }

    public void setOnDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.LINK);
        }
    }

    public void dragPutanjaSlike(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        String putanja = dragboard.getFiles().get(0).getAbsolutePath();
        dragboard.getFiles().clear();
        putanjaSlike.setText(putanja);
    }

    public void brisiSaListeKupaca(){
        int indeks = listaKupaca.getSelectionModel().getSelectedIndex();
        if(indeks != -1){
            Kupac.brisiListaKupaca(indeks);
            azurirajListuKupaca();
            labelIme.setText("");
            labelAdresa.setText("");
            labelTelefon.setText("");
            textAreaNaruceno.setText("");
        }

    }

    public void izaberiSaListe(){
        int i = listaKupaca.getSelectionModel().getSelectedIndex();
        if(i != -1){
            Kupac kupac = Kupac.getListaKupaca().get(i);
            labelIme.setText(kupac.getIme());
            labelAdresa.setText(kupac.getAdresa());
            labelTelefon.setText(kupac.getBrojTel());
            textAreaNaruceno.setText(kupac.getKupljeneStvari());
        }

    }

    public void praviObjekatRiba(){
        if(!(ime.getText().equals("")||naucnoIme.getText().equals("")||temperament.getText().equals("")||ishrana.getText().equals("")||
        velicinaAkvarijuma.getText().equals("")||ph.getText().equals("")||temperatura.getText().equals("")||
                velicinaRibe.getText().equals("")||cena.getText().equals(""))){
            String apsolutnaPutanjaSlike = putanjaSlike.getText();
            String imeJpgDatoteke = new File(apsolutnaPutanjaSlike).getName();
            try {
                BufferedImage image = ImageIO.read(new File(apsolutnaPutanjaSlike));
                ImageIO.write(image,"jpg",new File("src\\server\\slike\\" + imeJpgDatoteke));
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Riba(ime.getText(),naucnoIme.getText(),temperament.getText(),ishrana.getText(),Integer.parseInt(velicinaAkvarijuma.getText()),
                    ph.getText(),temperatura.getText(),velicinaRibe.getText(),Integer.parseInt(cena.getText()),imeJpgDatoteke) ;
        }
        else {
            Alert obavestenje = new Alert(Alert.AlertType.WARNING);
            obavestenje.setHeaderText("Nisu popunjena sva polja!");
            obavestenje.setTitle("Prazna polja");
            obavestenje.setContentText("Popuniti prazna polja");
            obavestenje.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Riba.generatorRiba();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ServerSocket serverSocket= null;
                Socket socket= null;
                BufferedReader in = null;
                OutputStream out = null;
                try{
                    serverSocket = new ServerSocket(7777);
                    while(true){
                        SlikeIO.slanjeBrojaTransfera(serverSocket,"slike");
                        for(String imeDatoteke:new File("src\\server\\slike").list()) {
                            SlikeIO.slanjeSlike("src\\server\\slike\\" + imeDatoteke,imeDatoteke,serverSocket);
                        }
                        SlikeIO.slanjeBrojaTransfera(serverSocket,"slikeGalerija");
                        for(String imeDatoteke:new File("src\\server\\slikeGalerija").list()) {
                            SlikeIO.slanjeSlike("src\\server\\slikeGalerija\\" + imeDatoteke,imeDatoteke,serverSocket);
                        }
                        socket = serverSocket.accept();
                        new KlijentServerThread(socket, in, out).start();

                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        new Thread(task).start();

        new Thread(() -> {          /// azuriranje statistike i liste kupaca na 10 sec
            while (true){
                azurirajListuKupaca();
                azurirajStatistiku();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



}
