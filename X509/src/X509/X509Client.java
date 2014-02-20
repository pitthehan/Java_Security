package X509;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.*;

public class X509Client {
	

	public static void main(String[] args) throws Exception 
	{
		String host = "127.0.0.1";
		int port = 6789;
		Socket s = new Socket(host, port);
	    
		/*Read the certificate file*/
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
        InputStream inStream = new FileInputStream("hehan.cer");
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
        inStream.close();

        /*Print the contents of the certificate*/
        System.out.println("The contents of the certificate is:");
        System.out.println(cert);
        
        /*Check certificate's expiration date*/
        Date date = cert.getNotAfter();
        if(date.after(new Date()))
        {
        	System.out.println("The certificate is current!!");
        	System.out.println("It is valid from "+ cert.getNotBefore()+ " to "+cert.getNotAfter()) ;
        }
        else
        {     
        	System.out.println("The certificate is expired!!");
        }
       
        /*Check server's signature*/
        byte[] sig = cert.getSignature();
        System.out.println(new BigInteger(sig).toString(16));
        try
        {
    	   cert.checkValidity();
    	   System.out.println("The certificate is valid!!");
        } catch (Exception e){
    	   System.out.println(e);   
        }
        
       // System.out.println("Please enter a string:");
        Scanner input = new Scanner(System.in);
        String message= input.nextLine();
        
        /*Retrieve the public key from the certificate*/
        RSAPublicKey eServer = (RSAPublicKey) cert.getPublicKey();
        
        /*Use server's public key to encrypt the message and sent server the ciphertext*/
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, eServer);
        byte[] cipherText = cipher.doFinal(message.getBytes());
        System.out.println("The ciphertext is: " + cipherText);
        os.writeObject(cipherText);
		os.flush();
		os.close();
	    s.close();
	}

}