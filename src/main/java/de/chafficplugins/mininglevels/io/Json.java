package de.chafficplugins.mininglevels.io;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class Json {
    private static final Gson g = new Gson();

    public static void saveFile(String filename, Object object) throws IOException {
        FileWriter file = new FileWriter(filename);
        file.write(g.toJson(object));
        file.flush();
    }

    private static String getFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }
        String everything = sb.toString();
        br.close();
        return everything;
    }

    public static <T> T loadFile(String jsonFilePath, Type type) throws IOException {
        return g.fromJson(getFile(jsonFilePath), type);
    }
}
