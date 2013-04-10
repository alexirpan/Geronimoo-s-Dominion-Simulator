package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class JesterCard extends DomCard {
    public JesterCard () {
      super( DomCardName.Jester);
    }

    public void play() {
      owner.addAvailableCoins(2);
      for (DomPlayer thePlayer : owner.getOpponents()) {
          if (thePlayer.checkDefense()) 
        	continue;
          ArrayList< DomCard > theCards = thePlayer.revealTopCards(1);
          if (theCards.isEmpty())
        	continue;
          thePlayer.discard(theCards.get(0));
          if (theCards.get(0).hasCardType(DomCardType.Victory)){
        	  if (owner.getCurrentGame().countInSupply(DomCardName.Curse)>0)
        		thePlayer.gain(DomCardName.Curse);
        	  continue;
          }
          if (owner.getCurrentGame().countInSupply(theCards.get(0).getName())==0)
        	continue;
          if (owner instanceof DomHumanPlayer) {
        	  DomCard theCard = theCards.get(0);
        	  ArrayList<String> choices = new ArrayList<String>();
        	  choices.add("Gain a copy");
        	  choices.add(thePlayer + " gains a copy");
        	  String choice = ((DomHumanPlayer) owner).chooseOption(choices, "Jester - revealed a " + theCard.getName().toString());
        	  if (choice.startsWith("Gain"))
        		  owner.gain(theCard.getName());
        	  else
        		  thePlayer.gain(theCard.getName());
          } else {
        	  if (theCards.get(0).getTrashPriority()<16) {
        		  thePlayer.gain(theCards.get(0).getName());
        	  } else {
        		  owner.gain(theCards.get(0).getName());
        	  }
          }
      }
    }
}