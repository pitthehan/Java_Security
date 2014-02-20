package MessageDigest;
import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Hash {
	
	public static byte[] makeDigest(byte[] mush, String alg) throws NoSuchAlgorithmException 
	{
		MessageDigest md = MessageDigest.getInstance(alg);
		md.update(mush);
		return md.digest();
	}


	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		// TODO Auto-generated method stub
		System.out.println("Input the string you want to hash:");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String input = in.readLine();
		
		byte [] shaDigest = makeDigest(input.getBytes(),"SHA");
		byte [] md5Digest = makeDigest(input.getBytes(),"MD5");
		
		System.out.println("The original message is: "+input);
		System.out.println("SHA digest: "+new BigInteger(shaDigest).toString());
		System.out.println("MD5 digest: "+new BigInteger(md5Digest).toString());
	}

}
