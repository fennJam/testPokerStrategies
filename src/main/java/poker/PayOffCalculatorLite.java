package poker;

import poker.hand_evaluators.SmallHandEvaluator;

public class PayOffCalculatorLite {


	// return hand strengths for all hands passed in through the constructor
	public static int[] getHandStrengths(int[] playerCards, int[] board) throws Exception {
		int[] handStrengths = new int[2];
		// store the hand and table cards together
		for (int player = 0; player < playerCards.length; player++) {
			handStrengths[player] = getHandStrength(playerCards[player], board);
		}
		return handStrengths;

	}

	public static int getHandStrength(int playerCard, int[] board) throws Exception {
		if (board.length == 2) {
			// store the hand and table cards together
			long combinationCode = 0l;
			combinationCode = combinationCode | getCardNumber(playerCard);
			for (int boardCard : board)
				combinationCode = combinationCode | getCardNumber(boardCard);

			return SmallHandEvaluator.evaluateRhodeIslandHand(combinationCode);

		} else if (board.length == 0) {
			// store the hand and table cards together
			long combinationCode = 0l;
			combinationCode = combinationCode | getCardNumber(playerCard);

			return SmallHandEvaluator.evaluateSingleCardPokerHand(combinationCode);

		} else if (board.length == 1) {

			// store the hand and table cards together
			long combinationCode = 0l;
			combinationCode = combinationCode | getCardNumber(playerCard);
			for (int boardCard : board)
				combinationCode = combinationCode | getCardNumber(boardCard);
			;

			return SmallHandEvaluator.evaluateTwoCardPokerHand(combinationCode);

		}
		throw new Exception("PokerGame with board length : " + board.length + " not recognised");
	}

	/**
	 * Returns the number of the card as a long.
	 */
	public static long getCardNumber(int card) {
		int suitShift = card / 13;
		int heightShift = card % 13;
		return (1l << (16 * suitShift + heightShift));
	}

	public static int[] getPayOffsForTwoPlayerLiteGame(int[] playerCards, int[] board, int[] pot) throws Exception {
		int[] payoffs = new int[playerCards.length];
		int[] handStrengths = getHandStrengths(playerCards, board);
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
