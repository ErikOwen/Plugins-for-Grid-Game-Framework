import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;


public class GameLoaderTest extends TestCase {

	protected void setUp() throws Exception
	{
		
	}
	
	public void testMultipleRuns()
	{
		String [] collapseArgs = new String [] {"Collapse", "Console",
		    "collapse/testdata/testInput1.txt", "collapse/testdata/actualOutput1.txt"};
		String [] kaboomArgs = new String [] {"Kaboom", "Console",
	    	"kaboom/testdata/testInput1.txt", "kaboom/testdata/actualOutput1.txt"};
		try
		{
			GameLoader.main(kaboomArgs);
			GameLoader.main(collapseArgs);
		}
		catch (Exception e) {

		}
	}

}
