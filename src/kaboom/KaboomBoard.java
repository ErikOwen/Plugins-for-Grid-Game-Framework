package kaboom;

import java.util.LinkedList;
import java.util.Random;
import gridgame.GridBoard;
import gridgame.Preferences;

/**
 * Class which represents the board for the Kaboom plugin
 * 
 * @author erikowen
 * @version 1
 */
public class KaboomBoard extends GridBoard<KaboomCell>
{

    private Preferences prefs;
    private int boardSize, difficulty, boardNum, numBombs;
    
    /**
     * Constuctor to make a KaboomBoard object
     */
    public KaboomBoard()
    {
        super();
        
        prefs = Preferences.getInstance("kaboom");
    }
    
    /**
     * Resets the board to the specified boardNumber
     * 
     * @param boardNumber to set this board to
     * @param size the size of the board to be set
     * @param diff the difficulty to make the game
     */
    public void resetBoard(int boardNumber, int size, int diff)
    {
        boardNum = boardNumber;
        //boardSize = Integer.parseInt(prefs.get("Board Size"));
        //difficulty = Integer.parseInt(prefs.get("Difficulty"));
        
        boardSize = size;
        difficulty = diff;
        
        numBombs = 0;
        grid = new KaboomCell[boardSize][boardSize];
        int numGeneratedBombs = (boardSize * boardSize) / difficulty;
        Random generator = new Random(boardNum);
        
        /*Iterates through all of the bombs and adds them to the board*/
        for(int iter = 0; iter < numGeneratedBombs; iter++)
        {
            int curRow = generator.nextInt(boardSize);
            int curCol = generator.nextInt(boardSize);
            
            /*Checks if the current spot doesn't have a bomb on it yet*/
            if(grid[curRow][curCol] == null)
            {
            	grid[curRow][curCol] = new KaboomCell(KaboomPieces.bomb, 
                    curRow, curCol);
                this.numBombs++;
            }
        }
        
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < getRowCount(); rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < getColumnCount(); colIter++)
            {
                /*sets the cell state if spot is empty*/
                if(grid[rowIter][colIter] == null)
                {
                    setCellState(rowIter, colIter);
                }
            }
        }
    }
    
    /**
     * Helper method that sets all of the cell's states that aren't bombs
     * @param row the current row of the cell
     * @param col the current column of the cell
     */
    private void setCellState(int row, int col)
    {
    	grid[row][col] = new KaboomCell(KaboomPieces.empty, row, col);
        int numBombsNear = 0;
        int[] rowDirections = {-1, 0, 1};
        int[] colDirections = {-1, 0, 1};

        /*Iterates through all of the different neighboring rows*/
        for(int rDir : rowDirections)
        {
            /*Iterates through all of the neighboring columns*/
            for(int cDir : colDirections)
            {
                numBombsNear += checkDirectionForBombs(row, col, rDir, cDir);
            }
        }
        
        grid[row][col].setNumBombsNear(numBombsNear);
    }
    
    /**
     * Helper method that checks a specified direction for bombs
     * 
     * @param row the row of the cell being examined
     * @param col the column of the cell being examined
     * @param rDir the row direction to search for bombs
     * @param cDir the column direction to search for bombs
     * @return 1 if there is a bomb, 0 otherwise.
     */
    private int checkDirectionForBombs(int row, int col, int rDir, int cDir)
    {
        int retVal = 0;
        
        /*Makes sure current cell is not being checked for being a bomb*/
        if(rDir != 0 || cDir != 0)
        {
            /*Makes sure row direction stays in bounds*/
            if(row + rDir >= 0 && row + rDir < getRowCount())
            {
                /*Makes sure column direction stays in bounds*/
                if(col + cDir >= 0 && col + cDir < getColumnCount())
                {
                    /*Sees if spot being checked is a bomb or not*/
                    if(grid[row + rDir][col + cDir] != null && 
                        grid[row + rDir][col + cDir].isBomb())
                    {
                        retVal = 1;
                    }
                }
            }
        }
        
        return retVal;
    }
    
    /**
     * Accessor method to get the number of bombs on the board
     * @return int specifying the number of bombs on the board
     */
    public int getNumBombs()
    {
        return numBombs;
    }
    
    /**
     * Takes a turn on this board
     * 
     * @param row the row of the cell chosen to take turn at
     * @param col the column of the cell chosen to take turn at
     */
    public void takeTurn(int row, int col)
    {
        //this.numMoves++;
        KaboomCell chosenCell = getValueAt(row, col);
        chosenCell.setUncovered();
        
        /*Determines if the cell chosen is a bomb*/
        if(chosenCell.isBomb())
        {
            chosenCell.setCellState(KaboomPieces.bombHit);
            setToPeek();
        }
        /*Determines if there are any bombs near this cell*/
        else if(chosenCell.getNumBombsNear() == 0)
        {   
            uncoverNeighboringEmptyCells(row, col);  
        }
    }
    
    /**
     * Method which uncovers empty cells if a cell is empty
     * @param row the current cell row being checked for empty neighbors
     * @param col the current cell column being checked for empty neighbors
     */
    private void uncoverNeighboringEmptyCells(int row, int col)
    {
        int[] rowDirections = {-1, 0, 1};
        int[] colDirections = {-1, 0, 1};
        KaboomCell curCell;
        LinkedList<KaboomCell> queue = new LinkedList<KaboomCell>();
        queue.add((KaboomCell)this.getValueAt(row, col));
        
        /*Continues to add empty cells to queue until there are none left*/
        while(!queue.isEmpty())
        {
            curCell = queue.remove();
            curCell.setUncovered();

            /*Iterates through the row directions*/
            for(int rDir : rowDirections)
            {
                /*Iterates through the column directions*/
                for(int cDir : colDirections)
                {
                    checkForEmptyNeighbors(curCell.getRow(),
                        curCell.getColumn(), rDir, cDir, queue);
                }
            }
        }
    }
    
    /**
     * Helper method to see if there are empty neighbors
     * @param row the current cell's row position
     * @param col the current cell's column position
     * @param rDir the row's direction which is being checked for emptyness
     * @param cDir the column's direction which is being checked for emptyness
     * @param queue the queue to add an empty cell to
     */
    private void checkForEmptyNeighbors(int row, int col, int rDir,
        int cDir, LinkedList<KaboomCell> queue)
    {
        /*Makes sure to not add self to queue*/
        if(rDir != 0 || cDir != 0)
        {
            /*makes sure row direction stays in bounds*/
            if(row + rDir >= 0 && row + rDir < getRowCount())
            {
                /*makes sure column direction stays in bounds*/
                if(col + cDir >= 0 && col + cDir < getColumnCount())
                {
                    /*Determines if current spot is covered and not a bomb*/
                    if(grid[row + rDir][col + cDir] != null && grid[row + 
                        rDir][col + cDir].getCellState() == 
                        KaboomPieces.covered && !grid[row + rDir][col + 
                        cDir].isBomb())
                    {
                        /*Checks to see if current spot is also empty*/
                        if(grid[row + rDir][col + cDir].getNumBombsNear() == 0)
                        {
                            queue.add((KaboomCell)grid[row + rDir]
                                [col + cDir]);
                        }
                        else
                        {
                        	grid[row + rDir][col + cDir].setUncovered();
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Sets the Kaboom board to the peek formation
     */
    public void setToPeek()
    {
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < getRowCount(); rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < getColumnCount(); colIter++)
            {
            	grid[rowIter][colIter].setUncovered();
            }
        }
    }
    
    /**
     * Sets the Kaboom board to the cheat formation
     */
    public void setToCheat()
    {
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < getRowCount(); rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < getColumnCount(); colIter++)
            {
            	grid[rowIter][colIter] = new KaboomCell(KaboomPieces.empty, 
                    rowIter, colIter);
            	grid[rowIter][colIter].setUncovered();
            }
        }
        
        grid[0][0] = new KaboomCell(KaboomPieces.bomb, 0, 0);
        grid[0][1] = new KaboomCell(KaboomPieces.empty, 0, 1);
        grid[0][1].setNumBombsNear(1);
    }
    
    /**
     * Determines if this board has been won
     * 
     * @return true if board is cleared, false otherwise.
     */
    public boolean boardIsCleared()
    {
        boolean boardCleared = true;
        
        /*Iterates through all of the rows*/
        for(int rowIter = 0; rowIter < getRowCount() && boardCleared; rowIter++)
        {
            /*Iterates through all of the columns*/
            for(int colIter = 0; colIter < getColumnCount() && boardCleared;
                colIter++)
            {
                /*Sees if there is a covered cell that is not a bomb*/
                if(grid[rowIter][colIter].getCellState() == 
                    KaboomPieces.covered && !grid[rowIter][colIter].isBomb())
                {
                    boardCleared = false;
                }
            }
        }
        
        return boardCleared;
    }
    
    
    /**
     * Accessor method to get the cell at the specified coordinates
     * 
     * @param row the row coordinate
     * @param col the column coordinate
     * 
     * @return the KaboomCell at the specified coordinates
     */
    @Override
    public KaboomCell getValueAt(int row, int col)
    {
        return grid[row][col];
    }
}
