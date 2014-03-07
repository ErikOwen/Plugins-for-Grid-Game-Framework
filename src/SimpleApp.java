import gridgame.*;
import simple.*;
import java.io.*;
/** The application class for a simple demonstration of 
 *  a plugin for the GridGame framework.
 */
public final class SimpleApp
{
    public static void main(String[] args)
    {
        GridGame gridGame;     // The instance of the game
        GridBoard gridBoard;   // The instance of the board
        GridStatus gridStatus; // The instance of status
        
        // Create the game components                  
        gridBoard = new SimpleBoard();        
        gridStatus = new SimpleStatus();    
        gridGame = new SimpleGame(gridBoard,gridStatus);

        
        // setup the game
        gridGame.init();

        // Create the view 
        GridGUI gui = new GridGUI("Simple", gridGame);
        gui.createUI();   
        
        GridConsole console = new GridConsole("Simple", gridGame);
        console.createUI();   
        
        gui.setIOsources(new InputStreamReader(System.in), 
                         new OutputStreamWriter(System.out));
        console.setIOsources(new InputStreamReader(System.in), 
                         new OutputStreamWriter(System.out));
                         
        // Link the model and view to each other
        gridGame.addObserver(gui);
        gridGame.addObserver(console);
        gridGame.setDialoger(gui.getDialoger());       
       
        // Make the view visible and available for user interaction
        gui.setVisible(true);  
        console.setVisible(true);
    }
}