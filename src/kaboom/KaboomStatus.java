package kaboom;

import java.util.Timer;

import javax.swing.JLabel;

import gridgame.GridStatus;
import gridgame.TimerLabel;

public class KaboomStatus extends GridStatus {

	private TimerLabel timer;
	
	public KaboomStatus()
	{
		super();
		
		timer = new TimerLabel();
		statusLabel = new JLabel();
		this.add(statusLabel);
	}

	public TimerLabel getTimer()
	{
		return timer;
	}
	
	@Override
	public void setLabelText(String newText)
	{
		statusLabel.setText(newText);
	}
	
	@Override
	public String getLabelText()
	{
		return statusLabel.getText() + timer.getFormattedTime();
	}
}
