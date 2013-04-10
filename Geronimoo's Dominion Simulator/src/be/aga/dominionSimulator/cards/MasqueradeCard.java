package be.aga.dominionSimulator.cards;

import java.util.ArrayList;
import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class MasqueradeCard extends DomCard {
    public MasqueradeCard () {
      super( DomCardName.Masquerade);
    }

    public void play() {
      owner.drawCards(2);
      // TODO make this more consistent when I'm less lazy
      if (!owner.getOpponents().isEmpty()) {
        owner.passCardToTheLeftForMasquerade(owner.chooseCardToPass(),owner);
      }
      if (owner.getCardsInHand().isEmpty())
    	  return;
      if (owner instanceof DomHumanPlayer) {
    	  ArrayList<String> choices = new ArrayList<String>();
    	  choices.add("Trash a card");
    	  choices.add("Don't trash");
    	  String choice = ((DomHumanPlayer) owner).chooseOption(choices, "Masquerade - trash a card?");
    	  if (choice.startsWith("Trash")) {
    		  DomCard toTrash = ((DomHumanPlayer) owner).chooseExactlyNCardsFromList(1, owner.getCardsInHand(), "Masquerade - trash a card").get(0);
    		  owner.trash(owner.removeCardFromHand(toTrash));
    	  } else {
    		  DomEngine.addToLog(owner + " trashes nothing");
    	  }
      } else {
    	  Collections.sort(owner.getCardsInHand(),SORT_FOR_TRASHING);
    	  DomCard theCardToTrash=owner.getCardsInHand().get( 0 );
    	  if (theCardToTrash.getTrashPriority() < 16 && !owner.removingReducesBuyingPower( theCardToTrash )) {
    		  owner.trash(owner.removeCardFromHand( theCardToTrash));
    	  } else {
    		  if (DomEngine.haveToLog ) DomEngine.addToLog( owner + " trashes nothing");
    	  }
      }
    }
}