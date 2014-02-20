package RSA;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;


public class RSAServer {
	
	private static byte [] digestMessage(byte [] msg) throws Exception{
		
		MessageDigest md = MessageDigest.getInstance("SHA");
		md.update(msg);
		return md.digest();
	}
	
	private static boolean verifySignature(byte [] msg, byte [] sig, PublicKey key) throws Exception{
		Signature signature = Signature.getInstance("SHA1withRSA");
		signature.initVerify(key);
		signature.update(msg);
		return signature.verify(sig);
	}
	
	private static byte [] decryptMessage(byte [] encMsg, PrivateKey key) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] plainText = cipher.doFinal(encMsg);
		return plainText;
	}
	
	public static void main(String [] args) throws Exception{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());
		
		byte [] encMsg = (byte [])is.readObject();
		byte [] digest = (byte [])is.readObject();
		byte [] signature = (byte [])is.readObject();
		
		// read Alice's (client) public key
		ObjectInputStream in = new ObjectInputStream(new FileInputStream("AlicePublicKey.txt"));
		PublicKey pubKey = (PublicKey)in.readObject();
		
		// read Bob's (server) private key
		ObjectInputStream in2 = new ObjectInputStream(new FileInputStream("BobPrivateKey.txt"));
		PrivateKey priKey = (PrivateKey)in2.readObject();
		
		// check digest
		if( Arrays.equals(digest, digestMessage(encMsg)) != true){
			System.out.println("Integrity Violation: Message has been modified");
			System.exit(0);
		}
		
		// verify signature
		if(verifySignature(digest, signature, pubKey) != true ){
			System.out.println("Authentication Violation: Message is not signed by Alice");
			System.exit(0);
		}
		
		System.out.println("Integrity Validated, Authenticated, Dcrypting msg....");
		
		// decrypt msg
		byte [] plainText = decryptMessage(encMsg, priKey);
		System.out.println("Plain text is: "+ new String(plainText));
	}

}

