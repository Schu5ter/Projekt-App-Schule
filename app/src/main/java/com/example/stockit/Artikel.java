package com.example.stockit;

public class Artikel {

    String artikelId;
    String artikelname;
    String number;


    public Artikel(){

    }

    public Artikel(String artikelId, String artikelname, String number) {
        this.artikelId = artikelId;
        this.artikelname = artikelname;
        this.number = number;
    }

    public String getArtikelId() {
        return artikelId;
    }

    public String getArtikelname() {
        return artikelname;
    }

    public String getNumber() {
        return number;
    }
}
