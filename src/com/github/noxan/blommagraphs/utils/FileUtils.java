package com.github.noxan.blommagraphs.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class FileUtils {
    public static void writeFile(String pathName, String text) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(pathName));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        writer.write(text);
        writer.close();
        fos.close();
    }

    public static String readFile(String pathName) throws IOException {
        FileInputStream fis = new FileInputStream(new File(pathName));

        StringBuilder string = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        while ((line = reader.readLine()) != null) {
            string.append(line + "\n");
        }

        reader.close();
        fis.close();

        return string.toString();
    }
}
