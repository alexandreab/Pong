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
	static final int ACCELERATION = 125; // Ogni quanti frame aumenta di 1 pixel
											// la velocità

	// Proprietà dei carrelli
	private static final int SPEED = 12; // Velocità dei carrelli
	static final int HEIGHT = 50; // SEMI-altezza del carrello
	static final int WIDTH = 20;
	static final int TOLERANCE = 5;
	static final int PADDING = 10;
	static int TYPE = 2; // Servidor

	Player player1;
	Player player2;

	boolean new_game = true;

	public Ball ball;
	Screen screen;


	// Constructor
	public Pong(Player p1, Player p2, Ball ball, Screen screen) {
		super();

		player1 = p1;
		player2 = p2;
		this.ball = ball;


		this.screen = screen;
		screen.game = this;
	}

	// Compute destination of the ball
	void computeDestination(Player player) {
		if (TYPE == 1) {
			//player.destination = receptor.getPlayerDestination(player);
		} else {
			if (ball.getBall_x_speed() > 0)
				player.destination = ball.getBall_y()
						+ (screen.getWidth() - PADDING - WIDTH - RADIUS - ball
								.getBall_x()) * (int) (ball.getBall_y_speed())
						/ (int) (ball.getBall_x_speed());
			else
				player.destination = ball.getBall_y()
						- (ball.getBall_x() - PADDING - WIDTH - RADIUS)
						* (int) (ball.getBall_y_speed())
						/ (int) (ball.getBall_x_speed());

			if (player.destination <= RADIUS)
				player.destination = 2 * PADDING - player.destination;

			if (player.destination > screen.getHeight() - 10) {
				player.destination -= RADIUS;
				if ((player.destination / (screen.getHeight() - 2 * RADIUS)) % 2 == 0)
					player.destination = player.destination
							% (screen.getHeight() - 2 * RADIUS);
				else
					player.destination = screen.getHeight() - 2 * RADIUS
							- player.destination
							% (screen.getHeight() - 2 * RADIUS);
				player.destination += RADIUS;
			}
		}
	}

	// Set new position of the player
	private void movePlayer(Player player, int destination) {
		int distance = Math.abs(player.position - destination);

		if (distance != 0) {
			int direction = -(player.position - destination) / distance;

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
	void computePosition(Player player) {

		if (player.getType() == Player.ENEMY) {
			// player.position = receptor.getPlayerPosition(player);
		}
		// MOUSE
		else if (player.getType() == Player.MOUSE) {
			if (screen.mouse_inside) {
				int cursor = screen.getMousePosition().y;
				movePlayer(player, cursor);
			}
		}
		// KEYBOARD
		else if (player.getType() == Player.KEYBOARD) {
			if (screen.key_up && !screen.key_down) {
				movePlayer(player, player.position - SPEED);
			} else if (screen.key_down && !screen.key_up) {
				movePlayer(player, player.position + SPEED);
			}
		}
		// CPU HARD
		else if (player.getType() == Player.CPU_HARD) {
			movePlayer(player, player.destination);
		}
		// CPU EASY
		else if (player.getType() == Player.CPU_EASY) {
			movePlayer(player, ball.getBall_y());
		}
	}

	void computeBall(Screen screen) {

		// Prepara il campo di gioco
		checkNewGame(screen);

		if (TYPE == 1) {
			// ball.setBall_x(receptor.getBall_x());
			// .setBall_y(receptor.getBall_y());
			// .setBall_x_speed(receptor.getBall_x_speed());
			// ball.setBall_y_speed(receptor.getBall_y_speed());
			// ball.setAcceleration(receptor.isAcceleration());
			// ball.setBall_acceleration_count(receptor.getBall_acceleration_count());
			// Calcola la posizione del primo giocatore
			if (player1.getType() == Player.MOUSE
					|| player1.getType() == Player.KEYBOARD
					|| ball.getBall_x_speed() < 0)
				computePosition(player1);

			// Calcola la posizione del secondo giocatore
			if (player2.getType() == Player.MOUSE
					|| player2.getType() == Player.KEYBOARD
					|| ball.getBall_x_speed() > 0)
				computePosition(player2);
		} else {

			// Calcola la posizione del primo giocatore
			if (player1.getType() == Player.MOUSE
					|| player1.getType() == Player.KEYBOARD
					|| ball.getBall_x_speed() < 0)
				computePosition(player1);

			// Calcola la posizione del secondo giocatore
			if (player2.getType() == Player.MOUSE
					|| player2.getType() == Player.KEYBOARD
					|| ball.getBall_x_speed() > 0)
				computePosition(player2);

			// Calcola la posizione della pallina
			ball.setBall_x((int) (ball.getBall_x() + ball.getBall_x_speed()));
			ball.setBall_y((int) (ball.getBall_y() + ball.getBall_y_speed()));
			/*
			 * ball_x += ball_x_speed; ball_y += ball_y_speed;
			 */
			if (ball.getBall_y_speed() < 0) // Hack to fix double-to-int
											// conversion
				ball.setBall_y(ball.getBall_y() + 1);

			// Accelera la pallina
			if (ball.isAcceleration()) {
				ball.setBall_acceleration_count(ball
						.getBall_acceleration_count() + 1);
				if (ball.getBall_acceleration_count() == Pong.ACCELERATION) {
					ball.setBall_x_speed(ball.getBall_x_speed()
							+ (int) ball.getBall_x_speed()
							/ Math.hypot((int) ball.getBall_x_speed(),
									(int) ball.getBall_y_speed()) * 2);
					ball.setBall_y_speed(ball.getBall_y_speed()
							+ (int) ball.getBall_y_speed()
							/ Math.hypot((int) ball.getBall_x_speed(),
									(int) ball.getBall_y_speed()) * 2);
					ball.setBall_acceleration_count(0);
				}
			}
		}
		checkCollision(screen);

	}

	private void checkCollision(Screen screen) {
		// Border-collision LEFT
		if (ball.getBall_x() <= Pong.PADDING + Pong.WIDTH + Pong.RADIUS) {
			int collision_point = ball.getBall_y()
					+ (int) (ball.getBall_y_speed() / ball.getBall_x_speed() * (Pong.PADDING
							+ Pong.WIDTH + Pong.RADIUS - ball.getBall_x()));
			if (collision_point > player1.position - Pong.HEIGHT
					- Pong.TOLERANCE
					&& collision_point < player1.position + Pong.HEIGHT
							+ Pong.TOLERANCE) {
				ball.setBall_x(2 * (Pong.PADDING + Pong.WIDTH + Pong.RADIUS)
						- ball.getBall_x());
				ball.setBall_x_speed(Math.abs(ball.getBall_x_speed()));
				ball.setBall_y_speed(ball.getBall_y_speed()
						- Math.sin((double) (player1.position - ball
								.getBall_y()) / Pong.HEIGHT * Math.PI / 4)
						* Math.hypot(ball.getBall_x_speed(),
								ball.getBall_y_speed()));
				if (player2.getType() == Player.CPU_HARD)
					computeDestination(player2);
			} else {
				player2.points++;
				new_game = true;
			}
		}

		// Border-collision RIGHT
		if (ball.getBall_x() >= screen.getWidth() - Pong.PADDING - Pong.WIDTH
				- Pong.RADIUS) {
			int collision_point = ball.getBall_y()
					- (int) (ball.getBall_y_speed() / ball.getBall_x_speed() * (ball
							.getBall_x()
							- screen.getWidth()
							+ Pong.PADDING
							+ Pong.WIDTH + Pong.RADIUS));
			if (collision_point > player2.position - Pong.HEIGHT
					- Pong.TOLERANCE
					&& collision_point < player2.position + Pong.HEIGHT
							+ Pong.TOLERANCE) {
				ball.setBall_x(2
						* (screen.getWidth() - Pong.PADDING - Pong.WIDTH - Pong.RADIUS)
						- ball.getBall_x());
				ball.setBall_x_speed(-1 * Math.abs(ball.getBall_x_speed()));
				ball.setBall_y_speed(ball.getBall_y_speed()
						- Math.sin((double) (player2.position - ball
								.getBall_y()) / Pong.HEIGHT * Math.PI / 4)
						* Math.hypot(ball.getBall_x_speed(),
								ball.getBall_y_speed()));
				if (player1.getType() == Player.CPU_HARD)
					computeDestination(player1);
			} else {
				player1.points++;
				new_game = true;
			}
		}

		// Border-collision TOP
		if (ball.getBall_y() <= Pong.RADIUS) {
			ball.setBall_y_speed(Math.abs(ball.getBall_y_speed()));
			ball.setBall_y(2 * Pong.RADIUS - ball.getBall_y());
		}

		// Border-collision BOTTOM
		if (ball.getBall_y() >= screen.getHeight() - Pong.RADIUS) {
			ball.setBall_y_speed(-1 * Math.abs(ball.getBall_y_speed()));
			ball.setBall_y(2 * (screen.getHeight() - Pong.RADIUS)
					- ball.getBall_y());
		}
	}

	private void checkNewGame(Screen screen) {
		if (new_game) {
			ball.setBall_x(screen.getWidth() / 2);
			ball.setBall_y(screen.getHeight() / 2);

			double phase = Math.random() * Math.PI / 2 - Math.PI / 4;
			ball.setBall_x_speed((int) (Math.cos(phase) * Pong.START_SPEED));
			ball.setBall_y_speed((int) (Math.sin(phase) * Pong.START_SPEED));

			ball.setBall_acceleration_count(0);

			if (player1.getType() == Player.CPU_HARD
					|| player1.getType() == Player.CPU_EASY) {
				player1.position = screen.getHeight() / 2;
				computeDestination(player1);
			}
			if (player2.getType() == Player.CPU_HARD
					|| player2.getType() == Player.CPU_EASY) {
				player2.position = screen.getHeight() / 2;
				computeDestination(player2);
			}

			new_game = false;
		}
	}
}
