package kaboom;

import javax.swing.JLabel;

import gridgame.GridStatus;
import gridgame.TimerLabel;

/**
 * Class which represents the status bar for the Kaboom plugin
 * 
 * @author erikowen
 * @version 1
 */
public class KaboomStatus extends GridStatus
{

    private TimerLabel timer;
    
    /**
     * Constructor to create a KaboomStatus
     */
    public KaboomStatus()
    {
        super();
        
        timer = new TimerLabel();
        statusLabel = new JLabel();
        this.add(statusLabel);
    }
    
    /**
     * Accessor method to get the timer
     * 
     * @return the timer being used by this KaboomStatus
     */
    public TimerLabel getTimer()
    {
        return timer;
    }
    
    /**
     * Setter method which sets the label's text
     * 
     * @param newText the text to set this label to
     */
    @Override
    public void setLabelText(String newText)
    {
        statusLabel.setText(newText);
    }
    
    
    /**
     * Accessor method to get the label's text
     * 
     * @return this label's text
     */
    @Override
    public String getLabelText()
    {
        return statusLabel.getText() + timer.getFormattedTime();
    }
}
