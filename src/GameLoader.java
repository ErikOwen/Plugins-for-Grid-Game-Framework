import gridgame.GridBoard;
import gridgame.GridConsole;
import gridgame.GridGUI;
import gridgame.GridGame;
import gridgame.GridStatus;
import gridgame.GridView;
import gridgame.SimpleDialoger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * Class which loads the GridGame plugin and runs it
 */
public final class GameLoader extends Object
{
    private static final int kTwoArgs = 2, kFourArgs = 4, kInputFile = 2,
    kOutputFile = 3;

    /**
     * Constructor to build GameLoader object
     */
    public GameLoader()
    {
    }
    
    /**
     * Dynamically runs the GridGame plugin based on the arguments provided.
     * @param args the arguments for the game
     */
    public void loadGame(String [] args) throws IOException
    {
        GridView app = null;
        Object [] params = new Object [] {};
        String classType = "";
        
        /*Determines if there is a correct number of program arguments*/
        if(args.length == kTwoArgs || args.length == kFourArgs)
        {
            try
            {
                classType = "Board";
                Class<?> curClass = Class.forName(args[0].toLowerCase()
                    + "." + args[0] + classType);
                GridBoard<?> board = (GridBoard<?>)curClass.newInstance();
                
                classType = "Status";
                curClass = Class.forName(args[0].toLowerCase() + "."
                    + args[0] + classType);
                GridStatus status = (GridStatus)curClass.newInstance();
                
                classType = "Game";
                curClass = Class.forName(args[0].toLowerCase() + "." +
                    args[0] + classType);
                Constructor<?> curConstr = curClass.getConstructor(GridBoard.class,
                    GridStatus.class);
                params = new Object[]{board, status};
                GridGame game = (GridGame) curConstr.newInstance(params);

                app = createView(args, game);
                setIOSources(app, args);
                
                game.setDialoger(app.getDialoger());
                game.init();
                
                app.createUI();
                game.addObserver(app);
                app.setVisible(true);
            }
            catch(ClassNotFoundException cnf)
            {
                System.err.println("Unable to find a class named '" +
                     args[0] + classType + "' in the CLASSPATH.");
            }
            catch(NoSuchMethodException nsm)
            {
                System.err.println("No constructor found for " + args[0] +
                    classType + " with an argument list of size " +
                    params.length + ".");
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
            catch (InstantiationException e)
            {
                e.printStackTrace();
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            
        }
        else
        {
            System.err.println("Missing parameter.");
        }
    }
    
    private void setIOSources(GridView app, String [] args) throws IOException
    {
        /*Determines if user wants to specify input / output file*/
        if(args.length == kFourArgs)
        {
            File input = new File(args[kInputFile]);
            app.setIOsources(new FileReader(input), new FileWriter(args[kOutputFile]));
            
        }
        else
        {
            app.setIOsources(new InputStreamReader(System.in), 
                new OutputStreamWriter(System.out));
        }
    }
    
    private GridView createView(String [] args, GridGame game)
    {
        GridView app = null;
        /*Determines if the GUI interface should be instantiated*/
        if(args[1].equals("GUI"))
        {
            app = new GridGUI(args[0], game);
            
        }
        /*Determines if console interface should be instantiated*/
        else if(args[1].equals("Console"))
        {
            app = new GridConsole(args[0], game);
        }
        
        return app;
    }
    
    /**
     * Main method which runs the Grid Game plugin
     * 
     * @param args command line arguments
     */
    public static void main(String [] args) throws IOException
    {
        GameLoader loader = new GameLoader();
        loader.loadGame(args);
    }
}
