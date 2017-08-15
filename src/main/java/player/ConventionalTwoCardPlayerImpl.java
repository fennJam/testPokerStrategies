package player;

import java.util.ArrayList;
import java.util.List;

import card.history.CardHistoryBuilder;
import poker.Board;
import poker.Card;
import poker.Hand;
import poker.actions.PokerAction;

public class ConventionalTwoCardPlayerImpl  extends TwoCardPlayer {

	
	public ConventionalTwoCardPlayerImpl(String strategyFileAddress){
		importStrategyMapFromJSONFile(strategyFileAddress);
	}
	
	public ConventionalTwoCardPlayerImpl(){

	}
	
	
	@Override
	protected String getNodeId(Hand hand, Board board, List<PokerAction> actionsTaken) {
		String cardHistory = "";
		if (board != null) {
			cardHistory = getThreeCardHistory(singleCardHand.getCard(0).getDeckNumber(), board.getTurnedCards());
		} else {

		}
		return cardHistory + getActionsTakenString(actionsTaken);

	}
	
	private String getActionsTakenString(List<PokerAction> actionsTaken) {
		String actionString = "";
		for (PokerAction action : actionsTaken) {
			actionString += action.toString() + ",";
		}
		if (actionString != null && actionString.length() > 0
				&& actionString.charAt(actionString.length() - 1) == ',') {
			actionString = actionString.substring(0, actionString.length() - 1);
		}
		return actionString;
	}
	
	/**
	 * Returns a string representation of the card history
	 */
	private String getThreeCardHistory(int singleCardHand, List<Card> turnedCards) {

		boolean playerSuitedWithTurn = true;
		boolean riverSuitedWithTurn = true;

		int turnedCount = turnedCards.size();

		List<Integer> handSuit = new ArrayList<Integer>();
		handSuit.add(singleCardHand / 13);
		List<Integer> handRanks = new ArrayList<Integer>();
		handRanks.add(singleCardHand % 13);

		for (Card boardCard : turnedCards) {
			int deckNum = boardCard.getDeckNumber();
			handSuit.add(deckNum / 13);
			handRanks.add(deckNum % 13);
		}

		if (turnedCount > 0 && handSuit.get(0) != handSuit.get(1)) {
			playerSuitedWithTurn = false;
		}

		if (turnedCount == 2) {
			riverSuitedWithTurn = (handSuit.get(1) == handSuit.get(2));
		}

		String str = "[" + (playerSuitedWithTurn ? "S" : "O") + "," + (riverSuitedWithTurn ? "S" : "O") + ",";
		for (int i = 0; i < handRanks.size() - 1; i++)
			str += handRanks.get(i) + ",";

		str += handRanks.get(handRanks.size() - 1) + "]";
		return str;
	}
}
