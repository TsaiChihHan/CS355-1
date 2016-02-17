package cs355.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.Controller;
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
			
			 //object->world->view
			if(!shape.getShapeType().equals(Shape.type.LINE))
			{
				g2d.setTransform(Controller.instance().object_world_view(shape));
			}
			
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
			double zoom = Controller.instance().getZoom();
			
			//draw circle handle
			g2d.setTransform(new AffineTransform()); //make sure sure no transforms are goin on
			Point2D.Double point = new Point2D.Double(0, -(height/2) - (12/zoom));
			point = Controller.instance().objectPoint_viewPoint(c, point);
			g2d.drawOval((int)(point.getX()-6), (int)(point.getY()-6), 11, 11); //center
			
			//draw bounding box
			g2d.setTransform(Controller.instance().object_world_view(c)); //object->world->view
			g2d.setStroke(new BasicStroke((float) (1/zoom)));
			g2d.drawRect(-width/2,-height/2,width,height);
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
			double zoom = Controller.instance().getZoom();
			
			//draw circle handle
			g2d.setTransform(new AffineTransform()); //make sure sure no transforms are goin on
			Point2D.Double point = new Point2D.Double(0, -(height/2) - (12/zoom));
			point = Controller.instance().objectPoint_viewPoint(e, point);
			g2d.drawOval((int)(point.getX()-6), (int)(point.getY()-6), 11, 11); //center
			
			//draw bounding box
			g2d.setTransform(Controller.instance().object_world_view(e)); //object->world->view
			g2d.setStroke(new BasicStroke((float) (1/zoom)));
			g2d.drawRect(-width/2,-height/2,width,height);
		}
		else
		{
			g2d.fillOval(-width/2, -height/2, width, height);
		}
	}
	
	private void drawLine(Graphics2D g2d, Line l, boolean selected) 
	{
		g2d.setTransform(new AffineTransform()); //make sure sure no transforms are goin on
		
		Point2D.Double start = Controller.instance().worldPoint_viewPoint(l.getCenter());
		Point2D.Double end = Controller.instance().worldPoint_viewPoint(l.getEnd());
		int x1 = (int)start.getX();
		int y1 = (int)start.getY();
		int x2 = (int)end.getX();
		int y2 = (int)end.getY();
		
		if(selected)
		{
			g2d.drawOval(x1-6, y1-6, 11, 11); //center
			g2d.drawOval(x2-6, y2-6, 11, 11); //end
		}
		else
		{
			g2d.drawLine(x1, y1, x2, y2);
		}
	}
	
	private void drawRectangle(Graphics2D g2d, Rectangle r, boolean selected) 
	{
		int width = (int) r.getWidth();
		int height = (int) r.getHeight();
		
		if(selected)
		{
			double zoom = Controller.instance().getZoom();
			
			//draw circle handle
			g2d.setTransform(new AffineTransform()); //make sure sure no transforms are goin on
			Point2D.Double point = new Point2D.Double(0, -(height/2) - (12/zoom));
			point = Controller.instance().objectPoint_viewPoint(r, point);
			g2d.drawOval((int)(point.getX()-6), (int)(point.getY()-6), 11, 11); //center
			
			//draw bounding box
			g2d.setTransform(Controller.instance().object_world_view(r)); //object->world->view
			g2d.setStroke(new BasicStroke((float) (1/zoom)));
			g2d.drawRect(-width/2,-height/2,width,height);
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
			double zoom = Controller.instance().getZoom();
			
			//draw circle handle
			g2d.setTransform(new AffineTransform()); //make sure sure no transforms are goin on
			Point2D.Double point = new Point2D.Double(0, -(height/2) - (12/zoom));
			point = Controller.instance().objectPoint_viewPoint(s, point);
			g2d.drawOval((int)(point.getX()-6), (int)(point.getY()-6), 11, 11); //center
			
			//draw bounding box
			g2d.setTransform(Controller.instance().object_world_view(s)); //object->world->view
			g2d.setStroke(new BasicStroke((float) (1/zoom)));
			g2d.drawRect(-width/2,-height/2,width,height);
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
			double zoom = Controller.instance().getZoom();
			
			//draw circle handle
			g2d.setTransform(new AffineTransform()); //make sure sure no transforms are goin on
			Point2D.Double point = new Point2D.Double();
			
			if(ya <= yb && ya <= yc)
			{
				point.x = xa;
				point.y = ya - (12/zoom);
			}
			else if(yb <= ya && yb <= yc)
			{
				point.x = xb;
				point.y = yb - (12/zoom);
			}
			else if(yc <= yb && yc <= ya)
			{
				point.x = xc;
				point.y = yc - (12/zoom);
			}
			
			point = Controller.instance().objectPoint_viewPoint(t, point);
			g2d.drawOval((int)(point.getX()-6), (int)(point.getY()-6), 11, 11); //center
			
			//draw bounding box
			g2d.setTransform(Controller.instance().object_world_view(t)); //object->world->view
			g2d.setStroke(new BasicStroke((float) (1/zoom)));
			g2d.drawPolygon(xCoordinates, yCoordinates, 3);
		}
		else
		{
			g2d.fillPolygon(xCoordinates, yCoordinates, 3);
		}
	}
}
