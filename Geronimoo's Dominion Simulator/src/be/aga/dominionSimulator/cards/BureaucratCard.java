package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class BureaucratCard extends DomCard {
    public BureaucratCard () {
      super( DomCardName.Bureaucrat);
    }

    public void play() {
    	DomCard theSilver = owner.getCurrentGame().takeFromSupply(DomCardName.Silver);
    	if (theSilver!=null)
    	  owner.gainOnTopOfDeck(theSilver);
    	for (DomPlayer thePlayer : owner.getOpponents()) {
    	   if (thePlayer.checkDefense())
    		   continue;
           ArrayList<DomCard> victoriesInHand = thePlayer.getCardsFromHand(DomCardType.Victory);
		   if (victoriesInHand.isEmpty()) {
        	 thePlayer.showHand();
		   } else {
			   if (thePlayer instanceof DomHumanPlayer) {
				   DomCard c = ((DomHumanPlayer) thePlayer).chooseExactlyNCardsFromList(1, victoriesInHand, "Bureaucrat - topdeck Victory card").get(0);
				   thePlayer.putOnTopOfDeck(thePlayer.removeCardFromHand(c));
			   } 
			   else 
				   thePlayer.putOnTopOfDeck(thePlayer.removeCardFromHand(victoriesInHand.get(0)));
		   }
    	}
    }
}