package frames;

import resources.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import components.AdvancedButton;
import components.AdvancedFrame;
import components.Decorator;
import main.Program;
import managers.FrameManager;

public class MainFrame 
	extends AdvancedFrame
{
	public JPanel pnl_buttons;
	public JLabel lbl_header;
	public AdvancedButton btn_online, btn_profile, btn_settings, btn_quit;
	
	public MainFrame () throws Exception
	{
		super ();
		
		this.setTitle ("Play Gomoku");
		this.setSize (400, 500);
		this.setLocationRelativeTo(null);
		
		this.pnl_pane.setBorder(new EmptyBorder(30, 30, 30, 30));
		
		this.pnl_buttons = new JPanel ();
		this.pnl_buttons.setLayout (new GridLayout (3, 1, 0, 15));
		this.pnl_buttons.setBackground (Colors.transparent);
		this.pnl_buttons.setBorder (Decorator.makeBorder(Dimensions.medium, Dimensions.massive));
		
		this.lbl_header = new JLabel ("Gomoku");
		this.lbl_header.setFont (new Font(this.lbl_header.getFont().getName(), Font.BOLD, 50));
		this.lbl_header.setForeground (Colors.main);
		this.lbl_header.setPreferredSize (new Dimension(getWidth(), getHeight() / 4));
		this.lbl_header.setHorizontalAlignment (SwingConstants.CENTER);
		
		this.pnl_pane.add (this.lbl_header, BorderLayout.NORTH);
		this.pnl_pane.add (this.pnl_buttons, BorderLayout.CENTER);
		
		this.btn_online = new AdvancedButton ("Play Online", Colors.main, null);
		this.btn_profile = new AdvancedButton ("Profile", Colors.main, null);
		this.btn_settings = new AdvancedButton ("Settings", Colors.main, null);
		this.btn_quit = new AdvancedButton ("Quit", Colors.secondary, null);
		
		this.pnl_buttons.add (btn_online);
		this.pnl_buttons.add (btn_profile);
		
		JPanel p = new JPanel (new GridLayout(1, 2, 15, 0));
		p.add (btn_settings);
		p.add (btn_quit);
		
		this.pnl_buttons.add (p);
		
		btn_online.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				FrameManager.run(new LobbyFrame());
			}
		});
	}
}
