/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

import javax.swing.JOptionPane;

public class Player {
	// Tipi di giocatore
	public static final int CPU_EASY = 0;
	public static final int CPU_HARD = 1;
	public static final int MOUSE = 2;
	public static final int KEYBOARD = 3;
	public static final int ENEMY = 4;
	
	private int type;
	public int position = 0;
	public int destination = 0;
	public int points = 0;
	private int id;
	
	public Player (int type) {
		if (type < 0 || type > 4) {
			type = CPU_EASY;
			JOptionPane.showMessageDialog (null, "Some errors in player definition");
		}
		this.type = type;
	}
	
	public int getType () {
		return type;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}
}
