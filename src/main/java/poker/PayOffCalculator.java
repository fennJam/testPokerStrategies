package poker;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import poker.PokerGameType;
import poker.hand_evaluators.SmallHandEvaluator;

public class PayOffCalculator {

	// return hand strengths for all hands passed in through the constructor
	public static int[] getHandStrengths(Map<Integer, Hand> hands, Board board, PokerGameType PokerGameType) {
		int[] handStrengths = new int[hands.size()];
		// store the hand and table cards together
		for (int hand = 0; hand < hands.size(); hand++) {
			handStrengths[hand] = getHandStrength(hands.get(hand), board, PokerGameType);
		}
		return handStrengths;

	}

	public static Integer getHandStrength(Hand playerHand, Board board, PokerGameType pokerGameType) {
		if (pokerGameType == PokerGameType.RHODE_ISLAND) {
			// store the hand and table cards together
			HandSingleCard playerSingleCardHand = (HandSingleCard) playerHand;
			long combinationCode = 0l;
			combinationCode = combinationCode | playerSingleCardHand.getCard().getNumber();
			for (Card card : board.getTurnedCards())
				combinationCode = combinationCode | card.getNumber();

			return SmallHandEvaluator.evaluateRhodeIslandHand(combinationCode);

		} else if (pokerGameType == PokerGameType.SINGLE_CARD) {
			// store the hand and table cards together
			HandSingleCard playerSingleCardHand = (HandSingleCard) playerHand;
			long combinationCode = 0l;
			combinationCode = combinationCode | playerSingleCardHand.getCard().getNumber();

			return SmallHandEvaluator.evaluateSingleCardPokerHand(combinationCode);

		} else if (pokerGameType == PokerGameType.TWO_CARD) {

			// store the hand and table cards together
			HandSingleCard playerSingleCardHand = (HandSingleCard) playerHand;
			long combinationCode = 0l;
			combinationCode = combinationCode | playerSingleCardHand.getCard().getNumber();
			for (Card card : board.getTurnedCards())
				combinationCode = combinationCode | card.getNumber();

			return SmallHandEvaluator.evaluateTwoCardPokerHand(combinationCode);

		}

		return null;
	}

	public static int[] getPayOffsForTwoPlayerGame(Map<Integer, Hand> hands, Board board, Pot pot,
			PokerGameType PokerGameType) {
		int[] payoffs = new int[hands.size()];
		int[] handStrengths = getHandStrengths(hands, board, PokerGameType);
		int player0Strength = handStrengths[0];
		int player1Strength = handStrengths[1];
		if (player0Strength > player1Strength) {
			Integer player1Loss = pot.getPlayersContributionToPot(1);
			payoffs[0] = player1Loss;
			payoffs[1] = -player1Loss;
		} else if (player0Strength < player1Strength) {
			Integer player0Loss = pot.getPlayersContributionToPot(0);
			payoffs[0] = -player0Loss;
			payoffs[1] = player0Loss;
		} else {
			// payoffs already set to zero
		}

		return payoffs;
	}

	// return hand strengths for all hands passed in through the constructor
	public static int[] getHandStrengths(int[] playerCards, int[] board, PokerGameType pokerGameType) throws Exception {
		int[] handStrengths = new int[2];
		// store the hand and table cards together
		for (int player = 0; player < playerCards.length; player++) {
			handStrengths[player] = getHandStrength(playerCards[player], board, pokerGameType);
		}
		return handStrengths;

	}

	public static int getHandStrength(int playerCard, int[] board, PokerGameType pokerGameType) throws Exception {
		if (pokerGameType == PokerGameType.RHODE_ISLAND) {
			// store the hand and table cards together
			long combinationCode = 0l;
			combinationCode = combinationCode | getCardNumber(playerCard);
			for (int boardCard : board)
				combinationCode = combinationCode | getCardNumber(boardCard);

			return SmallHandEvaluator.evaluateRhodeIslandHand(combinationCode);

		} else if (pokerGameType == PokerGameType.SINGLE_CARD) {
			// store the hand and table cards together
			long combinationCode = 0l;
			combinationCode = combinationCode | getCardNumber(playerCard);

			return SmallHandEvaluator.evaluateSingleCardPokerHand(combinationCode);

		} else if (pokerGameType == PokerGameType.TWO_CARD) {

			// store the hand and table cards together
			long combinationCode = 0l;
			combinationCode = combinationCode | getCardNumber(playerCard);
			for (int boardCard : board)
				combinationCode = combinationCode | getCardNumber(boardCard);
			;

			return SmallHandEvaluator.evaluateTwoCardPokerHand(combinationCode);

		}
		throw new Exception("PokerGameType : " + pokerGameType + " not recognised");
	}

	/**
	 * Returns the number of the card as a long.
	 */
	public static long getCardNumber(int card) {
		int suitShift = card / 13;
		int heightShift = card % 13;
		return (1l << (16 * suitShift + heightShift));
	}

	public static int[] getPayOffsForTwoPlayerLiteGame(int[] playerCards, int[] board, int[] pot,
			PokerGameType pokerGameType) throws Exception {
		int[] payoffs = new int[playerCards.length];
		int[] handStrengths = getHandStrengths(playerCards, board, pokerGameType);
		Integer player0Strength = handStrengths[0];
		Integer player1Strength = handStrengths[1];
		if (player0Strength > player1Strength) {
			Integer player1Loss = pot[1];
			payoffs[0]= player1Loss;
			payoffs[1]= -player1Loss;
		} else if (player0Strength < player1Strength) {
			Integer player0Loss = pot[0];
			payoffs[0]= -player0Loss;
			payoffs[1]= player0Loss;
		} else {
			// payoffs already set to zero
		}

		return payoffs;
	}

}
