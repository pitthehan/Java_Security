
package X509;
import java.io.*;
import java.net.*;
import java.security.*;

import javax.crypto.*;

public class X509Server {

	public static void main(String[] args) throws Exception 
	{   
		
		String aliasname="hehan";
        char[] password="19890213".toCharArray();
		
        int port = 6789;
		ServerSocket server = new ServerSocket(port);
		Socket s = server.accept();
		ObjectInputStream is = new ObjectInputStream(s.getInputStream());

	    
		/*Read the keystore and retrieve the server's private key*/
        KeyStore ks = KeyStore.getInstance("jks");
        ks.load(new FileInputStream("hehan.keystore"), password);
        PrivateKey dServer = (PrivateKey)ks.getKey(aliasname, password);
       
        /*Use itself public key to decrypt the message from the client*/
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte[] in = (byte[]) is.readObject();
		cipher.init(Cipher.DECRYPT_MODE, dServer);
		byte[] plaintText = cipher.doFinal(in);
		System.out.println("The plaintext is: " + new String(plaintText));
		
	}
}

