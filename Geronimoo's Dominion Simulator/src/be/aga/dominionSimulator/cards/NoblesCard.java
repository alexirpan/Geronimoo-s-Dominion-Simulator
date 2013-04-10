package be.aga.dominionSimulator.cards;

import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.enums.DomCardName;

public class NoblesCard extends DomCard {
	public NoblesCard () {
		super( DomCardName.Nobles);
	}

	public void play() {
		if (owner instanceof DomHumanPlayer) {
			String choice = ((DomHumanPlayer) owner).chooseOption("Nobles - choose one", "+2 Actions", "+3 Cards");
			if (choice.startsWith("+2"))
				owner.addActions(2);
			else
				owner.drawCards(3);
		} else if (owner.getNextActionToPlay()!=null && owner.getActionsLeft()==0) {
			owner.addActions(2);
		} else {
			owner.drawCards(3);
		}
	}
}