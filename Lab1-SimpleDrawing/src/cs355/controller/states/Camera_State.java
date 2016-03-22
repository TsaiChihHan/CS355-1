package cs355.controller.states;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import cs355.model.scene.CS355Scene;

public class Camera_State implements IControllerState {
	
	private static final float UNIT = 1.0f;

	public Camera_State()
	{
//		CS355.position = new Point3D(0.0d, 0.0d, 0.0d);
//		this.yaw = 0.0f;
//		this.moveHome();
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
		while(iterator.hasNext())
		{
			switch(iterator.next())
			{
				case KeyEvent.VK_A:
					this.moveLeft();
					break;
				case KeyEvent.VK_D:
					this.moveRight();
					break;
				case KeyEvent.VK_W:
					this.moveForward();
					break;
				case KeyEvent.VK_S:
					this.moveBackward();
					break;
				case KeyEvent.VK_Q:
					this.turnLeft();
					break;
				case KeyEvent.VK_E:
					this.turnRight();
					break;
				case KeyEvent.VK_R:
					this.moveUp();
					break;
				case KeyEvent.VK_F:
					this.moveDown();
					break;
				case KeyEvent.VK_H:
					this.moveHome();
					break;
			}
		}
	}

	@Override
	public stateType getType()
	{
		return IControllerState.stateType.CAMERA;
	}
	
	
	//CAMERA MOVIN AND GROOVIN *************************************************************************************************/
	
	private void moveLeft()
	{
		CS355Scene.instance().camPos.x += UNIT * (float)Math.sin(Math.toRadians(CS355Scene.instance().camRot-90));
	    CS355Scene.instance().camPos.z -= UNIT * (float)Math.cos(Math.toRadians(CS355Scene.instance().camRot-90));
	}
	
	private void moveRight()
	{
		CS355Scene.instance().camPos.x += UNIT * (float)Math.sin(Math.toRadians(CS355Scene.instance().camRot+90));
	    CS355Scene.instance().camPos.z -= UNIT * (float)Math.cos(Math.toRadians(CS355Scene.instance().camRot+90));
	}
	
	private void moveForward()
	{
		CS355Scene.instance().camPos.x -= UNIT * (float)Math.sin(Math.toRadians(CS355Scene.instance().camRot));
	    CS355Scene.instance().camPos.z += UNIT * (float)Math.cos(Math.toRadians(CS355Scene.instance().camRot));
	}
	
	private void moveBackward()
	{
		CS355Scene.instance().camPos.x += UNIT * (float)Math.sin(Math.toRadians(CS355Scene.instance().camRot));
	    CS355Scene.instance().camPos.z -= UNIT * (float)Math.cos(Math.toRadians(CS355Scene.instance().camRot));
	}
	
	private void turnLeft()
	{
		CS355Scene.instance().camRot += UNIT;
	}
	
	private void turnRight()
	{
		CS355Scene.instance().camRot -= UNIT;
	}
	
	private void moveUp()
	{
		CS355Scene.instance().camPos.y += UNIT;
	}
	
	private void moveDown()
	{
		CS355Scene.instance().camPos.y -= UNIT;
	}
	
	private void moveHome()
	{
		CS355Scene.instance().camPos.x = 0;
		CS355Scene.instance().camPos.y = 3;
		CS355Scene.instance().camPos.z = 10;
		CS355Scene.instance().camRot = 180.0f;
	}

}
