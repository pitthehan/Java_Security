package Authentication;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Arrays;

public class ProtectedServer
{
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException 
	{
		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.
		int length = in.readInt();
		byte [] digest = new byte[length];
		
		in.readFully(digest);
		String user = in.readUTF();
		long t1 = in.readLong();
		long t2 = in.readLong();
		double q1 = in.readDouble();
		double q2 = in.readDouble();
		
		byte [] first = Protection.makeDigest(user, this.lookupPassword(user), t1, q1);
		byte [] second = Protection.makeDigest(first, t2, q2);
		
		if( Arrays.equals(second, digest)){
			return true;
		}else{
			return false;
		}
		
	}

	protected String lookupPassword(String user) { return "abc123"; }

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
		  System.out.println("Client logged in.");
		else
		  System.out.println("Client failed to log in.");

		s.close();
	}
}