import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Receptor extends Thread{
	Player player1;
	Player player2;
	Ball ball;
	boolean openConnection;
	public Receptor(Player player1, Player player2, Ball ball) {
		this.player1 = player1;
		this.player2 = player2;
		this.ball = ball;
		openConnection = true;
	}

	public int connectSend(String ip, int port) throws UnknownHostException, IOException{
		

		return 0;
	}
	public int connectReceive(String ip, int port) throws UnknownHostException, IOException{

		return 0;
	}
	

	public int getPlayerDestination(Player player) {
		// TODO Auto-generated method stub
		return 200;
	}

	public int getPlayerPosition(Player player) {
		// TODO Auto-generated method stub
		return 400;
	}

	public int getBall_x() {
		return 0;
	}

	public int getBall_y() {
		return 0;
	}

	public double getBall_x_speed() {
		return 0;
	}

	public double getBall_y_speed() {
		return 0;
	}

	public int getBall_acceleration_count() {
		return 0;
	}

	public boolean isAcceleration() {
		// TODO Auto-generated method stub
		return false;
	}

}
