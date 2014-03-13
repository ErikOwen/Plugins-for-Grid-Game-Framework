
import java.io.IOException;
import junit.framework.TestCase;

public class CollapseGameTest extends TestCase {
	public CollapseGameTest()
	{
		
	}
	
	public void testOne()
	{
		String [] args = {"Collapse", "Console", "testdata/testInput1.txt", "testdata/actualOutput1.txt"};
		try
		{
			GameLoader.main(args);
		}
		catch (IOException e)
		{
			System.err.println("Test one failed");
		}
	}
}
