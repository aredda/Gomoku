package models;

import java.io.Serializable;
import java.util.Date;

public class Game 
	implements Serializable
{
	public static final int FIFTEEN_BOARD = 15;
	public static final int NINETEEN_BOARD = 19;
	
	public String name;
	public String host, guest;
	public int type;
	public Date date;
	public String winner;
	
	public Game (String name, String host, String guest, int type) 
	{
		this.name = name;
		this.host = host;
		this.guest = guest;
		this.type = type;
	}
	
	@Override
	public String toString() 
	{
		return "Name: " + name + " - Mode: " + type + "x" + type;
	}
}
