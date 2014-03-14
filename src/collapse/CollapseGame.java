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
import gridgame.Preferences;

/**
 * Class which represents the game logic for the Collapse plugin
 * 
 * @author erikowen
 * @version 1
 */
public class CollapseGame extends GridGame
{

    private CollapseBoard cBoard;
    private CollapseStatus cStatus;
    private int numMoves;
    private static final int kTotalNumBoards = 5000;
    
    /**
     * Constructor to create a CollapseGame object
     * 
     * @param board the board to use for this game
     * @param status the status to use for this game
     */
    public CollapseGame(GridBoard<CollapseCell> board, GridStatus status)
    {
        super();
        cBoard = (CollapseBoard)board;
        cStatus = (CollapseStatus)status;
        
        myPrefs = Preferences.getInstance("collapse");
        myPrefs.loadPreferences();
        
        init();
    }
    
    /**
     * Accessor method to get the CollapseBoard
     * 
     * @return the CollapseBoard being used in this game
     */
    @Override
    public CollapseBoard getBoardToView()
    {
        return cBoard;
    }

    /**
     * Accessor method to get the Collapse status
     * 
     * @return the CollapseStatus used in this game
     */
    @Override
    public CollapseStatus getStatusToView()
    {
        return cStatus;
    }

    /**
     * Sets up this CollapseGame
     */
    @Override
    public void init()
    {
        setRandomGame();
        
        int boardSize = Integer.parseInt(myPrefs.get("Board Size"));
        
        cBoard.resetBoard(getGame(), boardSize);
        numMoves = 0;
        
        cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
                + numMoves + "\n");
        
    }

    /**
     * Makes a move on the CollapseBoard
     * 
     * @param row the row to make a move in
     * @param col the column to make a move in
     */
    @Override
    public void makeMove(int row, int col)
    {
        /*Determines if the chosen cell is empty*/
        if(cBoard.getValueAt(row, col).getState() != CollapsePiece.empty)
        {
            cBoard.makeMove(row, col);
            numMoves++;
            
            cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
                    + numMoves + "\n");
            
            
            setChanged();
            notifyObservers();
            
            /*Determines if the board has been won*/
            if(cBoard.isWin())
            {
                String saveTitle = "Game Won Notification";
                String saveMessage = "Game " + getGame() +
                    " Cleared! \nSave your score? (y/n)";
                String userInput = dialoger.showInputDialog(saveTitle, saveMessage);
                
                /*Determines if the user wants to save their score*/
                if(userInput != null && userInput.toLowerCase().equals("y"))
                {   
                    saveScore(numMoves);
                }
            }
        }
    }

    /**
     * Restarts the board being used
     */
    @Override
    public void restart()
    {
        int boardSize = Integer.parseInt(myPrefs.get("Board Size"));
        cBoard.resetBoard(getGame(), boardSize);
        numMoves = 0;
        
        cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
            + numMoves + "\n");
        
        setChanged();
        notifyObservers(getGame());
        
    }
    
    /**
     * Gets the menu actions for this game
     * 
     * @return the list of menu actions
     */
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
    
    /**
     * Action for the menu which restarts the current game
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
     * Action for the menu which goes to the next game
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
     * Action for the menu which selects a game to be played
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
                if(userBoardChoice > 0 && userBoardChoice <= kTotalNumBoards)
                {
                	setGame(userBoardChoice);
                	restart();
                }
            }
            catch(NumberFormatException nfe)
            {
                title += "";
            }
        }
    }
    
    /**
     * Action for the menu which displays the high scores
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
     * Action for the menu which sets the board to cheat
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
            cBoard.setToCheat();
            
            cStatus.setLabelText("Tiles left: " + cBoard.getTilesLeft() + "    Moves: "
                    + numMoves + "\n");
            
            setChanged();
            notifyObservers();
        }
    }
    
    /**
     * Action for the menu which quits the game
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
            
            setChanged();
            notifyObservers();
        }
    }

}
