package io.droidme.commons.crypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author droidme
 */
public class CrypterTest {

    Crypter cut;

    @Before
    public void init() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cut = new Crypter();
        cut.init();
    }

    @Test
    public void generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey key = keyGen.generateKey();
        String encodedKey = Base64.getEncoder()
                .encodeToString(key.getEncoded());
        assertNotNull(key);
    }

    @Test
    public void encrypt() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String encrypt = cut.encrypt("password");
        System.out.println("password -> " + encrypt);
    }

    @Test
    public void viceversa() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String encrypted = cut.encrypt("password");
        String decrypted = cut.decrypt(encrypted);
        assertThat(decrypted, is("password"));
    }

}
