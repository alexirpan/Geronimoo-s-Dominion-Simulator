package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class HavenCard extends DomCard {
	private DomCard myHavenedCard;

	public HavenCard () {
		super( DomCardName.Haven);
	}

	public void play() {
		owner.addActions(1);
		owner.drawCards(1);
		if (owner.getCardsInHand().isEmpty())
			return;
		if (owner instanceof DomHumanPlayer) {
			DomCard havened = ((DomHumanPlayer) owner).chooseExactlyNCardsFromList(1, owner.getCardsInHand(), "Haven - set aside a card").get(0);
			havenAway(havened);
		} else {
			//first look for excess terminals
			ArrayList< DomCard > theTerminalsInHand = owner.getCardsFromHand( DomCardType.Terminal );
			Collections.sort(theTerminalsInHand, DomCard.SORT_FOR_DISCARD_FROM_HAND);
			if (owner.getProbableActionsLeft()<0) {
				havenAway(theTerminalsInHand.get(0));
				return;
			}
			//TODO now Haven will try to stow away the best treasure card, but more handling is probably needed
			Collections.sort(owner.getCardsInHand(),SORT_FOR_DISCARD_FROM_HAND);
			for (int i = owner.getCardsInHand().size()-1;i>0;i--) {
				DomCard theCardToHavenAway = owner.getCardsInHand().get( i );
				if (theCardToHavenAway.hasCardType(DomCardType.Treasure)
						&& !owner.removingReducesBuyingPower( theCardToHavenAway )) {
					havenAway(theCardToHavenAway );
					return;
				}
			}
			//nothing found to put away so put away the worst card
			havenAway(owner.getCardsInHand().get( 0 ) );
		}
	}

	private void havenAway(DomCard aCard) {
		myHavenedCard=owner.removeCardFromHand( aCard);
		if (DomEngine.haveToLog ) DomEngine.addToLog( owner + " puts " + myHavenedCard + " aside");
	}

	public void resolveDuration() {
		if (myHavenedCard!=null) {
			owner.putInHand(myHavenedCard);
			owner.showHand();
		} else {
			if (DomEngine.haveToLog ) DomEngine.addToLog( owner + " adds nothing to his hand");
		}
		myHavenedCard=null;
	}
}