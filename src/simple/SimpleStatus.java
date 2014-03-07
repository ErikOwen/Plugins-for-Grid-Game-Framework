package simple;
import gridgame.*;
import javax.swing.*;

/**
 * Write a description of class SimpleStatus here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SimpleStatus extends GridStatus
{
    public SimpleStatus()
    {
        super();
        statusLabel = new JLabel();
        this.add(statusLabel);
    }
    
    public void setLabelText(String newText)
    {
        statusLabel.setText(newText);
    }
}