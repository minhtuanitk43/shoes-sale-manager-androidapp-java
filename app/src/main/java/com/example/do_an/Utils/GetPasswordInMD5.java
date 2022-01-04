package com.example.do_an.Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class GetPasswordInMD5 {
    public static String getPasswordInMD5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] pwByteArr = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (byte b : pwByteArr) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString().toUpperCase(Locale.ROOT);
    }
}
