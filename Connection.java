import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection implements Runnable {
	private Player me;
	private Player enemy;
	private Ball ball;
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
				String linha = entrada.readLine();
				while (linha != null && !(linha.trim().equals(""))) {

					// System.out.println("Recebi "+ linha);
					enemy.toObject(linha);
					linha = entrada.readLine();

					PrintStream saida = new PrintStream(
							conexao.getOutputStream());
					saida.println(me.toString());

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
			Socket conexao = new Socket(host, 2000);
			BufferedReader entrada = new BufferedReader(new InputStreamReader(
					conexao.getInputStream()));
			PrintStream saida = new PrintStream(conexao.getOutputStream());
			String linha;
			// loop principal
			while (true) {
				linha = me.toString();
				saida.println(linha);
				
				
				linha = entrada.readLine();
				enemy.toObject(linha);
				if (linha == null) {
					System.out.println("Conexão encerrada!");
					break;
				}
				conexao.close();
			}
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
