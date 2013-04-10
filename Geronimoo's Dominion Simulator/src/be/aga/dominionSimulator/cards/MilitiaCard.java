package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class MilitiaCard extends DomCard {
	public MilitiaCard () {
		super( DomCardName.Militia);
	}

	public void play() {
		owner.addAvailableCoins(2);
		for (DomPlayer thePlayer : owner.getOpponents()) {
			if (!thePlayer.checkDefense()) {
				if (thePlayer instanceof DomHumanPlayer)
					((DomHumanPlayer) thePlayer).doForcedDiscard(thePlayer.getCardsInHand().size()-3, false, "Militia - discard down to 3 cards");
				else
					thePlayer.doForcedDiscard(thePlayer.getCardsInHand().size()-3, false);
			}
		}
	}
}