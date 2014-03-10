package collapse;
import gridgame.Renderable;


public class CollapseCell implements Renderable {

	private CollapsePiece state;
	
	public CollapseCell(CollapsePiece pieceType) {
		state = pieceType;
	}
	
	@Override
    public String getText()
    {
		String text = " ";
		
		if(state == CollapsePiece.green)
		{
			text = "+";
		}
		else if(state == CollapsePiece.red)
		{
			text = "o";
		}
		else if(state == CollapsePiece.purple)
		{
			text = "x";
		}
		
		return text;
    }
    
	@Override
    public String toString()
    {
		String stateStr = null;
		
		if(state != CollapsePiece.empty)
		{
			stateStr = state.toString();
		}
		
		return stateStr;
    }
	
	public void setToEmpty() {
		state = CollapsePiece.empty;
	}
	
	public CollapsePiece getState()
	{
		return state;
	}

}
