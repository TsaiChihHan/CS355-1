package cs355.view;

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
		ArrayList<Shape> shapes = (ArrayList<Shape>) Drawing.instance().getShapes();
		int selectedShapeIndex = Drawing.instance().getCurrentShapeIndex();
		
		for (int i=0;i<shapes.size();i++) 
		{
			Shape shape = shapes.get(i);
			g2d.setColor(shape.getColor()); //set color of shape we are going to draw
			AffineTransform objToWorld = new AffineTransform();
			objToWorld.translate(shape.getCenter().getX(),shape.getCenter().getY());
			// rotate to its orientation (first transformation)
			objToWorld.rotate(shape.getRotation());
			// set the drawing transformation
			g2d.setTransform(objToWorld);
			// and finally draw
			switch (shape.getShapeType()) 
			{
				case LINE:
					this.drawLine(g2d, (Line)shape, selectedShapeIndex==i);
					break;
				case ELLIPSE:
					this.drawEllipse(g2d, (Ellipse)shape, selectedShapeIndex==i);
					break;
				case RECTANGLE:
					this.drawRectangle(g2d, (Rectangle)shape, selectedShapeIndex==i);
					break;
				case CIRCLE:
					this.drawCircle(g2d, (Circle)shape, selectedShapeIndex==i);
					break;
				case SQUARE:
					this.drawSquare(g2d, (Square)shape, selectedShapeIndex==i);
					break;
				case TRIANGLE:
					this.drawTriangle(g2d, (Triangle)shape, selectedShapeIndex==i);
					break;
				default:
					break;
			}
		}
	}
	
	private void drawCircle(Graphics2D g2d, Circle c, boolean isSelected) 
	{
		double radius = c.getRadius();
		int width = (int) (2*radius);
		int height = width;
		
		g2d.fillOval(-width/2, -height/2, width, height);
	}

	private void drawEllipse(Graphics2D g2d, Ellipse e, boolean isSelected) 
	{
		int height = (int)e.getHeight();
		int width = (int)e.getWidth();
		
		g2d.fillOval(-width/2, -height/2, width, height);
	}
	
	private void drawLine(Graphics2D g2d, Line l, boolean isSelected) 
	{
		int x1 = (int)l.getCenter().getX();
		int y1 = (int)l.getCenter().getY();
		int x2 = (int)l.getEnd().getX();
		int y2 = (int)l.getEnd().getY();
		
		g2d.drawLine(0, 0, x2-x1, y2-y1);
	}
	
	private void drawRectangle(Graphics2D g2d, Rectangle r, boolean isSelected) 
	{
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		
		g2d.fillRect(-width/2,-height/2,width,height);
	}
	
	private void drawSquare(Graphics2D g2d, Square s, boolean isSelected) 
	{
		int width = (int) s.getSize();
		int height = width;
		
		g2d.fillRect(-width/2,-height/2,width,height);
	}
	
	private void drawTriangle(Graphics2D g2d, Triangle t, boolean isSelected) 
	{
		int xa = (int)(t.getA().getX()-t.getCenter().getX());
		int xb = (int)(t.getB().getX()-t.getCenter().getX());
		int xc = (int)(t.getC().getX()-t.getCenter().getX());
		
		int ya = (int)(t.getA().getY()-t.getCenter().getY());
		int yb = (int)(t.getB().getY()-t.getCenter().getY());
		int yc = (int)(t.getC().getY()-t.getCenter().getY());
		
		int xCoordinates[] = {xa, xb, xc};
		int yCoordinates[] = {ya, yb, yc};
		
		g2d.fillPolygon(xCoordinates, yCoordinates, 3);
	}
}
