package be.aga.dominionSimulator.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomGame;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomPhase;

public class SupplyCardListener implements MouseListener {

	private DomCardName name;
	private DomPlayer buyer;
	private DomGame game;
	
	public SupplyCardListener(DomCardName cardName, DomPlayer player)
	{
		name = cardName;
		buyer = player;
		game = buyer.getCurrentGame();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(name);
		if (buyer.getPhase() == DomPhase.Buy)
		{
			DomCost cost = name.getCost(game);
			if (buyer.getTotalAvailableCurrency().compareTo(cost) >= 0)
			{
				buyer.tryToBuy(name);
			}
		}
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
