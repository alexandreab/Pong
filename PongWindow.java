/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class PongWindow extends JFrame {
	public PongWindow () {
		super ();
		Player player1 = new Player(Player.CPU_HARD);
		Player player2 = new Player(Player.KEYBOARD);
		Ball ball = new Ball(false);
		Screen screen = new Screen();
		Receptor receptor = new Receptor(player1, player2, ball);
		configureObjects(player1, player2, ball, screen, receptor);
	}

	public PongWindow (Player player1, Player player2, Ball ball, Screen screen, Receptor receptor) {
		super ();
		
		configureObjects(player1, player2, ball, screen, receptor);
	}
	
	
	private void configureObjects(Player player1, Player player2, Ball ball, Screen screen, Receptor receptor) {
		setTitle ("Pong");
		setSize (640, 480);
		Pong content = new Pong (player1, player2, ball, screen, receptor);
		content.ball.setAcceleration(true);
		
		getContentPane().add(screen);
		addMouseListener (screen);
		addKeyListener (screen);
		
		Timer timer = new Timer (20, screen);
		timer.start ();
	}

}
