package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class Counting_HouseCard extends DomCard {
    public Counting_HouseCard () {
      super( DomCardName.Counting_House);
    }

    public void play() {
      ArrayList<DomCard> theCoppers = owner.removeCardsFromDiscard(DomCardName.Copper);
      int numCoppers = theCoppers.size();
      if (owner instanceof DomHumanPlayer) {
    	  numCoppers = ((DomHumanPlayer) owner).chooseNumberUpToN(theCoppers.size(), "Counting House - pull how many coppers?");
      }
      for (DomCard theCard : theCoppers) {
    	  if (numCoppers > 0) {
    		  owner.putInHand(theCard);
    		  numCoppers--;
    	  } else {
    		  owner.discard(theCard);
    	  }
      }
    }
}