import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
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
		String url = "";
		String line = "";
		os = new DataOutputStream(s.getOutputStream());
		is = new DataInputStream(s.getInputStream());

		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		line = br.readLine();
		url = geturl(line);
		FileInputStream fis = null;

		if (url.contains("login")){
			this.singin(url);
		}

		if (this.isLogged(br)){
			Boolean fileExists = true;
			try {
				fis = new FileInputStream(url);
			} catch (FileNotFoundException e) {
				fileExists = false;
			}


			if (fileExists) {
				StringBuilder sb = this.openFile(url);
			} else {
				File folder = new File(url);
				File[] listOfFiles = folder.listFiles();
				if(folder.exists()){
					this.listFoldersFiles(listOfFiles, url);
				}else{
					this.return404();
				}
			}

			if (fileExists) {
				try {
					sendBytes(fis);
				} catch (Exception e) {
					e.printStackTrace();
				}
				fis.close();
			}

		}else{
			this.login();
		}
		//is.close();
		//os.close();
	}

	private void listFoldersFiles(File[] listOfFiles, String url) throws IOException {

		os = new DataOutputStream(s.getOutputStream());
		StringBuilder sb = new StringBuilder("HTTP/1.0 ");
		sb.append("200 OK" + CRLF);
		sb.append("Content-type: text/html"+ CRLF);
		sb.append(CRLF);
		sb.append("<html><body>");

		for (int i = 0; i < listOfFiles.length; i++) {



			if (listOfFiles[i].isFile()) {
				sb.append(("<a href=\"" + url + listOfFiles[i].getName()+"\">" + "File " + listOfFiles[i].getName())+"<a/><br>");
			} else if (listOfFiles[i].isDirectory()) {
				sb.append(("<a href=\"" + url.substring(1) + listOfFiles[i].getName()+"/\">" + "Directory " + listOfFiles[i].getName())+"<a/><br>");
			}

		}

		sb.append("</body></html>");

		os.writeBytes(sb.toString());
		os.close();

	}

	private void return404() throws IOException {
		StringBuilder sb = new StringBuilder("HTTP/1.0 ");
		os = new DataOutputStream(s.getOutputStream());
		sb.append("404 Not Found");
		sb.append(CRLF);
		sb.append("Content-type: text/html"+ CRLF);
		sb.append(CRLF);
		sb.append("<html><head><title>Not Found</title>" +
				"</head><body>Not Found</body></html>");
		os.writeBytes(sb.toString());
		os.close();
	}

	private StringBuilder openFile(String url) {
		StringBuilder sb = new StringBuilder("HTTP/1.0 ");
		sb.append("200 OK");
		sb.append(CRLF);
		sb.append("Content-type: " + 
				contentType( url ) + CRLF);
		sb.append(CRLF);

		return sb;

	}

	private void login() throws IOException {

		os = new DataOutputStream(s.getOutputStream());
		StringBuilder sb = new StringBuilder("HTTP/1.0 ");
		sb.append("200 OK" + CRLF);
		sb.append("Content-type: text/html"+ CRLF);
		sb.append(CRLF);
		sb.append("<html><body><form action=\"/login\">User:<br><input type=\"text\" " +
				"name=\"name\"><br>Password:<br><input type=\"text\" name=\"password\"> " + 
				"<br><br><input type=\"submit\" value=\"Submit\"></form></body></html>");

		os.writeBytes(sb.toString());
		os.close();

	}

	private void singin(String url) throws IOException {
		os = new DataOutputStream(s.getOutputStream());
		String user = url.split("name=")[1].split("&")[0];
		String password = url.split("password=")[1];

		StringBuilder sb = new StringBuilder("HTTP/1.0 ");
		if(user.equals("network") && password.equals("network123")){
			sb.append("200 OK" + CRLF);
			sb.append("Content-type: text/html" + CRLF);
			sb.append("Set-Cookie:logado=1; Path=/;" + CRLF);
			sb.append(CRLF);
			sb.append("<html><head><title>Logado</title>" +
					"</head><body>Logado</body></html>");
			os.writeBytes(sb.toString());

		}
		os.close();
	}

	private boolean isLogged(BufferedReader br) throws IOException {
		while (br.ready()){
			if(br.readLine().contains("logado=1")){
				return true;
			}
		}
		return false;
	}

	private void sendBytes(FileInputStream fis)throws Exception
	{
		byte[] buffer = new byte[1024];
		int bytes = 0;

		while((bytes = fis.read(buffer)) != -1 ) {
			os.write(buffer, 0, bytes);
		}
	}

	public String geturl(String line){
		StringTokenizer tokens = new StringTokenizer(line);
		tokens.nextToken();
		String url = tokens.nextToken();

		url = "." + url;
		return url;
	}

	public String contentType(String url){
		if(url.endsWith(".jpg")){
			return "image/jpeg";			
		}
		if (url.endsWith(".html")){
			return "text/html";
		}
		if (url.endsWith(".gif")){
			return "image/gif";
		}
		return "application/octet-stream";
	}

}
