package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class OracleCard extends DomCard {
	public OracleCard () {
		super( DomCardName.Oracle);
	}

	public void play() {
		oracleYourself();
		oracleOpponents();
		owner.drawCards(2);
	}

	private void oracleOpponents() {
		for (DomPlayer thePlayer : owner.getOpponents()) {
			if (thePlayer.checkDefense()) 
				continue;
			ArrayList<DomCard> theRevealedCards = thePlayer.revealTopCards(2);
			if (theRevealedCards.isEmpty()) 
				continue;
			if (owner instanceof DomHumanPlayer) {
				DomEngine.addToLog(thePlayer + " reveals " + theRevealedCards);
				String choice = ((DomHumanPlayer) owner).chooseOption("Oracle - discard or put back", "Discard cards", "Put back cards");
				if (choice.startsWith("Discard"))
					thePlayer.discard(theRevealedCards);
				else {
					// Assumes there is only one human
					for (DomCard theCard : theRevealedCards)
						thePlayer.putOnTopOfDeck(theCard);
				}
			} else {
				Collections.sort(theRevealedCards, SORT_FOR_DISCARDING);
				int theTotalValue=0;
				for (DomCard theCard : theRevealedCards) {
					theTotalValue+=theCard.getDiscardPriority(1); 
				}
				if (theTotalValue>=32){
					thePlayer.discard(theRevealedCards);
				}else{
					if (thePlayer instanceof DomHumanPlayer) {
						theRevealedCards = ((DomHumanPlayer) thePlayer).reorderCards(theRevealedCards, "Oracle - reorder cards");
						Collections.reverse(theRevealedCards);
						for (DomCard theCard : theRevealedCards) {
							thePlayer.putOnTopOfDeck(theCard);
						}
					} else {
						for (DomCard theCard : theRevealedCards) {
							thePlayer.putOnTopOfDeck(theCard); 
						}
					}
				}
			}
		}
	}

	private void oracleYourself() {
		ArrayList<DomCard> theRevealedCards = owner.revealTopCards(2);
		if (theRevealedCards.isEmpty()) 
			return;
		int theTotalValue = 0;
		for (DomCard theCard : theRevealedCards) {
			theTotalValue+=theCard.getDiscardPriority(owner.getActionsLeft()); 
		}
		if (theTotalValue<32){
			owner.discard(theRevealedCards);
		}else{
			for (DomCard theCard : theRevealedCards) {
				owner.putOnTopOfDeck(theCard); 
			}
		}
	}
}