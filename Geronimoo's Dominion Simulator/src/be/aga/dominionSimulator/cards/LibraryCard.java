package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class LibraryCard extends DrawUntilXCardsCard {
	private ArrayList<DomCard> myPutAsideCards=new ArrayList<DomCard>();

	public LibraryCard () {
		super( DomCardName.Library);
	}

	public void play() {
		while (owner.getCardsInHand().size()<7 && !owner.isDeckEmpty()) {
			DomCard theRevealedCard = owner.revealTopCards(1).get(0);
			if (theRevealedCard.hasCardType(DomCardType.Action)) {
				if (owner instanceof DomHumanPlayer) {
					ArrayList<String> choices = new ArrayList<String>();
					choices.add("Set aside " + theRevealedCard.getName().toString());
					choices.add("Put in hand");
					String choice = ((DomHumanPlayer) owner).chooseOption(choices, "Library - set aside " + theRevealedCard.getName().toString() + "?");
					if (choice.startsWith("Set"))
						putCardAside(theRevealedCard);
					else
						owner.putInHand(theRevealedCard);
				} else if( owner.getActionsLeft()==0 || (theRevealedCard.hasCardType(DomCardType.Terminal) && owner.getProbableActionsLeft()<=0)){
					putCardAside(theRevealedCard);
				}
			} else {
				owner.putInHand(theRevealedCard);
			}
		}
		discardPutAsideCards();
		owner.showHand();
	}

	private void discardPutAsideCards() {
		owner.discard(myPutAsideCards);
		myPutAsideCards.clear();
	}

	private void putCardAside(DomCard theRevealedCard) {
		if (DomEngine.haveToLog) DomEngine.addToLog("... and puts it aside");
		myPutAsideCards.add(theRevealedCard);
	}
}