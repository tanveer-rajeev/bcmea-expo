package com.betafore.evoting.security_config;

import com.betafore.evoting.Exception.CustomException;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CustomCipherService {
    @Value("${application.customCipherConfig.encoder.secret}")
    private String secret;

    @Value("${application.customCipherConfig.encoder.iv}")
    private String IV;

    public String encode(CharSequence cs) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(cs.toString().getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException |
                 InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException | InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String encodeLong(Long id) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(longToBytes(id));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String decode(CharSequence encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted.toString()));
            return new String(original);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Long decodeLong(CharSequence encrypted) throws CustomException {
        try {
            IvParameterSpec iv = new IvParameterSpec(IV.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(secret.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            return bytesToLong( cipher.doFinal(Base64.getDecoder().decode(encrypted.toString())));
        } catch (Exception ex) {
            throw new CustomException("Decoded exception occurred: "+ex);
        }
    }

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    private long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public boolean matches(CharSequence cs, String string) {
        return encode(cs).equals(string);
    }
}
