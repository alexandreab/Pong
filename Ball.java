
public class Ball {
	private int ball_x;
	private int ball_y;
	private double ball_x_speed;
	private double ball_y_speed;
	private boolean acceleration;
	private int ball_acceleration_count;

	public Ball(boolean acceleration) {
		this.acceleration = acceleration;
	}

	public int getBall_x() {
		return ball_x;
	}

	public void setBall_x(int ball_x) {
		this.ball_x = ball_x;
	}

	public int getBall_y() {
		return ball_y;
	}

	public void setBall_y(int ball_y) {
		this.ball_y = ball_y;
	}

	public double getBall_x_speed() {
		return ball_x_speed;
	}

	public void setBall_x_speed(double ball_x_speed) {
		this.ball_x_speed = ball_x_speed;
	}

	public double getBall_y_speed() {
		return ball_y_speed;
	}

	public void setBall_y_speed(double ball_y_speed) {
		this.ball_y_speed = ball_y_speed;
	}

	public boolean isAcceleration() {
		return acceleration;
	}

	public void setAcceleration(boolean acceleration) {
		this.acceleration = acceleration;
	}

	public int getBall_acceleration_count() {
		return ball_acceleration_count;
	}

	public void setBall_acceleration_count(int ball_acceleration_count) {
		this.ball_acceleration_count = ball_acceleration_count;
	}
}