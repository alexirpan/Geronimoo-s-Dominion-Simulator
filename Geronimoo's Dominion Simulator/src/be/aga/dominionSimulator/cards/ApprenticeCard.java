package be.aga.dominionSimulator.cards;

import java.util.Collections;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class ApprenticeCard extends DomCard {
    public ApprenticeCard () {
      super( DomCardName.Apprentice);
    }

    public void play() {
      owner.addActions( 1 );
      if (owner.getCardsInHand().isEmpty())
    	  return;
      if (owner instanceof DomHumanPlayer) {
    	  DomCard c = ((DomHumanPlayer) owner).chooseExactlyNCardsFromList(1, owner.getCardsInHand(), "Apprentice - choose card to trash").get(0);
    	  owner.trash(owner.removeCardFromHand(c));
    	  owner.drawCards(getApprenticeValue(c));
      } else {
    	  Collections.sort( owner.getCardsInHand() , SORT_FOR_TRASHING);
    	  DomCard theCardToTrash = owner.getCardsInHand().get(0);
    	  for (DomCard theCard : owner.getCardsInHand()) {
    		  if (getApprenticeValue(theCard) > 0 && enoughMoneyLeft(theCard) && areVPsNotInDanger(theCard)){
    			  theCardToTrash=theCard;
    			  break;
    		  }
    	  }
    	  owner.trash(owner.removeCardFromHand( theCardToTrash));
    	  owner.drawCards(getApprenticeValue(theCardToTrash));
      }
    }

	private boolean areVPsNotInDanger(DomCard theCard) {
		if (theCard.getName()==DomCardName.Estate)
			return true;
		if (!theCard.hasCardType(DomCardType.Victory))
			return true;
		if (owner.countVictoryPoints()-theCard.getVictoryValue()<owner.countMaxOpponentsVictoryPoints())
		  return false;
		return true;
	}

	private boolean enoughMoneyLeft(DomCard theCard) {
		 if (owner.getTotalMoneyInDeck()-theCard.getCoinValue()
				 < (owner.getCurrentGame().countInSupply(DomCardName.Colony)>0 ? 11 : 8))
			 //we don't want to destroy our buying power
		   return false;
		 return true;
	}

	private int getApprenticeValue(DomCard theCard) {
      return theCard.getCoinCost(owner.getCurrentGame())+theCard.getPotionCost()*2;
	}
}