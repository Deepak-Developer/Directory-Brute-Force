
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.net.ssl.*;

public class Main{
	public static void main(String[] args) throws Exception{
		
		/* Download wordlist file 
		   in same Directory and run through CMD.
		            or
		    Import in IDE Package
		*/
		File f=new File("wordlist.txt");
		if(!f.exists()) {
			System.out.println("wordlist file doesn't exist");
			System.exit(0);
		}

		BufferedReader fin=new BufferedReader(new FileReader(f));
		String path="",tpath;
		while((tpath=fin.readLine())!=null) {
			path+=tpath+" ";
		}
		MultiThread.path=path.split(" ");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("How many url do you want to bruteforce:");
		int n=Integer.parseInt(br.readLine());
		MultiThread urls[]=new MultiThread[n];
		System.out.println("Enter webapp URLs: ");
		for(int i=0;i<n;i++) {
			String target_url = br.readLine(); 
			target_url+=target_url.charAt(target_url.length()-1)=='/'?"":"/";
			MultiThread t=new MultiThread(target_url);
			t.setName(i+target_url);
			urls[i]=t;
		}
		System.out.println("How many success status codes you want to enter:");
		int m=Integer.parseInt(br.readLine());
		System.out.println("Enter success status codes:");
		for(int i=0;i<m;i++) {
			MultiThread.success_code.add(Integer.parseInt(br.readLine()));
		}
		for(int i=0;i<n;i++) {
			urls[i].start();
		}
		br.close();
		fin.close();
		br.close();
	}
}


class MultiThread extends Thread{
	private String target_url;
	static String path[];
	static Set<Integer> success_code=new HashSet<Integer>(); 
	public MultiThread(String ur) {
		target_url=ur;
	}
	public void run() {
		try {
			for(int i=0;i<path.length;i++) {
				
				URL url = new URL(target_url+path[i]);
				HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
				http.setRequestMethod("HEAD");
				
				// Retrieving Status code
				int responseCode = http.getResponseCode();
				if(success_code.contains(responseCode))
						System.out.println(target_url+path[i] + " [Status code " +responseCode +"]");
				Thread.sleep(1000);				
			}
		} catch (Exception e) {
		}
	}
}

