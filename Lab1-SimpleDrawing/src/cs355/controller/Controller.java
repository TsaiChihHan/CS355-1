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
	private mode currentMode = mode.NONE;
	private boolean rotating = false;
	private String movingLine = "";
	
	public enum mode {
		SHAPE, SELECT, ZOOM_IN, ZOOM_OUT, NONE
	}

	public Controller()
	{
		this.view = new View(); //creates view, which attaches itself as listener to model
		trianglePoints = new ArrayList<Point2D.Double>();
		currentShapeIndex = -1;
		mouseDragStart = null;
	}
	
	public void switchStates(mode m)
	{
		this.trianglePoints.clear();
		this.currentMode = m;
		Drawing.instance().setCurrentShapeIndex(-1);
		currentShapeIndex = -1;
		mouseDragStart = null;
		rotating = false;
		movingLine = "";
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if(currentMode.equals(mode.SHAPE))
		{
			if(Drawing.instance().getCurrentShape().equals(Shape.type.TRIANGLE))
			{
				this.trianglePoints.add(new Point2D.Double(e.getX(), e.getY()));
				if (this.trianglePoints.size() == 3) //user placed final point needed to draw triangle
				{
					double centerX = (trianglePoints.get(0).getX() + trianglePoints.get(1).getX() + trianglePoints.get(2).getX())/3;
					double centerY = (trianglePoints.get(0).getY() + trianglePoints.get(1).getY() + trianglePoints.get(2).getY())/3;
					
					Drawing.instance().addShape(new Triangle(Drawing.instance().getCurrentColor(), new Point2D.Double(centerX,centerY), trianglePoints.get(0), trianglePoints.get(1), trianglePoints.get(2)));
					trianglePoints.clear();
				}
			}
		}
		else if(currentMode.equals(mode.SELECT))
		{
			this.currentShapeIndex = Drawing.instance().selectShape(new Point2D.Double(e.getX(),e.getY()), 5);
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if(currentMode.equals(mode.SHAPE))
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
		else if(currentMode.equals(mode.SELECT) && currentShapeIndex != -1)
		{
			int x = e.getX();
			int y = e.getY();
			Point2D.Double point = new Point2D.Double((double)x, (double)y);
			if(Drawing.instance().getShape(currentShapeIndex).getShapeType().equals(Shape.type.LINE))
			{
				Line l = (Line)Drawing.instance().getShape(currentShapeIndex);
				
				double startDistance = Math.sqrt(Math.pow(l.getCenter().getX() - x, 2) + Math.pow(l.getCenter().getY() - y, 2));
				double endDistance = Math.sqrt(Math.pow(l.getEnd().getX() - x, 2) + Math.pow(l.getEnd().getY() - y, 2));
				if(6>=startDistance)
				{
					movingLine = "start";
				}
				else if(6>=endDistance)
				{
					movingLine = "end";
				}
				
			}
			else if(Drawing.instance().mousePressedInRotationHandle(point, 4))
			{
				rotating = true;
			}
			else if(Drawing.instance().mousePressedInSelectedShape(point, 4))
			{
				mouseDragStart = point;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(currentMode.equals(mode.SHAPE))
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
		else if(currentMode.equals(mode.SELECT) && currentShapeIndex != -1)
		{
			movingLine = "";
			rotating = false;
			this.mouseDragStart=null;
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
		if (currentMode.equals(mode.SHAPE) && this.currentShapeIndex != -1) //if we are currently manipulating a shape
		{
			Drawing.instance().updateShape(this.currentShapeIndex, this.mouseDragStart, e);	
		}
		else if(currentMode.equals(mode.SELECT) && currentShapeIndex != -1) //shape selected and being moved
		{
			if(movingLine.equals("start"))
			{
				Line l = (Line)Drawing.instance().getShape(currentShapeIndex);
				l.setCenter(new Point2D.Double((double)e.getX(), (double)e.getY()));
				Drawing.instance().updateView();
			}
			else if(movingLine.equals("end"))
			{
				Line l = (Line)Drawing.instance().getShape(currentShapeIndex);
				l.setEnd(new Point2D.Double((double)e.getX(), (double)e.getY()));
				Drawing.instance().updateView();
			}
			if(rotating)
			{
				Drawing.instance().rotateShape(this.currentShapeIndex, e);
			}
			else if(mouseDragStart != null)
			{
				Drawing.instance().moveShape(this.currentShapeIndex, this.mouseDragStart, e);
				this.mouseDragStart = new Point2D.Double((double)e.getX(), (double)e.getY());
			}
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
		switchStates(mode.SHAPE);
	}

	@Override
	public void squareButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.SQUARE);
		switchStates(mode.SHAPE);
	}

	@Override
	public void rectangleButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.RECTANGLE);
		switchStates(mode.SHAPE);
	}

	@Override
	public void circleButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.CIRCLE);
		switchStates(mode.SHAPE);
	}

	@Override
	public void ellipseButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.ELLIPSE);
		switchStates(mode.SHAPE);
	}

	@Override
	public void triangleButtonHit()
	{
		Drawing.instance().setCurrentShape(Shape.type.TRIANGLE);
		switchStates(mode.SHAPE);
	}

	@Override
	public void selectButtonHit()
	{
		trianglePoints.clear();
		switchStates(mode.SELECT);
	}

	@Override
	public void zoomInButtonHit()
	{
		trianglePoints.clear();
		switchStates(mode.ZOOM_IN);
	}

	@Override
	public void zoomOutButtonHit()
	{
		trianglePoints.clear();
		switchStates(mode.ZOOM_OUT);
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
		switchStates(mode.NONE);
		Drawing.instance().open(file);
	}

	@Override
	public void doDeleteShape()
	{
		if(this.currentShapeIndex != -1)
		{
			Drawing.instance().deleteShape(this.currentShapeIndex);
			this.currentShapeIndex = -1;
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
		if(this.currentShapeIndex != -1)
		{
			Drawing.instance().moveForward(this.currentShapeIndex);
			this.currentShapeIndex = Drawing.instance().getCurrentShapeIndex();
		}
	}

	@Override
	public void doMoveBackward()
	{
		if(this.currentShapeIndex != -1)
		{
			Drawing.instance().moveBackward(this.currentShapeIndex);
			this.currentShapeIndex = Drawing.instance().getCurrentShapeIndex();
		}
	}

	@Override
	public void doSendToFront()
	{
		if(this.currentShapeIndex != -1)
		{
			Drawing.instance().moveToFront(this.currentShapeIndex);
			this.currentShapeIndex = Drawing.instance().getCurrentShapeIndex();
		}
	}

	@Override
	public void doSendtoBack()
	{
		if(this.currentShapeIndex != -1)
		{
			Drawing.instance().movetoBack(this.currentShapeIndex);
			this.currentShapeIndex = Drawing.instance().getCurrentShapeIndex();
		}
	}

	public ViewRefresher getView()
	{
		return view;
	}

	public mode getCurrentMode()
	{
		return currentMode;
	}

	public void setCurrentMode(mode currentMode)
	{
		this.currentMode = currentMode;
	}

}
