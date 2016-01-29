import java.io.*;
import java.security.*;

public class ComputeSHA{
	static MessageDigest md;
	static FileInputStream file;
	public static void main(String[] args){
		if(args.length == 0){
			System.out.println("Please input the file name");
			return;
		}
		
		try{
			md = MessageDigest.getInstance("SHA-1");
		}
		catch (NoSuchAlgorithmException ex){
			System.out.println("cannot compute SHA1 hash code");
			return;
		}

		try{
			file = new FileInputStream(args[0]);
		}
		catch(FileNotFoundException ex){
			System.out.println("cannot find the file " + args[0]);
			return;
		}
			
		byte[] dataBytes = new byte[1024];
		int read = 0;

		try{
			while((read = file.read(dataBytes)) != -1){
				md.update(dataBytes, 0, read);
			}
			byte[] mdbytes = md.digest();
			StringBuffer stb = new StringBuffer();
			for(int i=0; i<mdbytes.length; i++){
				String hex = Integer.toHexString(0xFF & mdbytes[i]);
				if(hex.length() == 1)	stb.append("0");
				stb.append(hex);
			}
			file.close();
			System.out.println(stb.toString());
		}
		catch(IOException ex){
			System.out.println("cannot read from the file");
			return;
		}

	}
}