package kaboom;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import gridgame.GridBoard;
import gridgame.GridGame;
import gridgame.GridStatus;
import gridgame.Preferences;
import gridgame.TimerLabel;

/**
 * The class which hold the game logic for the Kaboom plugin
 * 
 * @author erikowen
 * @version 1
 */
public class KaboomGame extends GridGame
{

    private KaboomBoard kBoard;
    private KaboomStatus kStatus;
    private int numMoves, flagCount;
    private TimerLabel timer;
    private boolean hasWon, hasLost;
    
    /**
     * Constructor to create a KaboomGame object
     * 
     * @param board the board for this game to use
     * @param status the status for this game to use
     */
    public KaboomGame(GridBoard<KaboomCell> board, GridStatus status)
    {
        super();
        kBoard = (KaboomBoard)board;
        kStatus = (KaboomStatus)status;
        timer = kStatus.getTimer();
        
        //Preferences.getInstance("Kaboom").loadPreferences();
        
        init();
    }

    /**
     * Accessor method to get the board
     * 
     * @return the board used by this game
     */
    @Override
    public KaboomBoard getBoardToView()
    {
        return kBoard;
    }

    /**
     * Accessor method to get the status
     * 
     * @return the status used by this game
     */
    @Override
    public KaboomStatus getStatusToView()
    {
        return kStatus;
    }

    /**
     * Helps initialize the KaboomGame
     */
    @Override
    public void init()
    {
        setRandomGame();
        kBoard.resetBoard(getGame());
        numMoves = 0;
        flagCount = 0;
        hasWon = false;
        hasLost = false;
        
        timer.restart();
        kStatus.setLabelText("Moves: " + numMoves + "   Flags: " +
            flagCount + "/" + kBoard.getNumBombs() + "  ");
        kStatus.add(timer);
        
    }

    /**
     * Makes a move on the board
     * 
     * @param row the row where to make the move
     * @param col the col where to make the move
     */
    @Override
    public void makeMove(int row, int col)
    {
        kBoard.takeTurn(row, col);
        numMoves++;
        kStatus.setLabelText("Moves: " + numMoves + "   Flags: " + flagCount
            + "/" + kBoard.getNumBombs() + "  ");
        KaboomCell chosenCell = kBoard.getValueAt(row, col);
        
        setChanged();
        notifyObservers();
        
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
            String saveMessage = "Game " + getGame() + " Cleared! \nSave your time of "
                + timer.getFormattedTime()  + "? (y/n)";
            String userInput = dialoger.showInputDialog(saveTitle, saveMessage);
            
            /*Determines if user wants to save their score*/
            if(userInput != null && userInput.toLowerCase().equals("y"))
            {   
                saveScore(timer.getFormattedTime());
            }
            
        }
        
    }

    /**
     * Restarts the current game
     */
    @Override
    public void restart()
    {
        kBoard.resetBoard(getGame());
        numMoves = 0;
        flagCount = 0;
        hasWon = false;
        hasLost = false;
        
        timer.restart();
        
        kStatus.setLabelText("Moves: " + numMoves + "   Flags: " +
            flagCount + "/" + kBoard.getNumBombs() + "  ");
        
        setChanged();
        notifyObservers(getGame());
        
    }
    
    /**
     * Handles right click functionality on the board
     * 
     * @param row the row where the right click was made
     * @param col the column where the right click was made
     */
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
        
        kStatus.setLabelText("Moves: " + numMoves + "   Flags: " + flagCount
            + "/" + kBoard.getNumBombs() + "  ");
        
        setChanged();
        notifyObservers();
    }
    
    /**
     * Accessor method to get the menu actions
     * 
     * @return a list of the menu actions
     */
    public List<Action> getMenuActions()
    {
        List<Action> list = new ArrayList<Action>();
        Action restartGame = new RestartAction("Restart");
        Action newGame = new NewGameAction("New");
        Action selectGame = new SelectGameAction("Select");
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
    
    /**
     * Menu option which restarts the game
     */
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

    /**
     * Menu option which starts a new game
     */
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

    /**
     * Menu option which selects a game
     */
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
                title += "";
            }
        }
    }

    /**
     * Menu option which show the high scores
     */
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

    /**
     * Menu option which sets the game to peek
     */
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
               
            kStatus.setLabelText("Moves: " + numMoves + "   Flags: " + flagCount
                + "/" + kBoard.getNumBombs() + "  ");
               
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Menu option which sets the game to cheat mode
     */
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

            kStatus.setLabelText("Moves: " + numMoves + "   Flags: " + flagCount
                + "/" + kBoard.getNumBombs() + "  ");

            setChanged();
            notifyObservers();
        }
    }

    /**
     * Menu option which shows the about dialog
     */
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
    
    /**
     * Menu option which quits the game
     */
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
