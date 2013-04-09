package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class Horse_TradersCard extends DomCard {
    public Horse_TradersCard () {
      super( DomCardName.Horse_Traders);
    }

    public void play() {
      owner.addAvailableBuys(1);
      owner.addAvailableCoins(3);
      if (owner instanceof DomHumanPlayer) {
    	  ((DomHumanPlayer) owner).doForcedDiscard(2, false, "Horse Traders - discard 2 cards");
      } else {
    	  owner.doForcedDiscard(2, false);
      }
    }
}