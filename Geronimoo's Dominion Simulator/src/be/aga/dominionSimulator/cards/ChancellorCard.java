package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class ChancellorCard extends DomCard {
    public ChancellorCard () {
      super( DomCardName.Chancellor);
    }

    public void play() {
      owner.addAvailableCoins(2);
      if (owner instanceof DomHumanPlayer) {
    	  ArrayList<String> choices = new ArrayList<String>();
    	  choices.add("Discard deck");
    	  choices.add("Don't discard");
    	  String choice = ((DomHumanPlayer) owner).chooseOption(choices, "Chancellor - discard deck?");
    	  if (choice.startsWith("Discard"))
    		  owner.putDeckInDiscard();
    	  else
    		  DomEngine.addToLog(owner + " does not discard.");
      } else {
    	  owner.putDeckInDiscard();
      }
    }
    @Override
    public int getPlayPriority() {
    	if (owner.getActionsLeft()>1)
    		return DomCardName.Counting_House.getPlayPriority()-1;
    	return super.getPlayPriority();
    }
}