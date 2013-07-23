/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Teste2 {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Player playerLeft;
		Player playerRight;
		String tipo = "cliente";
		PongWindow window;

		if (args.length > 0) {
			tipo = args[0];
		}

		System.out.println(tipo);

		Ball ball = new Ball(false);
		Screen screen = new Screen();
		Connection conexao;
		if (tipo.compareTo("servidor") == 0) {
			playerLeft = new Player(Player.MOUSE);
			playerRight = new Player(Player.ENEMY);

			conexao = new Connection(1, playerLeft, playerRight, ball, 7656,
					"127.0.0.1");
			System.out.println("é do tipo: " + tipo);
		} else {
			System.out.println("é do tipo: " + tipo);
			playerLeft = new Player(Player.ENEMY);
			playerRight = new Player(Player.MOUSE);
			conexao = new Connection(0, playerRight, playerLeft, ball, 7656,
					"127.0.0.1");

		}
		Thread t1 = new Thread(conexao);
		t1.start();

		window = new PongWindow(playerLeft, playerRight, ball, screen);

		if (tipo.compareTo("servidor") == 0) {
			window.setTitle("Servidor Pong");
		} else {
			window.setTitle("Cliente Pong");
		}

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

	}

}