package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class AlchemistCard extends DomCard {
    public AlchemistCard () {
      super( DomCardName.Alchemist);
    }

    public void play() {
      owner.addActions(1);
      owner.drawCards(2);
    }

    @Override
    public void handleCleanUpPhase() {
    	if (owner instanceof DomHumanPlayer) {
    		for (DomCard theCard : owner.getCardsInPlay()) {
    			if (theCard.getName()==DomCardName.Potion) {
    				ArrayList<String> options = new ArrayList<String>();
    				options.add("topdeck Alchemist");
    				options.add("don't topdeck");
    				String choice = ((DomHumanPlayer) owner).chooseOption(options, "Alchemist - return to top?");
    				if (choice.startsWith("topdeck")) {
    					owner.putOnTopOfDeck( this );
    					return;
    				} else {
    					break;
    				}
    			}
    		}
    	} else {
    		for (DomCard theCard : owner.getCardsInPlay()) {
    			if (theCard.getName()==DomCardName.Potion) {
    				owner.putOnTopOfDeck( this );
    				return;
    			}
    		}
    	}
    	super.handleCleanUpPhase();
    }
}