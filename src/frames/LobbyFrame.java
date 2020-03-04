package frames;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import components.AdvancedButton;
import components.AdvancedFrame;
import components.Decorator;
import main.Program;
import managers.FrameManager;
import models.Game;
import models.Message;
import models.MessageType;
import models.Room;
import resources.Colors;
import resources.Dimensions;

public class LobbyFrame 
	extends AdvancedFrame
{
	AdvancedButton btn_host, btn_join;
	JComboBox<String> cmb_mode;
	
	public LobbyFrame()
	{
		super ();
		
		this.setTitle("Play Online");
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		
		JPanel pnl_header = new JPanel(new BorderLayout());
		JPanel pnl_body = new JPanel(new GridLayout(6, 2, Dimensions.medium, Dimensions.small));
		JLabel lbl_header = Decorator.createLabel("Find a game", Dimensions.header1, Font.BOLD, Colors.background, SwingConstants.CENTER);
		
		pnl_pane.add(pnl_header, BorderLayout.NORTH);
		pnl_pane.add(pnl_body, BorderLayout.CENTER);
		
		pnl_header.add(lbl_header, BorderLayout.CENTER);
		pnl_header.setBorder(Decorator.makeBorder(Dimensions.medium));
		pnl_header.setBackground(Colors.main);
		
		pnl_body.setBorder(Decorator.makeBorder(Dimensions.massive));
		pnl_body.setBackground(Colors.background);
		
		String[] headers = new String [] {"Username", "Host's IP", "Host's Port", "Game's Name"};
		String[] names = new String [] {"txt_username", "txt_ip", "txt_port", "txt_room"};
		
		for (int i = 0; i < headers.length; i++) 
		{
			pnl_body.add(Decorator.createLabel(headers[i], Dimensions.header3, Font.PLAIN, Colors.main, SwingConstants.LEFT));
			pnl_body.add(register (names[i], new JTextField()));
		}
		
		cmb_mode = new JComboBox <String> ();
		btn_host = new AdvancedButton("Host a game", Colors.main, null);
		btn_join = new AdvancedButton("Join", Colors.secondary, null);
		
		pnl_body.add(Decorator.createLabel("Game's Mode", Dimensions.header3, Font.PLAIN, Colors.main, SwingConstants.LEFT));
		pnl_body.add(cmb_mode);
		pnl_body.add(btn_host);
		pnl_body.add(btn_join);
		
		this.setContentPane(pnl_pane);
		
		try
		{			
			this.initialize ();
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void initialize () throws Exception
	{
		// Load username
		((JTextField) find("txt_username")).setText(Program.db.username);
		
		// Load combo box
		cmb_mode.addItem("15x15 Board");
		cmb_mode.addItem("19x19 Board");
		
		// Default values
		((JTextField) find ("txt_ip")).setText("127.0.0.1");
		((JTextField) find ("txt_port")).setText("9015");
		((JTextField) find ("txt_room")).setText("game_name");
		
		// Add host button action listener
		btn_host.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try
				{	
					String[] required = new String [] { "txt_username", "txt_port", "txt_room" };
					
					for (String name : required) 
						if (((JTextField) find (name)).getText().length() == 0)
							throw new Exception("Please fill all text fields!");
					
					String username = ((JTextField)find("txt_username")).getText();
					String roomName = ((JTextField)find("txt_room")).getText();
					int port = Integer.parseInt (((JTextField)find ("txt_port")).getText());
					int type = cmb_mode.getSelectedIndex() == 0 ? Game.FIFTEEN_BOARD : Game.NINETEEN_BOARD; 
					
					// Update the username in settings
					Program.db.username = username;
					
					// Show the room's frame
					Room room = new Room(roomName, port, new Game(roomName, username, null, type));
					
					FrameManager.run(new RoomFrame(room));
				} 
				catch (Exception e2) 
				{
					JOptionPane.showMessageDialog(LobbyFrame.this, e2.getMessage());
				}
			}
		});
		
		// Add join button action
		btn_join.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try
				{
					String username = ((JTextField) find ("txt_username")).getText();
					String room = ((JTextField) find ("txt_room")).getText();
					String ip = ((JTextField) find ("txt_ip")).getText();
					int port = Integer.parseInt(((JTextField) find ("txt_port")).getText());
					
					// Update database
					Program.db.username = username;
					
					Socket socket = new Socket(ip, port);
					
					// Send the room's name in order for the server to validate
					new ObjectOutputStream (socket.getOutputStream()).writeObject(new Message(MessageType.ROOM_INFO, room));
					
					// Receive joining state
					Game gameInfo = (Game) ((Message) new ObjectInputStream(socket.getInputStream()).readObject()).extra;
						
					FrameManager.run(new RoomFrame(socket, gameInfo));
				}
				catch (Exception x) 
				{
					JOptionPane.showMessageDialog(LobbyFrame.this, x.getMessage());
				}
			}
		});
	}
}
