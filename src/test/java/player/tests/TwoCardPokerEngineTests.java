package player.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import engine.Engine;
import engine.TwoCardPokerEngineImpl;
import poker.Card;
import poker.actions.CallAction;
import poker.actions.FoldAction;
import poker.actions.PokerAction;
import poker.actions.RaiseAction;

public class TwoCardPokerEngineTests {

	Engine engine;
	
	PokerAction foldAction;
	PokerAction callAction;
	PokerAction raise1Action;
	PokerAction raise2Action;
	PokerAction raise3Action;
	PokerAction raise4Action;
	PokerAction raise5Action;
	PokerAction raise6Action;
	PokerAction raise7Action;
	PokerAction raise8Action;
	
	@Before
	public void init(){
		
		String file1 = "C:\\Users\\fennell1\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";
		String file2 = "C:\\Users\\fennell1\\Dropbox\\StrategyMaps\\500iterationsConventionalStrategy.json";
		
	engine = new TwoCardPokerEngineImpl(file1,file2); 	
		
	foldAction = FoldAction.getInstance();
	callAction = CallAction.getInstance();
	raise1Action = new RaiseAction(1);
	raise2Action = new RaiseAction(2);
	raise3Action = new RaiseAction(3);
	raise4Action = new RaiseAction(4);
	raise5Action = new RaiseAction(5);
	raise6Action = new RaiseAction(6);
	raise7Action = new RaiseAction(7);
	raise8Action = new RaiseAction(8);
	}
	
	
	@Test
	public void testActions() {
		
		List<PokerAction> actionsTaken = new ArrayList<PokerAction>();
		RaiseAction raise4 = (RaiseAction)raise4Action;
		Card[] cardCombo = {new Card(10),new Card(20),new Card(30)};
		engine.dealCards(cardCombo);
		engine.postBlinds();
		assertTrue(engine.performRaiseAction(0, raise4, actionsTaken).get(0) instanceof RaiseAction);
		
		assertEquals(0,engine.getPot().getLastBet().getKey().intValue());
		assertEquals(4,engine.getPot().getLastBet().getValue().intValue());
		assertEquals(6,engine.getPot().getPlayersContributionToPot(0));
		assertEquals(2,engine.getPot().getPlayersContributionToPot(1));
		assertEquals(8,engine.getPot().getTotalPotSize());
		
		
		
	}

}
