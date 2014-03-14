package collapse;

import junit.framework.TestCase;

public class CollapseCellTest extends TestCase {

	protected void setUp() throws Exception
	{
		
	}
	
	public void testCell()
	{
		CollapseCell cellG = new CollapseCell(CollapsePiece.green);
		CollapseCell cellP = new CollapseCell(CollapsePiece.purple);
		CollapseCell cellR = new CollapseCell(CollapsePiece.red);
		
		assertEquals("green", cellG.toString());
		assertEquals("purple", cellP.toString());
		assertEquals("red", cellR.toString());
		
		assertEquals("+", cellG.getText());
		assertEquals("x", cellP.getText());
		assertEquals("o", cellR.getText());
		
		cellG.setToEmpty();
		cellP.setToEmpty();
		cellR.setToEmpty();
		
		assertEquals("", cellG.toString());
		assertEquals("", cellP.toString());
		assertEquals("", cellR.toString());
		
		assertEquals(CollapsePiece.empty, cellG.getState());
		assertEquals(" ", cellG.getText());
	}

}
