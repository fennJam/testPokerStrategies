// Written by James Fennell
// 26/1/2017

package poker;

public enum BetRound {
	PRETURN, TURN, RIVER;

	private static BetRound[] vals = values();

	public BetRound next() {
		return vals[(this.ordinal() + 1) % vals.length];
	}
}