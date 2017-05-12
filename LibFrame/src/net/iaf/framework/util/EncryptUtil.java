package net.iaf.framework.util;

import android.annotation.SuppressLint;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptUtil {

    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    // MD5的引入
    public static String MD5(String inStr) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // //System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = inStr.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    @SuppressLint("TrulyRandom")
    public static String encryptDES(String encryptString, String encryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.substring(0, 8).getBytes());
            SecretKeySpec key = new SecretKeySpec(encryptKey.substring(0, 8).getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
            return Base64.encode(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        byte[] byteMi = Base64.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.substring(0, 8).getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData);
    }

    public static void encodeStream(InputStream inputStream, OutputStream outputStream, String encryptKey)
            throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(encryptKey.substring(0, 8).getBytes(), "DES");
        Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] buffer = new byte[1024];
        CipherInputStream cin = new CipherInputStream(inputStream, c);
        int i;
        while ((i = cin.read(buffer)) != -1) {
            outputStream.write(buffer, 0, i);
        }
        outputStream.close();
        cin.close();
    }

    // 解密  
    public static void decodeStream(InputStream inputStream, OutputStream outputStream, String decryptKey)
            throws Exception {

        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
        Cipher c = Cipher.getInstance("DES/CBC/PKCS5Padding");
        // 用密钥初始化此 cipher  
        c.init(Cipher.DECRYPT_MODE, key, zeroIv);

        byte[] buffer = new byte[1024];
        CipherOutputStream cout = new CipherOutputStream(outputStream, c);
        int i;
        while ((i = inputStream.read(buffer)) != -1) {
            cout.write(buffer, 0, i);
        }
        cout.close();
        inputStream.close();
    }
//	public static byte[] encryptDES(byte[] encryptBytes, String encryptKey) {
//		try{
//			IvParameterSpec zeroIv = new IvParameterSpec(encryptKey.substring(0, 8).getBytes());
//			SecretKeySpec key = new SecretKeySpec(encryptKey.substring(0, 8).getBytes(), "DES");
//			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
//			byte[] encryptedData = cipher.doFinal(encryptBytes);
//			return encryptedData;
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//		return null;
//	}
//	
//	public static  byte[]  decryptDES( byte[]  decryptBytes, String decryptKey) throws Exception {
//		IvParameterSpec zeroIv = new IvParameterSpec(iv);
//		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
//		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
//		byte decryptedData[] = cipher.doFinal(decryptBytes);
//		return decryptedData;
//	}
}
