package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class KlijentServerThread extends Thread {
    private Socket socket;
    private BufferedReader in;
    private OutputStream out;



    KlijentServerThread(Socket socket, BufferedReader in, OutputStream out) {
        this.socket = socket;
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {

        try {                                   // slanje imena i slike objekta Riba klijentu
            System.out.println("Prijava");
            Statistika.plusPosecenost();
            Statistika.plusTrPosecenost();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();
            out.write((Riba.getListaVrsta().size() + "\n").getBytes());
            for (Riba riba:Riba.getListaVrsta()) {
                out.write((riba.getIme() + "\n").getBytes());
                out.write((riba.getImeJpgFajla()+ "\n").getBytes());
            }

            out.write((Statistika.getProsecnaOcena() + "\n").getBytes());    // slanje Statistike klijentu
            for (Riba riba : Statistika.getTop5Najjeftiniji()) {
                out.write((riba.toString() + "\n").getBytes());
            }
            for (Riba riba : Statistika.getTop5NajproRtnRiba()) {
                out.write((riba.toString() + "\n").getBytes());
            }

            while (true){
                String zahtev = in.readLine().toLowerCase();

                if(zahtev.equals("kraj")) {
                    Statistika.minusTrPosecenost();
                    break;
                }
                if(zahtev.equals("podacivrste")){
                    String trazenaVrsta = in.readLine();
                    Riba trazenaRiba = Riba.nadji(trazenaVrsta);
                    String ime = trazenaRiba.getIme(), naucnoIme = trazenaRiba.getNaucnoIme(), temperament = trazenaRiba.getTemperament()
                            ,ishrana = trazenaRiba.getIshrana(), ph = trazenaRiba.getPh(), temperatura = trazenaRiba.getTemperatura()
                            ,velicinaAkvarijuma = trazenaRiba.getVelicinaAkvarijuma() + "", velicinaRibe = trazenaRiba.getVelicinaRibe(),
                            cena = trazenaRiba.getCena() + "", jpg = trazenaRiba.getImeJpgFajla();
                    out.write((naucnoIme + "\n").getBytes());
                    out.write((temperament + "\n").getBytes());
                    out.write((ishrana + "\n").getBytes());
                    out.write((ph + "\n").getBytes());
                    out.write((temperatura + "\n").getBytes());
                    out.write((cena + "\n").getBytes());
                    out.write((velicinaAkvarijuma + "\n").getBytes());
                    out.write((velicinaRibe + "\n").getBytes());
                    out.write((jpg + "\n").getBytes());
                }
                if(zahtev.equals("porudzbina")){
                    String kupljeneStvari = new Kupac(in.readLine(),in.readLine(),in.readLine(),
                            in.readLine().replaceAll("/","\n")).getKupljeneStvari();
                    Riba.racunajBrProdatih(kupljeneStvari);
                    Statistika.plusBrojOcena();
                    Statistika.plusZbirOcena(Integer.parseInt(in.readLine()));
                    Statistika.plusUkupnaZarada(Integer.parseInt(in.readLine()));

                }

            }
            in.close();     out.close();
            System.out.println("odjava");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
