package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Riba implements Comparable<Riba>{
    private String ime,naucnoIme,temperament,ishrana,ph,temperatura,velicinaRibe;
    private int velicinaAkvarijuma,cena,brojProdatih;
    private String imeJpgFajla;
    private static ArrayList<Riba> listaVrsta = new ArrayList<Riba>();

    Riba(String ime, String naucnoIme, String temperament, String ishrana, int velicinaAkvarijuma, String ph,
         String temperatura, String velicinaRibe, int cena, String imeJpgFajla) {
        this.ime = ime;
        this.naucnoIme = naucnoIme;
        this.temperament = temperament;
        this.ishrana = ishrana;
        this.velicinaAkvarijuma = velicinaAkvarijuma;
        this.ph = ph;
        this.temperatura = temperatura;
        this.velicinaRibe = velicinaRibe;
        this.cena = cena;
        this.imeJpgFajla = imeJpgFajla;
        this.brojProdatih = 0;
        listaVrsta.add(this);
        Collections.sort(listaVrsta);
    }

    static Riba nadji(String ime){
        Riba returnRiba = null;
        for (Riba riba:listaVrsta) {
            if(riba.getIme().toLowerCase().equals(ime)){
                returnRiba = riba;
                break;
            }
        }
        return returnRiba;
    }

    static ArrayList<Riba> getListaVrsta() {
        return listaVrsta;
    }

    String getIme() {
        return ime;
    }

    String getTemperament() {
        return temperament;
    }

    String getIshrana() {
        return ishrana;
    }

    String getNaucnoIme() {
        return naucnoIme;
    }

    int getVelicinaAkvarijuma() {
        return velicinaAkvarijuma;
    }

    String getImeJpgFajla() {
        return imeJpgFajla;
    }

    String getPh() {
        return ph;
    }

    String getTemperatura() {
        return temperatura;
    }

    String getVelicinaRibe() {
        return velicinaRibe;
    }

    int getCena() {
        return cena;
    }

    int getBrojProdatih() {
        return brojProdatih;
    }

    public static void racunajBrProdatih(String s){
        String[] niz = s.split("\n");
        for (String string: niz) {
            String[] ime_broj = string.replaceFirst("\\(.+\\) \\*","").split(" ");
            for (int i = 0; i < listaVrsta.size(); i++) {
                if(listaVrsta.get(i).ime.equals(ime_broj[0])){
                    listaVrsta.get(i).brojProdatih += Integer.parseInt(ime_broj[1]);
                    System.out.println(listaVrsta.get(i).brojProdatih);
                    break;
                }
            }

        }
    }

    static void generatorRiba(){
        new Riba("Arowana", "Arowanas","Agresivna", "Mesojed", 300,
                "6-7,2","25C-28 °C","50cm",12000,"arowana.jpg");
        new Riba("Skalar", "Pterophyllum","Srednje agresivan", "Svastojed",50,
                "6-7,2","24C-30 °C","15cm",400,"skalar.jpg");
        new Riba("Diskus", "Symphysodon","Miroljubiv",
            "Svastojed",150,"6-7,2","26C-30 °C","25cm",1200,"diskus.jpg");
        new Riba("Khuli Khuli", "Pangio kuhlii","Miroljubiv", "Svastojed,  muljar",30,
                "6-7,2","24C-30 °C","8cm - 10cm",400,"khuli.jpg");
        new Riba("Neonka", "Paracheirodon","Jatne, miroljubive", "Svastojed",30,
            "6-7,2","22C-29 °C","5cm",200,"neonka.jpg");
        new Riba("Borac", "Betta splendens","Agresivna", "Svastojed",30,
                "6-7,2","22C-24 °C","7cm",500,"borac.jpg");
        new Riba("Patuljasta razbora", "Boraras maculatus","Jatne, miroljubive", "Svastojed",30,
                "4"," 20 – 28 °C","2cm - 2.5cm",200,"dwarfRazbora.jpg");
        new Riba("Endler gupi", " Poecilia wingei","Miroljubiva", "Svastojed",30,
                "6.7-8.5","22 - 26 °С","3cm",200,"endler.jpg");
        new Riba("Gupi", "Poecilia reticulata","Miroljubiva", "Svastojed",30,
                "6.7-8.5","22 - 26 °С","6cm",150,"Gupi.jpg");
        new Riba("Ksifo", "Xiphophorus hellerii","Miroljubiva", "Svastojed",30,
                "6.7-8.5","22 - 26 °С","6cm",200,"ksifo.jpg");
        new Riba("Mikrorazbora", " Poecilia wingei","Miroljubiva, jatna", "Svastojed",30,
                "6.5-7.5","20 - 26 °С","3cm",200,"mikrorazbora.jpg");
        new Riba("Moli", "Poecilia sphenops","Miroljubiva", "Svastojed",30,
                "6.7-8.5","22 - 26 °С","6cm",200,"mollyFish.jpg");
        new Riba("Ramirez", "Mikrogeophagus ramirezi","Agresivna", "Svastojed",30,
                "5.5-8","22 - 26 °С","6cm",400,"Ramirezi.jpg");
        new Riba("Razbora", "Trigonostigma Heteromorpha","Miroljubiva,jatna", "Svastojed",30,
                "5.5-8","23 - 28 °С","6cm",150,"Rasbora.jpg");

    }

    static ArrayList<Riba> porediPoCeni(){
        ArrayList<Riba> r = new ArrayList<>(getListaVrsta());
        r.sort(Comparator.comparingInt(Riba::getCena));
       return r;
    }

    static ArrayList<Riba> porediPoProdaji(){
        ArrayList<Riba> r = new ArrayList<>(getListaVrsta());
        r.sort((o1, o2) -> o2.brojProdatih - o1.brojProdatih);
        return r;
    }

    @Override
    public String toString() {
        return this.ime + " " + this.cena + " din.";
    }

    @Override
    public int compareTo(Riba o) {
        return getIme().compareTo(o.getIme());
    }
}
