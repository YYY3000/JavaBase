package rsa;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author yinyiyun
 * @date 2018/5/22 14:26
 */
public class RsaCoder {

    /**
     * String to hold name of the encryption algorithm.
     */
    private static final String ALGORITHM = "RSA";

    /**
     * 加密模式
     */
    private static final String PADDING = "RSA/ECB/OAEPWithSHA1AndMGF1Padding";

    /**
     * 生成密钥对
     *
     * @param keyLength 长度
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair(int keyLength) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(keyLength);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 将base64编码后的公钥字符串转成PublicKey实例
     *
     * @param publicKey base64编码后的公钥字符串
     * @return PublicKey实例
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将base64编码后的私钥字符串转成PrivateKey实例
     *
     * @param privateKey base64编码后的私钥字符串
     * @return PrivateKey实例
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 将PublicKey实例转成base64编码后的公钥字符串
     *
     * @param publicKey PublicKey实例
     * @return base64编码后的公钥字符串
     * @throws Exception
     */
    public static String getPublicKey(PublicKey publicKey) {
        return new String(Base64.getEncoder().encode(publicKey.getEncoded()));
    }

    /**
     * 公钥加密
     *
     * @param content 加密内容
     * @param publicKey 公钥
     * @return 加密后的byte数组
     * @throws Exception
     */
    private static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
        //java默认"RSA"="RSA/ECB/PKCS1Padding"
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 公钥加密
     *
     * @param data 加密内容
     * @param publicKey base64编码后的公钥串
     * @return base64编码后的加密串
     * @throws Exception
     */
    public static String encrypt(String data, String publicKeyString) throws Exception {
        PublicKey publicKey = getPublicKey(publicKeyString);
        byte[] encryptedBytes = encrypt(data.getBytes(), publicKey);
        String encryptedData = new String(Base64.getEncoder().encode(encryptedBytes));
        return encryptedData;
    }

    /**
     * 私钥解密
     *
     * @param content 解密内容
     * @param privateKey 私钥
     * @return 解密后的byte数组
     * @throws Exception
     */
    private static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
        //java默认"RSA"="RSA/ECB/PKCS1Padding"
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    /**
     * 公钥加密
     *
     * @param data 解密内容
     * @param privateKeyString base64编码后的私钥串
     * @return 解密串
     * @throws Exception
     */
    public static String decrypt(String data, String privateKeyString) throws Exception {
        PrivateKey privateKey = getPrivateKey(privateKeyString);
        byte[] decryptedBytes = decrypt(Base64.getDecoder().decode(data.getBytes()), privateKey);
        return new String(decryptedBytes);
    }

    public static String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGgm16Jdv10M0wT55AzYJjn2pQfmYnBNmSLYs23NnjjEmxs/RnqbkbCzyjM+P/qELSsKbsT0/X6BPW3EFoo2yKoe3gu+s3k25/SHsCkdVb6KoE1wJrY1iSwNHYmSriDUC+84Sh8BSMQd/637CGsCxxsKSq+ryeQB+7PzkvuWKQvQIDAQAB";
    public static String privateKeyString = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIaCbXol2/XQzTBPnkDNgmOfalB+ZicE2ZItizbc2eOMSbGz9GepuRsLPKMz4/+oQtKwpuxPT9foE9bcQWijbIqh7eC76zeTbn9IewKR1VvoqgTXAmtjWJLA0diZKuINQL7zhKHwFIxB3/rfsIawLHGwpKr6vJ5AH7s/OS+5YpC9AgMBAAECgYBIZJoXS5j+y8ojXjaGGhU7GOlXOTAxf+K5UdnDRUnftflTss6vnCEL9RhFnf/v8NdGHd05AdavFTDYSuLJNmuLyZYseg1w7b9ioQBTkjyFRxEUemAn2S7+oWsm3/NiLh5/wxly5PTFFxMTTptjmDeJfbXFSZzhV4D1UFtYm48b4QJBAL8YXLa/GrQWpDxxK/+GinskCVediSDllHD4nRU1VohO+NbdbDmOjbFz94W7d53zzm3m5M8SU6RYdVpEW9j2GNcCQQC0MfdU7+E38fPHccvA0NMkyEPC1Ggkq9am30TXUkGYGa22vmQ7qhhCF/qDe1e9bSwio0aNbXpc0kUhR3HCDwyLAkAm8OT7zIe5iR+gvYM0yAryOw64Tv8BuCb+unrwNnVSw50L1AVY2UbgkXwwQVqH0oYPeUzJMbfftrL7WRDKbqPzAkAmU1ZKr3aUgpaZ/f8bNI8kDKA06R55Zj8SYPxwW6Nt6VBERPxmoEfmjtkObAZ+WKEBgP65h+Z1pZdke6CbaL3/AkArf67UUo/wHOAN/upTsRPmVUItR414+NoQrVmI+OGPq/RHKEOhbZt8ylRydJcamG+xpsxHb85hMtjd2tYFnpVm";
    public static String encryptedData = "HgzExEc8hMAggLepCeHiEEd9z4/1/eaDCPEW19/ka6zKeqMSZWPupbgeTEOTSAj8lg1ZOjdv+2fBo/5kQzAbTdeRZhlYRL+q+wyoJLOYMtpXwDxI+BdiZJXp+hdPUUlZGD91UcEeoc3ho0IZP4YHnHRoq8qMKHud6inJiBygPE8=";

    public static void main(String[] args) {
        String data = "123456";

        try {

            //公钥加密
            String encryptedData = RsaCoder.encrypt(data, publicKeyString);
            System.out.println("加密后：" + encryptedData);

            //私钥解密
            String decryptedData = decrypt(encryptedData, privateKeyString);
            System.out.println("解密后：" + decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
