package project.encryption.ElGamal;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.encodings.OAEPEncoding;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import project.encryption.EncryptionService;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

public class ElGamalService implements EncryptionService {
    private int keySize=1024;
    private ElGamalParameters params=null;
    private AsymmetricCipherKeyPair keyPair=null;
    private SecureRandom random=null;

    public ElGamalService() throws NoSuchAlgorithmException {
        random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(keySize, random);
        BigInteger g = new BigInteger("2"); // Generator
        params = new ElGamalParameters(p, g);
        ElGamalKeyPairGenerator generator = new ElGamalKeyPairGenerator();
        generator.init(new ElGamalKeyGenerationParameters(random, params));
        keyPair = generator.generateKeyPair();
    }

    @Override
    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    @Override
    public void generateKeyPair() {
        random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(keySize, random);
        BigInteger g = new BigInteger("2"); // Generator
        params = new ElGamalParameters(p, g);
        ElGamalKeyPairGenerator generator = new ElGamalKeyPairGenerator();
        generator.init(new ElGamalKeyGenerationParameters(random, params));
        keyPair = generator.generateKeyPair();
    }

    @Override
    public String getPublicKey() {
        return ((ElGamalPublicKeyParameters) keyPair.getPublic()).getY().toString();
    }

    @Override
    public String getPrivateKey() {
        return ((ElGamalPrivateKeyParameters) keyPair.getPrivate()).getX().toString();
    }

    @Override
    public String encrypt(String plainText) throws Exception {
        AsymmetricKeyParameter publicKey = keyPair.getPublic();
        byte[] plaintextBytes = plainText.getBytes();

        OAEPEncoding oaep = new OAEPEncoding(new ElGamalEngine());
        oaep.init(true, new org.bouncycastle.crypto.params.ParametersWithRandom(publicKey, random));

        int blockSize = oaep.getInputBlockSize();
        int blocks = (plaintextBytes.length + blockSize - 1) / blockSize;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < blocks; i++) {
            int start = i * blockSize;
            int length = Math.min(plaintextBytes.length - start, blockSize);
            outputStream.write(oaep.processBlock(plaintextBytes, start, length));
        }
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    @Override
    public String decrypt(String cipherText) throws Exception {
        AsymmetricKeyParameter privateKey = keyPair.getPrivate();
        byte[] encryptedBytes=Base64.getDecoder().decode(cipherText);

        OAEPEncoding oaep = new OAEPEncoding(new ElGamalEngine());
        oaep.init(false, privateKey);

        int blockSize = oaep.getInputBlockSize();
        int blocks = encryptedBytes.length / blockSize;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < blocks; i++) {
            int start = i * blockSize;
            outputStream.write(oaep.processBlock(encryptedBytes, start, blockSize));
        }
        return outputStream.toString();
    }
}
