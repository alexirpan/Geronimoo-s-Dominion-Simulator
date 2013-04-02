package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class CartographerCard extends DomCard {
    public CartographerCard () {
      super( DomCardName.Cartographer);
    }

    public void play() {
      owner.addActions(1);
      owner.drawCards(1);
      layOutMaps();
    }

	private void layOutMaps() {
	  ArrayList<DomCard> theRevealedCards = owner.revealTopCards(4);
	  if (theRevealedCards.isEmpty()) 
	    return;
	  if (owner instanceof DomHumanPlayer) {
		  ArrayList<DomCard> toDiscard = ((DomHumanPlayer) owner).chooseUpToNCardsFromList(theRevealedCards.size(),
			  																				theRevealedCards, 
			  																				"Cartographer - choose cards to discard");
		  for (DomCard d : toDiscard) {
			  owner.discard(d);
			  theRevealedCards.remove(d);
		  }
		  ArrayList<DomCard> toPutBack = ((DomHumanPlayer) owner).reorderCards(theRevealedCards, "Cartographer - rearrange cards");
		  for (int i = toPutBack.size() - 1; i >= 0; i--) {
			  owner.putOnTopOfDeck(toPutBack.get(i));
		  }
	  } else {
		  Collections.sort(theRevealedCards,SORT_FOR_DISCARDING);
		  for (DomCard theRevealedCard : theRevealedCards){
			  if (theRevealedCard.getDiscardPriority(1)<16) {
				  owner.discard(theRevealedCard);
			  } else {
				  owner.putOnTopOfDeck(theRevealedCard);    		
			  }
		  }
	  }
	}
	@Override
	public int getPlayPriority() {
		if (owner.getKnownTopCards()==0)
			return 3;
		return super.getPlayPriority();
	}
}