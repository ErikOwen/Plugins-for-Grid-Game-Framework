import gridgame.GridBoard;
import gridgame.GridConsole;
import gridgame.GridGUI;
import gridgame.GridGame;
import gridgame.GridStatus;
import gridgame.GridView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;


public final class GameLoader extends Object
{
	public GameLoader()
	{
		
	}
	
	public static void main(String [] args) throws FileNotFoundException, IOException
	{
		final String gameName;
		GridView app = null;
		String [] classNames = {"Board", "Status", "Game"};
		Object [] curParams = new Object [] {};
		int ndx = 0;

		if(args.length == 2 || args.length == 4)
		{
			gameName = args[0];
			
			try
			{
				Class curClass = Class.forName(gameName.toLowerCase() + "." + gameName + classNames[ndx]);
				Constructor curConstr = curClass.getConstructor();
				curParams = new Object [] {};
				GridBoard board = (GridBoard)curConstr.newInstance(curParams);
			    
				curClass = Class.forName(gameName.toLowerCase() + "." + gameName + classNames[++ndx]);
				curConstr = curClass.getConstructor();
				curParams = new Object [] {};
				GridStatus status = (GridStatus)curConstr.newInstance(curParams);
			
				curClass = Class.forName(gameName.toLowerCase() + "." + gameName + classNames[++ndx]);
				curConstr = curClass.getConstructor(GridBoard.class, GridStatus.class);
				curParams = new Object [] {board, status};
				GridGame game = (GridGame) curConstr.newInstance(curParams);
				
				game.init();
				
				if(args[1].equals("GUI"))
				{
					app = new GridGUI("Simple", game);
					
				}
				else if(args[1].equals("Console"))
				{
					app = new GridConsole("Simple", game);
				}
				
				if(args.length == 4)
				{
					File input = new File(gameName.toLowerCase() + File.separator + args[2]);
					
					app.setIOsources(new FileReader(input),
			                new BufferedWriter(new OutputStreamWriter(
			                new FileOutputStream(gameName.toLowerCase() + File.separator
			                + args[3]), "utf-8")));
					
				}
				else
				{
			        app.setIOsources(new InputStreamReader(System.in), 
			            new OutputStreamWriter(System.out));
				}
				
				app.createUI();
				game.addObserver(app);
				app.setVisible(true);
			}
			catch(ClassNotFoundException cnf)
			{
				System.err.println("Unable to find a class named " + gameName + classNames[++ndx] + " in the CLASSPATH.");
			}
			catch(NoSuchMethodException nsm)
			{
				System.err.println("No constructor found for " + gameName + classNames[++ndx] + " with an argument list of size " + curParams.length + ".");
		    }
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		else
		{
			System.err.println("Missing parameter.");
		}
	}
}