package poker.actions;

public class CallAction extends BasePokerAction {

	private static final CallAction CALL_ACTION = new CallAction();
	
	public static CallAction getInstance(){
		return CALL_ACTION;
	}
	
	private CallAction() {
		super(PokerActionType.CALL);
	}

	public String toString(){
		return "C";
	}
}
