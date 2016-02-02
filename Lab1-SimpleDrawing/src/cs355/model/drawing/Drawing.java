package cs355.model.drawing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Color;

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
	
	//notifies observer (view) that the model has changed
	public void updateView()
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
