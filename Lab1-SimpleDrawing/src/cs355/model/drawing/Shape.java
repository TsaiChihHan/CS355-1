package cs355.model.drawing;

import java.awt.Color;

/**
 * This is the base class for all of your shapes.
 * Make sure they all extend this class.
 */
public abstract class Shape {

	// The color of this shape.
	protected Color color;
	
	public enum type {
		CIRCLE, ELLIPSE, LINE, RECTANGLE, SQUARE, TRIANGLE
	}
	
	private type shapeType;

	/**
	 * Basic constructor that sets the field.
	 * @param color the color for this new shape.
	 */
	public Shape(Color color) {
		this.color = color;
	}

	/**
	 * Getter for this shape's color.
	 * @return the color of this shape.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Setter for this shape's color
	 * @param color the new color for the shape.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	public type getShapeType()
	{
		return shapeType;
	}

	public void setShapeType(type shapeType)
	{
		this.shapeType = shapeType;
	}
}
