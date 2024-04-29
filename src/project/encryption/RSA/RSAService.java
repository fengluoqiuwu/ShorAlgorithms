package project.encryption.RSA;

import project.encryption.EncryptionService;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.util.Base64;

public class RSAService implements EncryptionService {
    private int keySize=1024;
    private KeyPair keyPair;

    public RSAService() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        keyPair = keyPairGenerator.generateKeyPair();
    }
    @Override
    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }
    @Override
    public void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        keyPair = keyPairGenerator.generateKeyPair();
    }
    @Override
    public String getPublicKey(){
        return keyPair.getPublic().toString();
    }
    @Override
    public String getPrivateKey(){
        return keyPair.getPrivate().toString();
    }

    @Override
    public String encrypt(String plainText) throws Exception {
        PublicKey publicKey = keyPair.getPublic();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] plaintextBytes = plainText.getBytes();
        int blockSize = keySize/8-11;
        int blocks = (plaintextBytes.length + blockSize - 1) / blockSize;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < blocks; i++) {
            int start = i * blockSize;
            int length = Math.min(plaintextBytes.length - start, blockSize);
            outputStream.write(cipher.doFinal(plaintextBytes, start, length));
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
    @Override
    public String decrypt(String cipherText) throws Exception {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] encryptedBytes=Base64.getDecoder().decode(cipherText);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int blockSize = keySize/8;
        int blocks = encryptedBytes.length / blockSize;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < blocks; i++) {
            int start = i * blockSize;
            outputStream.write(cipher.doFinal(encryptedBytes, start, blockSize));
        }
        return outputStream.toString();
    }
}
