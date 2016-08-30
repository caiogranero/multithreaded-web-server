import java.net.*;
import java.io.*;
/**
 *
 * @author Valter
 */
public class Cliente extends Thread {
    // aqui no nosso caso será o 'localhost' que o ip da própria maquina aonde você executará
    private String server;
    // neste caso utilizaremos a partir da porta 1024, sendo que menor são para o Sistema Operacional
    private int porta;
    // aqui será a nossa string que será enviada para o servidor e ficará repetindo diversas vezes
    private String texto;
    public Cliente(String server, int porta) {
        try{
        // preparando o buffer para ler a stream (no caso o teclado)
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        // estamos lendo a stream que esta vindo do usuário
        texto = inFromUser.readLine();
        }catch(Exception e){
            e.printStackTrace();
        }
        this.server = server;
        this.porta = porta;
    }
    public static void main(String[] args) {
        try {
            // inicializando quem será nosso servidor (nossa máquina)
            String server = "localhost";
            // o numero da porta
            int porta = 1024;
            // o numero máx de clientes que podem ser atendidos, mais que isso começaremos a ter muitas exceções
            int numeroDeClientes = 10;
            for (int i = 0; i < numeroDeClientes; i++) {
                // criando um novo cliente para nosso servidor
                new Cliente(server, porta).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try {
            while (true) {
                // abrindo uma porta para fazermos a comunicação entre nosso cliente e nosso servidor
                Socket s = new Socket(server, porta);
                // instaciando para enviarmos ao nosso servidor o que digitamos no cliente
                ObjectOutputStream oo = new ObjectOutputStream(s.getOutputStream());
                // informando aonde estamos conectados
                System.out.println("Conectado a " + server + ":" + porta);
                // enviando ao nosso servidor a nossa string
                oo.writeObject(texto);
                //fechando a nossa conexão
                s.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}