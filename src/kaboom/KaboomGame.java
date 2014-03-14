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
    private static final int kTotalNumBoards = 5000;
    
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
        
        myPrefs = Preferences.getInstance("kaboom");
        myPrefs.loadPreferences();
        
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
        
        int size = Integer.parseInt(myPrefs.get("Board Size"));
        int difficulty = Integer.parseInt(myPrefs.get("Difficulty"));
        kBoard.resetBoard(getGame(), size, difficulty);
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
        int size = Integer.parseInt(myPrefs.get("Board Size"));
        int difficulty = Integer.parseInt(myPrefs.get("Difficulty"));
        kBoard.resetBoard(getGame(), size, difficulty);
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
        Action restartGame = new KaboomRestartAction("Restart", this);
        Action newGame = new KaboomNewGameAction("New", this);
        Action selectGame = new KaboomSelectGameAction("Select", this);
        Action scores = new KaboomScoresAction("Scores", this);
        Action peek = new KaboomPeekAction("Peek", this);
        Action cheat = new KaboomCheatAction("Cheat", this);
        Action about = new KaboomAboutAction("About", this);
        Action quit = new KaboomQuitAction("Quit", this);

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
     * Menu option which restarts a Kaboom game
     */
    private class KaboomRestartAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomRestartAction(String text, KaboomGame kGame)
        {
            super(text);
            game = kGame;
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('R', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent e) 
        {
            game.restart();
        }
    }

    /**
     * Menu option which starts a new Kaboom game
     */
    private class KaboomNewGameAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomNewGameAction(String text, KaboomGame kGame)
        {
            super(text);
            game = kGame;

            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('N', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent e) 
        {
            game.incrementGame();
            game.restart();
        }
    }

    /**
     * Menu option which selects a Kaboom game
     */
    private class KaboomSelectGameAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomSelectGameAction(String text, KaboomGame kGame)
        {
            super(text);
            game = kGame;
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('G', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent event) 
        {
            String userInput = dialoger.showInputDialog("Select Game",
                "Enter desired game number (1 - 5000):");

            try
            {
                int userBoardChoice = Integer.parseInt(userInput);
                /*Determines if selected board is in the valid range*/
                if(userBoardChoice > 0 && userBoardChoice <= kTotalNumBoards)
                {
                    game.setGame(userBoardChoice);
                    game.restart();
                }
            }
            catch(NumberFormatException numFmtException)
            {
                System.out.print("");
            }
        }
    }

    /**
     * Menu option which show the high scores for the Kaboom game
     */
    private class KaboomScoresAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomScoresAction(String textStr, KaboomGame kGame)
        {
            super(textStr);
            game = kGame;
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('S', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent event) 
        {
            game.showHighScores();
        }
    }

    /**
     * Menu option which sets the Kaboom game to peek
     */
    private class KaboomPeekAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomPeekAction(String textStr, KaboomGame kGame)
        {
            super(textStr);
            game = kGame;
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('P', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent event) 
        {
            game.getBoardToView().setToPeek();
            game.getStatusToView().setLabelText("Moves: " + numMoves +
                "   Flags: " + flagCount + "/" + kBoard.getNumBombs() + "  ");
               
            game.setChanged();
            game.notifyObservers();
        }
    }

    /**
     * Menu option which sets the Kaboom game to cheat mode
     */
    private class KaboomCheatAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomCheatAction(String textStr, KaboomGame kGame)
        {
            super(textStr);
            game = kGame;
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('C', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent e) 
        {
            game.getBoardToView().setToCheat();
            game.getStatusToView().setLabelText("Moves: " + numMoves + "   Flags: "
                + flagCount + "/" + kBoard.getNumBombs() + "  ");
            game.setChanged();
            game.notifyObservers();
        }
    }

    /**
     * Menu option which shows the Kaboom game's about dialog
     */
    private class KaboomAboutAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomAboutAction(String textStr, KaboomGame kGame)
        {
            super(textStr);
            game = kGame;
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('A', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent e) 
        {
            game.dialoger.showMessageDialog("About", "Kaboom Game by Erik Owen");
        }
    }
    
    /**
     * Menu option which quits the Kaboom game
     */
    private class KaboomQuitAction extends AbstractAction
    {
        private KaboomGame game;
        public KaboomQuitAction(String textStr, KaboomGame kGame)
        {
            super(textStr);
            game = kGame;
            putValue(Action.ACCELERATOR_KEY,
                KeyStroke.getKeyStroke('Q', ActionEvent.ALT_MASK));
        }

        public void actionPerformed(ActionEvent e) 
        {
            game.gameOver = true;
            timer.stop();
            game.setChanged();
            game.notifyObservers();
        }
    }
}
