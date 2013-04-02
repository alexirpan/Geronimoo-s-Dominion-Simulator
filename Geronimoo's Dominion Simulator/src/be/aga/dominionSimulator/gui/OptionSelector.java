package be.aga.dominionSimulator.gui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class allows the user to choose some number of choices from a list of options
 * Works on straight text or on lists of cards
 * Takes a string as a purpose, then returns that string when the correct number of options are filled.
 * 
 * @author alexI
 *
 */
public class OptionSelector extends JPanel {
	
	private JCheckBox[] optionCheckBoxes;

	public OptionSelector(ArrayList<String> options)
	{
		super(new FlowLayout());
		
		JCheckBox option;
		
		optionCheckBoxes = new JCheckBox[options.size()];
		for (int i = 0; i < options.size(); i++)
		{
			option = new JCheckBox(options.get(i));
			optionCheckBoxes[i] = option;
			add(option);
		}
	}
	
	/**
	 * Should only be called if the right number of boxes are selected
	 * @return The options selected
	 */
	public ArrayList<String> getSelectedOptions()
	{
		ArrayList<String> options = new ArrayList<String>();
		for (int i = 0; i < optionCheckBoxes.length; i++) 
		{
			if (optionCheckBoxes[i].isSelected())
				options.add(optionCheckBoxes[i].getText());
		}
		return options;
	}
	
	public int numSelected()
	{
		int count = 0;
		for (int i = 0; i < optionCheckBoxes.length; i++) 
		{
			if (optionCheckBoxes[i].isSelected())
				count++;
		}
		return count;
	}
}
