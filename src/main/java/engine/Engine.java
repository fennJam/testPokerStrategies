package engine;

import java.util.List;

import poker.Card;
import poker.actions.PokerAction;

public interface Engine {

	
	public void dealCards(Card[] cardCombo);
	
	public int[] playGame(List<PokerAction> actionsTaken);
	
	public void printResult(int [] payOffs);

	boolean isAtTerminalNode(List<PokerAction> actionsTaken);

	public void postBlinds();
	
	
}
