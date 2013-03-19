package be.aga.dominionSimulator;

import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomPhase;
import be.aga.dominionSimulator.gui.DomGameFrame;

public class DomHumanPlayer extends DomPlayer {

	private DomGameFrame playerInterface;
	private DomCardName selected;
	
	public DomHumanPlayer(String name) {
		super(name);
	}

	public void setInterface(DomGameFrame gui) {
		playerInterface = gui;
	}

	public void doCleanupPhase() {
		super.doCleanUpPhase();
	}
	
	public void setPhase(DomPhase aPhase) {
		super.setPhase(aPhase);
	}
	
	public void startActionPhase() {
		initializeTurn();
		resolveHorseTraders();
		resolveDurationEffects();
	}
	
	public void playTreasures() {
		ArrayList<DomCard> treasureCards = getCardsFromHand(DomCardType.Treasure);
		for (DomCard theCard : treasureCards) {
			play(removeCardFromHand( theCard ));
		}
    	/*
    	if (DomEngine.haveToLog) {
    		if (previousPlayedCardName!=null) {
    			DomEngine.addToLog( name + " plays " + (sameCardCount+1)+" "+ previousPlayedCardName.toHTML()
    					+ (sameCardCount>0 ? "s" : ""));
    			previousPlayedCardName=null;
    			sameCardCount=0;
    		}
    		showBuyStatus();
    	}
    	*/
    }
	
	public boolean tryToBuy( DomCardName aCardName ) {
        if (game.countInSupply( aCardName )==0) {
//          if (DomEngine.haveToLog) DomEngine.addToLog( aCardName + " is no more available to buy");
        	return false;
        }
        if (forbiddenCardsToBuy.contains(aCardName))
        	return false;

        if (aCardName==DomCardName.Grand_Market && !getCardsFromPlay(DomCardName.Copper).isEmpty())
        	return false;
        
        buy(game.takeFromSupply( aCardName ));
        return true;
    }
}
