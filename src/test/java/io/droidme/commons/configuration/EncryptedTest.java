/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.droidme.commons.configuration;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 *
 * @author droidme
 */
public class EncryptedTest {
    
    
    @Test
    public void encrypt() {
        String encrypted = _encrypt("password", "key");
        System.out.println("Encryped: " + encrypted);
        assertNotNull(encrypted);
    }
    
    private String _encrypt(String password, String key) {
        return password;
    }
    
}
