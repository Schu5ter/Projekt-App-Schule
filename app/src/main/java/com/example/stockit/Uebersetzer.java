package com.example.stockit;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.*;
import java.net.*;
import java.util.regex.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class Uebersetzer {


    public static void main (String args[]) {
        String apiToken = "9df0d6de4820b1c9baa8270ac23161"; // your private API access token
        String ean = "5000112546415"; // search for this EAN code
        String Name = "unknown";

        try {
            URL url= new URL("https://api.ean-search.org/api?token="
                    + apiToken + "&op=barcode-lookup&ean=" + ean);

            URLConnection conn = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            NodeList nodes = doc.getElementsByTagName("product");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                NodeList title = element.getElementsByTagName("name");
                Element line = (Element) title.item(0);
                System.out.println(line.getTextContent());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}

