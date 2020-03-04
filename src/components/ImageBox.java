package components;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImageBox 
	extends JPanel
{
	public Image image;
	public int width, height;
	
	public ImageBox(Image image) 
	{
		this.image = image;
	}
	public ImageBox(Image image, int width, int height)
	{
		this (image);
		
		this.width = width;
		this.height = height;
		
		this.setSize(width, height);
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
