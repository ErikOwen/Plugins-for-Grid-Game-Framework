package collapse;

import gridgame.GridBoard;
import gridgame.GridCellRenderer;
import gridgame.Preferences;
import java.util.Random;

public class CollapseBoard extends GridBoard<CollapseCell>{
	
    private CollapseCell[][] board;
    private GridCellRenderer renderer;
    private Preferences prefs;
    private int boardNum;
    
    public CollapseBoard(int boardNumber)
    {
        super();
        
        prefs.getInstance("collapse");
        boardNum = boardNumber;
        
        resetBoard();
    }
    
    public void resetBoard()
    {
    	final int boardSize = Integer.parseInt(prefs.get("Board Size"));
        CollapsePiece[] pieces = {CollapsePiece.green, CollapsePiece.purple, CollapsePiece.red};
        board = new CollapseCell[boardSize][boardSize];
        Random generator = new Random(boardNum);
        
        /*Iterates through each row on the board*/
        for (int row = 0; row < getRowCount(); row++)
        {
            /*Iterates through each column on the board*/
            for (int col = 0; col < getColumnCount(); col++)
            {
                board[row][col] = new CollapseCell(pieces[generator.nextInt(pieces.length)]);
            }
        }
    }

}
