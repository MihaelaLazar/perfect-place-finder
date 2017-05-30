package com.sgbd.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by Lazarm on 5/16/2017.
 */
public class SecurityUtil {

    public static String[] encryptPassword(String password) throws Exception {
        SecretKey secKey = getSecretEncryptionKey();
        byte[] cipherText = encryptText(password, secKey);
        String passwordToSave = bytesToHex(cipherText);
        String keyToSave = Base64.getEncoder().encodeToString(secKey.getEncoded());
        String passwordAndKey[] = {passwordToSave, keyToSave};



        return passwordAndKey;
    }

    public static SecretKey getSecretKeyFromDB(String dbKey){
        byte[] decodedKey = Base64.getDecoder().decode(dbKey);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//        System.out.println("After key(hex form) : " + bytesToHex(originalKey.getEncoded()));
        return originalKey;
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
    public static byte[] encryptText(String plainText,SecretKey secKey) {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        byte[] byteCipherText = null;
        try {
            Cipher aesCipher = Cipher.getInstance("AES");
            aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
            byteCipherText = aesCipher.doFinal(plainText.getBytes());
            return byteCipherText;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

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
    public static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
}
