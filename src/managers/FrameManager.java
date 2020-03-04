package managers;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import components.AdvancedFrame;
import main.Program;
import models.Database;

public abstract class FrameManager 
{
	private static ArrayList<AdvancedFrame> frames;
	
	public static void run (AdvancedFrame frame)
	{
		if (frames == null)
			frames = new ArrayList<>();
		
		if (frames.size() > 0)
			frames.get(frames.size() - 1).setVisible(false);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		frames.add(frame);
		
		// Automatically make the frames show up if the frame in forth is closed
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowActivated(WindowEvent e) {}
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}			
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) 
			{
				try 
				{	
					FrameManager.close ();
				} catch (Exception x) 
				{
					JOptionPane.showMessageDialog(null, x.getMessage ());
				}
			}
			
		});
	}
	
	public static void close () throws Exception
	{
		// Save changes
		if (frames.size() == 1)
			Database.save(Program.db);
		
		// Destroy closed frame
		disposeOfLastFrame();
		
		// Show the frame behind
		if (frames.size() > 0)
			frames.get (frames.size() - 1).setVisible(true);
	}
	
	public static void disposeOfLastFrame ()
	{
		if (frames == null)
			return;
		
		if (frames.size() == 0)
			return;
		
		// Dispose of the last frame
		frames.get(frames.size() - 1).dispose();
		// Remove it from list
		frames.remove(frames.size() - 1);
	}
}
