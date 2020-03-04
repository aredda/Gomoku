package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class BoardChip 
	extends JPanel
{
	public Color chipColor;
	public int offset;
	
	public BoardChip(Color chipColor, int offset) 
	{
		this.chipColor = chipColor;
		this.offset = offset;
	}
	
	@Override
	public void paintComponent (Graphics g) 
	{
		Decorator.drawGradient(this, g, chipColor.brighter(), chipColor, Decorator.CIRCLE, new Point(offset, offset));		
	}
}
