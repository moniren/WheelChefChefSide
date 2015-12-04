package com.wheelchef.wheelchefchef.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lyk on 12/4/2015.
 */
public class MD5Generator {
    public static String getMD5hash(String input){
        byte[] digested = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            digested = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        return digested.toString();
    }
}
