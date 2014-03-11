package collapse;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridStatus;


public class CollapseGame extends GridGame {

    private CollapseBoard cBoard;
    private CollapseStatus cStatus;
    private int numMoves;
	
    public CollapseGame(GridBoard board, GridStatus status)
    {
        super();
        cBoard = (CollapseBoard)board;
        cStatus = (CollapseStatus)status;
        
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
		
		cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
	            + numMoves + "\n");
		
	}

	@Override
	public void makeMove(int row, int col) {
		if(cBoard.getValueAt(row, col).getState() != CollapsePiece.empty)
		{
			cBoard.makeMove(row, col);
			numMoves++;
			
			cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
		            + numMoves + "\n");
			
			
			setChanged();
			notifyObservers();
			
			if(cBoard.isWin())
			{
				String saveTitle = "Game Won Notification";
				String saveMessage = "Game " + getGame() + " Cleared! \nSave your score? (y/n)";
				String userInput = dialoger.showInputDialog(saveTitle, saveMessage);
				
				if(userInput != null && userInput.toLowerCase().equals("y"))
				{	
					saveScore(numMoves);
				}
			}
		}
	}

	@Override
	public void restart() {
		cBoard.resetBoard(getGame());
		numMoves = 0;
		
		cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
            + numMoves + "\n");
		
		setChanged();
		notifyObservers(getGame());
		
	}
	
    public List<Action> getMenuActions()
    {
        List<Action> list = new ArrayList<Action>();
        Action restartGame = new RestartAction("Restart");
        Action newGame = new NewGameAction("New Game");
        Action selectGame = new SelectGameAction("Select Game");
        Action scores = new ScoresAction("Scores");
        Action cheat = new CheatAction("Cheat");
        Action quit = new QuitAction("Quit");
        
        list.add(restartGame);
        list.add(newGame);
        list.add(selectGame);
        list.add(scores);
        list.add(cheat);
        list.add(quit);
        
        return list;
    }
	
    private class RestartAction extends AbstractAction
    {
        public RestartAction(String text)
        {
            super(text);
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
        }
        
        public void actionPerformed(ActionEvent e) 
        {
        	restart();
        }
    }
    
    private class NewGameAction extends AbstractAction
    {
        public NewGameAction(String text)
        {
            super(text);
            
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke('N', ActionEvent.ALT_MASK));
        }
        
        public void actionPerformed(ActionEvent e) 
        {
        	incrementGame();
        	restart();
        }
    }
    
    private class SelectGameAction extends AbstractAction
    {
        public SelectGameAction(String text)
        {
            super(text);
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke('G', ActionEvent.ALT_MASK));
        }
        
        public void actionPerformed(ActionEvent e) 
        {
        	String prompt = "Enter desired game number (1 - 5000):";
        	String title = "Select Game";
        	String userInput = dialoger.showInputDialog(title, prompt);
        	
        	try
        	{
        		int userBoardChoice = Integer.parseInt(userInput);
        		setGame(userBoardChoice);
        		restart();
        	}
        	catch(NumberFormatException nfe)
        	{
        		title = title;
        	}
        }
    }
    
    private class ScoresAction extends AbstractAction
    {
        public ScoresAction(String text)
        {
            super(text);
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke('S', ActionEvent.ALT_MASK));
        }
        
        public void actionPerformed(ActionEvent e) 
        {
        	showHighScores();
        }
    }
    
    private class CheatAction extends AbstractAction
    {
        public CheatAction(String text)
        {
            super(text);
            putValue(Action.ACCELERATOR_KEY,
                    KeyStroke.getKeyStroke('C', ActionEvent.ALT_MASK));
        }
        
        public void actionPerformed(ActionEvent e) 
        {
        	cBoard.setToCheat();
        	
    		cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
    	            + numMoves + "\n");
        	
        	setChanged();
        	notifyObservers();
        }
    }
    
    private class QuitAction extends AbstractAction
    {
        public QuitAction(String text)
        {
            super(text);
            
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
        }
        
        public void actionPerformed(ActionEvent e) 
        {
        	gameOver = true;
        	
        	setChanged();
        	notifyObservers();
        }
    }

}
