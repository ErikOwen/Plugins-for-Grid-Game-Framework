package kaboom;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridStatus;

public class KaboomGame extends GridGame
{

    private KaboomBoard kBoard;
    private KaboomStatus kStatus;
    private int numMoves;
	
    public KaboomGame(GridBoard board, GridStatus status)
    {
        super();
        kBoard = (KaboomBoard)board;
        kStatus = (KaboomStatus)status;
        
        init();
    }

	@Override
	public GridBoard getBoardToView() {
		return kBoard;
	}

	@Override
	public GridStatus getStatusToView() {
		return kStatus;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeMove(int row, int col) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}
}
