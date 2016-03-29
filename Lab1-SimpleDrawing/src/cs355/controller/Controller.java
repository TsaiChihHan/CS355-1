package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.File;
import java.util.Iterator;

import cs355.GUIFunctions;
import cs355.controller.states.Camera_State;
import cs355.controller.states.Drawing_State;
import cs355.controller.states.IControllerState;
import cs355.controller.states.Nothing_State;
import cs355.controller.states.Select_State;
import cs355.model.drawing.*;
import cs355.model.image.Image;
import cs355.model.scene.CS355Scene;
import cs355.model.scene.Instance;
import cs355.model.scene.Point3D;
import cs355.view.View;
import cs355.view.ViewRefresher;

public class Controller implements CS355Controller, MouseListener, MouseMotionListener  {
	
	private static Controller _instance; //this class is a singleton
	
	private View view;
	IControllerState state;
	private double zoom;
	private double knobSize;
	private Point2D.Double viewCenter;
	private boolean updating = false;
	public static final double ZOOMIN = 2.0;
	public static final double ZOOMOUT = .5;
	public static final double ZOOMMIN = .25;
	public static final double ZOOMMAX = 4.0;
	public boolean ThreeD = false;
	public boolean displayImage = false;
	public Camera_State cameraState = new Camera_State();
	public CS355Scene scene;
	private static final float nearPlane = 1.0f;
	private static final float farPlane = 1000.0f;
	public Image image;
	
	public static Controller instance()
	{
		if (_instance == null) 
			_instance = new Controller();
		return _instance;
	}
	
	private Controller()
	{
		this.view = new View(); //creates view, which attaches itself as listener to model
		this.state = new Nothing_State();
		this.zoom = 1.0;
		this.knobSize = 512;
		this.viewCenter = new Point2D.Double(0,0);
		CS355Scene.instance().camPos.x = -28;
		CS355Scene.instance().camPos.y = -25;
		CS355Scene.instance().camPos.z = 64;
		CS355Scene.instance().camRot = 180.0f;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		state.mouseClicked(e);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		state.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		state.mouseReleased(e);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		state.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void colorButtonHit(Color c)
	{
		if(state.getType().equals(IControllerState.stateType.SELECT))
		{
			((Select_State)state).changeShapeColor(c);
		}
		Drawing.instance().setCurrentColor(c);
		GUIFunctions.changeSelectedColor(c);
		GUIFunctions.refresh();
	}

	@Override
	public void lineButtonHit()
	{
		this.state = new Drawing_State(Shape.type.LINE);
	}

	@Override
	public void squareButtonHit()
	{
		this.state = new Drawing_State(Shape.type.SQUARE);
	}

	@Override
	public void rectangleButtonHit()
	{
		this.state = new Drawing_State(Shape.type.RECTANGLE);
	}

	@Override
	public void circleButtonHit()
	{
		this.state = new Drawing_State(Shape.type.CIRCLE);
	}

	@Override
	public void ellipseButtonHit()
	{
		this.state = new Drawing_State(Shape.type.ELLIPSE);
	}

	@Override
	public void triangleButtonHit()
	{
		this.state = new Drawing_State(Shape.type.TRIANGLE);
	}

	@Override
	public void selectButtonHit()
	{
		this.state = new Select_State();
	}

	@Override
	public void zoomInButtonHit()
	{
		this.handleZoom(ZOOMIN);
	}

	@Override
	public void zoomOutButtonHit()
	{
		this.handleZoom(ZOOMOUT);
	}

	@Override
	public void hScrollbarChanged(int value)
	{
		viewCenter.x = value + this.knobSize / 2.0;
		if(!this.updating) //prevents refreshing view too many times
			Drawing.instance().updateView();
	}

	@Override
	public void vScrollbarChanged(int value)
	{
		viewCenter.y = (value + this.knobSize / 2.0);
		if(!this.updating) //prevents refreshing view too many times
			Drawing.instance().updateView();
	}

	@Override
	public void openScene(File file)
	{
		CS355Scene.instance().open(file);
		this.state = new Camera_State();
	}

	@Override
	public void toggle3DModelDisplay()
	{
		if(!ThreeD)
		{
			ThreeD=!ThreeD;
//			this.state = new Camera_State();
		}
		else
		{
			ThreeD=!ThreeD;
//			this.state = new Nothing_State();
		}
		Drawing.instance().updateView();
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator)
	{
		if(ThreeD)
		{
			cameraState.keyPressed(iterator);
			Drawing.instance().updateView();
		}
	}

	@Override
	public void openImage(File file)
	{
		this.image = new Image();
		this.image.open(file);
		Drawing.instance().updateView();
	}

	@Override
	public void saveImage(File file)
	{
		if(this.image != null)
			this.image.save(file);
	}

	@Override
	public void toggleBackgroundDisplay()
	{
		displayImage = !displayImage;
		Drawing.instance().updateView();
	}

	@Override
	public void saveDrawing(File file)
	{
		Drawing.instance().save(file);
	}

	@Override
	public void openDrawing(File file)
	{
		Drawing.instance().setCurrentShapeIndex(-1);
		this.state = new Nothing_State();
		Drawing.instance().open(file);
	}

	@Override
	public void doDeleteShape()
	{
		if(state.getType().equals(IControllerState.stateType.SELECT))
		{
			int currentShapeIndex = ((Select_State)state).getCurrentShapeIndex();
			if(currentShapeIndex != -1)
			{
				Drawing.instance().deleteShape(currentShapeIndex);
				((Select_State)state).setCurrentShapeIndex(-1);
			}
		}
	}

	@Override
	public void doEdgeDetection()
	{
		if(this.image != null)
		{
			this.image.edgeDetection();
			Drawing.instance().updateView();
		}
	}

	@Override
	public void doSharpen()
	{
		if(this.image != null)
		{
			this.image.sharpen();
			Drawing.instance().updateView();
		}
	}

	@Override
	public void doMedianBlur()
	{
		if(this.image != null)
		{
			this.image.medianBlur();
			Drawing.instance().updateView();
		}
	}

	@Override
	public void doUniformBlur()
	{
		if(this.image != null)
		{
			this.image.uniformBlur();
			Drawing.instance().updateView();
		}
	}

	@Override
	public void doGrayscale()
	{
		if(this.image != null)
		{
			this.image.grayscale();
			Drawing.instance().updateView();
		}
	}

	@Override
	public void doChangeContrast(int contrastAmountNum)
	{
		if(this.image != null)
		{
			this.image.contrast(contrastAmountNum);
			Drawing.instance().updateView();
		}
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum)
	{
		if(this.image != null)
		{
			this.image.brightness(brightnessAmountNum);
			Drawing.instance().updateView();
		}
	}

	@Override
	public void doMoveForward()
	{
		if(state.getType().equals(IControllerState.stateType.SELECT))
		{
			int currentShapeIndex = ((Select_State)state).getCurrentShapeIndex();
			if(currentShapeIndex != -1)
			{
				Drawing.instance().moveForward(currentShapeIndex);
				((Select_State)state).setCurrentShapeIndex(Drawing.instance().getCurrentShapeIndex());
			}
		}
	}

	@Override
	public void doMoveBackward()
	{
		if(state.getType().equals(IControllerState.stateType.SELECT))
		{
			int currentShapeIndex = ((Select_State)state).getCurrentShapeIndex();
			if(currentShapeIndex != -1)
			{
				Drawing.instance().moveBackward(currentShapeIndex);
				((Select_State)state).setCurrentShapeIndex(Drawing.instance().getCurrentShapeIndex());
			}
		}
	}

	@Override
	public void doSendToFront()
	{
		if(state.getType().equals(IControllerState.stateType.SELECT))
		{
			int currentShapeIndex = ((Select_State)state).getCurrentShapeIndex();
			if(currentShapeIndex != -1)
			{
				Drawing.instance().moveToFront(currentShapeIndex);
				((Select_State)state).setCurrentShapeIndex(Drawing.instance().getCurrentShapeIndex());
			}
		}
	}

	@Override
	public void doSendtoBack()
	{
		if(state.getType().equals(IControllerState.stateType.SELECT))
		{
			int currentShapeIndex = ((Select_State)state).getCurrentShapeIndex();
			if(currentShapeIndex != -1)
			{
				Drawing.instance().movetoBack(currentShapeIndex);
				((Select_State)state).setCurrentShapeIndex(Drawing.instance().getCurrentShapeIndex());
			}
		}
	}

	public ViewRefresher getView()
	{
		return view;
	}
	
	public double getZoom()
	{
		return zoom;
	}
	
	public AffineTransform object_world_view(Shape s)
	{
		AffineTransform transform = new AffineTransform();
		
		// world to view
        transform.concatenate(new AffineTransform(zoom, 0, 0, zoom, 0, 0)); //s
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, -viewCenter.getX() + 256*(1/zoom), -viewCenter.getY() + 256*(1/zoom))); //t

		// object to world
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, s.getCenter().getX(), s.getCenter().getY())); //t
		transform.concatenate(new AffineTransform(Math.cos(s.getRotation()), Math.sin(s.getRotation()), -Math.sin(s.getRotation()), Math.cos(s.getRotation()), 0, 0)); //r
		
		return transform;
	}
	
	public AffineTransform view_world_object(Shape s)
	{
		AffineTransform transform = new AffineTransform();

		// world to object
		transform.concatenate(new AffineTransform(Math.cos(s.getRotation()), -Math.sin(s.getRotation()), Math.sin(s.getRotation()), Math.cos(s.getRotation()), 0.0, 0.0)); //r
		transform.concatenate(new AffineTransform(1.0, 0.0, 0.0, 1.0, -s.getCenter().getX(), -s.getCenter().getY())); //t
		
		// view to world
        transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, -(-viewCenter.getX() + 256*(1/zoom)), -(-viewCenter.getY() + 256*(1/zoom)))); //t
        transform.concatenate(new AffineTransform(1/zoom, 0, 0, 1/zoom, 0, 0)); //s
		
		return transform;
	}
	
	public Point2D.Double viewPoint_worldPoint(MouseEvent e)
	{
		Point2D.Double point = new Point2D.Double(e.getX(), e.getY());
		return viewPoint_worldPoint(point);
	}
	
	public Point2D.Double viewPoint_worldPoint(Point2D.Double point)
	{
		Point2D.Double pointCopy = (Double) point.clone();
		AffineTransform transform = new AffineTransform();
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, viewCenter.getX() - 256*(1/zoom), viewCenter.getY() - 256*(1/zoom))); //t
        transform.concatenate(new AffineTransform(1/zoom, 0, 0, 1/zoom, 0, 0)); //s
        transform.transform(pointCopy, pointCopy); //transform pt to object coordinates
        return pointCopy;
	}
	
	public Point2D.Double objectPoint_viewPoint(Shape s, Point2D.Double point)
	{
		Point2D.Double pointCopy = (Double) point.clone();
		AffineTransform transform = object_world_view(s);
        transform.transform(pointCopy, pointCopy); //transform pt to object coordinates
        return pointCopy;
	}
	
	public Point2D.Double worldPoint_viewPoint(Point2D.Double point)
	{
		Point2D.Double pointCopy = (Double) point.clone();
		AffineTransform transform = new AffineTransform();
		transform.concatenate(new AffineTransform(zoom, 0, 0, zoom, 0, 0)); //s
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, -viewCenter.getX() + 256*(1/zoom), -viewCenter.getY() + 256*(1/zoom))); //t
        transform.transform(pointCopy, pointCopy); //transform pt to object coordinates
        return pointCopy;
	}
	
	public AffineTransform world_view()
	{
		AffineTransform transform = new AffineTransform();
        transform.concatenate(new AffineTransform(zoom, 0, 0, zoom, 0, 0));
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, -viewCenter.getX() + 256*(1/zoom), -viewCenter.getY() + 256*(1/zoom)));
		return transform;
	}
	
	private void handleZoom(double zoomChange)
	{
		double prevKnobSize = 512/zoom;
		
		//set zoom
		zoom = zoom * zoomChange;
		if(zoom < ZOOMMIN) zoom = ZOOMMIN;
		if(zoom > ZOOMMAX) zoom = ZOOMMAX;

		//set scroll bar size
		this.knobSize = (int) (512/zoom);

		//calculate the new top left of the view
		Point2D.Double newTopLeft = new Point2D.Double(viewCenter.x - this.knobSize/2, viewCenter.y - this.knobSize/2);
		if(newTopLeft.x < 0) newTopLeft.x = 0;
		if(newTopLeft.y < 0) newTopLeft.y = 0;
		if(newTopLeft.x + this.knobSize > 2048) newTopLeft.x = 2048 - this.knobSize;
		if(newTopLeft.y + this.knobSize > 2048) newTopLeft.y = 2048 - this.knobSize;
		
		this.updating = true; //prevents refreshing view too many times
		
		//change scroll bar sizes and positions
		if(prevKnobSize == 2048) //if we WERE fully zoomed out, we need to adjust the scroll bar size before moving scroll bars
		{
			GUIFunctions.setHScrollBarKnob((int) this.knobSize);
			GUIFunctions.setVScrollBarKnob((int) this.knobSize);
		}
		GUIFunctions.setHScrollBarPosit((int) newTopLeft.x);
		GUIFunctions.setVScrollBarPosit((int) newTopLeft.y);
		GUIFunctions.setHScrollBarKnob((int) this.knobSize);
		GUIFunctions.setVScrollBarKnob((int) this.knobSize);

		//update view
		GUIFunctions.setZoomText(zoom);
		Drawing.instance().updateView();
		
		this.updating = false;
	}
	
	public double[] camera_clip(Point3D point, Instance instance)
	{
		float theta = (float) Math.toRadians(CS355Scene.instance().getCameraRotation());
		double cameraX = CS355Scene.instance().getCameraPosition().x + instance.getPosition().x;
		double cameraY = CS355Scene.instance().getCameraPosition().y + instance.getPosition().y;
		double cameraZ = CS355Scene.instance().getCameraPosition().z + instance.getPosition().z;
		double e = (farPlane + nearPlane) / (farPlane - nearPlane);
		double f = (-2 * nearPlane * farPlane) / (farPlane - nearPlane);

		//World to camera translate
		double x1 = point.x - cameraX;
		double y1 = point.y - cameraY;
		double z1 = point.z - cameraZ;
		
		//World to camera rotate
		double x2 = x1 * Math.cos(theta) + z1 * Math.sin(theta);
		double z2 = -x1 * Math.sin(theta) + z1 * Math.cos(theta);

		//Camera to Clip
		double x = x2 * Math.sqrt(3) + Math.sqrt(3);
		double y = y1 * Math.sqrt(3);
		double z = f + e * z2;		
		double bigW = (-cameraZ * Math.cos(theta) + point.z * Math.cos(theta) + cameraX * Math.sin(theta) - point.x * Math.sin(theta));

		double[] result = {x, y, z, bigW};

		return result;
	}

	public Point3D clip_screen(Point3D point)
	{
		
		double x = 1024 + (1024 * point.x);
		double y = 1024 - (1024 * point.y);
		return new Point3D(x, y, 1);
	}

	public boolean clipTest(double[] start, double[] end)
	{
		double startX = start[0];
		double startY = start[1];
		double startZ = start[2];
		double startW = start[3];

		double endX = end[0];
		double endY = end[1];
		double endZ = end[2];
		double endW = end[3];

		if ((startX > startW && endX > endW) || (startX < -startW && endX < -endW)) return true;

		if ((startY > startW && endY > endW) || (startY < -startW && endY < -endW)) return true;

		if ((startZ > startW && endZ > endW)) return true;

		if (startZ <= -startW || endZ <= -endW) return true;

		return false;
	}
	
	public AffineTransform objectToWorld3D(Instance inst) {
		AffineTransform transform = new AffineTransform();
		//Translation
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, inst.getPosition().x, inst.getPosition().y));
		//Rotation
		transform.concatenate(new AffineTransform(Math.cos(inst.getRotAngle()), Math.sin(inst.getRotAngle()), -Math.sin(inst.getRotAngle()), Math.cos(inst.getRotAngle()), 0, 0));
		return transform;
	}
	
	public AffineTransform objectToView3D(Instance inst) {
		AffineTransform transform = new AffineTransform();
		// World to View
        transform.concatenate(world_view());
		// Object to World
		transform.concatenate(objectToWorld3D(inst));
		return transform;
	}

	public IControllerState getState()
	{
		return this.state;
	}
}
