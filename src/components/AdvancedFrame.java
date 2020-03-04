package components;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import resources.Colors;

public class AdvancedFrame 
	extends JFrame
{
	public JPanel pnl_pane;
	public HashMap<String, JComponent> components;
	
	public AdvancedFrame ()
	{
		this.setResizable (false);
		
		this.components = new HashMap<String, JComponent>();
		
		this.pnl_pane = new JPanel(new BorderLayout());
		this.pnl_pane.setBackground(Colors.background);
		
		this.setContentPane(this.pnl_pane);
	}
	
	protected JComponent register (String name, JComponent component)
	{
		component.setName (name);
		
		this.components.put (component.getName(), component);
		
		return component;
	}
	
	protected <T extends JComponent> T find (String name) throws Exception
	{
		if (!this.components.containsKey(name))
			throw new Exception("Cannot find a component with the given name");
		
		return (T) this.components.get(name);
	}
}
