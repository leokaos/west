package org.west.componentes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Classe responsável pela administração das funções de 
 * criptpografia do sistema
 * 
 * @author Leonardo Guerra 
 */
public class CriptoManager {
    
    /**
     * Método responsável por cifrar um determinada string através do 
     * AES, com a chave passada.
     * 
     * @param value a string a ser cifrada
     * @param chave a chave utilizada para cifrar
     * 
     * @return retorno - string resultante da cifração
     */

    public static String cifrar(String value, byte[] chave) {
        String retorno = null;
        try {
            SecretKeySpec spec = new SecretKeySpec(chave, "AES");

            AlgorithmParameterSpec paramSpec = new IvParameterSpec(new byte[16]);

            Cipher cifra = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cifra.init(Cipher.ENCRYPT_MODE, spec,paramSpec);

            byte[] cifrado = cifra.doFinal(value.getBytes());

            retorno = new BASE64Encoder().encode(cifrado);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retorno;
    }
    
    /**
     * Método responsável por decifrar um determinada string através do 
     * AES, com a chave passada.
     * 
     * @param cifra a string a ser decifrada
     * @param chave a chave utilizada para decifrar
     * 
     * @return retorno - string resultante da decifração
     */    

    public static String decifrar(String cifra, byte[] chave) {
        String retorno = null;
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(chave, "AES");

            AlgorithmParameterSpec paramSpec = new IvParameterSpec(new byte[16]);

            byte[] decoded = new BASE64Decoder().decodeBuffer(cifra);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec,paramSpec);
            
            retorno = new String(cipher.doFinal(decoded));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retorno;
    }

     /**
     * Método responsável criar o hash de uma string 
     * com o algoritimo SHA-1
     * 
     * @param value a string a ser hasheada
     * 
     * @return retorno - string resultante do hash
     */
    public static String hash(String value) {
        String retorno = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(value.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());

            retorno = hash.toString(16);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retorno;
    }
}