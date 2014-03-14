package kaboom;

import junit.framework.TestCase;

public class KaboomCellTest extends TestCase {

	protected void setUp() throws Exception
	{
		
	}
	
	public void testKaboomCell()
	{
		KaboomCell cell = new KaboomCell(KaboomPieces.bomb, 0, 0);
		assertTrue(cell.isBomb());
		assertEquals(KaboomPieces.covered, cell.getCellState());
		assertEquals(0, cell.getRow());
		assertEquals(0, cell.getColumn());
		assertFalse(cell.isFlagged());
		cell.setUncovered();
		assertTrue(KaboomPieces.bomb == cell.getCellState());
		
		cell.setFlagged();
		cell.setUnflagged();
		
		KaboomCell cellBomb = new KaboomCell(KaboomPieces.bomb, 0, 0);
		KaboomCell cellBombHit = new KaboomCell(KaboomPieces.bombHit, 0, 0);
		cellBombHit.setUncovered();
		KaboomCell cellEmpty = new KaboomCell(KaboomPieces.empty, 0, 0);
		cellEmpty.setUncovered();
		KaboomCell cellFlagged = new KaboomCell(KaboomPieces.flagged, 0, 0);
		cellFlagged.setFlagged();
		
		assertEquals("*", cellBombHit.getText());
		assertEquals("@", cellFlagged.getText());
		assertEquals("-", cellBomb.getText());
		assertEquals(" ", cellEmpty.getText());
		
		assertEquals("covered", cellBomb.toString());
		assertEquals("flagged", cellFlagged.toString());
		assertEquals("", cellEmpty.toString());
		assertEquals("bombHit", cellBombHit.toString());
		
		cellEmpty.setNumBombsNear(3);
		assertTrue(cellEmpty.toString() != "");
		
	}

}
