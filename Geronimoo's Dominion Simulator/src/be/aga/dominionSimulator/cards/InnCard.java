package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.HashSet;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class InnCard extends DomCard {
    public InnCard () {
      super( DomCardName.Inn);
    }

    public void play() {
      owner.addActions(2);
      owner.drawCards(2);
      if (owner instanceof DomHumanPlayer) {
    	  ((DomHumanPlayer) owner).doForcedDiscard(2, false, "Inn - discard 2 cards");
      } else {
    	  owner.doForcedDiscard(2, false);
      }
    }
    
    @Override
    public void doWhenGained() {
    	HashSet<DomCard> theCards = new HashSet<DomCard>();
		for (DomCard theCard : owner.getDeck().getDiscardPile()){
			if (owner instanceof DomHumanPlayer) {
				if (theCard.hasCardType(DomCardType.Action))
					theCards.add(theCard);
			} else if (theCard.hasCardType(DomCardType.Action) && theCard.getDiscardPriority(1)>10)
			  theCards.add(theCard);
		}
		ArrayList<DomCard> cards;
		if (owner instanceof DomHumanPlayer) {
			cards = ((DomHumanPlayer) owner).chooseUpToNCardsFromList(theCards.size(), new ArrayList<DomCard>(theCards), "Inn - choose actions to shuffle in");
		} else {
			cards = new ArrayList<DomCard>(theCards);
		}
		for (DomCard card : cards){
		    owner.getDeck().getDiscardPile().remove(card);
		    owner.putOnTopOfDeck(card);
		}
		owner.shuffleDeck();
    }
}