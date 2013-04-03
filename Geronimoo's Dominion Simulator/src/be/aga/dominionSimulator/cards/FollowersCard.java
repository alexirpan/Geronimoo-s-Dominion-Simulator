package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class FollowersCard extends DomCard {
    public FollowersCard () {
      super( DomCardName.Followers);
    }

    public void play() {
      owner.drawCards(2);
      DomCard theEstate = owner.getCurrentGame().takeFromSupply( DomCardName.Estate);
      if (theEstate!=null) {
        owner.gain(theEstate);
      } 
      for (DomPlayer thePlayer : owner.getOpponents()) {
        if (thePlayer.checkDefense()) 
          continue;
        if (owner.getCurrentGame().countInSupply(DomCardName.Curse )>0)
          thePlayer.gain(DomCardName.Curse);
        if (thePlayer instanceof DomHumanPlayer)
        	((DomHumanPlayer) thePlayer).doForcedDiscard(thePlayer.getCardsInHand().size()-3, false, "Followers - discard down to 3 cards");
        else
        	thePlayer.doForcedDiscard(thePlayer.getCardsInHand().size()-3, false);
      }
    }
}