package player;

import java.util.List;
import java.util.Map;

import poker.Board;
import poker.Card;
import poker.Hand;
import poker.actions.PokerAction;


public interface Player {

public PokerAction getMove(Board board, List<PokerAction>actionsTaken);

public PokerAction getMove(String nodeId);

public void setStrategy(Map<String, double[]> strategyMap);

public Map<String, double[]> importStrategyMapFromJSONFile(String fileAddress);

Hand getHand();

void setHand(Hand singleCardHand);

}
