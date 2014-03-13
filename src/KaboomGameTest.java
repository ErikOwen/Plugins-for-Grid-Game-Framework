
import java.io.IOException;
import junit.framework.TestCase;

public class KaboomGameTest extends TestCase {
	public KaboomGameTest()
	{
		
	}
	
	public void testOne()
	{
		String [] args = {"Kaboom", "Console", "testdata/testInput1.txt", "testdata/actualOutput1.txt"};
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
