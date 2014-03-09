package collapse;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridImages;
import gridgame.GridStatus;
import gridgame.Preferences;


public class CollapseGame extends GridGame {

    private static GridImages im;
    private CollapseBoard cBoard;
    private CollapseStatus cStatus;
    private Preferences prefs;
	
    public CollapseGame(GridBoard board, GridStatus status)
    {
        super();
        im = GridImages.createInstance("Simple");
        cBoard = (CollapseBoard)board;
        cStatus = (CollapseStatus)status;
        prefs = Preferences.getInstance("simple");
    }
	
	
	@Override
	public GridBoard getBoardToView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridStatus getStatusToView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void makeMove(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restart() {
		// TODO Auto-generated method stub
		
	}

}
