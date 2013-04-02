package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class BaronCard extends DomCard {
    public BaronCard () {
      super( DomCardName.Baron);
    }

    public void play() {
        owner.addAvailableBuys( 1 );
        if (owner instanceof DomHumanPlayer) {
        	int numEstates = ((DomHumanPlayer) owner).numCardsWithName(DomCardName.Estate, owner.getCardsInHand());
        	String choice = "Don't discard";
        	if (numEstates > 0) {
        		ArrayList<String> options = new ArrayList<String>();
        		options.add("Discard Estate");
        		options.add("Don't discard");
        		choice = ((DomHumanPlayer) owner).chooseOption(options, "Baron - discard Estate?");
        	}
        	if (choice.startsWith("Discard")) {
        		owner.addAvailableCoins(4);
        		owner.discardFromHand(DomCardName.Estate);
        	} else {
        		DomCard theEstate = owner.getCurrentGame().takeFromSupply(DomCardName.Estate);
        		if (theEstate!=null)
        			owner.gain(theEstate);
        	}
        } else {
        	if (owner.discardFromHand(DomCardName.Estate)){
        		owner.addAvailableCoins( 4 );
        	} else {
        		DomCard theEstate = owner.getCurrentGame().takeFromSupply(DomCardName.Estate);
        		if (theEstate!=null)
        			owner.gain(theEstate);
        	}
        }
    }

    public boolean wantsToBePlayed() {
      return owner.getCardsFromHand( DomCardName.Estate).size()>0
          || owner.wants( DomCardName.Estate)
          || owner.getCurrentGame().countInSupply( DomCardName.Estate )==0;
    }
    
    @Override
    public int getDiscardPriority(int aActionsLeft) {
    	if (aActionsLeft>0 && owner.getCardsInHand().contains(this) 
    	&& owner.getCardsFromHand(DomCardName.Baron).size()==1 && !owner.getCardsFromHand(DomCardName.Estate).isEmpty())
    		return 29;
    	return super.getDiscardPriority(aActionsLeft);
    }

}