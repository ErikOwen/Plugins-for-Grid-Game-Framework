package simple;
import gridgame.*;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
/**
 * Write a description of class SimpleGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SimpleGame extends GridGame
{
    private int moveCount;
    private SimpleBoard sBoard;
    private SimpleStatus sStatus;
    protected boolean gameOver;
    private GridCellRenderer renderer;
    private static GridImages im;
    private int timeSeconds;
    private Timer timer;
    private String keyPressed;
    private Preferences prefs;
    
    public SimpleGame(GridBoard board, GridStatus status)
    {
        super();
        im = GridImages.createInstance("Simple");
        sBoard = (SimpleBoard) board;
        sStatus = (SimpleStatus) status;
        keyPressed = "";
        prefs = Preferences.getInstance("simple");    }
    
    public GridBoard getBoardToView()
    {
        return sBoard;    
    }
    
    public GridStatus getStatusToView()
    {
        return sStatus;
    }
    
    public void init()
    {
        
        moveCount = 0;
        gameOver = false;
        timeSeconds = 0;

        String labelText = "Moves: " + moveCount + "   Time " + timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60) + keyPressed;
        sStatus.setLabelText(labelText);
        
        ActionListener timerListener = new ActionListener()
        {   
            @Override
            public void actionPerformed(ActionEvent e)
            {
                timeSeconds++;
                String labelText = "Moves: " + moveCount + "   Time " + timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60) + keyPressed;
                sStatus.setLabelText(labelText);
            }
        };
        timer = new Timer(1000, timerListener);
        timer.setInitialDelay(1000);
        timer.start();
        timer.restart();
    }
    
    public void makeMove(int row, int col)
    {
        moveCount++;
        sBoard.makeMove(row, col);
        String labelText = "Moves: " + moveCount + "   Time " + timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60) + keyPressed;
        sStatus.setLabelText(labelText);
        setChanged();
        notifyObservers();
        
        gameOver = gameOver();
        if(gameOver)
        {
            JOptionPane.showMessageDialog(null, "You've Lost!");
        }
      
        
        if(sBoard.isWin())
        {
        	timer.stop();
            JOptionPane.showMessageDialog(null, "You've Won!");
            saveScore(timeSeconds / 60 + " : " + String.format("%02d", timeSeconds % 60));
        }
    }
    
    public void handleRightClick(int row, int col)
    {
        if (sBoard.getRowCount() > row && sBoard.getColumnCount() > col)
        {
             sBoard.getValueAt(row, col).setNumber(1);
             setChanged();
             notifyObservers();   
        }
    }
    
    public void restart()
    {
    	try
    	{
    		timer.stop();
    		sBoard.reset();
    	}
    	catch(Exception e) 
    	{
//    		e.printStackTrace();
    	}
        init();
        
        setChanged();
        notifyObservers(getPreference("Background Color"));
        
        setChanged();
        notifyObservers();
    }
    
    public java.util.List<Action> getMenuActions()
    {
        List<Action> list = new ArrayList<Action>();
        Action newGame = new newGameAction("New Game");
        Action cheat = new cheatAction("Cheat");
        Action scores = new scoresAction("Scores");
        list.add(newGame);
        list.add(cheat);
        list.add(scores);
        
        return list;
    }
    
    public boolean gameOver()
    {
        for(int row = 0; row < sBoard.getRowCount(); row++) 
        {
            for(int col = 0; col < sBoard.getColumnCount(); col++)
            {
                if(sBoard.getValueAt(row, col).getNumber() < 0)
                {
                    return true;    
                }
               
            }
        }
        return false;
    }
    
    @Override
    public CursorKeyAdapter getKeyAdapter()
    {
    	return new CursorKeyAdapter() {

			@Override
			public void processDown() {
				keyPressed = "   Down Pressed";
		        String labelText = "Moves: " + moveCount + "   Time " + timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60) + keyPressed;
		        sStatus.setLabelText(labelText);
		        setChanged();
		        notifyObservers();
			}

			@Override
			public void processLeft() {
				keyPressed = "   Left Pressed";
		        String labelText = "Moves: " + moveCount + "   Time " + timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60) + keyPressed;
		        sStatus.setLabelText(labelText);
		        setChanged();
		        notifyObservers();
			}

			@Override
			public void processRight() {
				keyPressed = "   Right Pressed";
		        String labelText = "Moves: " + moveCount + "   Time " + timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60) + keyPressed;
		        sStatus.setLabelText(labelText);
		        setChanged();
		        notifyObservers();
			}

			@Override
			public void processUp() {
				keyPressed = "   Up Pressed";
		        String labelText = "Moves: " + moveCount + "   Time " + timeSeconds / 60 + ":" + String.format("%02d", timeSeconds % 60) + keyPressed;
		        sStatus.setLabelText(labelText);
		        setChanged();
		        notifyObservers();
			}
    		
    	};
    }
    
    private class newGameAction extends AbstractAction
    {
        public newGameAction(String text)
        {
            super(text);
        }
        
        public void actionPerformed(ActionEvent e) 
        {
            restart();
        }
    }
    
    private class cheatAction extends AbstractAction
    {
        public cheatAction(String text)
        {
            super(text);
        }
        
        public void actionPerformed(ActionEvent e) 
        {
            sBoard.setToCheat();
            setChanged();
            notifyObservers();
        }
    }
    
    private class scoresAction extends AbstractAction
    {
        public scoresAction(String text)
        {
            super(text);
        }
        
        public void actionPerformed(ActionEvent e) 
        {
            showHighScores();
        }
    }
    
    public void setPreferences(Preferences prefs)
    {
        super.setPreferences(prefs);
    }
}
