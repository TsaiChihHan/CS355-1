package cs355.view;

import java.awt.Graphics2D;
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
		for (Shape shape : shapes) 
		{
			g2d.setColor(shape.getColor()); //set color of shape we are going to draw
			switch (shape.getShapeType()) 
			{
				case LINE:
					this.drawLine(g2d, (Line)shape);
					break;
				case ELLIPSE:
					this.drawEllipse(g2d, (Ellipse)shape);
					break;
				case RECTANGLE:
					this.drawRectangle(g2d, (Rectangle)shape);
					break;
				case CIRCLE:
					this.drawCircle(g2d, (Circle)shape);
					break;
				case SQUARE:
					this.drawSquare(g2d, (Square)shape);
					break;
				case TRIANGLE:
					this.drawTriangle(g2d, (Triangle)shape);
					break;
				default:
					break;
			}
		}
	}
	
	private void drawCircle(Graphics2D g2d, Circle c) 
	{
		double radius = c.getRadius();
		
		int x = (int) (c.getCenter().getX() - radius);
		int y = (int) (c.getCenter().getY() - radius);
		int width = (int) (2*radius);
		int height = width;
		
		g2d.fillOval(x, y, width, height);
	}

	private void drawEllipse(Graphics2D g2d, Ellipse e) 
	{
		int height = (int)e.getHeight();
		int width = (int)e.getWidth();
		int x = (int) (e.getCenter().getX() - (width / 2));
		int y = (int) (e.getCenter().getY() - (height / 2));
		
		g2d.fillOval(x, y, width, height);
	}
	
	private void drawLine(Graphics2D g2d, Line l) 
	{
		int x1 = (int)l.getStart().getX();
		int y1 = (int)l.getStart().getY();
		int x2 = (int)l.getEnd().getX();
		int y2 = (int)l.getEnd().getY();
		
		g2d.drawLine(x1, y1, x2, y2);
	}
	
	private void drawRectangle(Graphics2D g2d, Rectangle r) 
	{
		int x = (int) r.getUpperLeft().getX();
		int y = (int) r.getUpperLeft().getY();
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		
		g2d.fillRect(x, y, width, height);
	}
	
	private void drawSquare(Graphics2D g2d, Square s) 
	{
		int x = (int) s.getUpperLeft().getX();
		int y = (int) s.getUpperLeft().getY();
		int width = (int) s.getSize();
		int height = width;
		
		g2d.fillRect(x, y, width, height);
	}
	
	private void drawTriangle(Graphics2D g2d, Triangle t) 
	{
		int x_coordinates[] = {(int)t.getA().getX(), (int)t.getB().getX(), (int)t.getC().getX()};
		int y_coordinates[] = {(int)t.getA().getY(), (int)t.getB().getY(), (int)t.getC().getY()};
		
		g2d.fillPolygon(x_coordinates, y_coordinates, 3);
	}

}
