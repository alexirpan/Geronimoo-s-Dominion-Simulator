package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class Ill_Gotten_GainsCard extends DomCard {
    public Ill_Gotten_GainsCard () {
      super( DomCardName.Ill_Gotten_Gains);
    }

    public void play() {
        owner.addAvailableCoins( 1 );
        DomCard theCopper = DomCardName.Copper.createNewCardInstance();
        theCopper.setOwner(owner);
        DomCost theCopperCoins = new DomCost(theCopper.getCoinValue(), 0);
        if (owner instanceof DomHumanPlayer) {
        	ArrayList<String> choices = new ArrayList<String>();
        	choices.add("Gain copper");
        	choices.add("Don't gain copper");
        	String choice = ((DomHumanPlayer) owner).chooseOption(choices, "IGG - gain a copper?");
        	if (choice.startsWith("Gain")) {
        		theCopper=owner.getCurrentGame().takeFromSupply(DomCardName.Copper);
        		if (theCopper!=null)
        			owner.gainInHand(theCopper);
        	}
        } else if (owner.wants(DomCardName.Copper) || 
        		//         	  (!owner.stillInEarlyGame() && owner.addingThisIncreasesBuyingPower(theCopperCoins))){
        		owner.addingThisIncreasesBuyingPower(theCopperCoins)){
        	theCopper=owner.getCurrentGame().takeFromSupply(DomCardName.Copper);
        	if (theCopper!=null)
        		owner.gainInHand(theCopper);
        }
    }
    
    @Override
	public void doWhenGained() {
      for (DomPlayer thePlayer : owner.getOpponents()) {
        if (owner.getCurrentGame().countInSupply(DomCardName.Curse )>0)
          thePlayer.gain(DomCardName.Curse);
      }
	}
}