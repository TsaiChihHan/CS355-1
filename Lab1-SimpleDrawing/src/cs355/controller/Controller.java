package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import cs355.GUIFunctions;
import cs355.model.drawing.*;
import cs355.view.View;
import cs355.view.ViewRefresher;

public class Controller implements CS355Controller, MouseListener, MouseMotionListener  {
	
	private View view;
	private ArrayList<Point2D.Double> trianglePoints;
	private int currentShapeIndex;
	private Point2D.Double mouseDragStart;

	public Controller()
	{
		this.view = new View(); //creates view, which attaches itself as listener to model
		trianglePoints = new ArrayList<Point2D.Double>();
		currentShapeIndex = -1;
		mouseDragStart = null;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(Drawing.instance().getCurrentShape().equals(Shape.type.TRIANGLE))
		{
			this.trianglePoints.add(new Point2D.Double(e.getX(), e.getY()));
			if (this.trianglePoints.size() == 3) //user placed final point needed to draw triangle
			{
				Drawing.instance().addShape(new Triangle(Drawing.instance().getCurrentColor(), trianglePoints.get(0), trianglePoints.get(1), trianglePoints.get(2)));
				trianglePoints.clear();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		Point2D.Double point = new Point2D.Double((double)x, (double)y);
		switch (Drawing.instance().getCurrentShape()) 
		{
			case CIRCLE:
				this.mouseDragStart = new Point2D.Double((double)x, (double)y);
				this.currentShapeIndex = Drawing.instance().addShape(new Circle(Drawing.instance().getCurrentColor(), point, 0));
				break;
			case ELLIPSE:
				this.mouseDragStart = new Point2D.Double((double)x, (double)y);
				this.currentShapeIndex = Drawing.instance().addShape(new Ellipse(Drawing.instance().getCurrentColor(), point, 0, 0));
				break;
			case LINE:
				this.mouseDragStart = new Point2D.Double((double)x, (double)y);
				this.currentShapeIndex = Drawing.instance().addShape(new Line(Drawing.instance().getCurrentColor(), point, point));
				break;
			case RECTANGLE:
				this.mouseDragStart = new Point2D.Double((double)x, (double)y);
				this.currentShapeIndex = Drawing.instance().addShape(new Rectangle(Drawing.instance().getCurrentColor(), point, 0, 0));
				break;
			case SQUARE:
				this.mouseDragStart = new Point2D.Double((double)x, (double)y);
				this.currentShapeIndex = Drawing.instance().addShape(new Square(Drawing.instance().getCurrentColor(), point, 0));
				break;
			default:
				break;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Shape.type currentShape = Drawing.instance().getCurrentShape();
		if (currentShape != null && this.currentShapeIndex!=-1) 
		{	
			switch (currentShape) 
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
		if (this.currentShapeIndex != -1) //if we are currently manipulating a shape
		{
			Drawing.instance().updateShape(this.currentShapeIndex, this.mouseDragStart, e);	
		}
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void colorButtonHit(Color c)
	{
		Drawing.instance().setCurrentColor(c);
		GUIFunctions.changeSelectedColor(c);
		GUIFunctions.refresh();
	}

	@Override
	public void lineButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.LINE);
		trianglePoints.clear();
	}

	@Override
	public void squareButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.SQUARE);
		trianglePoints.clear();
	}

	@Override
	public void rectangleButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.RECTANGLE);
		trianglePoints.clear();
	}

	@Override
	public void circleButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.CIRCLE);
		trianglePoints.clear();
	}

	@Override
	public void ellipseButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.ELLIPSE);
		trianglePoints.clear();
	}

	@Override
	public void triangleButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.TRIANGLE);
		trianglePoints.clear();
	}

	@Override
	public void selectButtonHit()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void zoomInButtonHit()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void zoomOutButtonHit()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void hScrollbarChanged(int value)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void vScrollbarChanged(int value)
	{
		// TODO Auto-generated method stub
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
		Drawing.instance().open(file);
	}

	@Override
	public void doDeleteShape()
	{
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	@Override
	public void doMoveBackward()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doSendToFront()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void doSendtoBack()
	{
		// TODO Auto-generated method stub
	}

	public ViewRefresher getView()
	{
		return view;
	}

}
