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
    
    public static final String CIPHER = "AES/ECB/PKCS5PADDING";
    public static final String KEY = "n43RbHusT/uHwC5rFYrOIA==";
    
    public Cipher cipher;
    
    @PostConstruct
    private void init() throws NoSuchAlgorithmException, 
            NoSuchPaddingException {
        cipher = Cipher.getInstance(Crypter.CIPHER);
    }
    
    public byte[] encrypt(String text) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytes = text.getBytes();
        byte[] decodedKey = Base64.getDecoder().decode(Crypter.KEY);
        SecretKey secretKey = new SecretKeySpec(
                decodedKey, 0, decodedKey.length, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] bytesEncrypted = cipher.doFinal(bytes);
        return bytesEncrypted;
    }
    
}
