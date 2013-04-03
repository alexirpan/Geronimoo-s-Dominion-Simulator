package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class ExplorerCard extends DomCard {
    public ExplorerCard () {
      super( DomCardName.Explorer);
    }

    public void play() {
    	if (owner.getCardsFromHand(DomCardName.Province).isEmpty()) {
    		owner.gainInHand(DomCardName.Silver);
    	} else {
    		if (owner instanceof DomHumanPlayer) {
    			ArrayList<String> choices = new ArrayList<String>();
    			choices.add("Reveal Province");
    			choices.add("Don't reveal");
    			String choice = ((DomHumanPlayer) owner).chooseOption(choices, "Explorer - reveal Province?");
    			boolean revealProv = choice.startsWith("Reveal");
    			if (revealProv) {
    				DomEngine.addToLog(owner + " reveals a Province");
    				owner.gainInHand(DomCardName.Gold);
    			} else {
    				owner.gainInHand(DomCardName.Silver);
    			}
    		} else {
    			if (DomEngine.haveToLog) 
    				DomEngine.addToLog( owner + " reveals a Province");
    			owner.gainInHand(DomCardName.Gold);
    		}
    	}
    }
}