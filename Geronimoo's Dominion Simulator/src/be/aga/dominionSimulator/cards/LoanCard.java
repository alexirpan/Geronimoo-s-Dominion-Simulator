package be.aga.dominionSimulator.cards;

import java.util.ArrayList;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;

public class LoanCard extends DomCard {
	public LoanCard () {
		super( DomCardName.Loan);
	}

	public void play() {
		owner.availableCoins+=getCoinValue();        
		ArrayList< DomCard > theCards = owner.revealUntilType(DomCardType.Treasure);
		for (DomCard theCard : theCards) {
			if (owner instanceof DomHumanPlayer) {
				if (theCard.hasCardType(DomCardType.Treasure)) {
					ArrayList<String> choices = new ArrayList<String>();
					choices.add("Trash " + theCard.getName().toString());
					choices.add("Discard " + theCard.getName().toString());
					String choice = ((DomHumanPlayer) owner).chooseOption(choices, "Loan - trash revealed treasure?");
					if (choice.startsWith("Trash"))
						owner.trash(theCard);
					else
						owner.discard(theCard);
				} else {
					owner.discard(theCard);
				}
			} else if (theCard.getName()==DomCardName.Copper 
					|| (theCard.getName()==DomCardName.Silver && owner.countInDeck(DomCardName.Venture)>3)
					|| (theCard.getName()==DomCardName.Silver && owner.countInDeck(DomCardName.Platinum)>0)
					|| (theCard.getName()==DomCardName.Loan && owner.countInDeck(DomCardName.Copper)<2)) {
				owner.trash(theCard);
			} else {
				owner.discard( theCard );
			}
		}
	}
}