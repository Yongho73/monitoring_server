package kr.co.bizmarvel.ccdm.component.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class EncryptUtils {

    public static String alg = "AES/CBC/PKCS5Padding";
    private static final String globalKey = "b00728328ac00e07cb41ae38944f3cb6";

    public static String encryptAES256(String text) throws Exception {
        return encryptAES256(text, globalKey);
    }

    public static String encryptAES256(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(key.substring(0, 16).getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptAES256(String cipherText) throws Exception {
        return decryptAES256(cipherText, globalKey);
    }

    public static String decryptAES256(String cipherText, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(alg);
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParamSpec = new IvParameterSpec(key.substring(0, 16).getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);

        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decodedBytes);
        return new String(decrypted, "UTF-8");
    }

}
