package simple;
import gridgame.*;
import javax.swing.ImageIcon;
/**
 * Write a description of class SimpleBoard here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SimpleBoard extends GridBoard<SimpleCell>
{
    private SimpleCell[][] board;
    private GridCellRenderer renderer;
    private Preferences prefs;
    
    public SimpleBoard()
    {
        super();
        prefs = Preferences.getInstance("simple");
        reset(Integer.parseInt(prefs.get("Board Size")));
    }
    
    public void makeMove(int row, int col)
    {
        board[row][col].decrement();
    }
    
    public void reset(int size)
    {
        board = new SimpleCell[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                board[i][j] = new SimpleCell();
            }
        }
    }
    
    public boolean isWin()
    {
        for (int i = 0; i < getRowCount(); i++)
        {
            for (int j = 0; j < getColumnCount(); j++)
            {
                SimpleCell value = getValueAt(i, j);
                if (value.getNumber() != 0)
                {
                    return false;
                }
            }
        }
        return true;                   
    }
    
    public int getColumnCount()
    {
        return board[0].length;
    }
    
    public int getRowCount()
    {
        return board.length;
    }
    
    public Class getColumnClass()
    {
    	return SimpleCell.class;
    }
    
    public SimpleCell getValueAt(int row, int col)
    {
        return board[row][col];
    }
    
    public void setToCheat()
    {
    	for(int row = 0; row < getRowCount(); row++)
    	{
    		for(int col = 0; col < getColumnCount(); col++)
    		{
    			board[row][col].setNumber(0);
    		}
    	}
    	
    	board[0][0].setNumber(1);
    }
}
