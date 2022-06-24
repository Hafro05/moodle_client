/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moodle;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
/**
 *
 * @author TCHAZEU
 */
public class HashPassword {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    /*public static void main(String[] args) throws Exception {
       
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a password in plain text : ");
        System.out.println("");
        String data = sc.nextLine();
        String algorithm = "SHA-256";
        System.out.println("Password hashed: "+generateHash(data,algorithm));
    }*/
    
     public static String generateHash(String data , String algorithm) throws NoSuchAlgorithmException{
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.reset();
        byte[] hash = digest.digest(data.getBytes());
        return bytesToStringHex(hash);
    }
    
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
      
    public static String bytesToStringHex(byte[] bytes){
        char[] hexChars = new char[bytes.length*2];
        for(int j=0; j<bytes.length; j++){
            int v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v >>> 4];
            hexChars[j*2+1] = hexArray[v & 0x0F];
        }
        
       return new String(hexChars);
    }
}
   