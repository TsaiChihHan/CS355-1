package cs355.controller.states;

import java.awt.event.MouseEvent;
import java.util.Iterator;

public abstract interface IControllerState {
	
	public enum stateType{
		NOTHING, DRAWING, SELECT, CAMERA
	}

	public void mouseClicked(MouseEvent e);
	
	public void mousePressed(MouseEvent e);
	
	public void mouseReleased(MouseEvent e);
	
	public void mouseDragged(MouseEvent e);
	
	public void keyPressed(Iterator<Integer> iterator);
	
	public stateType getType();

}
