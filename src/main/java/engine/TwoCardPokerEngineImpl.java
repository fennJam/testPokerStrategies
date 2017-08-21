package engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import poker.PayOffCalculator;
import poker.actions.CallAction;
import poker.actions.FoldAction;
import player.ConventionalTwoCardPlayerImpl;
import player.Player;
import player.SummaryTwoCardPlayerImpl;
import poker.Board;
import poker.Card;
import poker.Hand;
import poker.HandSingleCard;
import poker.PokerGameType;
import poker.Pot;
import poker.actions.DealAction;
import poker.actions.PokerAction;
import poker.actions.RaiseAction;

public class TwoCardPokerEngineImpl implements Engine {

	Board board;
	Pot pot;

	Player[] players = new Player[2];
	PokerGameType pokerGameType = PokerGameType.TWO_CARD;
	String strategy0;
	String strategy1;

	public TwoCardPokerEngineImpl(String summaryStrategyFileAddress, String conventionalStrategyFileAddress) {

		Player player0 = new SummaryTwoCardPlayerImpl();
		Player player1 = new ConventionalTwoCardPlayerImpl();

		player0.importStrategyMapFromJSONFile(summaryStrategyFileAddress);
		player1.importStrategyMapFromJSONFile(conventionalStrategyFileAddress);

		players[0] = player0;
		players[1] = player1;

		strategy0 = summaryStrategyFileAddress.substring(summaryStrategyFileAddress.lastIndexOf("\\") + 1);
		strategy1 = summaryStrategyFileAddress.substring(conventionalStrategyFileAddress.lastIndexOf("\\") + 1);

	}
	
	public TwoCardPokerEngineImpl(Player player0, Player player1) {

		players[0] = player0;
		players[1] = player1;

	}

	@Override
	public void dealCards(Card[] cardCombo) {
		Hand hand1 = new HandSingleCard(cardCombo[0]);
		Hand hand2 = new HandSingleCard(cardCombo[1]);
		Card[] boardCard = { cardCombo[2] };

		players[0].setHand(hand1);
		players[1].setHand(hand2);

		board = new Board(pokerGameType, boardCard);

	}

	@Override
	public int[] playGame(List<PokerAction> actionsTaken) {

		int playerToAct = getPlayerToAct(actionsTaken);

		if (actionsTaken.contains(FoldAction.getInstance())) {
			int[] payOffs = { 0, 0 };
			payOffs[playerToAct] = pot.getPlayersContributionToPot(1 - playerToAct);
			payOffs[1 - playerToAct] = -pot.getPlayersContributionToPot(1 - playerToAct);
			return payOffs;
		} else if (isAtTerminalNode(actionsTaken)) {
			Map<Integer, Hand> handMap = new HashMap<Integer, Hand>();
			handMap.put(0, players[0].getHand());
			handMap.put(1, players[1].getHand());
			return PayOffCalculator.getPayOffsForTwoPlayerGame(handMap, board, pot, pokerGameType);
		} else if (lastActionIsTerminalCallForTheBettingRound(actionsTaken)) {
			actionsTaken.add(DealAction.getInstance());
			board.turnNextCard();
			return playGame(actionsTaken);
		} else {
			PokerAction moveThisRound = players[playerToAct].getMove(board, actionsTaken);
			if (moveThisRound instanceof RaiseAction) {
				RaiseAction raiseAction = (RaiseAction) moveThisRound;
				performRaiseAction(playerToAct, raiseAction, actionsTaken);
			} else if (moveThisRound instanceof CallAction) {
				performCallAction(playerToAct, actionsTaken);
			} else if (moveThisRound instanceof FoldAction) {
				performFoldAction(playerToAct, actionsTaken);
			}
			return playGame(actionsTaken);
		}

	}

	@Override
	public List<PokerAction> performRaiseAction(int playerToAct, RaiseAction raiseAction,
			List<PokerAction> actionsTaken) {
		// match the call first
		int amountToCall = pot.getPlayersContributionToPot(1 - playerToAct)
				- pot.getPlayersContributionToPot(playerToAct);
		pot.addBet(playerToAct, amountToCall);
		// then raise
		pot.addBet(playerToAct, raiseAction.getRaiseAmount());
		actionsTaken.add(raiseAction);
		return actionsTaken;
	}

	@Override
	public List<PokerAction> performCallAction(int playerToAct, List<PokerAction> actionsTaken) {
		int amountToCall = pot.getPlayersContributionToPot(1 - playerToAct)
				- pot.getPlayersContributionToPot(playerToAct);
		pot.addBet(playerToAct, amountToCall);
		actionsTaken.add(CallAction.getInstance());
		return actionsTaken;
	}

	@Override
	public List<PokerAction> performFoldAction(int playerToAct, List<PokerAction> actionsTaken) {
		int amountToCall = pot.getPlayersContributionToPot(1 - playerToAct)
				- pot.getPlayersContributionToPot(playerToAct);
		
		if(amountToCall>0){
		actionsTaken.add(FoldAction.getInstance());
		}else{
			actionsTaken.add(CallAction.getInstance());
		}
		return actionsTaken;
	}

	@Override
	public boolean isAtTerminalNode(List<PokerAction> actionsTaken) {
		if ((actionsTaken.lastIndexOf(DealAction.getInstance()) != 0)
				&& lastActionIsTerminalCallForTheBettingRound(actionsTaken)) {
			return true;
		} else if (pot.getPlayersContributionToPot(0) == 10 && pot.getPlayersContributionToPot(1) == 10) {
			// both players all in
			board.turnAllCards();
			return true;
		}
		return false;
	}

	private boolean lastActionIsTerminalCallForTheBettingRound(List<PokerAction> actionsTaken) {
		int dealIndex = actionsTaken.lastIndexOf(DealAction.getInstance());
		int callIndex = actionsTaken.lastIndexOf(CallAction.getInstance());
		// any call that is not a "check" (a call after a deal action) is a
		// terminal call for the betting round
		return (callIndex - dealIndex > 1);

	}

	@Override
	public int getPlayerToAct(List<PokerAction> actionsTaken) {
		// get number of actions since last instance of deal action
		int noOfActions = actionsTaken.size() - actionsTaken.lastIndexOf(DealAction.getInstance()) + 1;

		return noOfActions % 2;
	}

	@Override
	public void postBlinds() {
		Integer[] playerArray = { 0, 1 };
		pot = new Pot(playerArray);
		pot.addBet(0, 1);
		pot.addBet(1, 2);

	}

	@Override
	public void resetGame() {
		this.board = null;
		this.pot = null;
		this.players = new Player[2];
		this.pokerGameType = PokerGameType.TWO_CARD;
		this.strategy0 = null;
		this.strategy1 = null;

	}

	@Override
	public Pot getPot() {
		return pot;
	}

}
