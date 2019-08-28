package com.hamait.test;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MCrypt {

   // private static final String iv = "58PsKmmZz6USuB58";
   // private static final String SecretKey = "rcbPuWtG3vBh87J6";



    private IvParameterSpec ivSpec;
    private SecretKeySpec keySpec;
    private Cipher cipher;

    public MCrypt(String iv ,  String SecretKey)
    {
        this.ivSpec = new IvParameterSpec(iv.getBytes());
        this.keySpec = new SecretKeySpec(SecretKey.getBytes(), "AES");

        try {
            this.cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String text) throws Exception
    {
        if(text == null || text.length() == 0)
            throw new Exception("Empty string");

        byte[] encrypted = null;

        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            encrypted = this.cipher.doFinal(MCrypt.padString(text).getBytes());
        } catch (Exception e)
        {
            throw new Exception("[encrypt] " + e.getMessage());
        }

        return encrypted;
    }


    public byte[] decrypt(String code ) throws Exception
    {
        if(code == null || code.length() == 0)
            throw new Exception("Empty string");

        byte[] decrypted = null;

        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivSpec);

            decrypted = this.cipher.doFinal(MCrypt.hexToBytes(code));
        } catch (Exception e)
        {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return decrypted;
    }



    public static String bytesToHex(byte[] data)
    {
        if (data==null)
        {
            return null;
        }

        int len = data.length;
        String str = "";
        for (int i=0; i<len; i++) {
            if ((data[i]&0xFF)<16)
                str = str + "0" + Integer.toHexString(data[i]&0xFF);
            else
                str = str + Integer.toHexString(data[i]&0xFF);
        }
        return str;
    }


    private static byte[] hexToBytes(String str) {
        if (str==null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i=0; i<len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
            }
            return buffer;
        }
    }



    private static String padString(String source)
    {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++)
        {
            source += paddingChar;
        }

        return source;
    }
}
