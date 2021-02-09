package klijent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import obradaSlika.SlikeIO;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML Button buttonGalerija, buttonRibice, buttonONama, buttonKorpa, buttonStatistika;
    @FXML ImageView profilnaSlikaRibe;
    @FXML Label labelImeRibe, naucnoIme, temperament, ishrana, ph, temperatura, cena, velicinaRibe, velicinaAkvarijuma;
    @FXML Label labelUkupnaCena, labelOcenaProdavnice;
    @FXML TextField textFImePorucilac,textFAdresa,textFBrTel;
    @FXML Button buttonNazad, buttonStaviUKorpu, buttonIzbrisiSelektovano,buttonNarucite;
    @FXML Button buttonStar1, buttonStar2, buttonStar3, buttonStar4, buttonStar5;
    @FXML FlowPane flowPanelRibice, flowPanelGalerija;
    @FXML AnchorPane panelONama, panelRibice, panelGalerija, panelKorpa, panelStatistika;
    @FXML ScrollPane scrollPanelRibice, scrollPanelRibiceInfo;
    @FXML Spinner spinner;
    @FXML ListView<String> listaKorpa, listaNajprodavanije, listaNajjeftinije;

    private Socket socket = null;
    private BufferedReader in = null;
    private OutputStream out = null;
    private int ukupnaCena = 0;
    private Map<String, Image> map = new HashMap<String, Image>();
    private String ocena = "0";

    // from scroll panel ribice to spr Info
    public void fromSPRtoSPRI(ActionEvent event){
        Button button = (Button) event.getTarget();
        String buttonText = button.getText();
        labelImeRibe.setText(buttonText);
        scrollPanelRibice.setVisible(false);
        scrollPanelRibiceInfo.setVisible(true);
        try {
            out.write("podacivrste\n".getBytes());
            String trazenaVrsta = buttonText.toLowerCase() + "\n";
            System.out.println(trazenaVrsta);
            out.write((trazenaVrsta).getBytes());
            naucnoIme.setText(in.readLine());
            temperament.setText(in.readLine());
            ishrana.setText(in.readLine());
            ph.setText(in.readLine());
            temperatura.setText(in.readLine());
            cena.setText(in.readLine() + " din.");
            velicinaAkvarijuma.setText(in.readLine() +"L");
            velicinaRibe.setText(in.readLine());
            profilnaSlikaRibe.setImage(map.get(in.readLine()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fromSPRItoSPR(){
        scrollPanelRibiceInfo.setVisible(false);
        scrollPanelRibice.setVisible(true);
    }

    public void buttonActionNaruci(){
        if(textFAdresa.getText().equals("")||textFBrTel.getText().equals("")||textFImePorucilac.getText().equals("")){
            Alert obavestenje = new Alert(Alert.AlertType.WARNING);
            obavestenje.setHeaderText("Prazna polja");
            obavestenje.setTitle("Obavestenje");
            obavestenje.setContentText("Morate popuniti sve podatke");
            obavestenje.showAndWait();
        }
        else if(ocena.equals("0")){
            Alert obavestenje = new Alert(Alert.AlertType.WARNING);
            obavestenje.setHeaderText("Niste ocenili prodavnicu");
            obavestenje.setTitle("Obavestenje");
            obavestenje.setContentText("Ocenite prodavnicu");
            obavestenje.showAndWait();
        }
        else if(listaKorpa.getItems().isEmpty()){
            Alert obavestenje = new Alert(Alert.AlertType.WARNING);
            obavestenje.setHeaderText("Prazna korpa");
            obavestenje.setTitle("Obavestenje");
            obavestenje.setContentText("Stavite zeljene ribice u korpu");
            obavestenje.showAndWait();
        }
        else{
            String porudzbina = "";
            for (String s:listaKorpa.getItems()) {
                porudzbina += s + "/";
            }
            try {
                out.write("porudzbina\n".getBytes());
                out.write((textFImePorucilac.getText() + "\n").getBytes());
                out.write((textFAdresa.getText() + "\n").getBytes());
                out.write((textFBrTel.getText() + "\n").getBytes());
                out.write((porudzbina + "\n").getBytes());
                out.write((ocena + "\n").getBytes());
                out.write((ukupnaCena + "\n").getBytes());

                Alert obavestenje = new Alert(Alert.AlertType.CONFIRMATION);
                obavestenje.setHeaderText("Uspesno poruceno!");
                obavestenje.setTitle("Obavestenje");
                obavestenje.setContentText("Hvala na poverenju!");
                obavestenje.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void buttonActionStar(ActionEvent event){
        Button button = (Button) event.getTarget();
        ocena = button.getText();
        buttonStar1.setStyle("-fx-background-color: white");
        buttonStar2.setStyle("-fx-background-color: white");
        buttonStar3.setStyle("-fx-background-color: white");
        buttonStar4.setStyle("-fx-background-color: white");
        buttonStar5.setStyle("-fx-background-color: white");
        ((Button) event.getTarget()).setStyle("-fx-background-color: green");
    }

    public void buttonActionONama(){
        panelStatistika.setVisible(false);
        panelRibice.setVisible(false);
        panelGalerija.setVisible(false);
        panelKorpa.setVisible(false);
        panelONama.setVisible(!panelONama.isVisible());
    }

    public void buttonActionKorpa(){
        panelRibice.setVisible(false);
        panelStatistika.setVisible(false);
        panelGalerija.setVisible(false);
        panelONama.setVisible(false);
        panelKorpa.setVisible(!panelKorpa.isVisible());
    }

    public void buttonActionRibice(){
        panelONama.setVisible(false);
        panelStatistika.setVisible(false);
        panelGalerija.setVisible(false);
        panelKorpa.setVisible(false);
        panelRibice.setVisible(!panelRibice.isVisible());
    }

    public void buttonActionStatistika(){
        panelONama.setVisible(false);
        panelRibice.setVisible(false);
        panelGalerija.setVisible(false);
        panelKorpa.setVisible(false);
        panelStatistika.setVisible(!panelStatistika.isVisible());
    }

    public void buttonActionGalerija(){
        panelKorpa.setVisible(false);
        panelONama.setVisible(false);
        panelStatistika.setVisible(false);
        panelRibice.setVisible(false);
        panelGalerija.setVisible(!panelGalerija.isVisible());
    }

    public void staviUKorpu(){
        String s = labelImeRibe.getText() + "( " + cena.getText() +" ) * " + spinner.getValue();
        ukupnaCena += (Integer.parseInt(cena.getText().replace(" din.","")) * (int)spinner.getValue());
        labelUkupnaCena.setText("" + ukupnaCena);
        listaKorpa.getItems().add(s);
        Alert obavestenje = new Alert(Alert.AlertType.INFORMATION);
        obavestenje.setHeaderText(labelImeRibe.getText() + " (" + spinner.getValue() + " kom.)");
        obavestenje.setTitle("Obavestenje");
        obavestenje.setContentText("Ubaceno u korpu");
        obavestenje.showAndWait();
    }

    public void izbrisiSelektovano(){
        int indeks = listaKorpa.getSelectionModel().getSelectedIndex();
        if(indeks != -1){
            List<String> brojevi = Arrays.asList(listaKorpa.getSelectionModel().getSelectedItem().
                    replaceAll("[^0-9]+", " ").trim().split(" "));
            int oduzmiOdCene = (Integer.parseInt(brojevi.get(0)) * Integer.parseInt(brojevi.get(1)));
            ukupnaCena -= oduzmiOdCene;
            labelUkupnaCena.setText(ukupnaCena + "");
            listaKorpa.getItems().remove(indeks);
        }
    }

    void exit(){
        try {
            out.write("kraj\n".getBytes());
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Integer> vrednosSpinnner = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,50,1);
        this.spinner.setValueFactory(vrednosSpinnner);

        int br;
        try {
            br =  SlikeIO.primanjeBrojaTransfera();
            System.out.println("Slanje slika...Broj slanja " + br);
            for (int i = 0; i < br; i++) {
                Object[] objects = SlikeIO.primanjeSlike();
                map.put((String) objects[0],(Image) objects[1]);
            }

            br = SlikeIO.primanjeBrojaTransfera();
            System.out.println("Slanje slika galerija...Broj slanja " + br);
            for (int i = 0; i < br; i++) {
                Image slikaGalerija = SlikeIO.primanjeSlikeGalerija();
                ImageView view = new ImageView(slikaGalerija);
                view.prefHeight(250); view.prefWidth(250);
                view.maxHeight(250); view.maxWidth(250); view.setPreserveRatio(true); view.setSmooth(true);
                flowPanelGalerija.getChildren().add(view);
            }


            socket = new Socket("127.0.0.1",7777);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("---------------\nPokretanje apl...");

        try {           //primanje imena i imena slike da bi se formirali buttoni na panelu ribice
            int brojac = Integer.parseInt(in.readLine()) ;
            for (int i = 0; i < brojac; i++) {
                String ime = in.readLine();
                Button b = new Button(ime);
                b.prefHeight(150.0); b.prefWidth(150.0); b.setMinHeight(150); b.setMinWidth(150); b.setMnemonicParsing(false);
                b.setContentDisplay(ContentDisplay.TOP);  b.setFont(new Font("System Bold",14.0));
                String imeJpg = in.readLine();
                ImageView view = new ImageView(map.get(imeJpg));
                view.setFitHeight(99.0); view.setFitWidth(129.0); view.setPickOnBounds(true); view.setPreserveRatio(true);
                b.setGraphic(view);
                b.setOnAction(this::fromSPRtoSPRI);
                flowPanelRibice.getChildren().add(b);
            }

            //primanje statistickih podataka
            try{
                String ocena = in.readLine();
                if(ocena.equals("0.0"))
                    ocena = "NEOCENJENO";
                labelOcenaProdavnice.setText(ocena);
                for (int i = 0; i < 5; i++) {
                    listaNajjeftinije.getItems().add(in.readLine());
                }
                for (int i = 0; i < 5; i++) {
                    listaNajprodavanije.getItems().add(in.readLine());
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
