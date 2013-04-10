package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class Mining_VillageCard extends DomCard {
    public Mining_VillageCard () {
      super( DomCardName.Mining_Village);
    }

    public void play() {
      owner.addActions( 2 );
      owner.drawCards( 1 );
      possiblyTrashThis();
    }

    private final void possiblyTrashThis() {
    	//quick fix for Tactician
    	if (owner instanceof DomHumanPlayer) {
    		ArrayList<String> options = new ArrayList<String>();
    		options.add("Trash Mining Village");
    		options.add("Don't trash");
    		String choice = ((DomHumanPlayer) owner).chooseOption(options, "Mining Village - trash for $2?");
    		if (choice.startsWith("Trash")) {
    			DomPlayer theOwner = owner;
    			owner.trash(owner.removeCardFromPlay(this));
    			theOwner.addAvailableCoins(2);
    		}
    	} else {
    		if (!owner.getCardsFromHand(DomCardName.Tactician).isEmpty())
    			return;
    		if (owner.addingThisIncreasesBuyingPower( new DomCost( 2,0 ))) {
    			DomPlayer theOwner = owner;
    			owner.trash(owner.removeCardFromPlay( this ));
    			//owner has now become null... so we use theOwner
    			theOwner.addAvailableCoins( 2 );
    		}
    	}
    }
}