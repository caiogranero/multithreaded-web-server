import java.net.*;
import java.io.*;

/**
 *
 * @author Valter
 */
public class Servidor implements Runnable {
	ServerSocket ss;

	public Servidor(int porta) throws Exception {
		// informando em qual porta o servidor estará ouvindo
		ss = new ServerSocket(porta);

		// criando uma nova thread e já estou inicializando ela
		new Thread(this).start();

		// mensagem inicial
		System.out.println("Servidor ouvindo na porta:" + porta);
	}

	@Override
	public void run() {
		try {
			while (true) {
				// aceitando a conexão com o cliente e inicializando a outra
				// thread
				new TrataCliente(ss.accept()).start();
				System.out.println("Mais um cliente atendido!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		try {
			new Servidor(1024);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}

class TrataCliente extends Thread {
	private Socket client;

	public TrataCliente(Socket s) {
		client = s;
	}

	@Override
	public void run() {
		try {
			// aqui vai a sua comunicacao com o cliente
			ObjectInputStream oi = new ObjectInputStream(client.getInputStream());
			// exibindo na tela o que recebemos do nosso cliente
			System.out.println("Chegou isso:" + oi.readObject());
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}