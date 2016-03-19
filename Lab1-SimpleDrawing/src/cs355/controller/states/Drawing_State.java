package cs355.controller.states;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.Iterator;

import cs355.controller.Controller;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Drawing;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class Drawing_State implements IControllerState {

	private ArrayList<Point2D.Double> trianglePoints;
	private int currentShapeIndex;
	private Point2D.Double mouseDragStart;
	private Shape.type shapeType;
	
	public Drawing_State(Shape.type shapeType)
	{
		this.trianglePoints = new ArrayList<Point2D.Double>();
		this.currentShapeIndex = -1;
		Drawing.instance().setCurrentShapeIndex(-1);
		this.mouseDragStart = null;
		this.shapeType = shapeType;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		Point2D.Double point = Controller.instance().viewPoint_worldPoint(e);
        
		if(this.shapeType.equals(Shape.type.TRIANGLE))
		{
			this.trianglePoints.add(new Point2D.Double(point.getX(), point.getY()));
			if (this.trianglePoints.size() == 3) //user placed final point needed to draw triangle
			{
				double centerX = (trianglePoints.get(0).getX() + trianglePoints.get(1).getX() + trianglePoints.get(2).getX())/3;
				double centerY = (trianglePoints.get(0).getY() + trianglePoints.get(1).getY() + trianglePoints.get(2).getY())/3;
				
				Drawing.instance().addShape(new Triangle(Drawing.instance().getCurrentColor(), new Point2D.Double(centerX,centerY), trianglePoints.get(0), trianglePoints.get(1), trianglePoints.get(2)));
				this.trianglePoints.clear();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		Point2D.Double point = Controller.instance().viewPoint_worldPoint(e);
        
		switch (this.shapeType) 
		{
			case CIRCLE:
				this.mouseDragStart = (Double) point.clone();
				this.currentShapeIndex = Drawing.instance().addShape(new Circle(Drawing.instance().getCurrentColor(), point, 0));
				break;
			case ELLIPSE:
				this.mouseDragStart = (Double) point.clone();
				this.currentShapeIndex = Drawing.instance().addShape(new Ellipse(Drawing.instance().getCurrentColor(), point, 0, 0));
				break;
			case LINE:
				this.mouseDragStart = (Double) point.clone();
				this.currentShapeIndex = Drawing.instance().addShape(new Line(Drawing.instance().getCurrentColor(), point, point));
				break;
			case RECTANGLE:
				this.mouseDragStart = (Double) point.clone();
				this.currentShapeIndex = Drawing.instance().addShape(new Rectangle(Drawing.instance().getCurrentColor(), point, 0, 0));
				break;
			case SQUARE:
				this.mouseDragStart = (Double) point.clone();
				this.currentShapeIndex = Drawing.instance().addShape(new Square(Drawing.instance().getCurrentColor(), point, 0));
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (this.currentShapeIndex!=-1) 
		{	
			switch (this.shapeType) 
			{
				case CIRCLE:
				case ELLIPSE:
				case LINE:
				case RECTANGLE:
				case SQUARE:
					this.currentShapeIndex=-1;
					this.mouseDragStart=null;
					break; //if we were manipulating  any of the above shapes, releasing mouse signals we are finished
				default:
					break;
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		Point2D.Double point = Controller.instance().viewPoint_worldPoint(e);
        
		if(this.currentShapeIndex != -1)
		{
			switch (Drawing.instance().getShape(this.currentShapeIndex).getShapeType()) 
			{
				case LINE:
					updateLine(point);
					break;
				case ELLIPSE:
					updateEllipse(mouseDragStart, point);
					break;
				case RECTANGLE:
					updateRectangle(mouseDragStart, point);
					break;
				case CIRCLE:
					updateCircle(mouseDragStart, point);
					break;
				case SQUARE:
					updateSquare(mouseDragStart, point);
					break;
				default:
					break;
			}
			Drawing.instance().updateView();
		}
	}
	
	@Override
	public void keyPressed(Iterator<Integer> iterator)
	{
		
	}

	@Override
	public stateType getType()
	{
		return IControllerState.stateType.DRAWING;
	}
	
	//*********************************************************************************************************************
	
	private void updateLine(Point2D.Double e)
	{
		Line l = (Line) Drawing.instance().getShape(this.currentShapeIndex);
		l.setEnd(new Point2D.Double((double)e.getX(), (double)e.getY()));
	}
	
	private void updateEllipse(Point2D.Double mouseDragStart, Point2D.Double e)
	{
		Ellipse el = (Ellipse) Drawing.instance().getShape(this.currentShapeIndex);
		
		double height = e.getY() - mouseDragStart.getY();
		double width =  e.getX() - mouseDragStart.getX();
		Point2D.Double center = new Point2D.Double(mouseDragStart.getX() + (width/2), mouseDragStart.getY() + (height/2));
		
		el.setHeight(Math.abs(height));
		el.setWidth(Math.abs(width));
		el.setCenter(center);
	}
	
	private void updateRectangle(Point2D.Double mouseDragStart, Point2D.Double e)
	{
		Rectangle r = (Rectangle) Drawing.instance().getShape(this.currentShapeIndex);
		
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
	
	private void updateCircle(Point2D.Double mouseDragStart, Point2D.Double e)
	{
		Circle c = (Circle) Drawing.instance().getShape(this.currentShapeIndex);
		
		double height = e.getY() - mouseDragStart.getY();
		double width =  e.getX() - mouseDragStart.getX();
		double radius = Math.min(Math.abs(width), Math.abs(height))/2;
		double x = mouseDragStart.getX() + Math.signum(width)*radius;
		double y = mouseDragStart.getY() + Math.signum(height)*radius;
		Point2D.Double center = new Point2D.Double(x,y);
		
		c.setRadius(radius);
		c.setCenter(center);
	}
	
	private void updateSquare(Point2D.Double mouseDragStart, Point2D.Double e)
	{
		Square s = (Square) Drawing.instance().getShape(this.currentShapeIndex);
		
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

}
