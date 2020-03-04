package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.Border;

public abstract class Decorator 
{
	public static final int RECT = 1;
	public static final int CIRCLE = 2;
	
	public static void drawGradient(JComponent target, Graphics g, Color startColor, Color endColor, int shape, Point offset) 
	{
		Graphics2D g2d = (Graphics2D) g.create();
		
		g2d.setPaint(new GradientPaint(new Point(0, 0), startColor, new Point(0, target.getHeight()), endColor));
		
		if (shape == RECT)
			g2d.fillRect(offset.x, offset.y, target.getWidth(), target.getHeight());
		else if (shape == CIRCLE)
			g2d.fillOval(offset.x, offset.y, target.getWidth() - (offset.x * 2), target.getHeight() - (offset.y * 2));
		
		g2d.dispose();
	}
	
	public static Border makeBorder (int horizontal, int vertical)
	{
		return BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal);
	}
	
	public static Border makeBorder (int all)
	{
		return makeBorder(all, all);
	}
	
	public static JLabel createLabel (String content, int fontSize, int fontStyle, Color fontColor, int horizontalAlignment)
	{
		JLabel target = new JLabel(content);
		
		target.setForeground(fontColor);
		target.setFont(new Font(target.getFont().getName(), fontStyle, fontSize));
		target.setHorizontalAlignment(horizontalAlignment);
		
		return target;
	}
}
