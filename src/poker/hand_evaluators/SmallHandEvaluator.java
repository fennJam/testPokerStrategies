package poker.hand_evaluators;

public class SmallHandEvaluator {
	
	public static enum HandCategory {
		NO_PAIR, PAIR, FLUSH, STRAIGHT, THREE_OF_A_KIND, STRAIGHT_FLUSH;
	}

private static final int   RANK_SHIFT_1		= 4;
private static final int   RANK_SHIFT_2		= RANK_SHIFT_1 + 4;
private static final int   RANK_SHIFT_3		= RANK_SHIFT_2 + 4;
private static final int   RANK_SHIFT_4		= RANK_SHIFT_3 + 4;
public static final int    VALUE_SHIFT		= RANK_SHIFT_4 + 8;
//Rhode Island Rankings
private static final int   RI_NO_PAIR			= 0;
private static final int   RI_PAIR				= RI_NO_PAIR			+ (1 << VALUE_SHIFT);
private static final int   RI_FLUSH				= RI_PAIR				+ (1 << VALUE_SHIFT);
private static final int   RI_STRAIGHT			= RI_FLUSH				+ (1 << VALUE_SHIFT);
private static final int   RI_THREE_OF_A_KIND	= RI_STRAIGHT			+ (1 << VALUE_SHIFT);
private static final int   RI_STRAIGHT_FLUSH	= RI_THREE_OF_A_KIND	+ (1 << VALUE_SHIFT);

//Two Card Poker Rankings
private static final int   TC_NO_PAIR			= 0;
private static final int   TC_FLUSH				= TC_NO_PAIR				+ (1 << VALUE_SHIFT);
private static final int   TC_STRAIGHT			= TC_FLUSH					+ (1 << VALUE_SHIFT);
private static final int   TC_PAIR				= TC_STRAIGHT				+ (1 << VALUE_SHIFT);
private static final int   TC_STRAIGHT_FLUSH	= TC_PAIR					+ (1 << VALUE_SHIFT);

	public static int evaluateRhodeIslandHand(long hand) {
	
		final int c = (int) hand & 0x1FFF;
		final int d = ((int) hand >>> 16) & 0x1FFF;
		final int h = (int) (hand >>> 32) & 0x1FFF;
		final int s = (int) (hand >>> 48) & 0x1FFF;

		int ranks = 0;
		int suits = 0;

		if (c != 0) {
			ranks = ranks | c;
			suits += 1;
		}
		if (d != 0) {
			ranks = ranks | d;
			suits += 1;
		}
		if (h != 0) {
			ranks = ranks | h;
			suits += 1;
		}
		if (s != 0) {
			ranks = ranks | s;
			suits += 1;
		}

		int i;
		switch (Long.bitCount(ranks)) {

		case 1: /* trips */
			return RI_THREE_OF_A_KIND | ranks;
		case 2: /* pair */
			i = c ^ d ^ h ^ s; /* kicker bits */
			return RI_PAIR | ((ranks ^ i) << RANK_SHIFT_2) | (i << RANK_SHIFT_1);

		case 3: /* flush and/or straight or no pair */
			boolean isFlush = (suits == 1);
			boolean isStraight = Integer.toBinaryString(ranks).startsWith("111");
			if (ranks == 4099) {
//				run A-2-3
				isStraight = true;
//				remove A high from low run
				ranks = 3;
			}
			if (isFlush && isStraight) {
				return RI_STRAIGHT_FLUSH | ranks;
			} else if (isFlush) {
				return RI_FLUSH | ranks;
			} else if (isStraight) {
				return RI_STRAIGHT | ranks;
			} else {
				return TC_NO_PAIR | ranks;
			}

		}
		System.out.println("Error in RHODE ISLAND HAND EVAL: Must be between 1 and 3 ranks. Returned "
				+ Long.bitCount(ranks) + " bits");
		return -1;
	}

	public static int evaluateTwoCardPokerHand(long hand) {

		final int c = (int) hand & 0x1FFF;
		final int d = ((int) hand >>> 16) & 0x1FFF;
		final int h = (int) (hand >>> 32) & 0x1FFF;
		final int s = (int) (hand >>> 48) & 0x1FFF;

		int ranks = 0;
		int suits = 0;

		if (c != 0) {
			ranks = ranks | c;
			suits += 1;
		}
		if (d != 0) {
			ranks = ranks | d;
			suits += 1;
		}
		if (h != 0) {
			ranks = ranks | h;
			suits += 1;
		}
		if (s != 0) {
			ranks = ranks | s;
			suits += 1;
		}

		switch (Long.bitCount(ranks)) {

		case 1: /* pair */
			return TC_PAIR | ranks;
		case 2: /* pair */
			boolean isFlush = (suits == 1);
			boolean isStraight = isTwoCardStraight(ranks);
			if (isFlush && isStraight) {
				return TC_STRAIGHT_FLUSH | ranks;
			} else if (isFlush) {
				return TC_FLUSH | ranks;
			} else if (isStraight) {
				return TC_STRAIGHT | ranks;
			} else {
				return TC_NO_PAIR | ranks;
			}
		}
		System.out.println("Error in TWO CARD POKER HAND EVAL: Must be between 1 and 2 ranks. Returned "
				+ Long.bitCount(ranks) + " bits");
		return -1;
	}
	
	public static int evaluateSingleCardPokerHand(long hand) {

		final int c = (int) hand & 0x1FFF;
		final int d = ((int) hand >>> 16) & 0x1FFF;
		final int h = (int) (hand >>> 32) & 0x1FFF;
		final int s = (int) (hand >>> 48) & 0x1FFF;

		int rank = 0;

		if (c != 0) {
			rank = rank | c;
		}
		if (d != 0) {
			rank = rank | d;
		}
		if (h != 0) {
			rank = rank | h;
		}
		if (s != 0) {
			rank = rank | s;
		}

		return rank;
	}

//	private static boolean isThreeCardStraight(int ranks) {
//
//		if (ranks == 4099) {// A-2-3
//			return true;
//		}
//		String BinaryStr = Integer.toBinaryString(ranks);
//		if (BinaryStr.startsWith("111")) {
//			return true;
//		}
//		return false;
//	}

	private static boolean isTwoCardStraight(int ranks) {

		if (ranks == 4097) {// A-2
			return true;
		}
		String BinaryStr = Integer.toBinaryString(ranks);
		if (BinaryStr.startsWith("11")) {
			return true;
		}
		return false;
	}
}
