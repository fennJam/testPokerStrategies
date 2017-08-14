package poker.decks;

import java.util.ArrayList;
import java.util.Collections;

import poker.Card;

public class RoyalDeckShuffled implements Deck {

	// jFennell 2017

	/**
	 * Class representing a single deck of cards, which is shuffled in random
	 * order. Cards can be drawn from the deck.
	 */

	private ArrayList<Integer> cardOrder;
	// private ArrayList<Integer> cardOrderSavePoint;

	/**
	 * Creates a new deck of 52 cards, represented by integers 0 to 51, which
	 * are then shuffled.
	 */
	public RoyalDeckShuffled() {
		cardOrder = new ArrayList<>();

		// add 10-A spades
		for (int card = 8; card < 13; card++) {
			cardOrder.add(card);
		}
		// add 10-A hearts
		for (int card = 21; card < 26; card++) {
			cardOrder.add(card);
		}
		// add 10-A clubs
		for (int card = 34; card < 39; card++) {
			cardOrder.add(card);
		}
		// add 10-A diamonds
		for (int card = 47; card < 52; card++) {
			cardOrder.add(card);
		}

		Collections.shuffle(cardOrder);
	}

	/**
	 * Refreshes the deck such that it is a shuffled deck of 52 cards again.
	 */
	@Override
	public void resetDeck() {
		cardOrder = new ArrayList<>();

		// add 10-A spades
		for (int card = 8; card < 13; card++) {
			cardOrder.add(card);
		}
		// add 10-A hearts
		for (int card = 21; card < 26; card++) {
			cardOrder.add(card);
		}
		// add 10-A clubs
		for (int card = 34; card < 39; card++) {
			cardOrder.add(card);
		}
		// add 10-A diamonds
		for (int card = 47; card < 52; card++) {
			cardOrder.add(card);
		}
		
		Collections.shuffle(cardOrder);
	}

	/**
	 * Pushes and returns the next card from the deck.
	 */
	@Override
	public Card nextCard() {
		if (cardOrder.size() <= 0) {
			System.err.println("The deck is empty");
			return null;
		}

		int nextCardNumber = cardOrder.remove(cardOrder.size() - 1);
		Card card = new Card(nextCardNumber);
		return card;
	}

	@Override
	public Card peekAtNextCard() {
		return new Card(cardOrder.get(cardOrder.size() - 1));
	}

	@Override
	public  Deck unShuffleDeck() {
		cardOrder = new ArrayList<>();

		// add 10-A spades
		for (int card = 8; card < 13; card++) {
			cardOrder.add(card);
		}
		// add 10-A hearts
		for (int card = 21; card < 26; card++) {
			cardOrder.add(card);
		}
		// add 10-A clubs
		for (int card = 34; card < 39; card++) {
			cardOrder.add(card);
		}
		// add 10-A diamonds
		for (int card = 47; card < 52; card++) {
			cardOrder.add(card);
		}
		return this;
	}
}
