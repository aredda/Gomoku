package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Room 
	implements Serializable
{
	public String name;
	public int port;
	public ServerSocket serverSocket;
	
	public Socket hostSocket;
	public Socket guestSocket;
	
	public Game game;
	
	public boolean isFull = false;
	
	public Room (String name, int port, Game gameInfo) throws IOException
	{
		this.serverSocket = new ServerSocket(port);
		
		this.name = name;
		this.port = port;
		this.game = gameInfo;
	}
	
	public void onPlayerJoined () throws Exception
	{
		while (!isFull)	
		{
			Socket joined = serverSocket.accept();
			
			ObjectInputStream in = new ObjectInputStream(joined.getInputStream());
			ObjectOutputStream out = new ObjectOutputStream(joined.getOutputStream());
			
			if (!((Message)in.readObject()).extra.equals(name))
			{
				// Rejection of player
				out.writeObject (new Message(MessageType.INVALID_ROOM));
				
				continue;
			}
			
			if (hostSocket == null)
				hostSocket = joined;
			else		
			{				
				guestSocket = joined;
	
				// Send validation
				out.writeObject (new Message(MessageType.ROOM_INFO, game));
			}
			
			// The room is full now
			isFull = hostSocket != null && guestSocket != null;
		}
	}
	
	public void kickGuest ()
	{
		this.guestSocket = null;
		this.isFull = false;
	}
	
	public void onPlayerPlayed () throws Exception
	{
		Socket[] sockets = new Socket[] { hostSocket, guestSocket };
		
		for (Socket s : sockets)
		{
			new Thread(new Runnable() {
				@Override
				public void run() 
				{
					ObjectInputStream inStream = null;
					
					try 
					{
						inStream = new ObjectInputStream(s.getInputStream());
					} 
					catch (IOException x) {}
					
					while (true)
					{
						System.out.println("Listening");
						
						try
						{
							Thread.sleep (500);
							
							Message msg = (Message) inStream.readObject();
							
							System.out.println("Type: " + msg.type);
						}
						catch (Exception e) 
						{
							System.out.println(e.getMessage());
						}
					}
				}
			}).start();
		}
	}
}
