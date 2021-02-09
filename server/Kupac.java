package server;

import java.util.ArrayList;

public class Kupac {
    private String id, ime, adresa, brojTel, kupljeneStvari;
    private static int brojKupaca = 0;
    private static volatile ArrayList<Kupac> listaKupaca = new ArrayList<>();

    public Kupac(String ime, String adresa, String brojTel, String kupljeneStvari) {
        this.id = brojKupaca + "";
        this.ime = ime;
        this.adresa = adresa;
        this.brojTel = brojTel;
        this.kupljeneStvari = kupljeneStvari;
        brojKupaca++;
        listaKupaca.add(this);
    }

    public String getId() {
        return id;
    }

    public static ArrayList<Kupac> getListaKupaca() {
        return listaKupaca;
    }

    public static void brisiListaKupaca(int i) {
       listaKupaca.remove(i);
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getBrojTel() {
        return brojTel;
    }

    public void setBrojTel(String brojTel) {
        this.brojTel = brojTel;
    }

    public String getKupljeneStvari() {
        return kupljeneStvari;
    }

    public void setKupljeneStvari(String kupljeneStvari) {
        this.kupljeneStvari = kupljeneStvari;
    }

    @Override
    public String toString() {
        return "ID Kupca: " + this.id;
    }
}
