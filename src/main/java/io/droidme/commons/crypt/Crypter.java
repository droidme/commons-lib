package io.droidme.commons.crypt;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.Singleton;

/**
 *
 * @author ga2merf
 */
@Singleton
public class Crypter {
    
    private static final String CIPHER = "AES/ECB/PKCS5PADDING";
    private static final String KEY = "n43RbHusT/uHwC5rFYrOIA==";
    
    SecretKey secret;
    Cipher cipher;
    
    @PostConstruct
    void init() throws NoSuchAlgorithmException, NoSuchPaddingException {
        this.cipher = Cipher
                .getInstance(Crypter.CIPHER);
        byte[] decodedKey = Base64.getDecoder()
                .decode(Crypter.KEY);
        this.secret = new SecretKeySpec(
                decodedKey, 0, decodedKey.length, "AES");
    }
    
    public String encrypt(String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytes = text.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, this.secret);
        byte[] bytesEncrypted = cipher.doFinal(bytes);
        return Base64.getEncoder()
                .encodeToString(bytesEncrypted);
    }
    
    public String decrypt(String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytes = Base64.getDecoder().decode(text);
        cipher.init(Cipher.DECRYPT_MODE, this.secret);
        byte[] bytesDecrypted = cipher.doFinal(bytes);
        return new String(bytesDecrypted);
    }
    
}
