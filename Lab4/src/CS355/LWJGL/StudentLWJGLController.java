package CS355.LWJGL;

//You might notice a lot of imports here.
//You are probably wondering why I didn't just import org.lwjgl.opengl.GL11.*
//Well, I did it as a hint to you.
//OpenGL has a lot of commands, and it can be kind of intimidating.
//This is a list of all the commands I used when I implemented my project.
//Therefore, if a command appears in this list, you probably need it.
//If it doesn't appear in this list, you probably don't.
//Of course, your milage may vary. Don't feel restricted by this list of imports.
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 *
 * @author Brennan Smith
 */
public class StudentLWJGLController implements CS355LWJGLController {
	
	private enum projectionType {
		ORTHOGRAPHIC, PERSPECTIVE
	}
	
	private projectionType projection; 
	
	private Vector3f position = new Vector3f();
	
	private float yaw = 0.0f;
	
	private static final float UNIT = 1.0f;

	// This is a model of a house.
	// It has a single method that returns an iterator full of Line3Ds.
	// A "Line3D" is a wrapper class around two Point2Ds.
	// It should all be fairly intuitive if you look at those classes.
	// If not, I apologize.
	private WireFrame model = new HouseModel();

	// This method is called to "resize" the viewport to match the screen.
	// When you first start, have it be in perspective mode.
	@Override
	public void resizeGL() //called only once when program starts
	{
		this.projection = projectionType.PERSPECTIVE;
		glViewport(0, 0, LWJGLSandbox.DISPLAY_WIDTH, LWJGLSandbox.DISPLAY_HEIGHT); //setup viewport
		
		//setup projection matrix
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
		moveHome();
	}

	@Override
	public void update()
	{

	}

	// This is called every frame, and should be responsible for keyboard
	// updates.
	// An example keyboard event is captured below.
	// The "Keyboard" static class should contain everything you need to finish
	// this up.
	@Override
	public void updateKeyboard()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			this.moveLeft();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			this.moveRight();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			this.moveForward();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			this.moveBackward();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_Q))
		{
			this.turnLeft();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			this.turnRight();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			this.moveUp();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_F))
		{
			this.moveDown();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_H))
		{
			this.moveHome();
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_O))
		{
			this.projection = projectionType.ORTHOGRAPHIC;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_P))
		{
			this.projection = projectionType.PERSPECTIVE;
		}
	}

	// This method is the one that actually draws to the screen.
	@Override
	public void render()
	{
		//Clear Screen
		glClear(GL_COLOR_BUFFER_BIT);
		
		glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        switch(this.projection)
        {
        	case PERSPECTIVE:
        		gluPerspective(60.0f, (float) LWJGLSandbox.DISPLAY_WIDTH / LWJGLSandbox.DISPLAY_HEIGHT, 1.0f, 100.0f);
        		break;
        	case ORTHOGRAPHIC:
        		glOrtho(-6.0f, 6.0f, -6.0f, 6.0f, 1.0f, 100.0f);
        		break;
        }
        
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        glTranslatef(position.x, position.y, position.z);
        
        this.draw();
	}
	
	/***********************************************************************************************************/
	
	/*
	 * Draws the house model
	 */
	private void draw()
	{
		
//		glPushMatrix();
//		glColor3f(100, 100, 100);
//		glTranslatef(10, 0,0);
//		glPushMatrix();
//		glColor3f(10, 100, 10);
//		glTranslatef(10, 10,0);
//		glPushMatrix();
//		glColor3f(200, 10, 200);
//		glTranslatef(10, 10,10);
//		
//		for(int j=0;j<3;j++)
//		{   
//			glTranslatef(j*10, 0,0);
//			
			glBegin(GL_LINES);
			Iterator<Line3D> i = model.getLines();
			int count = 1;
			while (i.hasNext())
			{
				Line3D line = i.next();
				glVertex3d(line.start.x, line.start.y, line.start.z);
				glColor3f(0, (float)Math.pow(2.0f,4*count)*0.6f, (float)Math.pow(count,3*count)*0.9f);
				glVertex3d(line.end.x, line.end.y, line.end.z);
				glColor3f(9*count*0.8f, (float)Math.pow(count,3)*0.5f, (float)Math.pow(2.0f,count)*0.7f);
				count++;
			}
//			glPopMatrix();
			glEnd();
//		}
		
	}
	
	/***********************************************************************************************************/
	
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
	
	/***********************************************************************************************************/

}
