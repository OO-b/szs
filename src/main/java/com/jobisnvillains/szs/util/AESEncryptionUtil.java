//package com.jobisnvillains.szs.util;
//
//
//import javax.crypto.Cipher;
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.util.Base64;
//
//public class AESEncryptionUtil {
//
//    private static final String ALGORITHM = "AES";
//
//
//    public static String encrypt(String plaintext, SecretKey secretKey) throws Exception {
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes());
//        return Base64.getEncoder().encodeToString(encryptedBytes);
//    }
//
//    public static String decrypt(String encryptedText, SecretKey secretKey) throws Exception {
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
//        return new String(decryptedBytes);
//    }
//
//    public static SecretKey generateSecretKey() throws Exception {
//        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
//        return keyGenerator.generateKey();
//    }
//
//    public static boolean matches(String rawPassword, String encodedPassword, SecretKey secretKey) throws Exception {
//        return decrypt(encodedPassword, secretKey).equals(rawPassword);
//    }
//}