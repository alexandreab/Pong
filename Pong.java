/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Pong extends JPanel implements ActionListener, MouseListener, KeyListener {
	// Proprietà della palla
	private static final int RADIUS = 10; // Raggio
	private static final int START_SPEED = 9; // Velocità iniziale
	private static final int ACCELERATION = 125; // Ogni quanti frame aumenta di 1 pixel la velocità 

	// Proprietà dei carrelli
	private static final int SPEED = 12; // Velocità dei carrelli
	private static final int HEIGHT = 50; // SEMI-altezza del carrello
	private static final int WIDTH = 20;
	private static final int TOLERANCE = 5;
	private static final int PADDING = 10;
	
	private Player player1;
	private Player player2;
	
	private boolean new_game = true;
	
	public Ball ball;
	private boolean mouse_inside = false;
	private boolean key_up = false;
	private boolean key_down = false;
	
	// Constructor
	public Pong (Player p1, Player p2, Ball ball) {
		super ();
		setBackground (new Color (0, 0, 0));
		
		player1 = p1;
		player2 = p2;
		this.ball = ball;
	}
	
	// Compute destination of the ball
	private void computeDestination (Player player) {
		if (ball.getBall_x_speed() > 0)
			player.destination = ball.getBall_y() + (getWidth() - PADDING - WIDTH - RADIUS - ball.getBall_x()) * (int)(ball.getBall_y_speed()) / (int)(ball.getBall_x_speed());
		else
			player.destination = ball.getBall_y() - (ball.getBall_x() - PADDING - WIDTH - RADIUS) * (int)(ball.getBall_y_speed()) / (int)(ball.getBall_x_speed());
		
		if (player.destination <= RADIUS)
			player.destination = 2 * PADDING - player.destination;
		
		if (player.destination > getHeight() - 10) {
			player.destination -= RADIUS;
			if ((player.destination / (getHeight() - 2 * RADIUS)) % 2 == 0)
				player.destination = player.destination % (getHeight () - 2 * RADIUS);
			else
				player.destination = getHeight() - 2 * RADIUS - player.destination % (getHeight () - 2 * RADIUS);
			player.destination += RADIUS;
		}
	}
	
	// Set new position of the player
	private void movePlayer (Player player, int destination) {
		int distance = Math.abs (player.position - destination);
		
		if (distance != 0) {
			int direction = - (player.position - destination) / distance;
			
			if (distance > SPEED)
				distance = SPEED;
			
			player.position += direction * distance;
			
			if (player.position - HEIGHT < 0)
				player.position = HEIGHT;
			if (player.position + HEIGHT > getHeight())
				player.position = getHeight() - HEIGHT;
		}
	}
	
	// Compute player position
	private void computePosition (Player player) {
		// MOUSE
		if (player.getType() == Player.MOUSE) {
			if (mouse_inside) {
				int cursor = getMousePosition().y;
				movePlayer (player, cursor);
			}
		}
		// KEYBOARD
		else if (player.getType() == Player.KEYBOARD) {
			if (key_up && !key_down) {
				movePlayer (player, player.position - SPEED);
			}
			else if (key_down && !key_up) {
				movePlayer (player, player.position + SPEED);
			}
		}
		// CPU HARD
		else if (player.getType() == Player.CPU_HARD) {
			movePlayer (player, player.destination);
		}
		// CPU EASY
		else if (player.getType() == Player.CPU_EASY) {
			movePlayer (player, ball.getBall_y());
		}
	}
	
	// Draw
	public void paintComponent (Graphics g) {
		super.paintComponent (g);
		
		// Prepara il campo di gioco
		if (new_game) {
			ball.setBall_x(getWidth () / 2);
			ball.setBall_y(getHeight () / 2);
			
			double phase = Math.random () * Math.PI / 2 - Math.PI / 4;
			ball.setBall_x_speed((int)(Math.cos (phase) * START_SPEED));
			ball.setBall_y_speed((int)(Math.sin (phase) * START_SPEED));
			
			ball.setBall_acceleration_count(0);
			
			if (player1.getType() == Player.CPU_HARD || player1.getType() == Player.CPU_EASY) {
				player1.position = getHeight () / 2;
				computeDestination (player1);
			}
			if (player2.getType() == Player.CPU_HARD || player2.getType() == Player.CPU_EASY) {
				player2.position = getHeight () / 2;
				computeDestination (player2);
			}
			
			new_game = false;
		}
		
		// Calcola la posizione del primo giocatore
		if (player1.getType() == Player.MOUSE || player1.getType() == Player.KEYBOARD || ball.getBall_x_speed() < 0)
			computePosition (player1);
		
		// Calcola la posizione del secondo giocatore
		if (player2.getType() == Player.MOUSE || player2.getType() == Player.KEYBOARD || ball.getBall_x_speed() > 0)
			computePosition (player2);
		
		// Calcola la posizione della pallina
		ball.setBall_x((int) (ball.getBall_x()+ball.getBall_x_speed()));
		ball.setBall_y((int) (ball.getBall_y()+ball.getBall_y_speed()));
		/*
		ball_x += ball_x_speed;
		ball_y += ball_y_speed;
		 */
		if (ball.getBall_y_speed() < 0) // Hack to fix double-to-int conversion
			ball.setBall_y(ball.getBall_y() + 1);
		
		// Accelera la pallina
		if (ball.isAcceleration()) {
			ball.setBall_acceleration_count(ball.getBall_acceleration_count() + 1);
			if (ball.getBall_acceleration_count() == ACCELERATION) {
				ball.setBall_x_speed(ball.getBall_x_speed() + (int)ball.getBall_x_speed() / Math.hypot ((int)ball.getBall_x_speed(), (int)ball.getBall_y_speed()) * 2);
				ball.setBall_y_speed(ball.getBall_y_speed() + (int)ball.getBall_y_speed() / Math.hypot ((int)ball.getBall_x_speed(), (int)ball.getBall_y_speed()) * 2);
				ball.setBall_acceleration_count(0);
			}
		}
		
		// Border-collision LEFT
		if (ball.getBall_x() <= PADDING + WIDTH + RADIUS) {
			int collision_point = ball.getBall_y() + (int)(ball.getBall_y_speed() / ball.getBall_x_speed() * (PADDING + WIDTH + RADIUS - ball.getBall_x()));
			if (collision_point > player1.position - HEIGHT - TOLERANCE && 
			    collision_point < player1.position + HEIGHT + TOLERANCE) {
				ball.setBall_x(2 * (PADDING + WIDTH + RADIUS) - ball.getBall_x());
				ball.setBall_x_speed(Math.abs (ball.getBall_x_speed()));
				ball.setBall_y_speed(ball.getBall_y_speed() - Math.sin ((double)(player1.position - ball.getBall_y()) / HEIGHT * Math.PI / 4)
				                * Math.hypot (ball.getBall_x_speed(), ball.getBall_y_speed()));
				if (player2.getType() == Player.CPU_HARD)
					computeDestination (player2);
			}
			else {
				player2.points ++;
				new_game = true;
			}
		}
		
		// Border-collision RIGHT
		if (ball.getBall_x() >= getWidth() - PADDING - WIDTH - RADIUS) {
			int collision_point = ball.getBall_y() - (int)(ball.getBall_y_speed() / ball.getBall_x_speed() * (ball.getBall_x() - getWidth() + PADDING + WIDTH + RADIUS));
			if (collision_point > player2.position - HEIGHT - TOLERANCE && 
			    collision_point < player2.position + HEIGHT + TOLERANCE) {
				ball.setBall_x(2 * (getWidth() - PADDING - WIDTH - RADIUS ) - ball.getBall_x());
				ball.setBall_x_speed(-1 * Math.abs (ball.getBall_x_speed()));
				ball.setBall_y_speed(ball.getBall_y_speed() - Math.sin ((double)(player2.position - ball.getBall_y()) / HEIGHT * Math.PI / 4)
				                * Math.hypot (ball.getBall_x_speed(), ball.getBall_y_speed()));
				if (player1.getType() == Player.CPU_HARD)
					computeDestination (player1);
			}
			else {
				player1.points ++;
				new_game = true;
			}
		}
		
		// Border-collision TOP
		if (ball.getBall_y() <= RADIUS) {
			ball.setBall_y_speed(Math.abs (ball.getBall_y_speed()));
			ball.setBall_y(2 * RADIUS - ball.getBall_y());
		}
		
		// Border-collision BOTTOM
		if (ball.getBall_y() >= getHeight() - RADIUS) {
			ball.setBall_y_speed(-1 * Math.abs (ball.getBall_y_speed()));
			ball.setBall_y(2 * (getHeight() - RADIUS) - ball.getBall_y());
		}
		
		// Disegna i carrelli
		g.setColor (Color.WHITE);
		g.fillRect (PADDING, player1.position - HEIGHT, WIDTH, HEIGHT * 2);
		g.fillRect (getWidth() - PADDING - WIDTH, player2.position - HEIGHT, WIDTH, HEIGHT * 2);
		
		// Disegna la palla
		g.fillOval (ball.getBall_x() - RADIUS, ball.getBall_y() - RADIUS, RADIUS*2, RADIUS*2);
		
		// Disegna i punti
		g.drawString (player1.points+" ", getWidth() / 2 - 20, 20);
		g.drawString (player2.points+" ", getWidth() / 2 + 20, 20);
	}
	
	// New frame
	public void actionPerformed (ActionEvent e) {
		repaint ();
	}
	
	// Mouse inside
	public void mouseEntered (MouseEvent e) {
		mouse_inside = true;
	}
	
	// Mouse outside
	public void mouseExited (MouseEvent e) {
		mouse_inside = false;
	}
	
	// Mouse pressed
	public void mousePressed (MouseEvent e) {}
	
	// Mouse released
	public void mouseReleased (MouseEvent e) {}
		
	// Mouse clicked
	public void mouseClicked (MouseEvent e) {}
	
	// Key pressed
	public void keyPressed (KeyEvent e) {
//		System.out.println ("Pressed "+e.getKeyCode()+"   "+KeyEvent.VK_UP+" "+KeyEvent.VK_DOWN);
		if (e.getKeyCode() == KeyEvent.VK_UP)
			key_up = true;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			key_down = true;
	}
	
	// Key released
	public void keyReleased (KeyEvent e) {
//		System.out.println ("Released "+e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_UP)
			key_up = false;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			key_down = false;
	}
	
	// Key released
	public void keyTyped (KeyEvent e) {}
}
