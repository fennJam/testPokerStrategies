package engine;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.stat.inference.TTest;

import player.CallPlayerImpl;
import player.ConventionalTwoCardPlayerImpl;
import player.Player;
import player.RandomActionPlayerImpl;
import player.SummaryTwoCardPlayerImpl;
import poker.Card;
import poker.actions.DealAction;
import poker.actions.PokerAction;

public class RunTest {

	// 500iterationsSummaryStrategy

	// static final String STRATEGY1 =
	// "C:\\Users\\James\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";
	// static final String STRATEGY0 =
	// "C:\\Users\\James\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";

	
	static final String STRATEGY0 = "C:\\Users\\fennell1\\Dropbox\\StrategyMaps\\2000iterationsConventionalStrategy.json";
	static final String STRATEGY1 = "C:\\Users\\fennell1\\Dropbox\\StrategyMaps\\2000iterationsConventionalStrategy.json";
//	static final String STRATEGY0 = "C:\\Users\\fennell1\\Dropbox\\StrategyMaps\\1000iterationsSummaryStrategy.json";

	public static void main(String[] args) throws Exception {
		
			List<Integer> payOffsList = new ArrayList<Integer>();
//			Player player0 = new SummaryTwoCardPlayerImpl(STRATEGY0);
			Player player0 = new ConventionalTwoCardPlayerImpl(STRATEGY0);
			Player player1 = new ConventionalTwoCardPlayerImpl(STRATEGY1);

			int count = 0;
			
			
			
			Engine testEngine = new TwoCardPokerEngineImpl(player0, player1);
			Card[][] validCardCombos = getListOfValidCardCombinations();
			
			for(int i=0;i<200;i++){
			for (Card[] cardCombo : validCardCombos) {
				count++;
				List<PokerAction> actionList = new ArrayList<PokerAction>();
				testEngine.dealCards(cardCombo);
				testEngine.postBlinds();
				actionList.add(DealAction.getInstance());
				int[] payOffs = testEngine.playGame(actionList);

				int player0Result = payOffs[0] - payOffs[1];
				payOffsList.add(player0Result);
				
				
//				if(count == 10){
//					break;
//				}
			}

			testEngine = new TwoCardPokerEngineImpl(player1, player0);
			for (Card[] cardCombo : validCardCombos) {
				count++;
				List<PokerAction> actionList = new ArrayList<PokerAction>();
				testEngine.dealCards(cardCombo);
				testEngine.postBlinds();
				actionList.add(DealAction.getInstance());
				int[] payOffs = testEngine.playGame(actionList);

				int player1Result = payOffs[1] - payOffs[0];
				payOffsList.add(player1Result);
				
//				if(count == 20){
//					break;
//				}
			}
			}
			printResult(payOffsList);
			int sum2 = payOffsList.stream().mapToInt(Integer::intValue).sum();
			System.out.println("Sum2 : "+sum2+" count"+count);
			
			double[] doubleArray = convertIntListTodoubleArray(payOffsList);
			System.out.println("T-test : "+new TTest().tTest(0.0, doubleArray));

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

	public static void printResult(List<Integer> payOffs) {
		BufferedWriter br;
		try {
			br = new BufferedWriter(new FileWriter(("ConventionalTwoCardPlayerImpl500 - vs - Call.csv")));

			StringBuilder sb = new StringBuilder();
			for (int payOff : payOffs) {
				sb.append(payOff + ",");
			}
			br.newLine();
			br.write(sb.toString());
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private static double[] convertIntListTodoubleArray(List<Integer> intList){
		double[] doubleArray = new double[intList.size()];
		for(int index=0 ;index<intList.size();index++ ){
			doubleArray[index] = Double.valueOf(intList.get(index));
		}
		return doubleArray;
	}

}
