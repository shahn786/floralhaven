package com.example.floralhaven.utilities;

import android.util.Base64;

import com.example.floralhaven.entities.CartItem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class CommonMethods {

    public static List<CartItem> cartItemsList = new ArrayList<>();

    public static String hashPassword(String password) {
        try {
            return Base64.encodeToString(MessageDigest.getInstance("SHA-256").digest(password.getBytes()), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {}
        return null;
    }
}
