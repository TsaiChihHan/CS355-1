package cs355.controller.states;

import java.awt.event.MouseEvent;
import java.util.Iterator;

public class Nothing_State implements IControllerState {

	public Nothing_State()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}
	
	@Override
	public void keyPressed(Iterator<Integer> iterator)
	{
		
	}

	@Override
	public stateType getType()
	{
		return IControllerState.stateType.NOTHING;
	}

}
