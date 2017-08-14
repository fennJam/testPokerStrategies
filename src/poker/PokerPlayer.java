package poker;

public class PokerPlayer {

	String id;
	int stack;
	Hand hand;

	public PokerPlayer(String id, int stack, Hand hand) {
		this.id = id;
		this.stack = stack;
		this.hand = hand;
	}

	public PokerPlayer(PokerPlayer player) {
		importPlayerProperties(player);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStack() {
		return stack;
	}

	public void setStack(int stack) {
		this.stack = stack;
	}

	public int addToStack(int chips) {
		this.stack = this.stack + chips;
		return stack;
	}

	public void takeFromStack(int chips) {
		if (stack < chips) {
			throw new Error("Cannot take " + chips + " chips away than the only player has " + stack + ".");
		}
		this.stack = this.stack - chips;
	}

	public Hand getHand() {
		return hand;
	}

	public void setHand(Hand hand) {
		this.hand = hand;
	}

	public void importPlayerProperties(PokerPlayer player) {
		this.id = player.getId();
		this.stack = player.getStack();
		this.hand = player.getHand();
	}

}
