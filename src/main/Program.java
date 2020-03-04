package main;

import javax.swing.JOptionPane;

import frames.*;
import managers.FrameManager;
import models.Database;
import models.Game;
import models.Room;

public class Program 
{
	public static Database db;
	
	public static void main(String[] args) 
	{
		try
		{
			db = Database.load ();
		}
		catch (Exception e) 
		{
			db = new Database ();
		}
		finally
		{
			try
			{				
				// FrameManager.run(new GameFrame (null, new Game ("haha", "host", "guest", 15)));
				FrameManager.run(new LobbyFrame());
				// FrameManager.run(new MainFrame());
			}
			catch (Exception e) 
			{
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}
	}

}
