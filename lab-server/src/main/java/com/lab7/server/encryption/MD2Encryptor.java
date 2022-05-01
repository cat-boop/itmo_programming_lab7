package com.lab7.server.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD2Encryptor implements IEncryptor {

    @Override
    public String encrypt(String messageToEncrypt) throws NoSuchAlgorithmException {
        final int radix = 16;
        final int stringLength = 32;

        MessageDigest md = MessageDigest.getInstance("MD2");
        byte[] messageDigest = md.digest(messageToEncrypt.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        StringBuilder hashText = new StringBuilder(no.toString(radix));
        while (hashText.length() < stringLength) {
            hashText.insert(0, "0");
        }
        return hashText.toString();
    }
}
