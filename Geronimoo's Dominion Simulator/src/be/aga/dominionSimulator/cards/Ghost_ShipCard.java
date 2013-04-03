package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class Ghost_ShipCard extends DomCard {
    public Ghost_ShipCard () {
      super( DomCardName.Ghost_Ship);
    }

    public void play() {
      owner.drawCards(2);
      for (DomPlayer thePlayer : owner.getOpponents()) {
        if (thePlayer.checkDefense()) 
        	continue;
        if (thePlayer instanceof DomHumanPlayer) {
        	((DomHumanPlayer) thePlayer).doForcedDiscard(thePlayer.getCardsInHand().size()-3, true, "Ghost Ship - topdeck to 3 cards in hand");
        } else {
        	thePlayer.doForcedDiscard(thePlayer.getCardsInHand().size()-3, true);
        }
      }
    }
}