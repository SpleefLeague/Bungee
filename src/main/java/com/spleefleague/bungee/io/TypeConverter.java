/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spleefleague.bungee.io;

import org.bson.Document;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jonas
 * @param <T>
 * @param <V>
 */
public abstract class TypeConverter<T, V> {

    public abstract V convertLoad(T t);

    public abstract T convertSave(V v);

    //Some common TypeConverters
    public static class UUIDStringConverter extends TypeConverter<String, UUID> {

        @Override
        public String convertSave(UUID t) {
            return t.toString();
        }

        @Override
        public UUID convertLoad(String v) {
            return UUID.fromString(v);
        }
    }

    public static class DateConverter extends TypeConverter<Date, Date> {

        @Override
        public Date convertSave(Date t) {
            return t;
        }

        @Override
        public Date convertLoad(Date v) {
            return v;
        }
    }

    public static class MapConverter extends TypeConverter<List, Map<String, Object>> {

        @Override
        public Map<String, Object> convertLoad(List t) {
            try {
                Map<String, Object> map = new HashMap<>();
                for (Document doc : (List<Document>) t) {
                    String key = doc.get("value", String.class);
                    Class c = Class.forName(doc.get("class", String.class));
                    map.put(key, EntityBuilder.load(doc, c));
                }
                return map;
            } catch (ClassNotFoundException ex) {
                //Error handling
                Logger.getLogger(TypeConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public List convertSave(Map<String, Object> v) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
