package team.ranunculus.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class CryptoUtils {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static String hashSha512(String input) throws NoSuchAlgorithmException {
        return CryptoUtils.hashSha512(input, CryptoUtils.DEFAULT_CHARSET);
    }

    public static String hashSha512(String input, Charset charset) throws NoSuchAlgorithmException {
        return CryptoUtils.hashSha512Unsafe(input, charset);
    }


    public static String hashSha512Unsafe(String input, Charset charset) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(input.getBytes(charset));
        StringBuilder hashBuilder = new StringBuilder();
        for (byte hashByte : md.digest()) {
            hashBuilder.append(String.format("%02x", hashByte));
        }
        return hashBuilder.toString();
    }
    private CryptoUtils() {

    }
}
