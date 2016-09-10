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
		}catch(IOException ex){
			System.out.println(ex);
		}
	}

	public void Listen() throws IOException{		 
		s = this.ss.accept();
		Thread t = new Thread(this);
		t.start();
	}

	public void ProcessRequest() throws IOException{
		String filename = "";
		String line = "";
		os = new DataOutputStream(s.getOutputStream());
		is = new DataInputStream(s.getInputStream());

		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		line = br.readLine();
		filename = getFileName(line);
		FileInputStream fis = null;
		Boolean fileExists = true;
		try {
			fis = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}


		StringBuilder sb = new StringBuilder("HTTP/1.0 ");
		
		if (fileExists) {
			sb.append("200 OK");
			sb.append(CRLF);
			sb.append("Content-type: " + 
					contentType( filename ) + CRLF);
			sb.append(CRLF);
		} else {
			sb.append("404 Not Found");
			sb.append(CRLF);
			sb.append("Content-type: text/html"+ CRLF);
			sb.append(CRLF);
			sb.append("<html><head><title>Not Found</title>" +
					"</head><body>Not Found</body></html>");
			os.writeBytes(sb.toString());

		}
		
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
		if(fileName.endsWith(".jpg")){
			return "image/jpeg";			
		}
		if (fileName.endsWith(".html")){
			return "text/html";
		}
		if (fileName.endsWith(".gif")){
			return "image/gif";
		}
		return "application/octet-stream";
	}

}
