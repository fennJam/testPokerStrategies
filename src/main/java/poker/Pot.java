// Copyright 2014 theaigames.com (developers@theaigames.com)

//    Licensed under the Apache License, Version 2.0 (the "License");
//    you may not use this file except in compliance with the License.
//    You may obtain a copy of the License at

//        http://www.apache.org/licenses/LICENSE-2.0

//    Unless required by applicable law or agreed to in writing, software
//    distributed under the License is distributed on an "AS IS" BASIS,
//    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//    See the License for the specific language governing permissions and
//    limitations under the License.
//	
//    For the full copyright and license information, please view the LICENSE
//    file that was distributed with this source code.

package poker;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class Pot is used for keeping track of the pot size for TWO players. There is
 * a a Pot class in the poker engine that will support multiple players
 */
public class Pot {
	// store the money each player has commited to the pot
	private Map<Integer, Integer> playerBetSizes;
	private int totalPot;
	private int roundPot;
	private Entry<Integer, Integer> lastBet;

	/**
	 * Creates a Pot object, used for keeping track of the pot for a specific
	 * hand.
	 * 
	 * @param integers
	 *            : integer array of player IDs that play in the current hand
	 */
	public Pot(Integer[] integers) {
		playerBetSizes = new HashMap<Integer, Integer>();
		for (int player = 0; player < integers.length; player++)
			playerBetSizes.put(integers[player], 0);

		totalPot = 0;
	}

	public Pot(Map<Integer, Integer> playerBetsMap) {
		playerBetSizes = playerBetsMap;
		for (Integer bets : playerBetsMap.keySet()) {
			totalPot = totalPot + bets;
		}
	}

	/**
	 * Stores the bet of a player.
	 * 
	 * @return
	 */
	public Pot addBet(Integer player, int size) {

		playerBetSizes.put(player, playerBetSizes.get(player) + size);
		roundPot += size;
		totalPot += size;
		lastBet = new AbstractMap.SimpleEntry<Integer, Integer>(player, size);

		return this;
	}

	/**
	 * Calculate the max raise for a pot limit game
	 */
	public int potLimitMaxBet() {

		return totalPot + 2 * lastBet.getValue();
	}

	/**
	 * retruns the amount staked in the hand so far by the specified player.
	 */
	public int getPlayersContributionToPot(Integer player) {
		return playerBetSizes.get(player);
	}

	/**
	 * Return value of function payoutWinners.
	 */
	public class PayoutWinnerInfo {
		private ArrayList<Integer> pots;
		private ArrayList<ArrayList<Integer>> winnerPerPot;

		public PayoutWinnerInfo(ArrayList<Integer> pots, ArrayList<ArrayList<Integer>> winnerPerPot) {
			super();
			this.pots = pots;
			this.winnerPerPot = winnerPerPot;
		}

		public ArrayList<Integer> getPots() {
			return pots;
		}

		public ArrayList<ArrayList<Integer>> getWinnerPerPot() {
			return winnerPerPot;
		}
	}

	/**
	 * Returns whether the pot is empty or not.
	 */
	public boolean isEmpty() {
		return totalPot == 0;
	}

	/**
	 * Returns the total size of the pot.
	 */
	public int getTotalPotSize() {
		return totalPot;
	}

	/**
	 * Returns the sum of bets in the current round.
	 */
	public int getRoundPotSize() {
		return roundPot;
	}

	public Map<Integer, Integer> getPlayerBetsMap() {
		return playerBetSizes;
	}

	public Entry<Integer, Integer> getLastBet() {
		return lastBet;
	}
	
	public Integer getLastBetValue() {
		return lastBet.getValue();
	}

	public Pot importPotProperties(Pot pot) {
		Map<Integer, Integer> copiedBetMap = pot.getPlayerBetsMap();
		for (Entry<Integer, Integer> betMapEntry : copiedBetMap.entrySet()) {
			playerBetSizes.put(betMapEntry.getKey(), betMapEntry.getValue());
		}
		this.totalPot = pot.getTotalPotSize();
		this.roundPot = pot.getRoundPotSize();
		this.lastBet = pot.getLastBet();

		return this;
	}

	public String toString() {
		return playerBetSizes.toString();
	}
	
}
