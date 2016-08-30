import java.io.*;
import java.net.*;

public final class WebServer {
	public static void main(String arvg[]) throws Exception {
		int port = 6789; // Ajustar o número da porta.
		ServerSocket ss = new ServerSocket(port); // Estabelecer o socket de
													// escuta.

		System.out.println("Server: Aguardando na porta " + ss.getLocalPort());

		// Processar a requisição de serviço HTTP em um laço infinito.
		while (true) {
			HttpRequest request = new HttpRequest(ss.accept()); // Escutar
																// requisição de
																// conexão TCP.
			System.out.println("Server: Processando solicitacao de " + request.socket.getInetAddress().getHostName());
			Thread thread = new Thread(request); // Criar um novo thread para
													// processar a requisição.
			thread.start(); // Iniciar o thread.
		}
	}
}

final class HttpRequest implements Runnable {
	final static String CRLF = "\r\n";
	Socket socket;

	// Construtor
	public HttpRequest(Socket socket) throws Exception {
		this.socket = socket;
	}

	// Implementar o método run() da interface Runnable.
	@Override
	public void run() {
		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void processRequest() throws Exception {
		String data = "http://www.dca.fee.unicamp.br/courses/PooJava/";
		// Obter uma referência para os trechos de entrada e saída do socket.
		InputStream is = null;
		DataOutputStream os = null;

		try {
			os.write(is.read(data.getBytes()));
			os.flush();
			os.close();

			// Ajustar os filtros do trecho de entrada.
			BufferedReader br = null;
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}