package be.aga.dominionSimulator.gui;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import be.aga.dominionSimulator.DomCard;

/**
 * This class allows the user to choose some number of choices from a list of options
 * Works on straight text or on lists of cards
 * Takes a string as a purpose, then returns that string when the correct number of options are filled.
 * 
 * @author alexI
 *
 */
public class Reorder extends JPanel {
	
	private ArrayList<ReorderLabel> cardLabels = new ArrayList<ReorderLabel>();
	protected int numSelected = 0;

	public Reorder(ArrayList<String> names)
	{
		super(new FlowLayout());
		for (String s : names) {
			ReorderLabel lbl = new ReorderLabel(s, this);
			cardLabels.add(lbl);
		}
		addAllLabels();
	}
	
	private void addAllLabels() {
		this.removeAll();
		for (ReorderLabel lbl : cardLabels) {
			add(lbl);
		}
		revalidate();
	}

	public void resetSelected() 
	{
		for (ReorderLabel lbl : cardLabels) {
			lbl.clicked = false;
		}
		numSelected = 0;
	}

	public void swapChoices() 
	{
		ReorderLabel card1 = null, card2 = null;
		for (ReorderLabel lbl : cardLabels) {
			if (lbl.clicked) {
				if (card1 == null)
					card1 = lbl;
				else {
					card2 = lbl;
					break;
				}
			}
		}
		int ind1 = cardLabels.indexOf(card1);
		int ind2 = cardLabels.indexOf(card2);
		cardLabels.set(ind1, card2);
		cardLabels.set(ind2, card1);
		addAllLabels();
	}
	
	public ArrayList<String> getOrder() {
		ArrayList<String> alist = new ArrayList<String>();
		for (ReorderLabel lbl : cardLabels) {
			alist.add(lbl.getText());
		}
		return alist;
	}
}
