package Authentication;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Date;
import java.util.Random;

public class ProtectedClient
{
	public void sendAuthentication(String user, String password, OutputStream outStream) throws IOException, NoSuchAlgorithmException 
	{
		DataOutputStream out = new DataOutputStream(outStream);

		// IMPLEMENT THIS FUNCTION.
		java.util.Date date= new java.util.Date();
		long t1 = date.getTime();
		long t2 = date.getTime();

		Random r = new Random();
		double q1 = r.nextDouble();
		double q2 = r.nextDouble();
		
		
		byte [] first = Protection.makeDigest(user, password, t1, q1);
		byte [] second = Protection.makeDigest(first, t2, q2);
		
		out.writeInt(second.length);
		out.write(second);
		out.writeUTF(user);
		out.writeLong(t1);
		out.writeLong(t2);
		out.writeDouble(q1);
		out.writeDouble(q2);
		
		out.flush();
	}

	public static void main(String[] args) throws Exception 
	{
		String host = "127.0.0.1";
		int port = 7999;
		String user = "George";
		String password = "abc123";
		Socket s = new Socket(host, port);

		ProtectedClient client = new ProtectedClient();
		client.sendAuthentication(user, password, s.getOutputStream());

		s.close();
	}
}