package Encryption;

import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
	    
	    
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		int port = 7999;

		// YOU NEED TO DO THESE STEPS:
		// -Generate a DES key.
		KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
		SecretKey myDesKey = keygenerator.generateKey();
		
		// -Store it in a file.
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("KeyFile.txt"));
		out.writeObject(myDesKey);
		out.flush();
		out.close();
		
		// -Use the key to encrypt the message above and send it over socket s to the server.	
		Cipher desCipher;
		desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
		desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
		byte[] textEncrypted = desCipher.doFinal(message.getBytes());
		System.out.println("encrypted message is: "+textEncrypted);
		
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
		os.writeObject(textEncrypted);
		s.close();
	}
}