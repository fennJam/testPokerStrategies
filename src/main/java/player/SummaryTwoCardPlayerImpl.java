package player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import card.history.CardHistoryBuilder;
import poker.Board;
import poker.Card;
import poker.Hand;
import poker.actions.PokerAction;

public class SummaryTwoCardPlayerImpl  extends TwoCardPlayer {

	public SummaryTwoCardPlayerImpl(String strategyFileAddress){
		importStrategyMapFromJSONFile(strategyFileAddress);
	}
	
	
	public SummaryTwoCardPlayerImpl() {

	}


	@Override
	protected String getNodeId(Hand hand, Board board, List<PokerAction> actionsTaken) {
		String unSummarisedActionsTaken = getActionsTakenString(actionsTaken).replaceAll(",", "");
		String summaryState = "";
		int dealCount = unSummarisedActionsTaken.length()-(unSummarisedActionsTaken.replaceAll("D", "").length());
		boolean dealLastAction = unSummarisedActionsTaken.endsWith("D");
		if(dealCount<2){
			summaryState = unSummarisedActionsTaken;
		}else{
			String[] actionRounds = unSummarisedActionsTaken.split("D");
			int roundToSummarise = actionRounds.length-1;
			if(dealLastAction){
				roundToSummarise = actionRounds.length;
				}
			for(int actionRoundIndex=0;actionRoundIndex<roundToSummarise;actionRoundIndex++){
				String actionRound = actionRounds[actionRoundIndex];
				if(actionRound.isEmpty()){
					continue;
				}
				int[] totalPlayerRaises = new int[2];
				int charIndex=0;
				for(char action:actionRound.toCharArray()){
					if(Character.isDigit(action)){
						int raise = Character.getNumericValue(action);
						totalPlayerRaises[charIndex%2]=totalPlayerRaises[charIndex%2]+raise;
					}
					charIndex++;
				}
				summaryState+="D"+Arrays.toString(totalPlayerRaises);
			}
			if(dealLastAction){
				summaryState+="D";
			}else{
				summaryState+="D"+actionRounds[actionRounds.length-1];
			}
		}
		String cardHistory = getThreeCardHistory(singleCardHand.getCard(0).getDeckNumber(), board.getTurnedCards());
		return cardHistory + summaryState;
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
