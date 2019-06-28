package encrypt;

import com.google.common.base.Strings;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Create by yinyiyun on 2019/4/28 14:14
 */
public class AESUtil {

    private AESUtil() {
    }

    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    private static final String OS_LINUX = "linux";

    private static final String OS_MAC = "mac";

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        Cipher cipher = getCipher(encryptKey, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(content.getBytes(UTF_8));
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        Cipher cipher = getCipher(decryptKey, Cipher.DECRYPT_MODE);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    private static Cipher getCipher(String key, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        KeyGenerator keyGenerator = getKeyGenerator(key);

        // 产生原始对称密钥
        SecretKey originalKey = keyGenerator.generateKey();

        // 获得原始对称密钥的字节数组
        byte[] raw = originalKey.getEncoded();

        // 根据字节数组生成AES密钥
        SecretKey secretKey = new SecretKeySpec(raw, "AES");

        // 根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES");

        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(mode, secretKey);
        return cipher;
    }

    private static KeyGenerator getKeyGenerator(String key) throws NoSuchAlgorithmException {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom random = getSecureRandom();

        //2.根据key规则初始化密钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        random.setSeed(key.getBytes());
        keyGenerator.init(128, random);

        return keyGenerator;
    }

    private static SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
        SecureRandom random;
        if (OS_NAME.contains(OS_LINUX) || OS_NAME.contains(OS_MAC)) {
            random = SecureRandom.getInstance("SHA1PRNG");
        } else {
            random = new SecureRandom();
        }
        return random;
    }

    /**
     * base 64 加密
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base 64 解密
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     */
    private static byte[] base64Decode(String base64Code) {
        if (Strings.isNullOrEmpty(base64Code)) {
            return new byte[0];
        }
        return Base64.getDecoder().decode(base64Code);
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     */
    public static String decrypt(String encryptStr, String decryptKey) {
        if (Strings.isNullOrEmpty(encryptStr)) {
            return null;
        }
        try {
            return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
        } catch (Exception e) {
            // todo 异常处理或抛出
        }
        return null;
    }

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     */
    public static String encrypt(String content, String encryptKey) {
        try {
            return base64Encode(aesEncryptToBytes(content, encryptKey));
        } catch (Exception e) {
            // todo 异常处理或抛出
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("13233333333", "862400d9-e461-4cab-942c-7d6244ee3da3"));
    }

}