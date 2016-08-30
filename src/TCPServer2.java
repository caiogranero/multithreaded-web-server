

//--- TCPServer2.java -------------------------------------
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *  Ilustra estrutura de um servidor TCP usando multithread.
 */
class DataProvider implements Runnable {
  Socket client;
  OutputStream os = null;

  public DataProvider(Socket s) throws IOException {
    client = s;
    os = client.getOutputStream();
  }

  public void run() {
    // obtem informaçao solicitada como sequencia de bytes
    String data = "http://www.dca.fee.unicamp.br/courses/PooJava/";
    byte[] buffer = data.getBytes();
    // escreve data para cliente
    try {
      os.write(buffer);
      os.flush();
      os.close();
      client.close();
    }
    catch (Exception e) {
      System.err.println(e);
    }
  }
}

public class TCPServer2 {
  public static void main(String[] args) {
    ServerSocket ss = null;
    Socket cliente = null;
    try {
      // cria servidor de sockets na porta escolhida pelo sistema
      ss = new ServerSocket(0);
      // aguarda solicitacao
      System.out.println("Server: Aguardando na porta " +
			 ss.getLocalPort());
      while (true) {
	// inicializacao da conexao
	cliente = ss.accept();
	// retornou de accept(), solicitacao recebida
	System.out.println("Server: Processando solicitacao de " +
			   cliente.getInetAddress().getHostName());
	DataProvider dp = new DataProvider(cliente);
	new Thread(dp).start();
      }
    }
    catch (Exception e) {
      System.err.println(e);
    }
    finally {
      // fechando servicos...
      try {
	ss.close();
      }
      catch (Exception e) {
	System.err.println(e);
      }
    }
  }
} 