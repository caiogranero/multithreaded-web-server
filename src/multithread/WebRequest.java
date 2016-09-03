package multithread;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;;

public final class WebRequest implements Runnable {

	private ServerSocket ss;
	private Socket s;
	
	public WebRequest(int port) throws IOException{
		ss = new ServerSocket(port);
		
	}
	
	@Override
	public void run() {
		if(s == null){
			System.out.println("socket não conseguiu inicializar.");
			return;
		}
		
		try{
			ProcessRequest();
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
	
	public void Listen() throws IOException{		 
		s = this.ss.accept();
		Thread t = new Thread(this);
		t.start();
	}
	
	public void ProcessRequest() throws IOException{
		DataOutputStream os = new DataOutputStream(s.getOutputStream());
		DataInputStream is = new DataInputStream(s.getInputStream());
		
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = "";
		while((line = br.readLine()) != null){
			System.out.println(line);
		}
		System.out.println();
		
		is.close();
		os.close();
	}
}
