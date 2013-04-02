package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomPlayStrategy;

public class AmbassadorCard extends DomCard {
    public AmbassadorCard () {
      super( DomCardName.Ambassador);
    }

    public void play() {
    	if (owner.getCardsInHand().isEmpty())
          return;
    	if (owner instanceof DomHumanPlayer) {
    		DomCard revealedCard = ((DomHumanPlayer) owner).chooseExactlyNCardsFromList(1, 
    															owner.getCardsInHand(),
    															"Ambassador - choose card to reveal").get(0);
    		DomEngine.addToLog( owner + " reveals " + revealedCard );
    		int numCopies = ((DomHumanPlayer) owner).numCardsWithName(revealedCard.getName(), 
    																	owner.getCardsInHand());
    		int numReturned = ((DomHumanPlayer) owner).chooseNumberUpToN(Math.min(numCopies, 2),
    																	"Ambassador - return how many copies?");
    		int i = 0;
    		while ((numReturned > 0) && (i < owner.getCardsInHand().size())) {
    			DomCard c = owner.getCardsInHand().get(i);
    			if (c.getName().equals(revealedCard.getName())) {
    				owner.returnToSupply(owner.removeCardFromHand(c));
    				numReturned--;
    			} else {
    				i++;
    			}
    		}
    		attackOpponents( revealedCard.getName() );
    	} else {
    		Collections.sort(owner.getCardsInHand(),SORT_FOR_TRASHING);
    		DomCard thePreviousTrash = null;
    		DomCard theRevealedCard = null;
    		int theTrashCount = 0;
    		while (theTrashCount<2 && !owner.getCardsInHand().isEmpty()) {
    			DomCard theCardToTrash = owner.getCardsInHand().get( 0 );
    			if (theCardToTrash.isFromBlackMarket())
    				return;
    			if (thePreviousTrash!=null && thePreviousTrash.getName()!=theCardToTrash.getName())
    				break;
    			theRevealedCard = theCardToTrash;
    			if (DomEngine.haveToLog) DomEngine.addToLog( owner + " reveals " + theRevealedCard );
    			if ((owner.getPlayStrategyFor(this)!=DomPlayStrategy.aggressiveTrashing
    					&& owner.removingReducesBuyingPower(theCardToTrash)) 
    					|| owner.getTotalMoneyInDeck()-theCardToTrash.getPotentialCoinValue() < 3)
    				break;
    			owner.returnToSupply(owner.removeCardFromHand( theCardToTrash));
    			theTrashCount++;
    			thePreviousTrash = theCardToTrash;
    		}
    		attackOpponents( theRevealedCard.getName() );          
    	}
    }

    private void attackOpponents( DomCardName aCardName ) {
      for (DomPlayer thePlayer : owner.getOpponents()) {
         if (thePlayer.checkDefense()) 
        	 continue;
         DomCard theCard = owner.getCurrentGame().takeFromSupply( aCardName);
         if (theCard!=null) {
           thePlayer.gain(theCard);
         }
      }
    }

    public boolean wantsToBePlayed() {
      for (DomCard theCard : owner.getCardsInHand()) {
        if (theCard!=this && theCard.getTrashPriority()<16 && !theCard.isFromBlackMarket())
          return true;
      }
      return false;
   }
    @Override
    public int getTrashPriority() {
    	if (owner==null)
    		return super.getTrashPriority();

    	int theCount=0;
    	for (DomCardName card : owner.getDeck().keySet()){
    		if (card==DomCardName.Ambassador)
    			continue;
    		if (card.getTrashPriority(owner)<16)
    			theCount+=owner.countInDeck(card);
    	}
    	if (theCount<3)
    		return 14;
    	return super.getTrashPriority();
    }
}