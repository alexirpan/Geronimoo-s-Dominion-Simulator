package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class DevelopCard extends DomCard {

    public DevelopCard () {
      super( DomCardName.Develop);
    }

    public void play() {
        if (owner.getCardsInHand().isEmpty())
        	return;
        if (owner instanceof DomHumanPlayer) {
        	DomCard trashed = ((DomHumanPlayer) owner).chooseExactlyNCardsFromList(1, owner.getCardsInHand(), "Develop - trash a card").get(0);
        	DomCost upCost = trashed.getName().getCost(owner.getCurrentGame()).add(new DomCost(1,0));
        	DomCost downCost = trashed.getName().getCost(owner.getCurrentGame()).add(new DomCost(-1,0));
        	ArrayList<String> orderChoices = new ArrayList<String>();
        	orderChoices.add("Gain " + downCost);
        	orderChoices.add("Gain " + upCost);
        	String choice = ((DomHumanPlayer) owner).chooseOption(orderChoices, "Develop - choose gain order (choice goes on top)");
        	if (choice.equals("Gain " + downCost)) {
        		((DomHumanPlayer) owner).gainCardOfExactCost(upCost, "Develop - gain card of cost " + upCost, true);
        		((DomHumanPlayer) owner).gainCardOfExactCost(downCost, "Develop - gain card of cost " + downCost, true);
        	} else {
        		((DomHumanPlayer) owner).gainCardOfExactCost(downCost, "Develop - gain card of cost " + downCost, true);
        		((DomHumanPlayer) owner).gainCardOfExactCost(upCost, "Develop - gain card of cost " + upCost, true);
        	}
        }
        Collections.sort( owner.getCardsInHand(), SORT_FOR_TRASHING);
        //find a card that will gain 2 cards
        for (DomCard theCard : owner.getCardsInHand()) {
          DomCardName theDesiredUpCard = owner.getDesiredCard(theCard.getName().getCost(owner.getCurrentGame()).add(new DomCost(1,0 ) ), true);
          if (theDesiredUpCard!=null) {
            DomCardName theDesiredDownCard = owner.getDesiredCard(theCard.getName().getCost(owner.getCurrentGame()).add(new DomCost(-1,0 ) ), true);
            if (theDesiredDownCard!=null) {
	           develop(owner.removeCardFromHand( theCard ));
	           return; 
            }
          }
        }
        //if no 2 useful cards can be gained, just trash the worst card
        develop(owner.removeCardFromHand( owner.getCardsInHand().get( 0 ) )); 
    }

	private void develop(DomCard aCardToTrash) {
        DomCost theUpCost = aCardToTrash.getName().getCost(owner.getCurrentGame()).add(new DomCost(1,0 )) ;
        DomCost theDownCost = aCardToTrash.getName().getCost(owner.getCurrentGame()).add(new DomCost(-1,0 )) ;
        owner.trash( aCardToTrash );
        DomCardName theDownCardName = owner.getCurrentGame().getBestCardInSupplyFor(owner, null, theDownCost, true);
        if (theDownCardName!=null) {
          DomCard theDownCard = owner.getCurrentGame().takeFromSupply(theDownCardName);
          owner.gainOnTopOfDeck(theDownCard);
        }
        DomCardName theUpcardName = owner.getCurrentGame().getBestCardInSupplyFor(owner, null, theUpCost, true);
        if (theUpcardName!=null) {
          DomCard theUpCard = owner.getCurrentGame().takeFromSupply(theUpcardName);
          owner.gainOnTopOfDeck(theUpCard);
        }
	}
	
    @Override
    public boolean wantsToBePlayed() {
        for (DomCard theCard : owner.getCardsInHand()) {
        	if (theCard==this)
        	  continue;
            if (theCard.getTrashPriority()<20)
              return true;
            DomCardName theDesiredUpCard = owner.getDesiredCard(theCard.getName().getCost(owner.getCurrentGame()).add(new DomCost(1,0 ) ), true);
            DomCardName theDesiredDownCard = owner.getDesiredCard(theCard.getName().getCost(owner.getCurrentGame()).add(new DomCost(-1,0 ) ), true);
            if (theDesiredUpCard!=null 
             && theDesiredDownCard!=null
             && theDesiredUpCard.getTrashPriority(owner)>=theCard.getTrashPriority())
            	return true;
        }
        return false;
   }

}