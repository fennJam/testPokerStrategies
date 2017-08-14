package engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

	TwoCardPokerEngineImpl(String summaryStrategyFileAddress, String conventionalStrategyFileAddress) {

		Player player0 = new SummaryTwoCardPlayerImpl();
		Player player1 = new ConventionalTwoCardPlayerImpl();

		player0.importStrategyMapFromJSONFile(summaryStrategyFileAddress);
		player1.importStrategyMapFromJSONFile(conventionalStrategyFileAddress);

		players[0] = player0;
		players[1] = player1;

		strategy0 = summaryStrategyFileAddress.substring(summaryStrategyFileAddress.lastIndexOf("\\")+1);
		strategy1 = summaryStrategyFileAddress.substring(conventionalStrategyFileAddress.lastIndexOf("\\")+1);
		
		Integer[] playerArray = {0,1};
		pot=new Pot(playerArray);

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

	private void performRaiseAction(int playerToAct, RaiseAction raiseAction, List<PokerAction> actionsTaken) {
		int amountToCall = pot.getPlayersContributionToPot(playerToAct)
				- pot.getPlayersContributionToPot(1-playerToAct);
		pot.addBet(playerToAct, amountToCall);
		pot.addBet(playerToAct, raiseAction.getRaiseAmount());
		actionsTaken.add(raiseAction);
	}

	private void performCallAction(int playerToAct, List<PokerAction> actionsTaken) {
		int amountToCall = pot.getPlayersContributionToPot(playerToAct)
				- pot.getPlayersContributionToPot(1-playerToAct);
		pot.addBet(playerToAct, amountToCall);
		actionsTaken.add(CallAction.getInstance());
	}

	private void performFoldAction(int playerToAct, List<PokerAction> actionsTaken) {
		actionsTaken.add(FoldAction.getInstance());
	}

	@Override
	public void printResult(int [] payOffs) {
		BufferedWriter br;
		try {
			br = new BufferedWriter(new FileWriter(strategy0+" - vs - "+strategy1+".csv"));
		
		StringBuilder sb = new StringBuilder();
		for (int element : payOffs) {
			sb.append(element);
			sb.append(",");
		}

		br.newLine();
		br.write(sb.toString());
		br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private int getPlayerToAct(List<PokerAction> actionsTaken) {
		// get number of actions since last instance of deal action
		int noOfActions = 0;
		actionsTaken.lastIndexOf(DealAction.getInstance());
		for (int action = actionsTaken.lastIndexOf(DealAction.getInstance()) + 1; action < actionsTaken
				.size(); action++) {
			noOfActions++;
		}
		return noOfActions % 2;
	}

	@Override
	public void postBlinds() {
		pot.addBet(0, 1);
		pot.addBet(1, 2);
		
	}

}
