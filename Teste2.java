/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

public class Teste2 {
	static Player player1 = new Player(Player.MOUSE);
	static Player player2 = new Player(Player.ENEMY);
	public static void main(String[] args) throws UnknownHostException,
			IOException {

		String hostname = "localhost";
		int port = 6789;

		// Player player2 = new Player(Player.KEYBOARD);
		Ball ball = new Ball(false);
		Screen screen = new Screen();

		PongWindow window = new PongWindow(player1, player2, ball, screen);
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
		else {
			port = 6789;
			Teste2 server = new Teste2( port );
			server.startServer();
		}

	}
// declare a server socket and a client socket for the server
    
    ServerSocket echoServer = null;
    Socket clientSocket = null;
    int port;
    
    public Teste2( int port ) {
	this.port = port;
    }
    
    public void stopServer() {
	System.out.println( "Server cleaning up." );
	System.exit(0);
    }
    
    public void startServer() {
	// Try to open a server socket on the given port
	// Note that we can't choose a port less than 1024 if we are not
	// privileged users (root)
	
        try {
	    echoServer = new ServerSocket(port);
        }
        catch (IOException e) {
	    System.out.println(e);
        }   
	
	System.out.println( "Waiting for connections. Only one connection is allowed." );
	
	// Create a socket object from the ServerSocket to listen and accept connections.
	// Use Server1Connection to process the connection.
	
	while ( true ) {
	    try {
		clientSocket = echoServer.accept();
		Server1Connection oneconnection = new Server1Connection(clientSocket, this, player1, player2);
		oneconnection.run();
	    }   
	    catch (IOException e) {
		System.out.println(e);
	    }
	}
    }
}

class Server1Connection {
    BufferedReader is;
    PrintStream os;
    Socket clientSocket;
    Teste2 server;
    Player player1;
    Player player2;
    public Server1Connection(Socket clientSocket, Teste2 server, Player player1, Player player2) {
	this.clientSocket = clientSocket;
	this.server = server;
	System.out.println( "Connection established with: " + clientSocket );
	try {
	    is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	    os = new PrintStream(clientSocket.getOutputStream());
	} catch (IOException e) {
	    System.out.println(e);
	}
    }
    
    public void run() {
        String line;
	try {
	    boolean serverStop = false;
	    
            while (true) {
                line = is.readLine();
                if (player1.getType() == Player.ENEMY) {
					player1.toObject(line);
				} else {
					player2.toObject(line);
				}
		System.out.println( "Received " + line );
                int n = Integer.parseInt(line);
		if ( n == -1 ) {
		    serverStop = true;
		    break;
		}
		if ( n == 0 ) break;
                os.println("" + n*n ); 
            }
	    
	    System.out.println( "Connection closed." );
            is.close();
            os.close();
            clientSocket.close();
	    
	    if ( serverStop ) server.stopServer();
	} catch (IOException e) {
	    System.out.println(e);
	}
    }
}