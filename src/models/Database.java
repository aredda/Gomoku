package models;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Database
	implements Serializable
{
	public static final String filename = "settings.db";
	
	public String username;
	public int winCount, loseCount;
	public int gameTheme;
	public ArrayList<Game> games;
	
	public Database ()
	{
		this.username = "Player";
		this.winCount = this.loseCount = 0;
		
		this.games = new ArrayList<>();
	}
	
	public void addGame (Game g)
	{
		this.games.add(g);
	}
	
	public static void save (Database d) throws Exception
	{
		FileOutputStream stream = new FileOutputStream(filename);
		ObjectOutputStream out = new ObjectOutputStream(stream);
		
		out.writeObject(d);
		out.close();
		
		stream.close();
	}
	
	public static Database load () throws Exception
	{
		FileInputStream stream = new FileInputStream(filename);
		ObjectInputStream in = new ObjectInputStream(stream);
		
		Database d = (Database) in.readObject();
		
		in.close();
		stream.close();
		
		return d;
	}
}
