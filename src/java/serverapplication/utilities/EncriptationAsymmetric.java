/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverapplication.utilities;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ResourceBundle;

/**
 *
 * @author Adrian
 */
public class EncriptationAsymmetric {
    /**
     * Cipher proporciona la funcionalidad de cifrado y descifrado
     */
    private static Cipher cipher;

    private final static String CRYPTO_METHOD = "RSA";
    private final static String OPCION_RSA = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";
    private final static String PUBLIC_PATH = ResourceBundle.getBundle("serverapplication.utilities.KeysProperties").getString("public_key");
    private final static String PRIVATE_PATH = ResourceBundle.getBundle("serverapplication.utilities.KeysProperties").getString("private_key");

    public static String encrypt(String messageToEncrypt) throws Exception {
        InputStream in = null;
        byte[] publicKeyBytes = null;
        in = EncriptationAsymmetric.class.getClassLoader().getResourceAsStream(PUBLIC_PATH);
        publicKeyBytes = new byte[in.available()];
        in.read(publicKeyBytes);
        in.close();

        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        cipher = Cipher.getInstance(OPCION_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(messageToEncrypt.getBytes());
        return toHexadecimal(encrypted);
    }

    public static String decrypt(String encryptedMessage) throws Exception {
        InputStream in = null;
        byte[] privateKeyBytes = null;
        in = EncriptationAsymmetric.class.getClassLoader().getResourceAsStream(PRIVATE_PATH);
        privateKeyBytes = new byte[in.available()];
        in.read(privateKeyBytes);
        in.close();

        KeyFactory keyFactory = KeyFactory.getInstance(CRYPTO_METHOD);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] messageInBytes = cipher.doFinal(hexStringToByteArray(encryptedMessage));
        String message = new String(messageInBytes);
        return message;
    }

    public static String toHexadecimal(byte[] resumen) {
        String HEX = "";
        for (int i = 0; i < resumen.length; i++) {
            String h = Integer.toHexString(resumen[i] & 0xFF);
            if (h.length() == 1) {
                HEX += "0";
            }
            HEX += h;
        }
        return HEX.toUpperCase();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
