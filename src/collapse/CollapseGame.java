package collapse;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridImages;
import gridgame.GridStatus;
import gridgame.Preferences;


public class CollapseGame extends GridGame {

    private CollapseBoard cBoard;
    private CollapseStatus cStatus;
    private Preferences prefs;
    private int numMoves;
	
    public CollapseGame(GridBoard board, GridStatus status)
    {
        super();
        cBoard = (CollapseBoard)board;
        cStatus = (CollapseStatus)status;
        prefs = Preferences.getInstance("collapse");
        
        init();
    }
	
	
	@Override
	public GridBoard getBoardToView() {
		return cBoard;
	}

	@Override
	public GridStatus getStatusToView() {
		return cStatus;
	}

	@Override
	public void init() {
		setRandomGame();
		cBoard.resetBoard(getGame());
		numMoves = 0;
		
	}

	@Override
	public void makeMove(int row, int col) {
		if(cBoard.getValueAt(row, col).getState() != CollapsePiece.empty)
		{
			cBoard.makeMove(row, col);
			numMoves++;
		}
		
		setChanged();
		notifyObservers();
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

}
