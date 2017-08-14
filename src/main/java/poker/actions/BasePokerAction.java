package poker.actions;

public class BasePokerAction implements PokerAction {

	PokerActionType actionType;
	
	BasePokerAction(PokerActionType pokerActionType){
		actionType = pokerActionType;
	}
	
	@Override
	public PokerActionType getActionType() {
		return actionType;
	}
	
	
	public String toString(){
		return actionType.toString();
	}
	
}
