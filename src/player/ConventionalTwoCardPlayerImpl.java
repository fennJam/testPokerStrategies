package player;

import java.util.List;

import card.history.CardHistoryBuilder;
import poker.Board;
import poker.Hand;
import poker.actions.PokerAction;

public class ConventionalTwoCardPlayerImpl  extends TwoCardPlayer {

	@Override
	protected String getNodeId(Hand hand, Board board, List<PokerAction> actionsTaken) {
		String cardHistory = "";
		if (board != null) {
			cardHistory = new CardHistoryBuilder(hand, board).build();
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
}
