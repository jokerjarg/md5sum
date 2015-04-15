import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


/*
 * 
 * coded by : www.meonsecurity.com
 * simple md5 checksum generator and checker
 * LineAL
 * 
 */

public class MD5Sum
{
	
	private static List<String> allFiles = new ArrayList<String>();
	private static List<String> allDirs = new ArrayList<String>();
	private static List<String> originalmd5Files = new ArrayList<String>();
	private static List<String> md5Files = new ArrayList<String>();

	
	// you can modify this for ur preference
	public static void md5Checksum(List<String> allFiles) throws NoSuchAlgorithmException, IOException
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		
		for(String f : allFiles)
		{
			FileInputStream fIn = new FileInputStream(f);
			byte[] fBytes = new byte[4096];
			int fRead = 0;
			
			while((fRead = fIn.read(fBytes)) != -1)
			{
				md.update(fBytes,0,fRead);
			}
			
			byte[] md5b = md.digest();
			
			StringBuffer sb = new StringBuffer();
			for(byte b : md5b)
			{
				sb.append(String.format("%02x", b));
			}
			md5Files.add(sb.toString());
			BufferedWriter bw = null;
			try
			{
				bw = new BufferedWriter(new FileWriter("new_md5.txt"));
				for(String md5 : md5Files)
				{
					bw.write(md5);
					bw.newLine();
				}
			}catch(IOException ex)
			{
				ex.printStackTrace();
			}
			bw.close();
		}
	}
	
	
	/*not used*/
	public static boolean contains(String file,List<String> md5Files)
	{
		if(file == null || md5Files == null)
		{
			return false;
		}
	
		for(String a : md5Files)
		{
			if(file.equals(a))
			{
				return true;
			}
		}
		return false;
	}
	
	public static void originalmd5(File file) throws FileNotFoundException
	{
		String line;
		BufferedReader br = new BufferedReader(new FileReader(file));
		try {
			while((line = br.readLine())!=null)
			{
				originalmd5Files.add((String) line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void findFiles(String dirPath) {
        	File dir = new File(dirPath);
        	File[] firstLevelFiles = dir.listFiles();
		if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            		for (File aFile : firstLevelFiles) {
                		if (aFile.isDirectory()) {
                			allDirs.add(aFile.getAbsolutePath().toString());
                			findFiles(aFile.getAbsolutePath());
                		} else {
                			allFiles.add(aFile.getAbsolutePath().toString());
                		}
            		}
        	}
    	}
	
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException,Exception {
    	if(args.length == 0)
    	{
    		System.out.println("Usage : ");
    		System.out.println("Generate md5 checksum : ./file -generate /directory/of/files/");
    		System.out.println("Test original md5 agains new : ./file -check /directory/of/files/ location_to_original_md5");
    		System.exit(1);
    	}else if(args[0].equals("-generate") && args[1] != null)
    	{
    		findFiles(args[1]);
    		md5Checksum(allFiles);
    		System.out.println("[+] Done!");
    	}else if(args[0].equals("-check") && args[1] != null && args[2] != null)
    	{
    		findFiles(args[1]);
    		md5Checksum(allFiles);
    		File file = new File(args[2]);
    		originalmd5(file);
    		int curr = 0;
        	for(String hash : md5Files)
        	{
        		if(!hash.equals(originalmd5Files.get(curr)))
        		{
        			System.out.println(allFiles.get(curr) + " : " + hash + " changed! original ("+originalmd5Files.get(curr));
        		}
        		curr++;
        	}
    	}
    }
}
