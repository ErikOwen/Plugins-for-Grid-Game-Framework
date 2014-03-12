package kaboom;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.KeyStroke;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridStatus;
import gridgame.TimerLabel;

public class KaboomGame extends GridGame
{

    private KaboomBoard kBoard;
    private KaboomStatus kStatus;
    private int numMoves, flagCount;
    private TimerLabel timer;
    private JLabel label;
    private boolean hasWon, hasLost;
	
    public KaboomGame(GridBoard board, GridStatus status)
    {
        super();
        kBoard = (KaboomBoard)board;
        kStatus = (KaboomStatus)status;
        timer = new TimerLabel();
        label = new JLabel();
        
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
		setRandomGame();
		kBoard.resetBoard(getGame());
		numMoves = 0;
		flagCount = 0;
		hasWon = false;
		hasLost = false;
		
		timer.restart();
		label.setText("Moves: " + numMoves + "   Flags: " + flagCount + "/" + kBoard.getNumBombs() + "  ");
		kStatus.add(label);
		kStatus.add(timer);
		
	}

	@Override
	public void makeMove(int row, int col) {
		kBoard.takeTurn(row, col);
		numMoves++;
		label.setText("Moves: " + numMoves + "   Flags: " + flagCount + "/" + kBoard.getNumBombs() + "  ");
		KaboomCell chosenCell = kBoard.getValueAt(row, col);
		
        /*Determines if the cell chosen is a bomb*/
        if(chosenCell.isBomb())
        {
            timer.pause();
            this.hasLost = true;
            chosenCell.setCellState(KaboomPieces.bombHit);
            kBoard.setToPeek();
            
            dialoger.showMessageDialog("Game Over", "You lost.");
        }
        /*Determines if the user has won the baord*/
        else if(kBoard.boardIsCleared() && !hasWon && !hasLost)
        {
        	hasWon = true;
        	
			String saveTitle = "Game Won Notification";
			String saveMessage = "Game " + getGame() + " Cleared! \nSave your score? (y/n)";
			String userInput = dialoger.showInputDialog(saveTitle, saveMessage);
			
			if(userInput != null && userInput.toLowerCase().equals("y"))
			{	
				saveScore(timer.getFormattedTime());
			}
        	
        }
		
		setChanged();
		notifyObservers();
		
	}

	@Override
	public void restart() {
		kBoard.resetBoard(getGame());
		numMoves = 0;
		flagCount = 0;
		hasWon = false;
		hasLost = false;
		
		timer.restart();
		
		label.setText("Moves: " + numMoves + "   Flags: " + flagCount + "/" + kBoard.getNumBombs() + "  ");
		
		setChanged();
		notifyObservers(getGame());
		
	}
	
	@Override
	public void handleRightClick(int row, int col)
	{
        KaboomCell flaggedCell = kBoard.getValueAt(row, col);
        
        /*Determines if the cell has already been flagged*/
        if(!flaggedCell.isFlagged())
        {
            /*Determines if the cell is flag-eligible*/
            if(flaggedCell.getCellState()  == KaboomPieces.covered)
            {
                flagCount++;
                flaggedCell.setFlagged();
            }
        }
        else
        {
            flagCount--;
            flaggedCell.setUnflagged();
        }
        
        label.setText("Moves: " + numMoves + "   Flags: " + flagCount + "/" + kBoard.getNumBombs() + "  ");
        
        setChanged();
        notifyObservers();
	}
	
	public List<Action> getMenuActions()
	{
		List<Action> list = new ArrayList<Action>();
		Action restartGame = new RestartAction("Restart");
		Action newGame = new NewGameAction("New Game");
		Action selectGame = new SelectGameAction("Select Game");
		Action scores = new ScoresAction("Scores");
		Action peek = new PeekAction("Peek");
		Action cheat = new CheatAction("Cheat");
		Action about = new AboutAction("About");
		Action quit = new QuitAction("Quit");

		list.add(restartGame);
		list.add(newGame);
		list.add(selectGame);
		list.add(scores);
		list.add(peek);
		list.add(cheat);
		list.add(about);
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
	   
	   private class PeekAction extends AbstractAction
	   {
		   public PeekAction(String text)
		   {
			   super(text);
			   putValue(Action.ACCELERATOR_KEY,
					   KeyStroke.getKeyStroke('P', ActionEvent.ALT_MASK));
		   }

		   public void actionPerformed(ActionEvent e) 
		   {
			   kBoard.setToPeek();
			   
			   label.setText("Moves: " + numMoves + "   Flags: " + flagCount + "/" + kBoard.getNumBombs() + "  ");
			   
			   setChanged();
			   notifyObservers();
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
			   kBoard.setToCheat();

			   label.setText("Moves: " + numMoves + "   Flags: " + flagCount + "/" + kBoard.getNumBombs() + "  ");

			   setChanged();
			   notifyObservers();
		   }
	   }

	   private class AboutAction extends AbstractAction
	   {
		   public AboutAction(String text)
		   {
			   super(text);

			   putValue(Action.ACCELERATOR_KEY,
					   KeyStroke.getKeyStroke('A', ActionEvent.ALT_MASK));
		   }

		   public void actionPerformed(ActionEvent e) 
		   {
			   dialoger.showMessageDialog("About", "Kaboom Game by Erik Owen");
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
			   timer.stop();
			   
			   setChanged();
			   notifyObservers();
		   }
	   }
}
