

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;;

public final class WebRequest implements Runnable {

	private ServerSocket ss;
	private Socket s;

	private DataOutputStream os;
	private DataInputStream is;

	final static String CRLF = "\r\n";

	public WebRequest(int port) throws IOException{
		ss = new ServerSocket(port);

	}

	@Override
	public void run() {
		if(s == null){
			System.out.println("socket n�o conseguiu inicializar.");
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

		os = new DataOutputStream(s.getOutputStream());
		is = new DataInputStream(s.getInputStream());

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String filename = getFileName(br.readLine());

		FileInputStream fis = null;
		Boolean fileExists = true;
		try {
			fis = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}


		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;

		if (fileExists) {
			statusLine = "200 OK";
			contentTypeLine = "Content-type: " + 
					contentType( filename ) + CRLF;
		} else {
			statusLine = "404 Not Found";
			contentTypeLine = "text/html";
			entityBody = "<HTML>" +
					"<HEAD><TITTLE>Not Found</TITTLE></HEAD>" +
					"<BODY>Not Found</BODY></HTML>";

		}

		os.writeBytes(statusLine);
		os.writeBytes(contentTypeLine);
		os.writeByte(0);

		if (fileExists) {
			try {
				sendBytes(fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
			fis.close();
		}


		is.close();
		os.close();

	}

	private void sendBytes(FileInputStream fis)throws Exception
	{
		byte[] buffer = new byte[1024];
		int bytes = 0;
		
		while((bytes = fis.read(buffer)) != -1 ) {
			os.write(buffer, 0, bytes);
		}
	}

	public String getFileName(String line){
		StringTokenizer tokens = new StringTokenizer(line);
		tokens.nextToken();
		String fileName = tokens.nextToken();

		fileName = "." + fileName;
		return fileName;
	}

	public String contentType(String fileName){
		return "text/html";
	}

}
