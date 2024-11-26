package org.example;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Shorthand shorthand = new Shorthand();
        BufferedReader reader = new BufferedReader(new FileReader("file.txt"));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        line = sb.toString();

        shorthand.code(line);
        shorthand.decode();
    }
}