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
		
		setTitle ("Pong");
		setSize (640, 480);
		
		Player player1 = new Player(Player.CPU_HARD);
		Player player2 = new Player(Player.KEYBOARD);
		Ball ball = new Ball(false);
		
		Pong content = new Pong (player1,player2, ball);
		content.ball.setAcceleration(true);
		getContentPane ().add (content);
		
		addMouseListener (content);
		addKeyListener (content);
		
		Timer timer = new Timer (20, content);
		timer.start ();
	}
	public PongWindow (Player player1, Player player2, Ball ball) {
		super ();
		setTitle ("Pong");
		setSize (640, 480);
		
		Pong content = new Pong (player1, player2, ball);
		content.ball.setAcceleration(true);
		getContentPane ().add (content);
		
		addMouseListener (content);
		addKeyListener (content);
		
		Timer timer = new Timer (20, content);
		timer.start ();
	}

}
