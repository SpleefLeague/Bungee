package com.spleefleague.bungee.util;

import com.mongodb.MongoCredential;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Josh on 09/08/2016.
 */
public class Config {

    //Assumed default values
    public static String DB_HOST = "mongo.spleefleague.com";
    public static int DB_PORT = 27017;
    private static HashMap<String, String> ADDITIONAL_CONFIG;

    static {
        ADDITIONAL_CONFIG = new HashMap<>();
    }

    public static void loadConfig() {
        if (!new File("db.conf").exists()) {
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader("db.conf"));
            String s;
            while ((s = br.readLine()) != null) {
                if (!s.startsWith("#") && s.contains(":")) {
                    while (s.startsWith(" ")) {
                        s = s.replaceAll(" ", "");
                    }
                    String[] command = s.split(":");
                    if (command.length < 2) {
                        continue;
                    }
                    if (command[0].equalsIgnoreCase("host")) {
                        DB_HOST = command[1];

                    } else if (command[0].equalsIgnoreCase("port")) {
                        DB_PORT = Integer.valueOf(command[1]);

                    } else {
                        ADDITIONAL_CONFIG.put(command[0], command[1]);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean hasKey(String key) {
        return ADDITIONAL_CONFIG.containsKey(key);
    }

    public static String getString(String key) {
        return (String) ADDITIONAL_CONFIG.get(key);
    }

    public static int getInteger(String key) {
        return Integer.parseInt(ADDITIONAL_CONFIG.get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(ADDITIONAL_CONFIG.get(key));
    }

    public static List<MongoCredential> getCredentials() {
        List<MongoCredential> credentials = new ArrayList<>();
        Iterator<String> i = ADDITIONAL_CONFIG.keySet().iterator();
        while (i.hasNext()) {
            String key = i.next();
            if (key.startsWith("pw.")) {
                MongoCredential credential = MongoCredential.createCredential("plugin", key.substring(3), getString(key).toCharArray());
                credentials.add(credential);
            }
        }
        return credentials;
    }
}
