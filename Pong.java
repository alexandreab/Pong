/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */


public class Pong {
	// Proprietà della palla
	static final int RADIUS = 10; // Raggio
	static final int START_SPEED = 9; // Velocità iniziale
	static final int ACCELERATION = 125; // Ogni quanti frame aumenta di 1 pixel la velocità 

	// Proprietà dei carrelli
	private static final int SPEED = 12; // Velocità dei carrelli
	static final int HEIGHT = 50; // SEMI-altezza del carrello
	static final int WIDTH = 20;
	static final int TOLERANCE = 5;
	static final int PADDING = 10;
	
	Player player1;
	Player player2;
	
	boolean new_game = true;
	
	public Ball ball;
	Screen screen;
	// Constructor
	public Pong (Player p1, Player p2, Ball ball, Screen screen) {
		super ();

		player1 = p1;
		player2 = p2;
		this.ball = ball;
		
		this.screen = screen;
		screen.game = this;
	}
	
	// Compute destination of the ball
	void computeDestination (Player player) {
		if (ball.getBall_x_speed() > 0)
			player.destination = ball.getBall_y() + (screen.getWidth() - PADDING - WIDTH - RADIUS - ball.getBall_x()) * (int)(ball.getBall_y_speed()) / (int)(ball.getBall_x_speed());
		else
			player.destination = ball.getBall_y() - (ball.getBall_x() - PADDING - WIDTH - RADIUS) * (int)(ball.getBall_y_speed()) / (int)(ball.getBall_x_speed());
		
		if (player.destination <= RADIUS)
			player.destination = 2 * PADDING - player.destination;
		
		if (player.destination > screen.getHeight() - 10) {
			player.destination -= RADIUS;
			if ((player.destination / (screen.getHeight() - 2 * RADIUS)) % 2 == 0)
				player.destination = player.destination % (screen.getHeight () - 2 * RADIUS);
			else
				player.destination = screen.getHeight() - 2 * RADIUS - player.destination % (screen.getHeight () - 2 * RADIUS);
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
			if (player.position + HEIGHT > screen.getHeight())
				player.position = screen.getHeight() - HEIGHT;
		}
	}
	
	// Compute player position
	void computePosition (Player player) {
		// MOUSE
		if (player.getType() == Player.MOUSE) {
			if (screen.mouse_inside) {
				int cursor = screen.getMousePosition().y;
				movePlayer (player, cursor);
			}
		}
		// KEYBOARD
		else if (player.getType() == Player.KEYBOARD) {
			if (screen.key_up && !screen.key_down) {
				movePlayer (player, player.position - SPEED);
			}
			else if (screen.key_down && !screen.key_up) {
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
	/*
	// New frame
	public void actionPerformed (ActionEvent e) {
		screen.repaint ();
	}
	
	// Mouse inside
	public void mouseEntered (MouseEvent e) {
		screen.mouse_inside = true;
	}
	
	// Mouse outside
	public void mouseExited (MouseEvent e) {
		screen.mouse_inside = false;
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
			screen.key_up = true;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			screen.key_down = true;
	}
	
	// Key released
	public void keyReleased (KeyEvent e) {
//		System.out.println ("Released "+e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_UP)
			screen.key_up = false;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
			screen.key_down = false;
	}
	
	// Key released
	public void keyTyped (KeyEvent e) {}
	*/
}
