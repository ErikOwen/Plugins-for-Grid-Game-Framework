package collapse;

import junit.framework.TestCase;

public class CollapseBoardTest extends TestCase {

	protected void setUp() throws Exception {
		
	}
	
	public void testBoard()
	{
		CollapseBoard board = new CollapseBoard();
		
		board.resetBoard(0, 8);
		
		for(int row = 0; row < board.getRowCount(); row++)
		{
			for(int col = 0; col < board.getColumnCount(); col++)
			{
				assertTrue(board.getValueAt(row, col).getState() != CollapsePiece.empty);
			}
		}
		
		board.setToCheat();
		
		for(int row = 0; row < board.getRowCount(); row++)
		{
			for(int col = 0; col < board.getColumnCount(); col++)
			{
				if(row == 0 && col == 0)
				{
					assertTrue(board.getValueAt(row, col).getState() == CollapsePiece.green);
				}
				else if(row == 0 && col == 1)
				{
					assertTrue(board.getValueAt(row, col).getState() == CollapsePiece.green);
				}
				else
				{
					assertTrue(board.getValueAt(row, col).getState() == CollapsePiece.empty);
				}
			}
		}
		
		board.makeMove(4,  4);
		assertFalse(board.isWin());
		board.makeMove(0, 0);
		assertTrue(board.isWin());
	}
	
	public void testWin()
	{
		CollapseBoard board = new CollapseBoard();
		board.resetBoard(2, 8);
		assertEquals(64, board.getTilesLeft());
		board.makeMove(7, 4);
		board.makeMove(7, 4);
		board.makeMove(7, 4);
		board.makeMove(7, 4);
		board.makeMove(7, 4);
		board.makeMove(7, 4);
		board.makeMove(7, 4);
		board.makeMove(7, 3);
		board.makeMove(7, 3);
		
		assertFalse(board.isWin());
		board.makeMove(7, 3);
		assertTrue(board.isWin());
		assertEquals(0, board.getTilesLeft());
		
		board.resetBoard(1, 8);
		board.makeMove(1, 1);
		board.makeMove(2, 2);
		board.makeMove(3, 3);
		board.makeMove(4, 4);
		board.makeMove(5, 5);
		board.makeMove(6, 6);
		board.makeMove(7, 7);
		
	}

}
