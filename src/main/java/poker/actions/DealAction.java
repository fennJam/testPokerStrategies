package poker.actions;

public final class DealAction extends BasePokerAction {

	private static final DealAction DEAL_ACTION = new DealAction();
	
	public static DealAction getInstance(){
		return DEAL_ACTION;
	}
	
	private DealAction() {
		super(PokerActionType.DEAL);
	}

	public String toString(){
		return "D";
	}
}