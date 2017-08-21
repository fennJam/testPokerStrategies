package engine;

import java.util.List;

import poker.Card;
import poker.Pot;
import poker.actions.PokerAction;
import poker.actions.RaiseAction;

public interface Engine {

	
	public void dealCards(Card[] cardCombo);
	
	public int[] playGame(List<PokerAction> actionsTaken);

	boolean isAtTerminalNode(List<PokerAction> actionsTaken);

	public void postBlinds();

	List<PokerAction> performRaiseAction(int playerToAct, RaiseAction raiseAction, List<PokerAction> actionsTaken);

	List<PokerAction> performCallAction(int playerToAct, List<PokerAction> actionsTaken);

	List<PokerAction> performFoldAction(int playerToAct, List<PokerAction> actionsTaken);
	
	void resetGame();
	
	Pot getPot();
	
	public int getPlayerToAct(List<PokerAction> actionsTaken);
	
	
}
