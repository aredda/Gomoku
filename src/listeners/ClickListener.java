package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class ClickListener 
	implements MouseListener
{

	public abstract void onClick (MouseEvent e);
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		onClick (e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

}
