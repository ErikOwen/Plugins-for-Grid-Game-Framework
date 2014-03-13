package collapse;
import gridgame.Renderable;

/**
 * Class which represents a cell in the CollapseBoard.
 * 
 * @author erikowen
 * @version 1
 */
public class CollapseCell implements Renderable
{
    
    private CollapsePiece state;
    
    /**
     * Constructor to instantiate a new CollapsePiece
     * 
     * @param pieceType what color of cell this piece should be
     */
    public CollapseCell(CollapsePiece pieceType)
    {
        state = pieceType;
    }
    
    /**
     * Gets the text at this cell
     * 
     * @return the text representation of this cell
     */
    @Override
    public String getText()
    {
        String text = " ";
        
        /*Determines if the state is green*/
        if(state == CollapsePiece.green)
        {
            text = "+";
        }
        /*Determines if the state is red*/
        else if(state == CollapsePiece.red)
        {
            text = "o";
        }
        /*Deterimines if the state is purple*/
        else if(state == CollapsePiece.purple)
        {
            text = "x";
        }
        
        return text;
    }
    
    /**
     * Method which helps the framework determine which
     * image to use for this cel
     * 
     * @return string representation of this cell
     */
    @Override
    public String toString()
    {
        String stateStr = "";
        
        /*Determines if the state is empty*/
        if(state != CollapsePiece.empty)
        {
            stateStr = state.toString();
        }
        
        return stateStr;
    }
    
    /**
     * Sets the state of this cell to CollapsePiece.empty
     */
    public void setToEmpty()
    {
        state = CollapsePiece.empty;
    }
    
    /**
     * Accessor method to get the state of this cell
     * 
     * @return the state of this cell
     */
    public CollapsePiece getState()
    {
        return state;
    }

}
