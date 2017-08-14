package poker;

import java.util.ArrayList;
import java.util.List;

import poker.PokerGameType;
import poker.decks.Deck;

public class Board {

	private Card[] cards;
	private Boolean[] isVisible;
	private PokerGameType pokerGameType;

	public Board() {
	}

	public Board(PokerGameType pokerGameType, Deck deck) {
		if (pokerGameType == PokerGameType.SINGLE_CARD) {
			this.pokerGameType = PokerGameType.SINGLE_CARD;
			cards = new Card[0];
			isVisible = new Boolean[0];
		} else if (pokerGameType == PokerGameType.TWO_CARD) {
			this.pokerGameType = PokerGameType.TWO_CARD;
			cards = new Card[1];
			isVisible = new Boolean[1];
			cards[0] = deck.nextCard();
			isVisible[0] = false;
		} else if (pokerGameType == PokerGameType.RHODE_ISLAND) {
			this.pokerGameType = PokerGameType.RHODE_ISLAND;
			cards = new Card[2];
			isVisible = new Boolean[2];
			cards[0] = deck.nextCard();
			isVisible[0] = false;
			cards[1] = deck.nextCard();
			isVisible[1] = false;
		} else {
		}
	}
	
	public Board(PokerGameType pokerGameType, Card[] boardCards) {
		if (pokerGameType == PokerGameType.SINGLE_CARD) {
			this.pokerGameType = PokerGameType.SINGLE_CARD;
			cards = new Card[0];
			isVisible = new Boolean[0];
		} else if (pokerGameType == PokerGameType.TWO_CARD) {
			this.pokerGameType = PokerGameType.TWO_CARD;
			cards = new Card[1];
			isVisible = new Boolean[1];
			cards = boardCards;
			isVisible[0] = false;
		} else if (pokerGameType == PokerGameType.RHODE_ISLAND) {
			this.pokerGameType = PokerGameType.RHODE_ISLAND;
			cards = boardCards;
			isVisible = new Boolean[2];
			isVisible[0] = false;
			isVisible[1] = false;
		} else {
		}
	}

	public Board(PokerGameType PokerGameType) {
		setPokerGameType(PokerGameType);
	}

	public Board setPokerGameType(PokerGameType pokerGameType) {
		if (pokerGameType == PokerGameType.SINGLE_CARD) {
			cards = new Card[0];
			isVisible = new Boolean[0];
		} else if (pokerGameType == PokerGameType.TWO_CARD) {
			cards = new Card[1];
			isVisible = new Boolean[1];
			isVisible[0] = false;
		} else if (pokerGameType == PokerGameType.RHODE_ISLAND) {
			cards = new Card[2];
			isVisible = new Boolean[2];
			isVisible[0] = false;
			isVisible[1] = false;
		} else {
		}
		return this;
	}

	public boolean addTurnedCard(Card card) {
		for (int i = 0; i < cards.length; i++) {
			if (cards[i] == null) {
				cards[i] = card;
				isVisible[i] = true;
				return true;
			}
		}
		return false;
	}

	public void setCard(Card card, int index, boolean visible) {
		cards[index] = card;
		isVisible[index] = visible;

	}

	/**
	 * Returns a list of the turned board cards
	 */
	public List<Card> getTurnedCards() {
		List<Card> visibleCards = new ArrayList<Card>();
		for (int i = 0; i < isVisible.length; i++) {
			if (isVisible[i] == true) {
				visibleCards.add(cards[i]);
			}
		}
		return visibleCards;
	}

	/**
	 * Returns a specific card on this board
	 */
	public Card getCard(int index) {
		return cards[index];
	}

	/**
	 * Turn the next unturned card
	 */
	public boolean turnNextCard() {
		for (int i = 0; i < isVisible.length; i++) {
			if (isVisible[i] == false) {
				isVisible[i] = true;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Turn the next unturned card
	 */
	public Board turnAllCards() {
		for (int i = 0; i < isVisible.length; i++) {
				isVisible[i] = true;
			}
		return this;
	}

	/**
	 * Returns true if the board is of size 0,1 or all the cards are suited
	 */
	public boolean isSuited() {
		List<Card> turnedCards = getTurnedCards();
		if (turnedCards.size() > 1) {
			for (int i = 0; i < turnedCards.size() - 1; i++) {
				if (turnedCards.get(i).getSuit() != turnedCards.get(i + 1).getSuit()) {
					return false;
				}
			}
		}
		return true;
	}

	public List<CardSuit> getTurnedSuits() {
		List<CardSuit> suits = new ArrayList<CardSuit>();
		List<Card> turnedCards = getTurnedCards();
		for (int i = 0; i < turnedCards.size(); i++) {
			suits.add(turnedCards.get(i).getSuit());

		}
		return suits;
	}

	public List<CardHeight> getTurnedCardsRanks() {
		List<CardHeight> ranks = new ArrayList<>();
		List<Card> turnedCards = getTurnedCards();
		for (int i = 0; i < turnedCards.size(); i++) {
			ranks.add(turnedCards.get(i).getHeight());

		}
		return ranks;
	}

	/**
	 * Returns a string representation of the cards that are turned on the board
	 */
	public String toString() {
		List<Card> turnedCards = getTurnedCards();
		if (turnedCards.size() == 0) {
			return "No cards turned";
		} else {
			String str = "[";
			for (int i = 0; i < (turnedCards.size() - 1); i++)
				str += turnedCards.get(i).toString() + ",";
			str += turnedCards.get(turnedCards.size() - 1).toString() + "]";
			return str;
		}
	}

	public Card[] getCards() {
		return cards;
	}

	public Boolean[] getIsVisible() {
		return isVisible;
	}

	public PokerGameType getPokerGameType() {
		return pokerGameType;
	}

	public Board importBoardProperties(Board board) {
		if (board.getCards() == null || board.getIsVisible() == null) {
			this.cards = null;
			this.isVisible = null;
		} else {
			this.cards = board.getCards();
			this.isVisible = board.getIsVisible();
		}
		this.pokerGameType = board.pokerGameType;
		return this;
	}
}
