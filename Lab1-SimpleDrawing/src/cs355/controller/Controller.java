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
import cs355.controller.states.Drawing_State;
import cs355.controller.states.IControllerState;
import cs355.controller.states.Nothing_State;
import cs355.controller.states.Select_State;
import cs355.model.drawing.*;
import cs355.view.View;
import cs355.view.ViewRefresher;

public class Controller implements CS355Controller, MouseListener, MouseMotionListener  {
	
	private static Controller _instance; //this class is a singleton
	
	private View view;
	IControllerState state;
	private double zoom;
	private double knobSize;
	private Point2D.Double viewCenter;
	
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
		viewCenter = new Point2D.Double(256,256);
//		GUIFunctions.refresh();
//		this.refreshScroll();
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
		System.out.println(-256 + 256*(1/zoom));
		if(this.zoom > 2.00)
			return;
		this.zoom = this.zoom * 2.0;
		this.knobSize = 512 / zoom;
		this.refreshScroll();
	}

	@Override
	public void zoomOutButtonHit()
	{
		System.out.println(-256 + 256*(1/zoom));
		if(this.zoom < 0.5)
			return;
		this.zoom = this.zoom / 2.0;
		this.knobSize = 512 / zoom;
		this.refreshScroll();
	}

	@Override
	public void hScrollbarChanged(int value)
	{
		viewCenter.x = (value + this.knobSize / 2.0);
		Drawing.instance().updateView();
	}

	@Override
	public void vScrollbarChanged(int value)
	{
		viewCenter.y = (value + this.knobSize / 2.0);
		Drawing.instance().updateView();
	}
	
	private void refreshScroll()
	{
		GUIFunctions.setVScrollBarKnob((int) this.knobSize);
		GUIFunctions.setHScrollBarKnob((int) this.knobSize);
		
        GUIFunctions.setVScrollBarPosit((int) viewCenter.getY());
        GUIFunctions.setHScrollBarPosit((int) viewCenter.getX());
        
        GUIFunctions.setVScrollBarKnob((int) this.knobSize);
		GUIFunctions.setHScrollBarKnob((int) this.knobSize);
		
		Drawing.instance().updateView();
	}

	@Override
	public void openScene(File file)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void toggle3DModelDisplay()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(Iterator<Integer> iterator)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void openImage(File file)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void saveImage(File file)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void toggleBackgroundDisplay()
	{
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	@Override
	public void doSharpen()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doMedianBlur()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doUniformBlur()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doGrayscale()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doChangeContrast(int contrastAmountNum)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum)
	{
		// TODO Auto-generated method stub
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
		
        AffineTransform translation = new AffineTransform(1.0, 0, 0, 1.0, s.getCenter().getX(), s.getCenter().getY());
        AffineTransform rotation = new AffineTransform(Math.cos(s.getRotation()), Math.sin(s.getRotation()), -Math.sin(s.getRotation()), Math.cos(s.getRotation()), 0, 0);


		// World to View
        transform.concatenate(new AffineTransform(zoom, 0, 0, zoom, 0, 0)); //scale
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, -viewCenter.getX() + 256*(1/zoom), -viewCenter.getY() + 256*(1/zoom))); //t

		// Object to World
		transform.concatenate(translation);
		transform.concatenate(rotation);
		
		return transform;
	}
	
	public AffineTransform view_world_object(Shape s)
	{
		AffineTransform transform = new AffineTransform();
		
        AffineTransform translation = new AffineTransform(1.0, 0.0, 0.0, 1.0, -s.getCenter().getX(), -s.getCenter().getY());

        AffineTransform rotation = new AffineTransform(Math.cos(s.getRotation()), -Math.sin(s.getRotation()), Math.sin(s.getRotation()), Math.cos(s.getRotation()), 0.0, 0.0);


		// World to object
		transform.concatenate(rotation);
		transform.concatenate(translation);
		
		// View to world
        transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, -(-viewCenter.getX() + 256*(1/zoom)), -(-viewCenter.getY() + 256*(1/zoom)))); //t
        transform.concatenate(new AffineTransform(1/zoom, 0, 0, 1/zoom, 0, 0)); //scale
		
		return transform;
	}
	
	public Point2D.Double viewPoint_worldPoint(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		Point2D.Double point = new Point2D.Double((double)x, (double)y);
		return viewPoint_worldPoint(point);
	}
	
	public Point2D.Double viewPoint_worldPoint(Point2D.Double point)
	{
		Point2D.Double pointCopy = (Double) point.clone();
		AffineTransform transform = new AffineTransform();
		transform.concatenate(new AffineTransform(1.0, 0, 0, 1.0, -viewCenter.getX() + 256*(1/zoom), -(-viewCenter.getY() + 256*(1/zoom)))); //t
        transform.concatenate(new AffineTransform(1/zoom, 0, 0, 1/zoom, 0, 0));
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
}
