import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements Runnable {
	private Player me;
	private Player enemy;
	//private Ball ball;
	private int port;
	private String host;
	private int opt;

	public Connection(int opt, Player me, Player enemy, Ball ball, int port, String host) {
		this.me = me;
		this.enemy = enemy;
		this.port = port;
		this.host = host;
		this.opt = opt;
	}

	public void executaServidor(int port) {
		try {
			ServerSocket s = new ServerSocket(port);
			while (true) {
				System.out.print("Esperando alguém se conectar...");
				Socket conexao = s.accept();
				System.out.println(" Conectou!");

				BufferedReader entrada = new BufferedReader(
						new InputStreamReader(conexao.getInputStream()));
				String linha;
				while (true) {
					
					PrintStream saida = new PrintStream(
							conexao.getOutputStream());
					linha = me.toString();
					saida.println(linha);
					System.out.println("Estou enviando:     "+linha);
					
					
					// System.out.println("Recebi "+ linha);
					
					linha = entrada.readLine();
					System.out.println("Estou recebendo: "+ linha);
					
					
					if (linha == null) {
						System.out.println("Conexão encerrada!");
						break;
					}
					enemy.toObject(linha);
					

				}
				conexao.close();
				s.close();
			}
		} catch (IOException e) {
			// TODO: handle exception
			System.out.println("IOException do servidor: " + e);
		}
	}
	public void executaCliente(String host) {
		
		
		try {
			if(host.isEmpty())
				host = "127.0.0.1";
			Socket conexao = new Socket(host, port);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(
					conexao.getInputStream()));
			PrintStream saida = new PrintStream(conexao.getOutputStream());
			String linha;
			// loop principal
			while (true) {
				
				linha = entrada.readLine();
				System.out.println("Estou recebendo: "+ linha);
				if (linha == null) {
					System.out.println("Conexão encerrada!");
					break;
				}
				enemy.toObject(linha);
				System.out.println("Cliente diz: Passei pro meu inimigo local.");
				
				linha = me.toString();
				System.out.println("Estou enviando:     "+linha);
				saida.println(linha);
			}
			conexao.close();
		} catch (IOException e) {
			// caso ocorra alguma excessão de E/S, mostre qual foi.
			System.out.println("IOException do cliente: " + e);
		}
		
		
		
	}

	@Override
	public void run() {
		System.out.println("A thread de conexao comecou.");
		if(this.opt==1){ //Servidor
			System.out.println("Conexao do servidor");
			executaServidor(this.port);
		}
		else {
			executaCliente(host);
		}
		System.out.println("Executei a funcao devida.");
		// TODO Auto-generated method stub

	}

}
