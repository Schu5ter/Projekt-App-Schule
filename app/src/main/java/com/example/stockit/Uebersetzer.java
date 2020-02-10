package com.example.stockit;

import java.io.*;
import java.net.*;
import java.util.regex.*;


public class Uebersetzer {


    public static void main (String args[]) {
        String apiToken = "9df0d6de4820b1c9baa8270ac23161"; // your private API access token
        String ean = "5000112546415"; // search for this EAN code
        String Name = "unknown";

        try {
            URL url= new URL("https://api.ean-search.org/api?token="
                    + apiToken + "&op=barcode-lookup&ean=" + ean);
            InputStream is = url.openStream();
            int ptr = 0;

            StringBuffer apiResult = new StringBuffer();
            while ((ptr = is.read()) != -1) {
                apiResult.append((char)ptr);
            }
            // you can use a real XML parser,
            // but a regular expression is much faster
            Pattern p = Pattern.compile("(.*)");
            Matcher m = p.matcher(apiResult);
            if (m.find()) {
                Name = m.group(1);
                System.out.println(Name);
            }
        } catch ( Exception ex ) {
            System.out.println("test");
            ex.printStackTrace();
        }
        System.out.println("EAN " + ean + " => " + Name);
    }
}