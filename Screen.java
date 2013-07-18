import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Screen extends JPanel implements ActionListener, MouseListener, KeyListener {
	Pong game;
	public boolean mouse_inside = false;
	public boolean key_up = false;
	public boolean key_down = false;

	public Screen() {
		super();
		this.setBackground (new Color (0, 0, 0));
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
		

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println ("Pressed "+e.getKeyCode()+" "+KeyEvent.VK_UP+" "+KeyEvent.VK_DOWN);
		if (e.getKeyCode() == KeyEvent.VK_UP)
		key_up = true;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		key_down = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		System.out.println ("Released "+e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_UP)
		key_up = false;
		else if (e.getKeyCode() == KeyEvent.VK_DOWN)
		key_down = false;
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouse_inside = true;
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouse_inside = false;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		repaint ();
		
	}

	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		// Prepara il campo di gioco
		if (game.new_game) {
			game.ball.setBall_x(getWidth () / 2);
			game.ball.setBall_y(getHeight () / 2);
			
			double phase = Math.random () * Math.PI / 2 - Math.PI / 4;
			game.ball.setBall_x_speed((int)(Math.cos (phase) * Pong.START_SPEED));
			game.ball.setBall_y_speed((int)(Math.sin (phase) * Pong.START_SPEED));
			
			game.ball.setBall_acceleration_count(0);
			
			if (game.player1.getType() == Player.CPU_HARD || game.player1.getType() == Player.CPU_EASY) {
				game.player1.position = getHeight () / 2;
				game.computeDestination (game.player1);
			}
			if (game.player2.getType() == Player.CPU_HARD || game.player2.getType() == Player.CPU_EASY) {
				game.player2.position = getHeight () / 2;
				game.computeDestination (game.player2);
			}
			
			game.new_game = false;
		}
		
		// Calcola la posizione del primo giocatore
		if (game.player1.getType() == Player.MOUSE || game.player1.getType() == Player.KEYBOARD || game.ball.getBall_x_speed() < 0)
			game.computePosition (game.player1);
		
		// Calcola la posizione del secondo giocatore
		if (game.player2.getType() == Player.MOUSE || game.player2.getType() == Player.KEYBOARD || game.ball.getBall_x_speed() > 0)
			game.computePosition (game.player2);
		
		// Calcola la posizione della pallina
		game.ball.setBall_x((int) (game.ball.getBall_x()+game.ball.getBall_x_speed()));
		game.ball.setBall_y((int) (game.ball.getBall_y()+game.ball.getBall_y_speed()));
		/*
		ball_x += ball_x_speed;
		ball_y += ball_y_speed;
		 */
		if (game.ball.getBall_y_speed() < 0) // Hack to fix double-to-int conversion
			game.ball.setBall_y(game.ball.getBall_y() + 1);
		
		// Accelera la pallina
		if (game.ball.isAcceleration()) {
			game.ball.setBall_acceleration_count(game.ball.getBall_acceleration_count() + 1);
			if (game.ball.getBall_acceleration_count() == Pong.ACCELERATION) {
				game.ball.setBall_x_speed(game.ball.getBall_x_speed() + (int)game.ball.getBall_x_speed() / Math.hypot ((int)game.ball.getBall_x_speed(), (int)game.ball.getBall_y_speed()) * 2);
				game.ball.setBall_y_speed(game.ball.getBall_y_speed() + (int)game.ball.getBall_y_speed() / Math.hypot ((int)game.ball.getBall_x_speed(), (int)game.ball.getBall_y_speed()) * 2);
				game.ball.setBall_acceleration_count(0);
			}
		}
		
		// Border-collision LEFT
		if (game.ball.getBall_x() <= Pong.PADDING + Pong.WIDTH + Pong.RADIUS) {
			int collision_point = game.ball.getBall_y() + (int)(game.ball.getBall_y_speed() / game.ball.getBall_x_speed() * (Pong.PADDING + Pong.WIDTH + Pong.RADIUS - game.ball.getBall_x()));
			if (collision_point > game.player1.position - Pong.HEIGHT - Pong.TOLERANCE && 
			    collision_point < game.player1.position + Pong.HEIGHT + Pong.TOLERANCE) {
				game.ball.setBall_x(2 * (Pong.PADDING + Pong.WIDTH + Pong.RADIUS) - game.ball.getBall_x());
				game.ball.setBall_x_speed(Math.abs (game.ball.getBall_x_speed()));
				game.ball.setBall_y_speed(game.ball.getBall_y_speed() - Math.sin ((double)(game.player1.position - game.ball.getBall_y()) / Pong.HEIGHT * Math.PI / 4)
				                * Math.hypot (game.ball.getBall_x_speed(), game.ball.getBall_y_speed()));
				if (game.player2.getType() == Player.CPU_HARD)
					game.computeDestination (game.player2);
			}
			else {
				game.player2.points ++;
				game.new_game = true;
			}
		}
		
		// Border-collision RIGHT
		if (game.ball.getBall_x() >= getWidth() - Pong.PADDING - Pong.WIDTH - Pong.RADIUS) {
			int collision_point = game.ball.getBall_y() - (int)(game.ball.getBall_y_speed() / game.ball.getBall_x_speed() * (game.ball.getBall_x() - getWidth() + Pong.PADDING + Pong.WIDTH + Pong.RADIUS));
			if (collision_point > game.player2.position - Pong.HEIGHT - Pong.TOLERANCE && 
			    collision_point < game.player2.position + Pong.HEIGHT + Pong.TOLERANCE) {
				game.ball.setBall_x(2 * (getWidth() - Pong.PADDING - Pong.WIDTH - Pong.RADIUS ) - game.ball.getBall_x());
				game.ball.setBall_x_speed(-1 * Math.abs (game.ball.getBall_x_speed()));
				game.ball.setBall_y_speed(game.ball.getBall_y_speed() - Math.sin ((double)(game.player2.position - game.ball.getBall_y()) / Pong.HEIGHT * Math.PI / 4)
				                * Math.hypot (game.ball.getBall_x_speed(), game.ball.getBall_y_speed()));
				if (game.player1.getType() == Player.CPU_HARD)
					game.computeDestination (game.player1);
			}
			else {
				game.player1.points ++;
				game.new_game = true;
			}
		}
		
		// Border-collision TOP
		if (game.ball.getBall_y() <= Pong.RADIUS) {
			game.ball.setBall_y_speed(Math.abs (game.ball.getBall_y_speed()));
			game.ball.setBall_y(2 * Pong.RADIUS - game.ball.getBall_y());
		}
		
		// Border-collision BOTTOM
		if (game.ball.getBall_y() >= getHeight() - Pong.RADIUS) {
			game.ball.setBall_y_speed(-1 * Math.abs (game.ball.getBall_y_speed()));
			game.ball.setBall_y(2 * (getHeight() - Pong.RADIUS) - game.ball.getBall_y());
		}
		
		// Disegna i carrelli
		g.setColor (Color.WHITE);
		g.fillRect (Pong.PADDING, game.player1.position - Pong.HEIGHT, Pong.WIDTH, Pong.HEIGHT * 2);
		g.fillRect (getWidth() - Pong.PADDING - Pong.WIDTH, game.player2.position - Pong.HEIGHT, Pong.WIDTH, Pong.HEIGHT * 2);
		
		// Disegna la palla
		g.fillOval (game.ball.getBall_x() - Pong.RADIUS, game.ball.getBall_y() - Pong.RADIUS, Pong.RADIUS*2, Pong.RADIUS*2);
		
		// Disegna i punti
		g.drawString (game.player1.points+" ", getWidth() / 2 - 20, 20);
		g.drawString (game.player2.points+" ", getWidth() / 2 + 20, 20);
	}
	

}
