package player.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import player.ConventionalTwoCardPlayerImpl;
import player.Player;
import poker.Board;
import poker.actions.CallAction;
import poker.actions.FoldAction;
import poker.actions.PokerAction;
import poker.actions.RaiseAction;

public class TwoCardPlayerTests {

	Player player;
	Board mockedBoard;

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
	public void init() {
		player = new ConventionalTwoCardPlayerImpl();
		mockedBoard = Mockito.mock(Board.class);

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
	public void importingAndUsingStrategyMapsTest() {
		player.importStrategyMapFromJSONFile(
				"C:\\Dev\\poktestharness\\src\\main\\resources\\strategies\\simple_strategy.json");
		assertTrue(player.getMove("fold") instanceof FoldAction);
		assertTrue(player.getMove("call") instanceof CallAction);
		assertTrue(player.getMove("raise1") instanceof RaiseAction);
		RaiseAction raise = (RaiseAction) player.getMove("raise1");
		assertEquals(1, raise.getRaiseAmount());
		assertTrue(player.getMove("raise8") instanceof RaiseAction);
		raise = (RaiseAction) player.getMove("raise8");
		assertEquals(8, raise.getRaiseAmount());
	}

	@Test
	public void historyGeneratorTest() {

		List<PokerAction> actionsTaken = new ArrayList<PokerAction>();
		
//		assertTrue(player.getMove(mockedBoard, actionsTaken) instanceof RaiseAction);
	}

}
