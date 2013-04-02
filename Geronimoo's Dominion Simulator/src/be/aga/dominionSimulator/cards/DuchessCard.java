package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class DuchessCard extends DomCard {
    public DuchessCard () {
      super( DomCardName.Duchess);
    }

    public void play() {
      owner.addAvailableCoins(2);
      selectTopCard(owner);
	  for (DomPlayer player : owner.getOpponents())
		selectTopCard(player);
    }

	private void selectTopCard(DomPlayer player) {
		ArrayList<DomCard> theRevealedCard = player.revealTopCards(1);
		if (theRevealedCard.isEmpty()) 
			return;
		if (player instanceof DomHumanPlayer) {
			ArrayList<String> choices = new ArrayList<String>();
			choices.add("Discard " + theRevealedCard.get(0).getName().toString());
			choices.add("Don't discard");
			String choice = ((DomHumanPlayer) player).chooseOption(choices, "Duchess - discard top?");
			if (choice.startsWith("Discard"))
				player.discard(theRevealedCard.get(0));
			else
				player.putOnTopOfDeck(theRevealedCard.get(0));
		} else {
			if (theRevealedCard.get(0).getDiscardPriority(1)<16) {
				player.discard(theRevealedCard.get(0));
			} else {
				player.putOnTopOfDeck(theRevealedCard.get(0));    		
			}
		}
	}
}