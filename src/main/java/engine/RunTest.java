package engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import poker.Card;
import poker.actions.DealAction;
import poker.actions.PokerAction;

public class RunTest {

//	500iterationsSummaryStrategy
	
//	static final String STRATEGY1 = "C:\\Users\\James\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";
//	static final String STRATEGY0 = "C:\\Users\\James\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";

	static final String STRATEGY1 = "C:\\Users\\fennell1\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";
	static final String STRATEGY0 = "C:\\Users\\fennell1\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";

	
	public static void main(String[] args) throws Exception {
		int iterations = 2;

		Engine testEngine = new TwoCardPokerEngineImpl(STRATEGY0, STRATEGY1);
		Card[][] validCardCombos = getListOfValidCardCombinations();
		for (Card[] cardCombo : validCardCombos) {
			List<PokerAction> actionList = new ArrayList<PokerAction>();
			testEngine.dealCards(cardCombo);
			testEngine.postBlinds();
			actionList.add(DealAction.getInstance());
			int[] payOffs = testEngine.playGame(actionList);
			testEngine.printResult(payOffs);
			System.out.println(Arrays.toString(payOffs));
		}
	}

	public void printResult(int[] payOffs) {
		BufferedWriter br;
		try {
			br = new BufferedWriter(new FileWriter(STRATEGY0 + " - vs - " + STRATEGY1 + ".csv"));

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

	public static Card[][] getListOfValidCardCombinations() {
		Card[][] validCardCombinationLists = new Card[6840][3];
		int[] cardOrder = new int[20];

		int cardOrderPointer = 0;
		// add 10-A spades
		for (int card = 8; card < 13; card++) {
			cardOrder[cardOrderPointer] = card;
			cardOrderPointer++;
		}
		// add 10-A hearts
		for (int card = 21; card < 26; card++) {
			cardOrder[cardOrderPointer] = card;
			cardOrderPointer++;
		}
		// add 10-A clubs
		for (int card = 34; card < 39; card++) {
			cardOrder[cardOrderPointer] = card;
			cardOrderPointer++;
		}
		// add 10-A diamonds
		for (int card = 47; card < 52; card++) {
			cardOrder[cardOrderPointer] = card;
			cardOrderPointer++;
		}

		int validComboCount = 0;
		for (int card0 : cardOrder) {
			for (int card1 : cardOrder) {
				if (card0 == card1) {
					continue;
				}
				for (int boardCard1 : cardOrder) {
					if (boardCard1 == card1 || boardCard1 == card0) {
						continue;
					}
					Card[] validCardComination = new Card[3];
					validCardComination[0] = new Card(card0);
					validCardComination[1] = new Card(card1);
					validCardComination[2] = new Card(boardCard1);
					validCardCombinationLists[validComboCount] = validCardComination;
					validComboCount++;
				}
			}
		}

		return validCardCombinationLists;
	}

}