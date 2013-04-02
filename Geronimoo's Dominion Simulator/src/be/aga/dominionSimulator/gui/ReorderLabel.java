package be.aga.dominionSimulator.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import be.aga.dominionSimulator.enums.DomCardName;

public class ReorderLabel extends JLabel implements MouseListener {
	
	protected boolean clicked = false;
	private Reorder parent;
	
	public ReorderLabel(String name, Reorder parent) {
		super(name);
		this.parent = parent;
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!clicked) {
			clicked = true;
			parent.numSelected++;
			if (parent.numSelected == 2) {
				parent.swapChoices();
				parent.resetSelected();
			}
		} else {
			clicked = false;
			parent.numSelected--;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
