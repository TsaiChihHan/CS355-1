package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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
	
	private View view;
	IControllerState state;

	public Controller()
	{
		this.view = new View(); //creates view, which attaches itself as listener to model
		state = new Nothing_State();
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
		//TODO
	}

	@Override
	public void zoomOutButtonHit()
	{
		//TODO
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
}
