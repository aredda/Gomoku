package frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import components.AdvancedButton;
import components.AdvancedFrame;
import components.Decorator;
import main.Program;
import managers.FrameManager;
import models.Game;
import models.MessageType;
import models.Message;
import models.Room;
import resources.Colors;
import resources.Dimensions;

public class RoomFrame 
	extends AdvancedFrame
{
	private Room room;
	private Socket guestSocket;
	private Game gameInfo;
	
	private AdvancedButton btn_start, btn_kick;
	
	private Runnable playerListener;
	
	public RoomFrame() 
	{
		super ();
		
		this.setTitle ("Room's name");
		this.setSize (500, 325);
		
		JPanel pnl_header = new JPanel(new BorderLayout());
		JPanel pnl_body = new JPanel(new GridLayout(3, 2, Dimensions.medium, Dimensions.medium));
		JLabel lbl_header = Decorator.createLabel("Waiting for someone to join..", Dimensions.header1, Font.BOLD, Colors.background, SwingConstants.CENTER);
		
		pnl_pane.add(pnl_header, BorderLayout.NORTH);
		pnl_pane.add(pnl_body, BorderLayout.CENTER);
		
		pnl_header.add(lbl_header, BorderLayout.CENTER);
		pnl_header.setBorder(Decorator.makeBorder(Dimensions.medium));
		pnl_header.setBackground(Colors.main);
		
		pnl_body.setBorder(Decorator.makeBorder(Dimensions.massive));
		pnl_body.setBackground(Colors.background);
		
		String [] headers = new String [] {"Player One:", "Player Two:"};
		String [] names = new String [] {"lbl_playerOne", "lbl_playerTwo"};
		
		for (int i = 0; i < names.length; i++) 
		{
			pnl_body.add(Decorator.createLabel(headers[i], Dimensions.header3, Font.BOLD, Colors.main, SwingConstants.LEFT));
			pnl_body.add(register(names[i], Decorator.createLabel("-", Dimensions.header2, Font.BOLD, Colors.secondary, SwingConstants.CENTER)));
		}
		
		btn_start = new AdvancedButton("Start game", Colors.main, null);
		btn_kick = new AdvancedButton("Kick guest", Colors.secondary, null);
		
		pnl_body.add(btn_start);
		pnl_body.add(btn_kick);
		
		btn_start.activate(false);
		btn_kick.activate(false);
		
		this.setVisible (true);
		
		// Add button action listeners
		btn_kick.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try
				{					
					// Inform the guest of him being kicked out of the room xD
					new ObjectOutputStream (room.guestSocket.getOutputStream()).writeObject (new Message (MessageType.KICK));
					
					// Kick guest player
					room.kickGuest();
					
					// Update User Interface
					((JLabel) find ("lbl_playerTwo")).setText("-");
					
					btn_kick.activate(false);
					btn_start.activate(false);
					
					// Start listening to players again
					new Thread (playerListener).start();
				}
				catch (Exception x) 
				{}
			}
		});
		btn_start.setActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try 
				{					
					// Inform the guest that the game has started
					ObjectOutputStream out = new ObjectOutputStream (room.guestSocket.getOutputStream());
					ObjectInputStream in = new ObjectInputStream (room.guestSocket.getInputStream());
					
					out.writeObject (new Message (MessageType.GAME_START));
					
					// Open the game frame
					FrameManager.disposeOfLastFrame();
					FrameManager.run(new GameFrame(room, out));
				} 
				catch (Exception x) {}
			}
		});
	}
	
	public RoomFrame (Room room) 
			throws Exception
	{
		this ();
		
		this.room = room;

		// Initialize the player listener runnable
		playerListener = new Runnable() {
			
			@Override
			public void run() {
				try
				{
					room.onPlayerJoined();				
					
					// Activate buttons
					btn_start.activate(true);
					btn_kick.activate(true);
					
					// Listen for the guest's username
					room.game.guest = ((Message) new ObjectInputStream(room.guestSocket.getInputStream()).readObject()).extra.toString();
					
					// Change the guest slot text
					((JLabel) find ("lbl_playerTwo")).setText(room.game.guest);
				}
				catch (Exception e)
				{
					JOptionPane.showMessageDialog(RoomFrame.this, e.getMessage());
				}
			}
		};
		
		// Run in a new thread
		new Thread (playerListener).start();
			
		// Don't forget to join
		Socket host = new Socket(room.serverSocket.getInetAddress(), room.port);
			
		// Send the room's name to validate
		new ObjectOutputStream(host.getOutputStream()).writeObject(new Message(MessageType.ROOM_INFO, room.name));
		
		// Change ui
		this.setTitle("Name: " + room.name + " - Mode: " + room.game.type + "x" + room.game.type);
		
		// Change host slot name
		((JLabel) find ("lbl_playerOne")).setText(Program.db.username);
	}
	
	public RoomFrame (Socket socket, Game info) throws Exception
	{
		this ();
		
		this.guestSocket = socket;
		this.gameInfo = info;
		
		// Send the username to the host
		new ObjectOutputStream(socket.getOutputStream()).writeObject (new Message(MessageType.PLAYER_INFO, Program.db.username));
		
		// Change ui
		this.setTitle("Name: " + info.name + " - Mode: " + info.type + "x" + info.type);
		
		((JLabel) find ("lbl_playerOne")).setText(info.host);
		((JLabel) find ("lbl_playerTwo")).setText(Program.db.username);
		
		// Listen for server messages
		new Thread(new Runnable() {
			@Override
			public void run() 
			{
				while (true)
					try
					{
						Message msg = (Message) new ObjectInputStream (socket.getInputStream()).readObject();
						
						switch (msg.type)
						{
							case KICK:			
								// Show a kick message
								JOptionPane.showMessageDialog(RoomFrame.this, "You've been kicked out from the room.");
								
								FrameManager.close();
								break;
							case GAME_START:
								// Run the game frame because the game has started
								FrameManager.disposeOfLastFrame();
								FrameManager.run(new GameFrame (guestSocket, gameInfo));
							break;
						}
					}
					catch (Exception e) {}
			}
		}).start();
	}
}
