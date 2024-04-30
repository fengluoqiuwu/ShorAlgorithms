package project.encryption;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface EncryptionService {
    public void setKeySize(int keySize);
    public void generateKeyPair() throws Exception;
    public String getPublicKey();
    public String getPrivateKey();
    public String encrypt(String plainText) throws Exception;
    public String decrypt(String cipherText) throws Exception;
}
