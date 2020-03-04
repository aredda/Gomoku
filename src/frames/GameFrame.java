package frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import components.AdvancedFrame;
import components.BoardChip;
import components.Decorator;
import listeners.ClickListener;
import models.Game;
import models.Message;
import models.MessageType;
import models.Room;
import resources.Colors;
import resources.Dimensions;

public class GameFrame extends AdvancedFrame
{
	// Host
	private Room room;
	
	// Guest
	private Socket guest;
	private Game game;
	
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	
	private JPanel pnl_board;
	private JLabel lbl_rival_name;
	private JPanel pnl_chat_body;
	private JTextField txt_message;
	
	// Game rules
	private boolean isOver = false;
	private boolean canPlay = false;
	
	public GameFrame (ObjectOutputStream out, ObjectInputStream in) 
	{
		super ();
		
		this.outStream = out;
		this.inStream = in;
		
		this.setTitle("Game's name - Game's mode - against Rival's name");
		this.setSize(850, 600);
		this.setLocationRelativeTo(null);
		
		pnl_board = new JPanel();
		
		JPanel pnl_chat = new JPanel(new BorderLayout());
		JPanel pnl_chat_header = new JPanel(new BorderLayout());
		JPanel pnl_chat_footer = new JPanel(new BorderLayout());
		
		pnl_chat_body = new JPanel();
		lbl_rival_name = Decorator.createLabel("Rival's name", Dimensions.header3, Font.BOLD, Colors.background, SwingConstants.LEFT);
		txt_message = new JTextField();
		
		pnl_pane.setLayout(new BoxLayout(pnl_pane, BoxLayout.X_AXIS));
		
		this.pnl_pane.add(pnl_board);
		this.pnl_pane.add(pnl_chat);
		
		pnl_board.setPreferredSize(new Dimension (600, 600));
		pnl_board.setBackground(Colors.background);
		
		pnl_chat.setPreferredSize(new Dimension(250, 600));
		pnl_chat.setBackground(Color.white);
		
		pnl_chat_header.setBackground(Colors.secondary);
		pnl_chat_header.setBorder(Decorator.makeBorder(Dimensions.medium));
		
		pnl_chat.add(pnl_chat_header, BorderLayout.NORTH);
		pnl_chat.add(pnl_chat_body, BorderLayout.CENTER);
		pnl_chat.add(pnl_chat_footer, BorderLayout.SOUTH);
		
		pnl_chat_header.add(lbl_rival_name, BorderLayout.CENTER);
		
		pnl_chat_body.setBackground(Colors.main_light);
		
		txt_message.setBorder(Decorator.makeBorder(Dimensions.small));
		txt_message.setText("Write your message ...");
		
		pnl_chat_footer.add(register ("txt_message", txt_message), BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	public GameFrame (Room room, ObjectOutputStream out, ObjectInputStream in) 
			throws Exception
	{
		this (out, in);
		
		this.room = room;
		
		// Set up interface
		createBoard(room.game.type);
		prepareInterface(room.game);
		
		room.onPlayerPlayed();
		
		// The host will play first
		this.canPlay = true;
	}
	
	public GameFrame (Socket guestSocket, Game game, ObjectOutputStream out, ObjectInputStream in) 
			throws Exception
	{
		this (out, in);
		
		this.guest = guestSocket;
		this.game = game;
		
		createBoard(game.type);
		prepareInterface(game);
	}
	
	public void prepareInterface(Game game)
	{
		// Change frame title
		this.setTitle(game.toString());
		
		// Change chat header text
		lbl_rival_name.setText(room == null ? game.host : game.guest);
	}
	
	public void createBoard (int size)
	{
		pnl_board.setLayout(new GridLayout(size, size, 0, 0));
		pnl_board.setBorder(Decorator.makeBorder(Dimensions.small));
		
		for (int i = 1; i <= size; i++)
			for (int j = 1; j <= size; j++)
			{				
				// Construct name
				String name = "chip_x" + j + "y" + i;
				
				JPanel board_cell = new JPanel(new GridLayout(1, 1));
				board_cell.setBorder(BorderFactory.createLineBorder(Colors.secondary, 1));
				board_cell.setBackground(Colors.background);				
				board_cell.addMouseListener(new ClickListener() {
					@Override
					public void onClick(MouseEvent e) 
					{
						try 
						{							
							if (!GameFrame.this.canPlay)
								return;
							
							// Inform the rival that you made a move
							GameFrame.this.outStream.writeObject(new Message(MessageType.PLAY_CHIP, name));
							
							// Play the chip locally
							board_cell.add(new BoardChip(Colors.main, 2));
							board_cell.repaint();
							
							// force repainting
							GameFrame.this.setVisible(true);
							
							// Deliver turn
							// canPlay = false;
							
							// Deactivate cell
							board_cell.removeMouseListener(board_cell.getMouseListeners()[0]);
						} 
						catch (Exception x) 
						{
							JOptionPane.showMessageDialog(null, "Error: " + x.getMessage());
						}
					}
				});
				
				pnl_board.add(register (name, board_cell));
			}
	}
}
