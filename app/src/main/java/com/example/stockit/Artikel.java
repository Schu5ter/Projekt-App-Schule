package com.example.stockit;

public class Artikel {

    private String artikelId;
    private String artikelname;
    private String number;
    private String userId;


    public Artikel(){

    }



    public Artikel( String userId, String artikelId, String artikelname, String number) {
        this.artikelId = artikelId;
        this.artikelname = artikelname;
        this.number = number;
        this.userId = userId;
    }

    public String getArtikelId() {
        return artikelId;
    }

    public String getUserId() { return userId; }

    public String getArtikelname() {
        return artikelname;
    }

    public String getNumber() {
        return number;
    }
}
