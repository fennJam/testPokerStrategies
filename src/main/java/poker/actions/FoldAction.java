package poker.actions;

public final class FoldAction extends BasePokerAction {

	private static final FoldAction FOLD_ACTION = new FoldAction();
	
	public static FoldAction getInstance(){
		return FOLD_ACTION;
	}
	
	private FoldAction() {
		super(PokerActionType.FOLD);
	}

	public String toString(){
		return "F";
	}
}
