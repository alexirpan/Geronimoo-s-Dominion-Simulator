package be.aga.dominionSimulator.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;

import be.aga.dominionSimulator.DomBoard;
import be.aga.dominionSimulator.DomCard;
import be.aga.dominionSimulator.DomCost;
import be.aga.dominionSimulator.DomEngine;
import be.aga.dominionSimulator.DomGame;
import be.aga.dominionSimulator.DomHumanPlayer;
import be.aga.dominionSimulator.DomPlayer;
import be.aga.dominionSimulator.enums.DomCardName;
import be.aga.dominionSimulator.enums.DomPhase;
import be.aga.dominionSimulator.enums.DomSet;

public class DomGameFrame extends JFrame implements ActionListener {
	private DomEngine myEngine;
	private DomHumanPlayer thePlayer;
	private JLabel myDollarLabel;
	private JLabel myActionsLabel;
	private JLabel myTurnLabel;
	private JLabel myBuysLabel;
	private HashMap<JLabel, DomCardName> myBoardCards = new HashMap<JLabel, DomCardName>();
	private JEditorPane myLogArea;
	private JPanel myInPlayPanel;
	private DomHandPanel myHandPanel;
	private JPanel myKingdomPanel;
	private JPanel myCommonPanel;
	private DomCardLabel myBigImage;
	private JLabel selection;
	private final JLabel PASS = new JLabel(); // Other methods set selection to PASS when appropriate
	private DomGame myGame;
	private DomBoard myBoard;
	
	private final int SPLIT_DIVIDER_SIZE = 5;
	private JButton myAddTreasureBTN;
	private JButton myBuyPhaseBTN;
	private JButton myResignBTN;
	private JButton myLogBTN;
	private JButton myEndTurnBTN;

public DomGameFrame(DomEngine anEngine, DomHumanPlayer theHuman) {
	 myEngine=anEngine;
	 myEngine.setGameFrame(this);
	 thePlayer = theHuman;
}

public void initialize() {
	 myLogArea = new JEditorPane();
	 myLogArea.setEditable(false);
	 myLogArea.setContentType("text/html");
	 buildGUI();
	 setTitle("Play Dominion");
//    setPreferredSize(RefineryUtilities.getMaximumWindowBounds().getSize());
	 setPreferredSize(new Dimension(800,600));
	 pack();
	 setVisible(true);
}

private void buildGUI() {
	setLayout(new BorderLayout());
    JSplitPane theSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, getBoardSplit(), getBottomSplit());
    theSplit.setResizeWeight(0.3);
//    theSplit.setDividerLocation(300);
    theSplit.setDividerSize(SPLIT_DIVIDER_SIZE*2);
    theSplit.resetToPreferredSizes();
	getContentPane().add(theSplit, BorderLayout.CENTER);
	getContentPane().add(myEngine.getStatusBar(), BorderLayout.SOUTH);
}

private JSplitPane getBottomSplit() {
    JSplitPane theSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, getInfoSplit(), getLogAndHandSplit());
    theSplit.setResizeWeight(0.1);
    theSplit.setDividerSize(SPLIT_DIVIDER_SIZE*2);
    theSplit.resetToPreferredSizes();
	return theSplit;
}

private JSplitPane getInfoSplit() {
    JSplitPane theSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, getInfoPanel(), getInPlayPanel());
    theSplit.setResizeWeight(0);
    theSplit.setDividerSize(SPLIT_DIVIDER_SIZE);
    theSplit.resetToPreferredSizes();
	return theSplit;
}

private JPanel getLogPanel() {
	JPanel thePanel = new JPanel();
	thePanel.add(myLogArea);
	return thePanel;
}

private JPanel getInfoPanel() {
	JPanel thePanel = new JPanel();
	thePanel.setLayout(new GridBagLayout());
	GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
	theCons.fill=GridBagConstraints.NONE;
	//turn indicator
//	myTurnLabel = new JLabel("Your turn");
//	thePanel.add(myTurnLabel, theCons);
	//Actions indicator
	myActionsLabel = new JLabel("Actions: 1");
//	theCons.gridx++;
	thePanel.add(myActionsLabel, theCons);
	//Info button
	JButton theBTN = new JButton("More info");
	theBTN.setActionCommand("Info");
	theBTN.addActionListener(this);
	theCons.gridx++;
	thePanel.add(theBTN, theCons);
	//Buys indicator
	myBuysLabel = new JLabel("Buys: 1");
	theCons.gridx=0;
	theCons.gridy++;
	thePanel.add(myBuysLabel, theCons);
	//$ indicator
	myDollarLabel = new JLabel("Money: $0");
	theCons.gridy++;
	thePanel.add(myDollarLabel, theCons);
	return thePanel;
}

private JPanel getBigCardImagePanel() {
	JPanel thePanel = new JPanel();
	thePanel.setLayout(new GridBagLayout());
//	thePanel.setPreferredSize(new Dimension(250,250));
	GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
	thePanel.add(getBigImage(), theCons);
	return thePanel;
}

private DomCardLabel getBigImage() {
	myBigImage = new DomCardLabel(); 
	return myBigImage;
}

//private JSplitPane getGameSplit() {
//    JSplitPane theGameSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, getBoardAndGameLogSplit(), getInPlayAndHandSplit());
//    theGameSplit.setResizeWeight(0.5);
//    theGameSplit.setDividerSize(5);
//	return theGameSplit;
//}

//private Component getBoardAndGameLogSplit() {
//    JSplitPane theSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, getBoardSplit(), getLogPane());
//    theSplit.setResizeWeight(0.6);
//    theSplit.setDividerSize(SPLIT_DIVIDER_SIZE);
//	return theSplit;
//}

private JSplitPane getBoardSplit() {
    JSplitPane theSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, new JScrollPane(getCommonPanel()), new JScrollPane(getKingdomPanel()));
	theSplit.setBorder(new TitledBorder("Board"));
    theSplit.setResizeWeight(0.3);
    theSplit.setDividerSize(SPLIT_DIVIDER_SIZE);
	return theSplit;
}

private JPanel getKingdomPanel() {
	myKingdomPanel = new JPanel();
	myKingdomPanel.setLayout(new GridBagLayout());
	GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
	myKingdomPanel.setMinimumSize(new Dimension(100,100));
	for (DomCardName cardName : myEngine.getCardsUsed()) {
		if (cardName.getSet()!=DomSet.Common){
			DomCardLabel label = getCardLabel(cardName);
			label.addMouseListener(new SupplyCardListener(cardName, this));
		    myKingdomPanel.add(label, theCons);
			theCons.gridx++;
		}
	}
	return myKingdomPanel;
}

private JPanel getCommonPanel() {
	myCommonPanel = new JPanel();
	myCommonPanel.setLayout(new GridBagLayout());
	GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
	myCommonPanel.setMinimumSize(new Dimension(100,100));
	for (DomCardName cardName : myEngine.getCardsUsed()) {
		if (cardName.getSet()==DomSet.Common){
			DomCardLabel label = getCardLabel(cardName);
			label.addMouseListener(new SupplyCardListener(cardName, this));
		    myCommonPanel.add(label, theCons);
			theCons.gridx++;
		}
	}
	return myCommonPanel;
}

private DomCardLabel getCardLabel(DomCardName cardName) {
    DomCardLabel theLBL = new DomCardLabel(cardName, this);
    myBoardCards.put(theLBL, cardName);
    return theLBL;
}

//private JTextArea getLogPane() {
//	myLogArea = new JTextArea("sdglksdljsdlhflsdlsdf<br>sdvsdldvlvsd<br>");
////	JScrollPane theScr = new JScrollPane(myLogArea);
////	return theScr;
//	return myLogArea;
//}

private Component getLogAndHandSplit() {
	JScrollPane log = new JScrollPane(getLogPanel());
    JSplitPane theSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, log, getHandAndButtonsSplit());
    theSplit.setResizeWeight(0.3);
    theSplit.setDividerSize(SPLIT_DIVIDER_SIZE);
	return theSplit;
}

private Component getInPlayPanel() {
	myInPlayPanel = new JPanel();
	myInPlayPanel.setMinimumSize(new Dimension(100,100));
	myInPlayPanel.setLayout(new GridBagLayout());
	myInPlayPanel.setBorder(new TitledBorder("In Play"));
	GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
	for (DomCard card : thePlayer.getCardsInPlay()) {
	    myInPlayPanel.add(getCardLabel(card.getName()), theCons);
		theCons.gridx++;
	}
	return myInPlayPanel;
}

private Component getHandAndButtonsSplit() {
    JSplitPane theSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, getButtonPanel(), getHandPanel());
    theSplit.setResizeWeight(0);
    theSplit.setDividerSize(SPLIT_DIVIDER_SIZE);
    theSplit.resetToPreferredSizes();
	return theSplit;
}

private JComponent getHandPanel() {
	myHandPanel = new DomHandPanel("In Hand", this);
	myHandPanel.setCards(thePlayer.getCardsInHand());
	return myHandPanel;
}

private void addCardsToHand(ArrayList<DomCard> aCards) {
	int theNumberOfSplits = 1;
	//2X^2 + 5X - "number of cards to display" > 0
	while (theNumberOfSplits*theNumberOfSplits*2 + 5*theNumberOfSplits - aCards.size()<0 || theNumberOfSplits>=4)
	  theNumberOfSplits*=2;

    if (theNumberOfSplits==1) {	
		GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
		for (DomCard card : aCards) {
		    myHandPanel.add(getCardLabel(card.getName()), theCons);
			theCons.gridx++;
		}
    }
    if (theNumberOfSplits==2) {
    	
		GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
		for (DomCard card : aCards) {
		    myHandPanel.add(getCardLabel(card.getName()), theCons);
			theCons.gridx++;
		}
    }
    
    
}

private JPanel getButtonPanel() {
    final JPanel thePanel = new JPanel();
    thePanel.setLayout( new GridBagLayout() );
    final GridBagConstraints theCons = DomGui.getGridBagConstraints( 2 );
    theCons.fill=GridBagConstraints.NONE;
    theCons.anchor=GridBagConstraints.NORTHWEST;
    //add treasures button
    myAddTreasureBTN = new JButton();
    myAddTreasureBTN.addActionListener(this);
    myAddTreasureBTN.setActionCommand("AddTreasures");
    myAddTreasureBTN.setText("+$0");
    thePanel.add(myAddTreasureBTN,theCons);
    //buy phase button
    myBuyPhaseBTN = new JButton("Buy Phase");
    myBuyPhaseBTN.addActionListener(this);
    myBuyPhaseBTN.setActionCommand("BuyPhase");
    theCons.gridx++;
    thePanel.add(myBuyPhaseBTN,theCons);
    //buy phase button
    myEndTurnBTN = new JButton("End turn");
    myEndTurnBTN.addActionListener(this);
    myEndTurnBTN.setActionCommand("EndTurn");
    theCons.gridx++;
    thePanel.add(myEndTurnBTN,theCons);
    //log button
    myLogBTN = new JButton("Game Log");
    myLogBTN.addActionListener(this);
    myLogBTN.setActionCommand("Log");
    theCons.gridx++;
    thePanel.add(myLogBTN,theCons);
    //place filler
    theCons.gridx++;
    DomGui.addHeavyLabel(thePanel, theCons);
   //Resign button
    myResignBTN = new JButton("Resign");
    myResignBTN.addActionListener(this);
    myResignBTN.setActionCommand("Resign");
    theCons.gridx++;
    thePanel.add(myResignBTN,theCons);

    return thePanel;
}

@Override
public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand().equals("Cancel")){
		dispose();
	}
	else if (e.getActionCommand().equals("OK")){
	}
	else if (e.getActionCommand().equals("Clear")){
	}
	else if (e.getActionCommand().equals("Delete")){
	}
	else if (e.getActionCommand().equals("Log")) {
		
	}
	else if (e.getActionCommand().equals("BuyPhase")) {
		thePlayer.setPhase(DomPhase.Buy);
	}
	else if (e.getActionCommand().equals("AddTreasures")) {
		thePlayer.setPhase(DomPhase.Buy);
		thePlayer.playTreasures();
		updateGraphics();
	}
	else if (e.getActionCommand().equals("EndTurn")) {
		thePlayer.doCleanupPhase();
		myGame.cycleActivePlayer();
		while (myGame.getActivePlayer() != thePlayer) {
			myGame.getActivePlayer().takeTurn();
			myGame.cycleActivePlayer();
		}
		thePlayer.startActionPhase();
		thePlayer.setPhase(DomPhase.Action);
		// Start this turn
		updateGraphics();
	}
}

public void setBigImage(DomCardName myCardName) {
	myBigImage.setCardName(myCardName); 	
}

public DomPlayer getPlayer() {
	return thePlayer;
}

public JLabel getSelection() {
	return selection;
}

public void setSelection(JLabel label) {
	selection = label;
}

public void updateGraphics() {
	updateHand();
	updateInPlay();
	updateTreasureButton();
	updateLabels();
	myLogArea.setText(DomEngine.myLog);
	myLogArea.repaint();
}

public void updateHand() {
	myHandPanel.setCards(thePlayer.getCardsInHand());
}

public void updateInPlay() {
	myInPlayPanel.removeAll();
	GridBagConstraints theCons = DomGui.getGridBagConstraints(2);
	for (DomCard card : thePlayer.getCardsInPlay()) {
	    myInPlayPanel.add(getCardLabel(card.getName()), theCons);
		theCons.gridx++;
	}
	myInPlayPanel.repaint();
	myInPlayPanel.revalidate();
}

public void updateTreasureButton() {
	myAddTreasureBTN.setText("+$" + thePlayer.getMoneyInHand());
	myAddTreasureBTN.repaint();
}

public void updateLabels() {
	myActionsLabel.setText("Actions: " + thePlayer.getActionsLeft());
	myActionsLabel.repaint();
	myBuysLabel.setText("Buys: " + thePlayer.getBuysLeft());
	myBuysLabel.repaint();
	String money = Integer.toString(thePlayer.availableCoins);
	for (int i = 0; i < thePlayer.availablePotions; i++) {
		money += "P";
	}
	myDollarLabel.setText("Money: $" + money);
	myDollarLabel.repaint();
	for (Component c : myCommonPanel.getComponents()) {
		if (c instanceof DomCardLabel) {
			int numCopies = myBoard.count(myBoardCards.get((JLabel) c));
			((DomCardLabel) c).setSupply(numCopies);
		}
	}
	for (Component c : myKingdomPanel.getComponents()) {
		if (c instanceof DomCardLabel) {
			int numCopies = myBoard.count(myBoardCards.get((JLabel) c));
			((DomCardLabel) c).setSupply(numCopies);
		}
	}
}


public void setGameAndBoard(DomGame game) {
	myGame = game;
	myBoard = game.getBoard();
}

public ArrayList<DomCardName> gainableCardsUpToCost(DomCost cost) {
	ArrayList<DomCardName> cardNames = new ArrayList<DomCardName>();
	for (DomCardName name : myBoard.keySet()) {
		if ((name.getCost(myGame).compareTo(cost) <= 0) && (myBoard.count(name) > 0))
			cardNames.add(name);
	}
	return cardNames;
}

public ArrayList<DomCardName> gainableCardsExactCost(DomCost domCost) {
	ArrayList<DomCardName> cardNames = new ArrayList<DomCardName>();
	for (DomCardName name : myBoard.keySet()) {
		if ((name.getCost(myGame).compareTo(domCost) == 0) && (myBoard.count(name) > 0))
			cardNames.add(name);
	}
	return cardNames;
}
}