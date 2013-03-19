package be.aga.dominionSimulator.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomGame;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomPhase;

public class HandCardListener implements MouseListener {

	private DomCard card;
	private DomPlayer owner;
	private DomGameFrame gui;
	
	public HandCardListener(DomCard card, DomGameFrame gui)
	{
		this.card = card;
		this.gui = gui;
		owner = gui.getPlayer();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (card.hasCardType(DomCardType.Action)) {
			if (owner.getPhase().equals(DomPhase.Action) && (owner.getActionsLeft() > 0)) {
				owner.play(owner.removeCardFromHand(card));
				owner.actionsLeft--;
			}
		} else if (card.hasCardType(DomCardType.Treasure)) {
			// TODO Auto change to buy phase
			if (owner.getPhase().equals(DomPhase.Buy)) {
				owner.play(owner.removeCardFromHand(card));
			}
		}
		gui.updateGraphics();
	}
		

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
