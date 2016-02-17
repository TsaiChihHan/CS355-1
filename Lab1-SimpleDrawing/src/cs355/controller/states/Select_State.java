package cs355.controller.states;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import cs355.controller.Controller;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Drawing;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class Select_State implements IControllerState {

	private static final int TOLERANCE = 4;
	private int currentShapeIndex;
	private Point2D.Double mouseDragStart;
	private linePoint lineHandleGrabbed;
	private boolean rotating;
	
	public enum linePoint {
		START, END, NAH
	}
	
	public Select_State()
	{
		this.currentShapeIndex = -1;
		Drawing.instance().setCurrentShapeIndex(-1);
		this.mouseDragStart = null;
		this.rotating = false;
		this.lineHandleGrabbed = linePoint.NAH;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		return;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		Point2D.Double point = new Point2D.Double((double)x, (double)y);
		
		//check drag handles
		if(this.currentShapeIndex != -1)
		{
			if(Drawing.instance().getShape(this.currentShapeIndex).getShapeType().equals(Shape.type.LINE))
			{
				Line l = (Line)Drawing.instance().getShape(currentShapeIndex);
				Point2D.Double start = Controller.instance().worldPoint_viewPoint(l.getCenter());
				Point2D.Double end = Controller.instance().worldPoint_viewPoint(l.getEnd());
				
				double startDistance = Math.sqrt(Math.pow(start.getX() - x, 2) + Math.pow(start.getY() - y, 2));
				double endDistance = Math.sqrt(Math.pow(end.getX() - x, 2) + Math.pow(end.getY() - y, 2));
				
				if(6>=startDistance)
				{
					this.lineHandleGrabbed = linePoint.START;
					return;
				}
				else if(6>=endDistance)
				{
					this.lineHandleGrabbed = linePoint.END;
					return;
				}
				else if(this.mousePressedInSelectedShape(point))
				{
					this.mouseDragStart = point;
					return;
				}
			}
			else if(this.mousePressedInRotationHandle(point))
			{
				this.rotating = true;
				return;
			}
		}
		
		this.checkShapeSelected(e);
		if(this.currentShapeIndex != -1 && this.mousePressedInSelectedShape(point))
		{
			if(Drawing.instance().getShape(this.currentShapeIndex).getShapeType().equals(Shape.type.LINE))
			{
				Line l = (Line)Drawing.instance().getShape(currentShapeIndex);
				double startDistance = Math.sqrt(Math.pow(l.getCenter().getX() - x, 2) + Math.pow(l.getCenter().getY() - y, 2));
				double endDistance = Math.sqrt(Math.pow(l.getEnd().getX() - x, 2) + Math.pow(l.getEnd().getY() - y, 2));
				
				if(6>=startDistance)
				{
					this.lineHandleGrabbed = linePoint.START;
					return;
				}
				else if(6>=endDistance)
				{
					this.lineHandleGrabbed = linePoint.END;
					return;
				}
				else if(this.mousePressedInSelectedShape(point))
				{
					this.mouseDragStart = point;
					return;
				}
			}
			else
			{
				this.mouseDragStart = point;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		this.lineHandleGrabbed = linePoint.NAH;
		this.rotating = false;
		this.mouseDragStart = null;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		if(this.lineHandleGrabbed.equals(linePoint.START))
		{
			Point2D.Double point2 = Controller.instance().viewPoint_worldPoint(e);
			Line l = (Line)Drawing.instance().getShape(this.currentShapeIndex);
			l.setCenter(new Point2D.Double(point2.getX(), point2.getY()));
			Drawing.instance().updateView();
		}
		else if(this.lineHandleGrabbed.equals(linePoint.END))
		{
			Point2D.Double point2 = Controller.instance().viewPoint_worldPoint(e);
			Line l = (Line)Drawing.instance().getShape(this.currentShapeIndex);
			l.setEnd(new Point2D.Double(point2.getX(), point2.getY()));
			Drawing.instance().updateView();
		}
		if(rotating)
		{
			this.rotateShape(e);
		}
		else if(mouseDragStart != null)
		{
			this.moveShape(this.mouseDragStart, e);
			this.mouseDragStart = new Point2D.Double((double)e.getX(), (double)e.getY());
		}
	}
	
	@Override
	public stateType getType()
	{
		return IControllerState.stateType.SELECT;
	}

	public void changeShapeColor(Color c)
	{
		if(this.currentShapeIndex != -1)
		{
			Shape shape = Drawing.instance().getShape(this.currentShapeIndex);
			shape.setColor(c);
		}
	}

	public int getCurrentShapeIndex()
	{
		return currentShapeIndex;
	}

	public void setCurrentShapeIndex(int currentShapeIndex)
	{
		this.currentShapeIndex = currentShapeIndex;
	}
	
	//*********************************************************************************************************************
	
	private void checkShapeSelected(MouseEvent e)
	{ 
		int x = e.getX();
		int y = e.getY();
		Point2D.Double point = new Point2D.Double((double)x, (double)y);
		this.currentShapeIndex = -1;
		
		ArrayList<Shape> shapes = (ArrayList<Shape>) Drawing.instance().getShapes();
		for(int i=shapes.size()-1;i>=0;i--)
		{
			Shape shape = shapes.get(i);
			Point2D.Double pointCopy = (Double) point.clone();
			
			if(shape.getShapeType() != Shape.type.LINE)
			{
				AffineTransform viewToObj = Controller.instance().view_world_object(shape);
				viewToObj.transform(pointCopy, pointCopy); //transform pt to object coordinates
			}
			
			if(shape.pointInShape(pointCopy, TOLERANCE)) //TODO
			{
				Drawing.instance().setCurrentColor(shape.getColor());
				this.currentShapeIndex = i;
				break;
			}
		}
		Drawing.instance().setCurrentShapeIndex(this.currentShapeIndex);
		Drawing.instance().updateView();
	}
	
	//moves the current selected shape according to how far the mouse has been dragged
	private void moveShape(Double mouseDragStart, MouseEvent e)
	{ 
		Shape shape = Drawing.instance().getShape(this.currentShapeIndex);
		
		double xDiff = (e.getX()-mouseDragStart.getX())/Controller.instance().getZoom();
		double yDiff = (e.getY()-mouseDragStart.getY())/Controller.instance().getZoom();
		double x = shape.getCenter().getX() + xDiff;
		double y = shape.getCenter().getY() + yDiff;
		Point2D.Double updatedCenter = new Point2D.Double((double)x, (double)y);
		
		if(shape.getShapeType().equals(Shape.type.LINE))
		{
			Line l = (Line) shape;
			double x2 = l.getEnd().getX() + xDiff;
			double y2 = l.getEnd().getY() + yDiff;
			Point2D.Double updatedEnd = new Point2D.Double((double)x2, (double)y2);
			l.setEnd(updatedEnd);
		}
		if(shape.getShapeType().equals(Shape.type.TRIANGLE))
		{
			Triangle t = (Triangle) shape;
			
			double xa = t.getA().getX() + xDiff;
			double xb = t.getB().getX() + xDiff;
			double xc = t.getC().getX() + xDiff;
			double ya = t.getA().getY() + yDiff;
			double yb = t.getB().getY() + yDiff;
			double yc = t.getC().getY() + yDiff;
			
			Point2D.Double updatedA = new Point2D.Double((double)xa, (double)ya);
			Point2D.Double updatedB = new Point2D.Double((double)xb, (double)yb);
			Point2D.Double updatedC = new Point2D.Double((double)xc, (double)yc);
			
			t.setA(updatedA);
			t.setB(updatedB);
			t.setC(updatedC);
			
			double centerX = (t.getA().getX() + t.getB().getX() + t.getC().getX())/3;
			double centerY = (t.getA().getY() + t.getB().getY() + t.getC().getY())/3;
			
			updatedCenter.x = centerX;
			updatedCenter.y = centerY;
		}
		
		shape.setCenter(updatedCenter);
		
		Drawing.instance().updateView();
	}
	
	//rotates the current selected shape according the position the mouse has been dragged to
	private void rotateShape(MouseEvent e)
	{ 
		Shape shape = Drawing.instance().getShape(this.currentShapeIndex);
		
		Point2D.Double point = Controller.instance().viewPoint_worldPoint(e);
		
		double xDiff = shape.getCenter().getX()-point.getX();
		double yDiff = shape.getCenter().getY()-point.getY();
		double angle = Math.atan2(yDiff, xDiff) - Math.PI / 2;
		shape.setRotation(angle % (2*Math.PI));
		Drawing.instance().updateView();
	}
	
	//checks if a mouse press occurred in the same boundaries of the selected shape
	private boolean mousePressedInSelectedShape(Point2D.Double point)
	{
		ArrayList<Shape> shapes = (ArrayList<Shape>) Drawing.instance().getShapes();
		Shape shape = shapes.get(currentShapeIndex);
		Point2D.Double pointCopy = (Double) point.clone();
		
		if(shape.getShapeType() != Shape.type.LINE)
		{
			AffineTransform viewToObj = Controller.instance().view_world_object(shape);
			viewToObj.transform(pointCopy, pointCopy); //transform pt to object coordinates
		}
		
		return shape.pointInShape(pointCopy, TOLERANCE);
	}
	
	
	//checks if a mouse press occurred in the rotation handle of the current selected shape
	private boolean mousePressedInRotationHandle(Point2D.Double point)
	{
		ArrayList<Shape> shapes = (ArrayList<Shape>)Drawing.instance().getShapes();
		Shape shape = shapes.get(currentShapeIndex);
		double height = -1;
		switch(shape.getShapeType())
		{
			case ELLIPSE:
				height = ((Ellipse)shape).getHeight();
				break;
			case RECTANGLE:
				height = ((Rectangle)shape).getHeight();
				break;
			case CIRCLE:
				height = 2*((Circle)shape).getRadius();
				break;
			case SQUARE:
				height = ((Square)shape).getSize();
				break;
			default:
				break;
		}
		if(height!=-1)
		{
			Point2D.Double handleCenter = new Point2D.Double(0, -(height/2) - (12/Controller.instance().getZoom()));
			handleCenter = Controller.instance().objectPoint_viewPoint(shape, handleCenter);
			
			double xDiff = handleCenter.getX() - point.getX();
			double yDiff = handleCenter.getY() - point.getY();
			
			double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			return (6>=distance);
		}
		if(shape.getShapeType().equals(Shape.type.TRIANGLE))
		{
			double zoom = Controller.instance().getZoom();
			Point2D.Double handleCenter = new Point2D.Double();
			
			Triangle t = (Triangle)shape;
			double xa = t.getA().getX()-t.getCenter().getX();
			double xb = t.getB().getX()-t.getCenter().getX();
			double xc = t.getC().getX()-t.getCenter().getX();
			
			double ya = t.getA().getY()-t.getCenter().getY();
			double yb = t.getB().getY()-t.getCenter().getY();
			double yc = t.getC().getY()-t.getCenter().getY();
			
			if(ya <= yb && ya <= yc)
			{
				handleCenter.x = xa;
				handleCenter.y = ya - (12/zoom);
			}
			else if(yb <= ya && yb <= yc)
			{
				handleCenter.x = xb;
				handleCenter.y = yb - (12/zoom);
			}
			else if(yc <= yb && yc <= ya)
			{
				handleCenter.x = xc;
				handleCenter.y = yc - (12/zoom);
			}
			
			handleCenter = Controller.instance().objectPoint_viewPoint(shape, handleCenter);
			
			double xDiff = handleCenter.getX() - point.getX();
			double yDiff = handleCenter.getY() - point.getY();
			
			double distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
			return (6>=distance);
		}
		return false;
	}
}
