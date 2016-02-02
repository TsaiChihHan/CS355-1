package cs355.controller.states;

import java.awt.event.MouseEvent;

public interface IControllerState {
	
	public enum stateType{
		NOTHING, DRAWING, SELECT
	}

	public void mouseClicked(MouseEvent e);
	
	public void mousePressed(MouseEvent e);
	
	public void mouseReleased(MouseEvent e);
	
	public void mouseDragged(MouseEvent e);
	
	public stateType getType();

}
