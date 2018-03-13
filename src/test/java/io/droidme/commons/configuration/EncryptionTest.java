/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.droidme.commons.configuration;

import io.droidme.commons.crypt.Crypter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author droidme
 */
public class EncryptionTest {

    Crypter crypter;

    @Before
    public void init() throws NoSuchAlgorithmException, NoSuchPaddingException {
        crypter = new Crypter();
        crypter.cipher = Cipher.getInstance(Crypter.CIPHER);
    }

    @Test
    @Ignore
    public void generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        String encodedKey = Base64.getEncoder()
                .encodeToString(key.getEncoded());
        System.out.println("key:" + encodedKey);
    }

    @Test
    public void encrypt() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] textEncrypted = crypter.encrypt("password");
        System.out.println("Text Encryted : " + textEncrypted);
    }

}
