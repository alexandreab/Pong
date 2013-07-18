/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JFrame;

public class Teste2 {
	public static void main(String[] args) throws UnknownHostException,
			IOException {

		String hostname = "localhost";
		int port = 6789;

		Player player1 = new Player(Player.ENEMY);
		Player player2 = new Player(Player.MOUSE);
		// Player player2 = new Player(Player.KEYBOARD);
		Ball ball = new Ball(false);
		Screen screen = new Screen();
		Receptor receptor = new Receptor(player1, player2, ball);

		PongWindow window = new PongWindow(player1, player2, ball, screen,
				receptor);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		boolean isClient=true;
		if (isClient) {
			Socket clientSocket = null;
			DataOutputStream os = null;
			BufferedReader is = null;
			// Initialization section:
			// Try to open a socket on the given port
			// Try to open input and output streams

			try {
				clientSocket = new Socket(hostname, port);
				os = new DataOutputStream(clientSocket.getOutputStream());
				is = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host: " + hostname);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to: "
						+ hostname);
			}

			// If everything has been initialized then we want to write some
			// data
			// to the socket we have opened a connection to on the given port

			if (clientSocket == null || os == null || is == null) {
				System.err.println("Something is wrong. One variable is null.");
				return;
			}

			try {
				while (true) {
					System.out
							.print("Enter an integer (0 to stop connection, -1 to stop server): ");
					BufferedReader br = new BufferedReader(
							new InputStreamReader(System.in));
					String keyboardInput = br.readLine();
					os.writeBytes(keyboardInput + "\n");

					int n = Integer.parseInt(keyboardInput);
					if (n == 0 || n == -1) {
						break;
					}

					String responseLine = is.readLine();
					if (player1.getType() == Player.ENEMY) {
						player1.toObject(responseLine);
					} else {
						player2.toObject(responseLine);
					}

				}

				// clean up:
				// close the output stream
				// close the input stream
				// close the socket

				os.close();
				is.close();
				clientSocket.close();
			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
			}
		}

	}
}
