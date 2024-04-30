package project.encryption.ECC;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import project.encryption.EncryptionService;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

public class ECCService implements EncryptionService {
    private int keySize=256;
    private KeyPair keyPair=null;

    public ECCService() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        String curveName = "secp"+keySize+"r1";
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curveName);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        keyPair = keyPairGenerator.generateKeyPair();
    }

    @Override
    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    @Override
    public void generateKeyPair() throws Exception{
        String curveName = "secp"+keySize+"r1";
        ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curveName);

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", "BC");
        keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
        keyPair = keyPairGenerator.generateKeyPair();
    }

    @Override
    public String getPublicKey() {
        return ((ECPublicKey) keyPair.getPublic()).toString();
    }

    @Override
    public String getPrivateKey() {
        return ((ECPrivateKey) keyPair.getPrivate()).toString();
    }

    @Override
    public String encrypt(String plainText) throws Exception {
        PublicKey publicKey = keyPair.getPublic();
        byte[] plaintextBytes = plainText.getBytes();

        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        int blockSize = 32;
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

        Cipher cipher = Cipher.getInstance("ECIES", "BC");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int blockSize = getBlockSize();
        int blocks = ( encryptedBytes.length + blockSize - 1) / blockSize;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < blocks; i++) {
            int start = i * blockSize;
            int length = Math.min(encryptedBytes.length - start, blockSize);
            outputStream.write(cipher.doFinal(encryptedBytes, start, length));
        }
        return outputStream.toString();
    }

    private int getBlockSize() {
        return switch (keySize) {
            case 192 -> 101;
            case 224 -> 109;
            case 256 -> 117;
            case 384 -> 149;
            case 521 -> 185;
            default -> 0;
        };
    }
}
