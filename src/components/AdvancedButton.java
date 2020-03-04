package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AdvancedButton 
	extends JPanel 
	implements MouseListener
{
	public String text;
	public Color background;
	public Image icon;
	
	public JLabel lbl_text;
	public ImageBox ib_icon;
	
	private boolean isEnabled = true;
	
	private Color visualBackground;
	private ActionListener actionListener;
	
	public AdvancedButton(String text, Color background, Image icon) 
	{	
		this.text = text;
		this.background = background;
		this.visualBackground = background;
		this.icon = icon;
		
		this.setLayout(new BorderLayout());

		this.lbl_text = new JLabel(text);
		this.lbl_text.setForeground(Color.white);
		this.lbl_text.setHorizontalAlignment(SwingConstants.CENTER);
		this.lbl_text.setFont(new Font(lbl_text.getFont().getName(), Font.BOLD, 14));
		
		this.ib_icon = new ImageBox(icon, this.getHeight(), this.getHeight());
		
		this.add(ib_icon, BorderLayout.CENTER);
		this.add(lbl_text, BorderLayout.CENTER);
		
		this.addMouseListener(this);
	}
	
	public AdvancedButton(String text, Color background, Image icon, Dimension iconSize) 
	{
		this (text, background, icon);
		
		this.ib_icon.setSize(iconSize);
	}
	
	public void activate (boolean enabled)
	{
		this.isEnabled = enabled;
		
		if (!enabled)
		{
			// Disable effects
			removeMouseListener(this);			
			// Change color
			visualBackground = Color.GRAY;
		}
		else
		{
			// Enable effects
			addMouseListener(this);
			// Change color
			visualBackground = background;
		}
		
		repaint();
	}
	
	public void setActionListener (ActionListener listener)
	{
		this.actionListener = listener;
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		Decorator.drawGradient(this, g, visualBackground.brighter(), visualBackground, Decorator.RECT, new Point(0, 0));
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{ 
		if (this.actionListener != null && isEnabled)
			this.actionListener.actionPerformed(null);
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{ visualBackground = background.darker(); repaint(); }

	@Override
	public void mouseExited(MouseEvent e) 
	{ visualBackground = background; repaint(); }

	@Override
	public void mousePressed(MouseEvent e) 
	{ visualBackground = background.brighter(); repaint(); }

	@Override
	public void mouseReleased(MouseEvent e) 
	{ visualBackground = background; repaint(); }
}
