package poker.actions;

public class RaiseAction extends BasePokerAction {
	int raise;

	public RaiseAction(int raise) {
		super(PokerActionType.RAISE);
		this.raise = raise;
	}
	
	public int getRaiseAmount(){
		return raise;	
	}
	
	public String toString() {
		return String.valueOf(raise);
	}

}
