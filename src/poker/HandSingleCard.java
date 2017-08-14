// Written by James Fennell
// 26/1/2017

package poker;
public class HandSingleCard extends Hand
{	
	Card card;
	CardSuit suit;
	CardHeight rank;
	
	/**
	 * A hand containing one card
	 * @param firstCard : the card
	 */
	public HandSingleCard(Card card)
	{
		this.handType = HandType.SINGLECARD; 
		this.cards = new Card[1];
		this.cards[0] = card;
		this.card = card;
		this.suit = card.getSuit();
		this.rank = card.getHeight();
	}
	
	public Card getCard() {
		return card;
	}

	public CardSuit getSuit() {
		return suit;
	}

	public CardHeight getRank() {
		return rank;
	}
	
	public HandType getHandType() {
		return handType;
	}

	public String toString(){
		return card.toString();
	}
	
}
