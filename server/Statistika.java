package server;

import java.util.ArrayList;

class Statistika {
    private static int zbirOcena = 0;
    private volatile static double prosecnaOcena = 0;
    private static int brojOcena = 0;
    private volatile static ArrayList<Riba> top5Najprodavaniji = new ArrayList<>();
    private volatile static ArrayList<Riba> top5Najjeftiniji = new ArrayList<>();
    private volatile static int posecenost = 0;
    private volatile static int trenutnaPosecenost = 0;
    private volatile static int ukupnaZarada = 0;

    synchronized static void plusUkupnaZarada(int i){
        ukupnaZarada += i;
    }

    static int getUkupnaZarada() {
        return ukupnaZarada;
    }

    synchronized static void plusBrojOcena(){
        brojOcena++;
    }

    synchronized static void plusTrPosecenost(){
        trenutnaPosecenost++;
    }

    static int getTrenutnaPosecenost() {
        return trenutnaPosecenost;
    }

    synchronized static void minusTrPosecenost(){
        trenutnaPosecenost--;
    }

    static void plusZbirOcena(int ocena) {
        Statistika.zbirOcena += ocena;
        Statistika.prosecnaOcena = (double) Statistika.zbirOcena/(double)Statistika.brojOcena;
    }

    static double getProsecnaOcena() {
        return prosecnaOcena;
    }

    static ArrayList<String> getTop5Najprodavaniji() {
        ArrayList<String> r = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String s = Riba.porediPoProdaji().get(i).getIme() + " - Prodato: " +
                    Riba.porediPoProdaji().get(i).getBrojProdatih() + " kom.";
            r.add(s);
        }
        return r;
    }

    static ArrayList<Riba> getTop5Najjeftiniji() {
        ArrayList<Riba> r = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            r.add(Riba.porediPoCeni().get(i));
        }
        return r;
    }

    static ArrayList<Riba> getTop5NajproRtnRiba() {
        ArrayList<Riba> r = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            r.add(Riba.porediPoProdaji().get(i));
        }
        return r;
    }

    static int getPosecenost() {
        return posecenost;
    }

    synchronized static void plusPosecenost() {
        Statistika.posecenost += 1;
    }
}
