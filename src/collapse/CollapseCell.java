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
//        return value.toString();
		return "";
    }
    
	@Override
    public String toString()
    {
//        String nbsp = "&nbsp;";
//        String returnVal = nbsp + nbsp + nbsp + nbsp +
//            value.toString() + nbsp + nbsp + nbsp + nbsp;
//        if(value.toString().equals("0"))
//        {
//            returnVal = "black";
//        }
//        return returnVal;
		return "";
    }
	
	public void setToEmpty() {
		state = CollapsePiece.empty;
	}

}
