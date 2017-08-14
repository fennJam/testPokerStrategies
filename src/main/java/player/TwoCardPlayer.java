package player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import card.history.CardHistoryBuilder;
import poker.Board;
import poker.Hand;
import poker.actions.CallAction;
import poker.actions.FoldAction;
import poker.actions.PokerAction;
import poker.actions.RaiseAction;

public abstract class TwoCardPlayer implements Player {

	PokerAction[] potentailActions = { FoldAction.getInstance(), CallAction.getInstance(), new RaiseAction(1),
			new RaiseAction(2), new RaiseAction(3), new RaiseAction(4), new RaiseAction(5), new RaiseAction(6),
			new RaiseAction(7), new RaiseAction(8) };

	Map<String, double[]> strategyMap;
	Hand singleCardHand;

	@Override
	public PokerAction getMove(Board board, List<PokerAction> actionsTaken) {

		String nodeId = getNodeId(singleCardHand, board, actionsTaken);
		System.out.println(nodeId);

		return getMove(nodeId);

	}

	@Override
	public void setStrategy(Map<String, double[]> strategyMap) {
		this.strategyMap = strategyMap;

	}

	public Map<String, double[]> importStrategyMapFromJSONFile(String fileAddress) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, double[]> map = null;
		// Convert JSON string from file to Object
		File strategyMapFile = new File(fileAddress);
		try {

			map = mapper.readValue(strategyMapFile, new TypeReference<Map<String, double[]>>() {
			});

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		strategyMap = map;
		return map;
	}

	@Override
	public Hand getHand() {
		return singleCardHand;
	}

	@Override
	public void setHand(Hand singleCardHand) {
		this.singleCardHand = singleCardHand;
	}

	@Override
	public PokerAction getMove(String nodeId) {
		double random = Math.random();
		double[] actionWeightings = strategyMap.get(nodeId);
		int action = 0;
		double totalWeight = 0;
		for (double weight : actionWeightings) {
			totalWeight += weight;
			if (totalWeight > random) {
				break;
			}
			action++;
		}
		return potentailActions[action];

	}

	protected abstract String getNodeId(Hand hand, Board board, List<PokerAction> actionsTaken);

}
