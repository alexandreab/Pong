/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

import javax.swing.JFrame;

public class Teste2 {
	public static void main (String[] args) {
		Player player1 = new Player(Player.MOUSE);
		Player player2 = new Player(Player.KEYBOARD);
		Ball ball = new Ball(false);
		PongWindow window = new PongWindow (player1,player2, ball);
		window.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		window.setVisible (true);
//		while(true) {
//			System.out.println("bola: "+ball.getBall_x());
//		}
	}
}
