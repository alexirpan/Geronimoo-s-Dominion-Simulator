package be.aga.dominionSimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import be.aga.dominionSimulator.cards.Secret_ChamberCard;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomCardType;
import be.aga.dominionSimulator.enums.DomPhase;
import be.aga.dominionSimulator.gui.DomGameFrame;
import be.aga.dominionSimulator.gui.OptionSelectorDialog;
import be.aga.dominionSimulator.gui.ReorderDialog;

public class DomHumanPlayer extends DomPlayer {

	private DomGameFrame playerInterface;
	
	public DomHumanPlayer(String name) {
		super(name);
	}

	public void setInterface(DomGameFrame gui) {
		playerInterface = gui;
	}

	public void doCleanupPhase() {
		super.doCleanUpPhase();
	}
	
	public void setPhase(DomPhase aPhase) {
		super.setPhase(aPhase);
	}
	
	public void startActionPhase() {
		initializeTurn();
		resolveHorseTraders();
		resolveDurationEffects();
	}
	
	@Override
	public void playTreasures() {
		ArrayList<DomCard> treasureCards = getCardsFromHand(DomCardType.Treasure);
		for (DomCard theCard : treasureCards) {
			play(removeCardFromHand( theCard ));
		}
		
		if (previousPlayedCardName!=null) {
			DomEngine.addToLog( name + " plays " + (sameCardCount+1)+" "+ previousPlayedCardName.toHTML()
					+ (sameCardCount>0 ? "s" : ""));
			previousPlayedCardName=null;
			sameCardCount=0;
		}
		showBuyStatus();
    }
	
	public boolean tryToBuy( DomCardName aCardName ) {
        if (game.countInSupply( aCardName )==0) {
//          if (DomEngine.haveToLog) DomEngine.addToLog( aCardName + " is no more available to buy");
        	return false;
        }
        if (forbiddenCardsToBuy.contains(aCardName))
        	return false;

        if (aCardName==DomCardName.Grand_Market && !getCardsFromPlay(DomCardName.Copper).isEmpty())
        	return false;
        
        buy(game.takeFromSupply( aCardName ));
        return true;
    }
	
	public String chooseOption(ArrayList<String> options, String message) {
		OptionSelectorDialog d = new OptionSelectorDialog(playerInterface, options, message, 1, 1);
		d.pack();
		d.setVisible(true);
		return d.getSelectedOptions().get(0);
	}
	
	public ArrayList<DomCard> chooseExactlyNCardsFromList(int n, ArrayList<DomCard> lst, String message) {
		if (lst.size() < n) {
			return chooseExactlyNCardsFromList(lst.size(), lst, message);
		}
		ArrayList<String> cardNames = new ArrayList<String>();
		for (DomCard c : lst) {
			cardNames.add(c.getName().toString());
		}
		OptionSelectorDialog d = new OptionSelectorDialog(playerInterface, cardNames, message, n, n);
		d.pack();
		d.setVisible(true);
		
		return getCardsFromNames(lst, d.getSelectedOptions());
	}
	
	public ArrayList<DomCard> chooseUpToNCardsFromList(int n, ArrayList<DomCard> lst, String message) {
		ArrayList<String> cardNames = new ArrayList<String>();
		for (DomCard c : lst) {
			cardNames.add(c.getName().toString());
		}
		OptionSelectorDialog d = new OptionSelectorDialog(playerInterface, cardNames, message, 0, n);
		d.pack();
		d.setVisible(true);
		
		return getCardsFromNames(lst, d.getSelectedOptions());
	}
	
	private ArrayList<DomCard> getCardsFromNames(ArrayList<DomCard> cardLst, ArrayList<String> names) {
		HashSet<DomCard> cards = new HashSet<DomCard>();
		for (String name : names) {
			for (DomCard c : cardLst) {
				if (!cards.contains(c) && c.getName().toString().equals(name)) {
					cards.add(c);
					break;
				}
			}
		}
		return new ArrayList<DomCard>(cards);
	}
	
	public int chooseNumberUpToN(int n, String message) {
		ArrayList<String> numbers = new ArrayList<String>();
		for (int i = 0; i <= n; i++) {
			numbers.add(Integer.toString(i));
		}
		return Integer.parseInt(chooseOption(numbers, message));
	}
	
	public int numCardsWithName(DomCardName name, ArrayList<DomCard> cards) {
		int count = 0;
		for (DomCard c : cards) {
			if (c.getName().equals(name))
				count++;
		}
		return count;
	}
	
	public ArrayList<DomCard> reorderCards(ArrayList<DomCard> cards, String title) {
		ArrayList<String> options = new ArrayList<String>();
		for (DomCard c : cards) {
			options.add(c.getName().toString());
		}
		ReorderDialog d = new ReorderDialog(playerInterface, options, title);
		d.pack();
		d.setVisible(true);
		ArrayList<String> newOrder = d.getOrderedOptions();
		ArrayList<DomCard> newCardOrder = new ArrayList<DomCard>();
		for (String s : newOrder) {
			int i = 0;
			while (i < cards.size()) {
				if (cards.get(i).getName().toString().equals(s)) {
					newCardOrder.add(cards.remove(i));
					break;
				}
				i++;
			}
		}
		return newCardOrder;
	}

	@Override
	public boolean checkDefense() {
		ArrayList<DomCard> reactions = getCardsFromHand(DomCardType.Reaction);
		boolean doneRevealing = false;
		boolean defense = false;
		while (!doneRevealing && !reactions.isEmpty()) {
			ArrayList<String> choices = new ArrayList<String>();
			if (!getCardsFromHand(DomCardName.Horse_Traders).isEmpty())
				choices.add("Horse Traders");
			if (!getCardsFromHand(DomCardName.Secret_Chamber).isEmpty())
				choices.add("Secret Chamber");
			if (!getCardsFromHand(DomCardName.Moat).isEmpty())
				choices.add("Moat");
			choices.add("Done revealing");
			
			String choice = chooseOption(choices, "Opponent played attack - reveal reaction?");
			if (choice.startsWith("Horse")) {
				if (DomEngine.haveToLog) DomEngine.addToLog( this + " sets a " + DomCardName.Horse_Traders.toHTML() + " aside" );
				horseTradersPile.add(removeCardFromHand(getCardsFromHand(DomCardName.Horse_Traders).get(0)));
			} else if (choice.startsWith("Secret")) {
				((Secret_ChamberCard) getCardsFromHand(DomCardName.Secret_Chamber).get(0)).react();
			} else if (choice.startsWith("Moat")) {
				if (DomEngine.haveToLog) DomEngine.addToLog( this + " reveals a " + DomCardName.Moat.toHTML() + " from hand and prevents the attack");
				defense = true;
			} else if (choice.startsWith("Done"))
				doneRevealing = true;
			
			reactions = getCardsFromHand(DomCardType.Reaction);
		}
		for (DomCard theCard: cardsInPlay) {
			if (theCard.getName()==DomCardName.Lighthouse ){
				if (DomEngine.haveToLog) DomEngine.addToLog( theCard + " prevents the attack!" );
				return true;
			}
		}
		return defense;
	}

	public void doForcedDiscard(int discardsLeft, boolean discardToTopOfDeck, String message) {
		if (discardsLeft <= 0)
			return;
		if (discardsLeft > getCardsInHand().size()) {
			doForcedDiscard(getCardsInHand().size(), discardToTopOfDeck, message);
			return;
		}
		ArrayList<DomCard> cardsToDiscard = chooseExactlyNCardsFromList(discardsLeft, getCardsInHand(), message);
		if (discardToTopOfDeck) {
			cardsToDiscard = reorderCards(cardsToDiscard, "Reorder top-decked cards");
			for (int i = cardsToDiscard.size(); i >= 0; i--) {
				discardFromHandToTopOfDeck(cardsToDiscard.get(i));
			}
		} else {
			for (DomCard c : cardsToDiscard) {
				discardFromHand(c);
			}
		}
	}
	
	/**
	 * Asks the player to discard any number of cards
	 * @param message The title of the message
	 * @return A list of the cards discarded
	 */
	public ArrayList<DomCard> discardAnyNumberOfCards(String message) {
		ArrayList<DomCard> cardsToDiscard = chooseUpToNCardsFromList(getCardsInHand().size(), getCardsInHand(), message);
		for (DomCard c : cardsToDiscard) {
			discardFromHand(c);
		}
		return cardsToDiscard;
	}
	
	public void gainCardOfExactCost(DomCost domCost, String message, boolean gainOnTop) {
		ArrayList<DomCardName> possibleNames = playerInterface.gainableCardsExactCost(domCost);
		if (!possibleNames.isEmpty()) {
			ArrayList<String> names = new ArrayList<String>();
			for (DomCardName n : possibleNames)
				names.add(n.toString());
			String choice = chooseOption(names, message);
			DomCardName cardName = null;
			for (DomCardName n : possibleNames) {
				if (n.toString().equals(choice)) {
					cardName = n;
					break;
				}
			}
			if (gainOnTop)
				gainOnTopOfDeck(getCurrentGame().takeFromSupply(cardName));
			else
				gain(cardName);
		}
	}

	public void gainCardUpToCost(DomCost domCost, String message) {
		ArrayList<DomCardName> possibleNames = playerInterface.gainableCardsUpToCost(domCost);
		if (!possibleNames.isEmpty()) {
			ArrayList<String> names = new ArrayList<String>();
			for (DomCardName n : possibleNames)
				names.add(n.toString());
			String choice = chooseOption(names, message);
			DomCardName cardName = null;
			for (DomCardName n : possibleNames) {
				if (n.toString().equals(choice)) {
					cardName = n;
					break;
				}
			}
			gain(cardName);
		}
	}

	public DomCardName nameCard(String message) 
	{
		Set<DomCardName> validNames = getCurrentGame().getBoard().keySet();
		ArrayList<String> names = new ArrayList<String>();
		for (DomCardName n : validNames) {
			names.add(n.toString());
		}
		String choice = chooseOption(names, message);
		for (DomCardName n : validNames) {
			if (n.toString().equals(choice))
				return n;
		}
		return null;
	}
}
