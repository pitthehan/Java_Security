package RSA;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;


public class RSAClient {
	
	private static byte [] encryptMessage(String msg, KeyPair key) throws Exception{
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key.getPublic());
		
		byte[] cipherText = cipher.doFinal(msg.getBytes());
		
		return cipherText;
	}
	
	private static byte [] signMessage(byte [] msg, KeyPair key) throws Exception{
		
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initSign(key.getPrivate(), new SecureRandom());
		signature.update(msg);
		byte[] sigBytes = signature.sign();
		
		return sigBytes;
	}
	
	private static byte [] digestMessage(byte [] msg) throws Exception{
		
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(msg);
		return md.digest();
	}
	
	public static void main(String []args) throws Exception{
		String message = "The quick brown fox jumps over the lazy dog.";

		String host = "localhost";
		int port = 7999;

		// generate keys, Alice and Bob should generate their own key seperately
		// But assume these keys are already generated and saved to local file system
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyBob = keyPairGenerator.genKeyPair();
		
		ObjectOutputStream out1 = new ObjectOutputStream(new FileOutputStream("BobPublicKey.txt"));
		out1.writeObject(keyBob.getPublic());
		
		ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream("BobPrivateKey.txt"));
		out2.writeObject(keyBob.getPrivate());
		
		KeyPair keyAlice = keyPairGenerator.genKeyPair();
		ObjectOutputStream out3 = new ObjectOutputStream(new FileOutputStream("AlicePublicKey.txt"));
		out3.writeObject(keyAlice.getPublic());
		
		ObjectOutputStream out4 = new ObjectOutputStream(new FileOutputStream("AlicePrivateKey.txt"));
		out4.writeObject(keyAlice.getPrivate());
		
		// encrypt message
		byte [] encMsg = encryptMessage(message, keyBob);
		
		// caculate encrypted msg digest
		byte [] dig = digestMessage(encMsg);
		
		// sign digest
		byte [] sign = signMessage(dig, keyAlice);
		
		// write to the server
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		os.writeObject(encMsg);
		os.writeObject(dig);
		os.writeObject(sign);
		
		os.flush();
		s.close();
	}
}
