package io.droidme.commons.crypt;

import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author ga2merf
 */
@RunWith(Arquillian.class)
public class CrypterIT {

    @Inject
    Crypter crypter;

    @Deployment
    public static WebArchive create() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackages(true, "io.droidme.commons")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void injected() {
        System.out.println(crypter.getClass());
        assertNotNull(crypter);
    }

    @Test
    public void enrypt() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String encrypt = crypter.encrypt("Encrypt this text, please.");
        System.out.println("Encrypted: " + encrypt);
        assertNotNull(encrypt);
    }

}
