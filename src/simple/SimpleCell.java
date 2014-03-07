package simple;
import gridgame.*;
import java.util.*;

/**
 * Write a description of class SimpleCell here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SimpleCell implements Renderable
{
    private Integer value;
    
    public SimpleCell()
    {
        Random generator = new Random();
        // initialise instance variables
        value = generator.nextInt(5) + 1;
    }
    
    public String getText()
    {
        return value.toString();
    }
    
    public String toString()
    {
        String nbsp = "&nbsp;";
        String returnVal = nbsp + nbsp + nbsp + nbsp +
            value.toString() + nbsp + nbsp + nbsp + nbsp;
        if(value.toString().equals("0"))
        {
            returnVal = "black";
        }
        return returnVal;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public void decrement()
    {
        value--;
    }
    
        
    public int getNumber()
    {
        return value;
    }
    
    public void setNumber(int number)
    {
        this.value = number;
    }
}
