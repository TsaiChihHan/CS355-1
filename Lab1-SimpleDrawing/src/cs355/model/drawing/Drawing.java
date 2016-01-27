package cs355.model.drawing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class Drawing extends CS355Drawing {
	
	private static Drawing _instance; //this class is a singleton
	
	private List<Shape> shapes;
	private Shape.type currentShape;
	private int currentShapeIndex;
	private Color currentColor;

	public static Drawing instance()
	{
		if (_instance == null) 
			_instance = new Drawing();
		return _instance;
	}

	private Drawing()
	{
		shapes = new ArrayList<Shape>();
		currentShape = null;
		currentColor = Color.WHITE;
		currentShapeIndex = -1;
	}
	
	public int selectShape(Point2D.Double point, double tolerance)
	{
		for(int i=shapes.size()-1;i>=0;i--)
		{
			Shape shape = shapes.get(i);
			Point2D.Double pointCopy = (Double) point.clone();
			if(shape.pointInShape(pointCopy, tolerance))
			{
				currentShapeIndex = i;
				this.setCurrentColor(shape.getColor());
				updateView();
				return i;
			}
		}
		currentShapeIndex = -1;
		updateView();
		return currentShapeIndex;
	}
	
	//checks if a mouse press occurred in the same boundaries of the selected shape
	public boolean mousePressedInSelectedShape(Point2D.Double point, double tolerance)
	{
		for(int i=shapes.size()-1;i>=0;i--)
		{
			Shape shape = shapes.get(i);
			Point2D.Double pointCopy = (Double) point.clone();
			if(shape.pointInShape(pointCopy, tolerance))
			{
				return (i == currentShapeIndex);
			}
		}
		return false;
	}
	
	public void moveShape(int index, Double mouseDragStart, MouseEvent e)
	{ 
		Shape shape = shapes.get(index);
		
		double xDiff = e.getX()-mouseDragStart.getX();
		double yDiff = e.getY()-mouseDragStart.getY();
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
		
		updateView();
	}
	
	//Updates shape as user modifies it
	public void updateShape(int index, Point2D.Double mouseDragStart, MouseEvent e) 
	{
		switch (this.shapes.get(index).getShapeType()) 
		{
			case LINE:
				updateLine(index, e);
				break;
			case ELLIPSE:
				updateEllipse(index, mouseDragStart, e);
				break;
			case RECTANGLE:
				updateRectangle(index, mouseDragStart, e);
				break;
			case CIRCLE:
				updateCircle(index, mouseDragStart, e);
				break;
			case SQUARE:
				updateSquare(index, mouseDragStart, e);
				break;
			default:
				break;
		}
		updateView();
	}
	
	private void updateLine(int index, MouseEvent e)
	{
		Line l = (Line) shapes.get(index);
		l.setEnd(new Point2D.Double((double)e.getX(), (double)e.getY()));
	}
	
	private void updateEllipse(int index, Point2D.Double mouseDragStart, MouseEvent e)
	{
		Ellipse el = (Ellipse) shapes.get(index);
		
		double height = e.getY() - mouseDragStart.getY();
		double width =  e.getX() - mouseDragStart.getX();
		Point2D.Double center = new Point2D.Double(mouseDragStart.getX() + (width/2), mouseDragStart.getY() + (height/2));
		
		el.setHeight(Math.abs(height));
		el.setWidth(Math.abs(width));
		el.setCenter(center);
	}
	
	private void updateRectangle(int index, Point2D.Double mouseDragStart, MouseEvent e)
	{
		Rectangle r = (Rectangle) shapes.get(index);
		
		double height = e.getY() - mouseDragStart.getY();
		double width =  e.getX() - mouseDragStart.getX();
		double x = mouseDragStart.getX();
		double y = mouseDragStart.getY();
		
		if(width < 0)
			x += width;
		if(height < 0)
			y += height;
		
		double centerX = x+Math.abs(width/2);
		double centerY = y+Math.abs(height/2);
		
		Point2D.Double center = new Point2D.Double(centerX, centerY);
	
		r.setHeight(Math.abs(height));
		r.setWidth(Math.abs(width));
		
		r.setCenter(center);
	}
	
	private void updateCircle(int index, Point2D.Double mouseDragStart, MouseEvent e)
	{
		Circle c = (Circle) shapes.get(index);
		
		double height = e.getY() - mouseDragStart.getY();
		double width =  e.getX() - mouseDragStart.getX();
		double radius = Math.min(Math.abs(width), Math.abs(height))/2;
		double x = mouseDragStart.getX() + Math.signum(width)*radius;
		double y = mouseDragStart.getY() + Math.signum(height)*radius;
		Point2D.Double center = new Point2D.Double(x,y);
		
		c.setRadius(radius);
		c.setCenter(center);
	}
	
	private void updateSquare(int index, Point2D.Double mouseDragStart, MouseEvent e)
	{
		Square s = (Square) shapes.get(index);
		
		double height = e.getY() - mouseDragStart.getY();
		double width =  e.getX() - mouseDragStart.getX();
		double x = mouseDragStart.getX();
		double y = mouseDragStart.getY();
	
		if(width < 0)
			x -= Math.min(Math.abs(width),Math.abs(height));
		if(height < 0)
			y -= Math.min(Math.abs(width),Math.abs(height));
		
		s.setSize(Math.min(Math.abs(width),Math.abs(height)));
		
		double centerX = x+Math.abs(s.getSize()/2);
		double centerY = y+Math.abs(s.getSize()/2);
		
		Point2D.Double center = new Point2D.Double(centerX, centerY);
		
		s.setCenter(center);
	}
	
	//notifies observer (view) that the model has changed
	private void updateView()
	{
		this.setChanged();
		this.notifyObservers();
	}

	@Override
	public Shape getShape(int index)
	{
		return shapes.get(index);
	}

	@Override
	public int addShape(Shape s)
	{
		shapes.add(s);
		updateView();
		return shapes.size()-1;
	}

	@Override
	public void deleteShape(int index)
	{
		shapes.remove(index);
		currentShapeIndex = -1;
		updateView();
	}
	
	@Override
	public void moveToFront(int index)
	{
		if(shapes.size() <= index)
			return;
		
		Shape s = shapes.get(index);
		shapes.remove(index);
		currentShapeIndex = addShape(s);
		updateView();
	}

	@Override
	public void movetoBack(int index)
	{
		if(index == 0 || shapes.size() <= index)
			return;
		
		Shape s = shapes.get(index);
		shapes.remove(index);
		shapes.add(0, s);
		currentShapeIndex = 0;
		updateView();
	}

	@Override
	public void moveForward(int index)
	{
		if(shapes.size()-1 <= index)
			return;
		
		Shape s = shapes.get(index);
		shapes.remove(index);
		shapes.add(index+1, s);
		currentShapeIndex = index+1;
		updateView();
	}

	@Override
	public void moveBackward(int index)
	{
		if(index == 0 || shapes.size() <= index)
			return;
		
		Shape s = shapes.get(index);
		shapes.remove(index);
		shapes.add(index-1, s);
		currentShapeIndex = index-1;
		updateView();
	}

	@Override
	public List<Shape> getShapes()
	{
		return shapes;
	}

	@Override
	public List<Shape> getShapesReversed()
	{
		ArrayList<Shape> clone = new ArrayList<Shape>(shapes);
		Collections.reverse(clone);
		return clone;
	}

	@Override
	public void setShapes(List<Shape> shapes)
	{
		this.shapes = shapes;
	}

	public Shape.type getCurrentShape()
	{
		return currentShape;
	}

	public void setCurrentShape(Shape.type currentShape)
	{
		this.currentShape = currentShape;
	}

	public Color getCurrentColor()
	{
		return currentColor;
	}

	public void setCurrentColor(Color currentColor)
	{
		this.currentColor = currentColor;
		updateView();
	}

	public int getCurrentShapeIndex()
	{
		return currentShapeIndex;
	}

	public void setCurrentShapeIndex(int currentShapeIndex)
	{
		this.currentShapeIndex = currentShapeIndex;
		updateView();
	}
}
