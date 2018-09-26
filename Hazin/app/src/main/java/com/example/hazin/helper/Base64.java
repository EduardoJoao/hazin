package com.example.hazin.helper;

public class Base64 {

    public static String encodeEmail(String email){
        return android.util.Base64.encodeToString(email.getBytes(), android.util.Base64.DEFAULT).replaceAll("(\\n|\\r)","" );
    }

    public static String decodeEmai(String codificado){
        return new String(android.util.Base64.decode(codificado, android.util.Base64.DEFAULT));
    }
}
