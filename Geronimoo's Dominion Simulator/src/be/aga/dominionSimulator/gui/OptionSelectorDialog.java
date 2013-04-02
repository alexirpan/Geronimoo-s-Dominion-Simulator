package be.aga.dominionSimulator.gui;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * This code is very much based off the custom dialog used in the Oracle tutorials
 * So credit goes to them for all the nice documentation and such.
 */
public class OptionSelectorDialog extends JDialog implements PropertyChangeListener {
	
	private OptionSelector select;
	private int minOptions;
	private int maxOptions;
	private JOptionPane optionPane;
	private ArrayList<String> optionsSelected = null;
	
	/** Creates the reusable dialog. */
	public OptionSelectorDialog(Frame aFrame, ArrayList<String> options, String title, int minOptions, int maxOptions) {
		super(aFrame, true);
		((DomGameFrame) aFrame).updateGraphics();
		this.minOptions = minOptions;
		this.maxOptions = maxOptions;
		setTitle(title);
		select = new OptionSelector(options);
		
//		Create the JOptionPane.
		optionPane = new JOptionPane(select,
				JOptionPane.PLAIN_MESSAGE);
		
//		Make this dialog display it.
		setContentPane(optionPane);
		
//		Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
//		Ensure the check boxes get focus
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				select.requestFocusInWindow();
			}
		});
		
//		Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
	}
	
	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (isVisible()
				&& (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||
						JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			
			if (optionPane.getValue() == JOptionPane.UNINITIALIZED_VALUE)
			{
				//do nothing
				return;
			}
			else if ((select.numSelected() >= minOptions) && (select.numSelected() <= maxOptions))
			{
				optionsSelected = select.getSelectedOptions();
				clearAndHide();
			}
			else
			{
				JOptionPane.showMessageDialog(
                        OptionSelectorDialog.this,
                        "Please select the right number of options.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
			}
			
			//Reset the JOptionPane's value.
			optionPane.setValue(
					JOptionPane.UNINITIALIZED_VALUE);
		}
	}
	
	public ArrayList<String> getSelectedOptions() {
		return optionsSelected;
	}
	
	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		getContentPane().removeAll();
		setVisible(false);
	}

}
