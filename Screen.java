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
		game.computeBall(this);
		
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
