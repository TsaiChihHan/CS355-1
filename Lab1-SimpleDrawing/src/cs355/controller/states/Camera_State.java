package cs355.controller.states;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;

import cs355.model.scene.Point3D;

public class Camera_State implements IControllerState {
	
	private Point3D position;
	private float yaw;
	private static final float UNIT = 1.0f;

	public Camera_State()
	{
		this.position = new Point3D(0.0d, 0.0d, 0.0d);
		this.yaw = 0.0f;
	}
	
	public Camera_State(Point3D position)
	{
		this.position = position;
		this.yaw = 0.0f;
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
		System.out.println("key-press");
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
		position.x -= UNIT * (float)Math.sin(Math.toRadians(yaw-90));
	    position.z += UNIT * (float)Math.cos(Math.toRadians(yaw-90));
	}
	
	private void moveRight()
	{
		position.x -= UNIT * (float)Math.sin(Math.toRadians(yaw+90));
	    position.z += UNIT * (float)Math.cos(Math.toRadians(yaw+90));
	}
	
	private void moveForward()
	{
		position.x -= UNIT * (float)Math.sin(Math.toRadians(yaw));
	    position.z += UNIT * (float)Math.cos(Math.toRadians(yaw));
	}
	
	private void moveBackward()
	{
		position.x += UNIT * (float)Math.sin(Math.toRadians(yaw));
	    position.z -= UNIT * (float)Math.cos(Math.toRadians(yaw));
	}
	
	private void turnLeft()
	{
		yaw -= UNIT;
	}
	
	private void turnRight()
	{
		yaw += UNIT;
	}
	
	private void moveUp()
	{
		position.y -= UNIT;
	}
	
	private void moveDown()
	{
		position.y += UNIT;
	}
	
	private void moveHome()
	{
		position.x = 0;
		position.y = -1.5f;
		position.z = -20;
		yaw = 0.0f;
	}

}
