package frames;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import components.AdvancedFrame;
import components.Decorator;
import resources.Colors;
import resources.Dimensions;

public class ProfileFrame 
	extends AdvancedFrame
{
	public ProfileFrame() 
	{
		super ();
		
		this.setTitle("Profile");
		this.setSize(600, 600);
		this.setLocationRelativeTo(null);
		
		JPanel pnl_header = new JPanel(new BorderLayout());
		JPanel pnl_info = new JPanel(new GridLayout(3, 2, 0, Dimensions.small));
		JPanel pnl_stat = new JPanel(new BorderLayout());
		JLabel lbl_header = Decorator.createLabel("About you", Dimensions.header1, Font.BOLD, Colors.background, SwingConstants.CENTER);
		JLabel lbl_history = Decorator.createLabel("History", Dimensions.header2, Font.BOLD, Colors.secondary, SwingConstants.LEFT);
		JTable tbl_stat = new JTable(5, 6);
		
		pnl_header.setBorder(Decorator.makeBorder(Dimensions.medium));
		pnl_header.setBackground(Colors.secondary);
		pnl_header.add(lbl_header, BorderLayout.CENTER);

		pnl_info.setBackground(Colors.background);
		tbl_stat.setBackground(Colors.background);
		pnl_stat.setBackground(Colors.background);
		
		pnl_info.setBorder(Decorator.makeBorder(Dimensions.massive));
		
		String[] labels = new String [] {"Username", "Win Count", "Loss Count"};
		String[] names = new String [] {"lbl_username", "lbl_win", "lbl_loss"};
		
		for (int i = 0; i < names.length; i++) 
		{
			pnl_info.add(Decorator.createLabel(labels[i], Dimensions.header2, Font.BOLD, Colors.secondary, SwingConstants.LEFT));
			pnl_info.add(register (names[i], Decorator.createLabel(labels[i], Dimensions.header3, Font.BOLD, Colors.main, SwingConstants.RIGHT)));
		}
		
		lbl_history.setBorder(Decorator.makeBorder(0, Dimensions.small));
		
		pnl_stat.setBorder(Decorator.makeBorder(Dimensions.massive));
		pnl_stat.add(lbl_history, BorderLayout.NORTH);
		pnl_stat.add(tbl_stat, BorderLayout.CENTER);
		
		pnl_pane.add(pnl_header, BorderLayout.NORTH);
		pnl_pane.add(pnl_info, BorderLayout.SOUTH);
		pnl_pane.add(pnl_stat, BorderLayout.CENTER);
		
		this.setVisible(true);
	}
}
