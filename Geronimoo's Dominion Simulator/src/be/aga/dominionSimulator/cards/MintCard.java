package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomBuyRule;
import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class MintCard extends DomCard {
    public MintCard () {
      super( DomCardName.Mint);
    }

    public void play() {
    	if (owner instanceof DomHumanPlayer) {
    		ArrayList<DomCard> treasures = owner.getCardsFromHand(DomCardType.Treasure);
    		if (treasures.isEmpty())
    			return;
    		DomCard chosen = ((DomHumanPlayer) owner).chooseExactlyNCardsFromList(1, treasures, "Mint - choose treasure to copy").get(0);
    		DomEngine.addToLog(owner + " reaveals " + chosen.getName() + " from hand");
    		owner.gain(chosen.getName());
    	} else {
    		DomCardName theCardToCopy=findCardToCopy();
    		if (theCardToCopy!=null) {
    			if (DomEngine.haveToLog) 
    				DomEngine.addToLog( owner+ " reveals " +theCardToCopy + " from hand");
    			owner.gain(theCardToCopy);
    		} else {
    			if (DomEngine.haveToLog) 
    				DomEngine.addToLog( " but does not find a suitable card to Mint");
    		}
    	}
    }

    private DomCardName findCardToCopy() {
    	for (DomBuyRule buyRule : owner.getBuyRules()){
    	  DomCardName theCard = buyRule.getCardToBuy();
    	  if (theCard.hasCardType(DomCardType.Treasure)
    	   && owner.wants(theCard) 
    	   && !owner.getCardsFromHand(theCard).isEmpty()) 
            return theCard;
    	}
		return null;
    }
    
	@Override
    public boolean wantsToBePlayed() {
      return findCardToCopy()!=null;
    }
}