package duson.java.core.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import duson.java.core.codec.BytesWapper;
import duson.java.core.codec.HexWapper;

/**
 * AES对称加密算法 
 */
public class AESCrytp {
      
    //加解密算法/工作模式/填充方式,Java6.0支持PKCS5Padding填充方式,BouncyCastle支持PKCS7Padding填充方式  
    public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";  
    
	private static final String AES = "AES";
	private static final String DEFAULT_URL_ENCODING = "UTF-8";
    private static final int DEFAULT_AES_KEYSIZE = 128;
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";
	private static final byte[] DEFAULT_KEY = new byte[] { -97, 88, -94, 9, 70, -76, 126, 25, 0, 3, -20, 113, 108, 28, 69, 125 };

    /** 
     * 生成密钥 
     */  
    public static String initkey() throws Exception{  
        KeyGenerator kg = KeyGenerator.getInstance(AES); //实例化密钥生成器  
        kg.init(128); //初始化密钥生成器:AES要求密钥长度为128,192,256位  
        SecretKey secretKey = kg.generateKey(); //生成密钥  
        return Base64.encodeBase64String(secretKey.getEncoded()); //获取二进制密钥编码形式
    }  
      
    /** 
     * 转换密钥 
     */  
    public static Key toKey(byte[] key) throws Exception{  
        return new SecretKeySpec(key, AES);  
    } 
      
    /** 
     * 加密数据 
     * @param data 待加密数据 
     * @param key  密钥 
     * @return 加密后的数据 
     * */  
    public static String encrypt(String data, String key) throws Exception{
        Key k = toKey(Base64.decodeBase64(key)); //还原密钥   
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);              //实例化Cipher对象，它用于完成实际的加密操作  
        cipher.init(Cipher.ENCRYPT_MODE, k);                               //初始化Cipher对象，设置为加密模式  
        return Base64.encodeBase64String(cipher.doFinal(data.getBytes())); //执行加密操作。加密后的结果通常都会用Base64编码进行传输 ;
    }    
      
    /** 
     * 解密数据 
     * @param data 待解密数据 
     * @param key  密钥 
     * @return 解密后的数据 
     * */  
    public static String decrypt(String data, String key) throws Exception{
    	data = data.replace("+cnit765+", "/");
    	key = key.replace("+cnit765+", "/");  //进行反转义
        Key k = toKey(Base64.decodeBase64(key));  
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);  
        cipher.init(Cipher.DECRYPT_MODE, k);                          //初始化Cipher对象，设置为解密模式  
        return new String(cipher.doFinal(Base64.decodeBase64(data))); //执行解密操作  
    }  
      
    /**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input
	 *            原始输入字符数组
	 * @throws Exception
	 */
	public static String aesEncrypt(String input) throws Exception {
		return BytesWapper.byte2HexStringByCommonsCodec(aesEncrypt(input.getBytes(DEFAULT_URL_ENCODING), DEFAULT_KEY));
	}

	/*
	* 支付宝的实现
	---------------------------------------------------------------------*/

	public static String encrypt(String content, String key) throws Exception {
        //String key = "开发者自己的AES秘钥";
        //String content = "需要加密的参数";
        String charset = "UTF-8"; // 项目使用的字符编码集
        String fullAlg = "AES/CBC/PKCS5Padding";
  
        Cipher cipher = Cipher.getInstance(fullAlg);
        IvParameterSpec iv = new IvParameterSpec(initIv(fullAlg));
        cipher.init(Cipher.ENCRYPT_MODE,
                new SecretKeySpec(Base64.decodeBase64(key.getBytes()), "AES"),
                iv);
  
        byte[] encryptBytes = cipher.doFinal(content.getBytes(charset));
        return new String(Base64.encodeBase64(encryptBytes));
    }
   
    /**
     * 初始向量的方法, 全部为0. 这里的写法适合于其它算法,针对AES算法的话,IV值一定是128位的(16字节).
     *
     * @param fullAlg
     * @return
     * @throws GeneralSecurityException
     */
    private static byte[] initIv(String fullAlg) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(fullAlg);
        int blockSize = cipher.getBlockSize();
        byte[] iv = new byte[blockSize];
        for (int i = 0; i < blockSize; ++i) {
            iv[i] = 0;
        }
        return iv;
    }

	/**
     * 解密
     * @param content 密文
     * @param key aes密钥
     * @param charset 字符集
     * @return 原文
     * @throws EncryptException
     */
    public String decrypt(String content, String key, String charset) throws Exception {
          
        //反序列化AES密钥
        SecretKeySpec keySpec = new SecretKeySpec(Base64.decodeBase64(key.getBytes()), "AES");
          
        //128bit全零的IV向量
        byte[] iv = new byte[16];
        for (int i = 0; i < iv.length; i++) {
            iv[i] = 0;
        }
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
          
        //初始化加密器并加密
        Cipher deCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] encryptedBytes = Base64.decodeBase64(content.getBytes(charset));
        byte[] bytes = deCipher.doFinal(encryptedBytes);
        return new String(bytes);
    }

	/*
	* 复杂
	---------------------------------------------------------------------*/
	 */
	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input
	 *            原始输入字符数组
	 * @param key
	 *            符合AES要求的密钥
	 * @throws Exception  
	 */
	public static String aesEncrypt(String input, String key) throws Exception {
		return BytesWapper.byte2HexStringByCommonsCodec(aesEncrypt(input.getBytes(DEFAULT_URL_ENCODING), HexWapper.hexString2ByteByCommonsCodec(key)));
	}

	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input
	 *            原始输入字符数组
	 * @param key
	 *            符合AES要求的密钥
	 * @throws Exception
	 */
	public static byte[] aesEncrypt(byte[] input, byte[] key) throws Exception {
		return aes(input, key, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input
	 *            原始输入字符数组
	 * @param key
	 *            符合AES要求的密钥
	 * @param iv
	 *            初始向量
	 * @throws Exception
	 */
	public static byte[] aesEncrypt(byte[] input, byte[] key, byte[] iv) throws  Exception {
		return aes(input, key, iv, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input
	 *            Hex编码的加密字符串
	 * @throws Exception
	 */
	public static String aesDecrypt(String input) throws Exception {
		return new String(aesDecrypt(HexWapper.hexString2ByteByCommonsCodec(input), DEFAULT_KEY), DEFAULT_URL_ENCODING);
	}

	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input
	 *            Hex编码的加密字符串
	 * @param key
	 *            符合AES要求的密钥
	 * @throws Exception
	 */
	public static String aesDecrypt(String input, String key) throws Exception {
		return new String(aesDecrypt(HexWapper.hexString2ByteByCommonsCodec(input), HexWapper.hexString2ByteByCommonsCodec(key)), DEFAULT_URL_ENCODING);
	}

	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input
	 *            Hex编码的加密字符串
	 * @param key
	 *            符合AES要求的密钥
	 * @throws Exception
	 */
	public static byte[] aesDecrypt(byte[] input, byte[] key) throws Exception {
		return aes(input, key, Cipher.DECRYPT_MODE);
	}

	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input
	 *            Hex编码的加密字符串
	 * @param key
	 *            符合AES要求的密钥
	 * @param iv
	 *            初始向量
	 * @throws Exception
	 */
	public static byte[] aesDecrypt(byte[] input, byte[] key, byte[] iv) throws  Exception {
		return aes(input, key, iv, Cipher.DECRYPT_MODE);
	}

	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param input
	 *            原始字节数组
	 * @param key
	 *            符合AES要求的密钥
	 * @param mode
	 *            Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 * @throws Exception
	 */
	private static byte[] aes(byte[] input, byte[] key, int mode) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, AES);
		Cipher cipher = Cipher.getInstance(AES);
		cipher.init(mode, secretKey);
		return cipher.doFinal(input);
	}
	

	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param input
	 *            原始字节数组
	 * @param key
	 *            符合AES要求的密钥
	 * @param iv
	 *            初始向量
	 * @param mode
	 *            Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 * @throws Exception
	 */
	private static byte[] aes(byte[] input, byte[] key, byte[] iv, int mode) throws  Exception {
		SecretKey secretKey = new SecretKeySpec(key, AES);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance(AES_CBC);
		cipher.init(mode, secretKey, ivSpec);
		return cipher.doFinal(input);
	}
	
	/**
	 * 
	 * @param secretKey
	 * @return
	 */
	private static SecretKeySpec getSecretKeySpec(SecretKey secretKey) {
		byte[] keyEncoded = secretKey.getEncoded();
		SecretKeySpec keySpec = new SecretKeySpec(keyEncoded, AES);
		return keySpec;
	}

	/**
	 * 
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static SecretKey getSecretKey(String key) throws NoSuchAlgorithmException {
		KeyGenerator kGen = KeyGenerator.getInstance(AES);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(key.getBytes());
		kGen.init(128, secureRandom);
		SecretKey secretKey = kGen.generateKey();
		return secretKey;
	}

	/**
	 * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
	 * @throws NoSuchAlgorithmException 
	 */
	public static String generateAesKeyString() throws NoSuchAlgorithmException {
		return BytesWapper.byte2HexStringByCommonsCodec(generateAesKey(DEFAULT_AES_KEYSIZE));
	}

	/**
	 * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] generateAesKey() throws NoSuchAlgorithmException {
		return generateAesKey(DEFAULT_AES_KEYSIZE);
	}

	/**
	 * 生成AES密钥,可选长度为128,192,256位.
	 * @throws NoSuchAlgorithmException 
	 */
	public static byte[] generateAesKey(int keysize) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
		keyGenerator.init(keysize);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}
      
    public static void main(String[] args) throws Exception {  
        Set<String> set = new HashSet<String>();
        Date begin = new Date();
    	for(int n = 0 ; n < 1000000 ; ++n){
        String source = n+""; 
          
        String key = initkey();
          
        String encryptData = encrypt(source, key);  
        System.out.println("加密：" + encryptData);  
        
        String result = key+encryptData;
        for(int i = 0 ; i < result.length() ; ++i){
        	set.add(result.charAt(i)+"");
        }
        
    	}
    	Date end = new Date();
    	System.out.println((end.getTime() - begin.getTime())/1000 + "s");
    	Iterator<String> it = set.iterator();
    	while(it.hasNext()){
    		System.out.print(it.next());
    	}
    }  
	
}
