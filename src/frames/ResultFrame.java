package frames;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import components.AdvancedButton;
import components.AdvancedFrame;
import components.Decorator;
import resources.Colors;
import resources.Dimensions;

public class ResultFrame 
	extends AdvancedFrame
{
	public JLabel lbl_header, lbl_desc;
	public AdvancedButton btn_rematch, btn_quit;
	
	public ResultFrame() 
	{
		super ();
		
		this.setTitle("Game is over");
		this.setSize(400, 400);
		this.setLocationRelativeTo(null);
		
		JPanel pnl_header = new JPanel(new BorderLayout());
		JPanel pnl_body = new JPanel(new GridLayout(3, 1, Dimensions.medium, Dimensions.medium));
		
		lbl_header = Decorator.createLabel("Game Result", Dimensions.header1, Font.BOLD, Colors.background, SwingConstants.CENTER);
		lbl_desc = Decorator.createLabel("Maybe, some filler words", Dimensions.header3, Font.BOLD, Colors.main, SwingConstants.CENTER);
		btn_rematch = new AdvancedButton("Request rematch", Colors.main, null);
		btn_quit = new AdvancedButton("Quit", Colors.secondary, null);
		
		pnl_header.setBorder(Decorator.makeBorder(Dimensions.medium));
		pnl_header.setBackground(Colors.secondary);
		pnl_header.add(lbl_header, BorderLayout.CENTER);
		
		pnl_body.setBackground(Colors.background);
		pnl_body.setBorder(Decorator.makeBorder(Dimensions.massive * 2));
		
		pnl_body.add(lbl_desc);
		pnl_body.add(btn_rematch);
		pnl_body.add(btn_quit);
		
		pnl_pane.add(pnl_header, BorderLayout.NORTH);
		pnl_pane.add(pnl_body, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
}
