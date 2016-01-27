package cs355.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.model.drawing.*;

public class View implements ViewRefresher {

	public View()
	{
		Drawing.instance().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg)
	{
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d)
	{
		GUIFunctions.changeSelectedColor(Drawing.instance().getCurrentColor());
		ArrayList<Shape> shapes = (ArrayList<Shape>) Drawing.instance().getShapes();
		int selectedShapeIndex = Drawing.instance().getCurrentShapeIndex();
		
		for (int i=0;i<shapes.size();i++) 
		{
			Shape shape = shapes.get(i);
			
			g2d.setColor(shape.getColor()); //set color of shape we are going to draw
			AffineTransform objToWorld = new AffineTransform();
			objToWorld.translate(shape.getCenter().getX(),shape.getCenter().getY());
			objToWorld.rotate(shape.getRotation());
			g2d.setTransform(objToWorld);
			
			switch (shape.getShapeType()) 
			{
				case LINE:
					this.drawLine(g2d, (Line)shape, false);
					break;
				case ELLIPSE:
					this.drawEllipse(g2d, (Ellipse)shape, false);
					break;
				case RECTANGLE:
					this.drawRectangle(g2d, (Rectangle)shape, false);
					break;
				case CIRCLE:
					this.drawCircle(g2d, (Circle)shape, false);
					break;
				case SQUARE:
					this.drawSquare(g2d, (Square)shape, false);
					break;
				case TRIANGLE:
					this.drawTriangle(g2d, (Triangle)shape, false);
					break;
				default:
					break;
			}
		}
		if(selectedShapeIndex != -1)
		{
			Shape shape = shapes.get(selectedShapeIndex);
			
			g2d.setColor(Color.red);
			AffineTransform objToWorld = new AffineTransform();
			objToWorld.translate(shape.getCenter().getX(),shape.getCenter().getY());
			objToWorld.rotate(shape.getRotation());
			g2d.setTransform(objToWorld);
			
			switch (shape.getShapeType()) 
			{
				case LINE:
					this.drawLine(g2d, (Line)shape, true);
					break;
				case ELLIPSE:
					this.drawEllipse(g2d, (Ellipse)shape, true);
					break;
				case RECTANGLE:
					this.drawRectangle(g2d, (Rectangle)shape, true);
					break;
				case CIRCLE:
					this.drawCircle(g2d, (Circle)shape, true);
					break;
				case SQUARE:
					this.drawSquare(g2d, (Square)shape, true);
					break;
				case TRIANGLE:
					this.drawTriangle(g2d, (Triangle)shape, true);
					break;
				default:
					break;
			}
		}
	}
	
	private void drawCircle(Graphics2D g2d, Circle c, boolean selected) 
	{
		double radius = c.getRadius();
		int width = (int) (2*radius);
		int height = width;
		
		if(selected)
		{
			g2d.drawRect(-width/2,-height/2,width,height);
			//TODO draw drag handle
		}
		else
		{
			g2d.fillOval(-width/2, -height/2, width, height);
		}
	}

	private void drawEllipse(Graphics2D g2d, Ellipse e, boolean selected) 
	{
		int height = (int)e.getHeight();
		int width = (int)e.getWidth();
		
		if(selected)
		{
			g2d.drawRect(-width/2,-height/2,width,height);
			//TODO draw drag handle
		}
		else
		{
			g2d.fillOval(-width/2, -height/2, width, height);
		}
	}
	
	private void drawLine(Graphics2D g2d, Line l, boolean selected) 
	{
		int x1 = (int)l.getCenter().getX();
		int y1 = (int)l.getCenter().getY();
		int x2 = (int)l.getEnd().getX();
		int y2 = (int)l.getEnd().getY();
		
		g2d.drawLine(0, 0, x2-x1, y2-y1);
	}
	
	private void drawRectangle(Graphics2D g2d, Rectangle r, boolean selected) 
	{
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		
		if(selected)
		{
			g2d.drawRect(-width/2,-height/2,width,height);
			//TODO draw drag handle
		}
		else
		{
			g2d.fillRect(-width/2,-height/2,width,height);
		}
	}
	
	private void drawSquare(Graphics2D g2d, Square s, boolean selected) 
	{
		int width = (int) s.getSize();
		int height = width;
		
		if(selected)
		{
			g2d.drawRect(-width/2,-height/2,width,height);
			//TODO draw drag handle
		}
		else
		{
			g2d.fillRect(-width/2,-height/2,width,height);
		}
	}
	
	private void drawTriangle(Graphics2D g2d, Triangle t, boolean selected) 
	{
		int xa = (int)(t.getA().getX()-t.getCenter().getX());
		int xb = (int)(t.getB().getX()-t.getCenter().getX());
		int xc = (int)(t.getC().getX()-t.getCenter().getX());
		
		int ya = (int)(t.getA().getY()-t.getCenter().getY());
		int yb = (int)(t.getB().getY()-t.getCenter().getY());
		int yc = (int)(t.getC().getY()-t.getCenter().getY());
		
		int xCoordinates[] = {xa, xb, xc};
		int yCoordinates[] = {ya, yb, yc};
		
		if(selected)
		{
			g2d.drawPolygon(xCoordinates, yCoordinates, 3);
			//TODO draw drag handle
		}
		else
		{
			g2d.fillPolygon(xCoordinates, yCoordinates, 3);
		}
	}
}
