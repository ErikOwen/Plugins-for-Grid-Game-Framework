package collapse;

import gridgame.GridBoard;
import gridgame.Preferences;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

/**
 * Class which represents the collapse board
 * 
 * @author erikowen
 * @version 1
 */
public class CollapseBoard extends GridBoard<CollapseCell>
{
    
    private CollapseCell[][] board;
    private Preferences prefs;
    private int boardNum;
    
    /**
     * Constructor to instaniaet a CollapseBoard
     */
    public CollapseBoard()
    {
        super();
        
        prefs = Preferences.getInstance("collapse");
    }
    
    
    /**
     * Resets the board to the specified board number
     * 
     * @param boardNumber the specified board number to switch the board to
     */
    public void resetBoard(int boardNumber)
    {
        final int boardSize = Integer.parseInt(prefs.get("Board Size"));
        CollapsePiece[] pieces = {CollapsePiece.green, CollapsePiece.purple,
            CollapsePiece.red};
        board = new CollapseCell[boardSize][boardSize];
        boardNum = boardNumber;
        Random generator = new Random(boardNum);
        
        /*Iterates through each row on the board*/
        for (int row = 0; row < getRowCount(); row++)
        {
            /*Iterates through each column on the board*/
            for (int col = 0; col < getColumnCount(); col++)
            {
                board[row][col] = new CollapseCell(
                    pieces[generator.nextInt(pieces.length)]);
            }
        }
    }
    
    /**
     * Determines if the board has been won or not
     * 
     * @return boolean if the board has been cleared
     */
    public boolean isWin()
    {
        boolean gameIsWon = true;
        
        /*Iterates through all of the board's row*/
        for(int row = 0; row < getRowCount() && gameIsWon; row++)
        {
            /*Iterates through all of the board's columns*/
            for(int col = 0; col < getColumnCount() && gameIsWon; col++)
            {
                /*Determiens if the current piece is empty or not*/
                if(board[row][col].getState() != CollapsePiece.empty)
                {
                    gameIsWon = false;
                }
            }
        }
        
        return gameIsWon;
    }
    
    /**
     * Makes a move on the board
     * 
     * @param row coordinate to make the move at
     * @param col coordinate to the make the move out
     */
    public void makeMove(int row, int col)
    {
        /*Checks to see if current position is not empty*/
        if(board[row][col].getState() != CollapsePiece.empty)
        {
            /*Determines if the tile chosen has a(n) adjacent tile(s)*/
            if(hasAdjacentTiles(row, col))
            {
                //Removes cell and all adjacent tiles of the same color
                removeSelection(row, col);
                //Shifts the necesssary cells downwards to fill in blank spots
                shiftCellsDownwards();
                //Shifts the columns to the center if necessary
                shiftColumnsToCenter();
            }
        }
    }
    
    private boolean hasAdjacentTiles(int row, int col)
    {
        boolean hasAdjacentTiles = false;
        CollapsePiece color = board[row][col].getState();
        
        /*Only checks for adjacent tiles if tile is not empty*/
        if(color != CollapsePiece.empty)
        {
            /*Looks above the current tile to see if it there is the same tile type*/
            if(row > 0 && board[row - 1][col].getState() == color)
            {
                hasAdjacentTiles = true;
            }
            /*Looks below the current tile to see if it there is the same tile type*/
            if(row < getRowCount() - 1 && board[row + 1][col].getState() == color)
            {
                hasAdjacentTiles = true;
            }
            /*Looks right of the current tile to see if it there is the same tile type*/
            if(col > 0 && board[row][col - 1].getState() == color)
            {
                hasAdjacentTiles = true;   
            }
            /*Looks left of the current tile to see if it there is the same tile type*/
            if(col < getColumnCount() - 1 && board[row][col + 1].getState() == color)
            {
                hasAdjacentTiles = true;
            }
        }
        
        return hasAdjacentTiles;
    }
    
    /**
     * Removes the block clicked along with any adjacent pieces.
     */
    private void removeSelection(int rowPos, int colPos)
    {
        CollapsePiece curColor = board[rowPos][colPos].getState();
        Point curSpot;
        LinkedList<Point> queue = new LinkedList<Point>();
        queue.add(new Point(rowPos, colPos));
        
        /*Keeps finding all of the adjacent tiles while there are unchecked tiles*/
        while(!queue.isEmpty())
        {
            curSpot = queue.remove();
            
            /*Add the below tile to the queue if it is the same color*/
            if(curSpot.getX() < getRowCount() - 1 && board[
                (int) (curSpot.getX() + 1)][(int)curSpot.getY()].getState()
                    == curColor)
            {
                queue.add(new Point((int)curSpot.getX() + 1, (int)curSpot.getY()));
            }
            /*Add the above tile to the queue if it is the same color*/
            if(curSpot.getX() > 0 && board[(int) (curSpot.getX()
                - 1)][(int) curSpot.getY()].getState() == curColor)
            {
                queue.add(new Point((int)curSpot.getX() - 1,
                    (int)curSpot.getY()));
            }
            /*Add the tile to the right to the queue if it is the same color*/
            if(curSpot.getY() < getColumnCount() - 1 &&
                board[(int)curSpot.getX()][(int)curSpot.getY() + 1].getState()
                    == curColor)
            {
                queue.add(new Point((int)curSpot.getX(), (int)curSpot.getY() + 1));
            }
            /*Add the tile to the left to the queue if it is the same color*/
            if(curSpot.getY() > 0 && board[(int)curSpot.getX()]
                [(int)curSpot.getY() - 1].getState() == curColor)
            {
                queue.add(new Point((int)curSpot.getX(), (int)curSpot.getY() - 1));
            }
            
            board[(int)curSpot.getX()][(int)curSpot.getY()].setToEmpty();
        }
    }
    
    /**
     * Shifts the remaining cells downwards if there is an open spot below them
     */
    private void shiftCellsDownwards()
    {
        /*Iterates through each row on the board*/
        for(int rowIter = getRowCount() - 2; rowIter >= 0; rowIter--)
        {
            /*Iterates through each column on the board*/
            for(int colIter = 0; colIter < getColumnCount(); colIter++)
            {
                //If the current position is not empty and has an empty spot
                //below it, then slide the tile down to correct position
                if(board[rowIter][colIter].getState() != CollapsePiece.empty &&
                    board[rowIter + 1][colIter].getState() == CollapsePiece.empty)
                {
                    int curSpot = rowIter + 1;
                    
                    /*Slides the tile down until it is above the tile or on the bottom*/
                    while(curSpot < getRowCount() - 1 && board
                        [curSpot + 1][colIter].getState() == CollapsePiece.empty)
                    {
                        curSpot++;
                    }
                    
                    board[curSpot][colIter] = board[rowIter][colIter];
                    board[rowIter][colIter] = new CollapseCell(CollapsePiece.empty);
                }

            }
        }
    }
    
    /**
     * Shfits the columns towards the center if there are any empty columns
     */
    private void shiftColumnsToCenter()
    {
        int centerCol = getColumnCount() / 2;
        
        /*Iterates through all of the columns left of the center*/
        for(int ndx = centerCol; ndx < getColumnCount() - 1; ndx++)
        {
            shift(ndx, 1);
        }
        
        /*If the board is even sized the move the center left one*/
        if(getColumnCount() % 2 == 0)
        {
            centerCol--;
        }
        
        //Shift the cells left of the center to the center, if needed
        for(int ndx = centerCol; ndx > 0; ndx--)
        {
            shift(ndx, -1);
        }
    }
    
    /**
     * Helper method to shiftColumnsToCenter, shifts
     * the current column in the specified direction
     */
    private void shift(int ndx, int dir)
    {
        /*Determines if current column is empty*/
        if(columnIsEmpty(ndx))
        {
            int curCol = ndx + dir;
                
            /*Searches for closest non-empty row to center*/
            while(curCol >= 0 && curCol < getColumnCount() && columnIsEmpty(curCol))
            {
                curCol = curCol + dir;
            }
                
            /*If there is a non-empty row, shift it*/ 
            if(curCol >= 0 && curCol < getColumnCount())
            {
                /*Shifts the column and makes the old spot empty*/
                for(int rowIter = 0; rowIter < getRowCount(); rowIter++)
                {
                    board[rowIter][ndx] = board[rowIter][curCol];
                    board[rowIter][curCol] = new CollapseCell(CollapsePiece.empty);
                }
            }
        }
    }
    
    /**
     * Checks to see if a column is empty.
     * 
     * @param col the current column being checked for being empty
     */
    private boolean columnIsEmpty(int column)
    {
        boolean isEmpty = true;
        
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < getRowCount() && isEmpty; rowIter++)
        {
            /*Determines if the current position is not empty*/
            if(board[rowIter][column].getState() != CollapsePiece.empty)
            {
                isEmpty = false;
            }
        }
        
        return isEmpty;
    }
    
    /**
     * Sets the board to the cheat formation
     */
    public void setToCheat()
    {
        /*Iterates through all of the rows of the current board*/
        for (int rowIter = 0; rowIter < getRowCount(); rowIter++)
        {
            /*Iterates throug all of the columns of the current board*/
            for(int colIter = 0; colIter < getColumnCount(); colIter++)
            {          
                board[rowIter][colIter] = new CollapseCell(CollapsePiece.empty);
            }
        
            board[0][0] = new CollapseCell(CollapsePiece.green);
            board[0][1] = new CollapseCell(CollapsePiece.green);  
        }
    }
    
    /**
     * Getter method for the number of tiles left the player needs to clear
     * 
     * @return the number of tiles left the player needs to clear
     */
    public int getTilesLeft()
    {
        int tilesLeft = 0;
        
        /*Iterates through all of the rows on the board*/
        for (int rowIter = 0; rowIter < getRowCount(); rowIter++)
        {
            /*Iterates through all of the columns on the board*/
            for(int colIter = 0; colIter < getColumnCount(); colIter++)
            {
                /*Detrmines if the current column is not empty*/
                if(board[rowIter][colIter].getState() != CollapsePiece.empty)
                {
                    tilesLeft++;
                }
            }
        }
        
        return tilesLeft;
    }
    
    /**
     * Accessor method to get the number of rows in this board
     * 
     * @return int the number of rows in this board
     */
    @Override
    public int getRowCount()
    {
        return board.length;
    }
    
    /**
     * Accessor method to get the number of columns in this board
     * 
     * @return int the number of columns in this board
     */
    @Override
    public int getColumnCount()
    {
        return board[0].length;
    }
    
    /**
     * Accessor method to get the value at the specified coordinates
     * 
     * @param row the specified row to find the cell
     * @param col the specified column to find the cell
     * 
     * @return the CollapseCell at this position
     */
    @Override
    public CollapseCell getValueAt(int row, int col)
    {
        return board[row][col];
    }

}
