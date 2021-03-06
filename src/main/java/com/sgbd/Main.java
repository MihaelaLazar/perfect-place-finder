package com.sgbd;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Main {
//
//    public static void main(String[] args) throws Exception {
//        String plainText = "Hello World";
//        SecretKey secKey = getSecretEncryptionKey();
//        byte[] cipherText = encryptText(plainText, secKey);
//        String decryptedText = decryptText(cipherText, secKey);
//
//        System.out.println("Original Text:" + plainText);
//        System.out.println("AES Key (Hex Form):"+bytesToHex(secKey.getEncoded()));
//        System.out.println("Encrypted Text (Hex Form):" + bytesToHex(cipherText));
//        System.out.println("Decrypted Text:" + decryptedText);
//
//        String keyAfter = Base64.getEncoder().encodeToString(secKey.getEncoded());
//        System.out.println("After key(string form) : " + keyAfter);
//        // decode the base64 encoded string
//        byte[] decodedKey = Base64.getDecoder().decode(keyAfter);
//        // rebuild key using SecretKeySpec
//        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//        System.out.println("After key(hex form) : " + bytesToHex(originalKey.getEncoded()));
//
//
//    }


    public static void main(String[] args) {
       Test t = new Test();
    }


    /**
     * gets the AES encryption key. In your actual programs, this should be safely
     * stored.
     * @return
     * @throws Exception
     */
    public static SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }

    /**
     * Encrypts plainText in AES using the secret key
     * @param plainText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }

    /**
     * Decrypts encrypted byte array using the key used for encryption.
     * @param byteCipherText
     * @param secKey
     * @return
     * @throws Exception
     */
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }

    /**
     * Convert a binary byte array into readable hex form
     * @param hash
     * @return
     */
    private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }


}
 class Test {
    // Declaratii de variabile statice
    static int x = 0, y, z;
    // Bloc static de initializare
    static {
        System.out.println("Initializare clasa...");
        int t=1;
        y = 2;
        z = x + y + t;
    }
    public Test() {

    }
}
