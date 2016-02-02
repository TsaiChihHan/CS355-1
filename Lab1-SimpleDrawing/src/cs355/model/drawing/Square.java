package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Add your square code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Square extends Shape {

	// The size of this Square.
	private double size;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param upperLeft the upper left corner of the new shape.
	 * @param size the size of the new shape.
	 */
	public Square(Color color, Point2D.Double center, double size) {

		// Initialize the superclass.
		super(color, center);
		super.setShapeType(Shape.type.SQUARE);

		// Set fields.
		this.size = size;
	}

	/**
	 * Getter for this Square's size.
	 * @return the size as a double.
	 */
	public double getSize() {
		return size;
	}

	/**
	 * Setter for this Square's size.
	 * @param size the new size.
	 */
	public void setSize(double size) {
		this.size = size;
	}

	@Override
	public boolean pointInShape(Double pt, double tolerance)
	{
		//check bounding box before transforming
		double boundingHeight = (size * Math.abs(Math.sin(rotation))) + (size * Math.abs(Math.cos(rotation)));
		double boundingWidth = boundingHeight;
		double boundingX1 = center.getX() - boundingWidth/2;
		double boundingX2 = center.getX() + boundingWidth/2;
		double boundingY1 = center.getY() - boundingHeight/2;
		double boundingY2 = center.getY() + boundingHeight/2;
		if(!((boundingX1<=pt.getX() && pt.getX()<=boundingX2) && (boundingY1<=pt.getY() && pt.getY()<=boundingY2)))
			return false;
				
		AffineTransform worldToObj = new AffineTransform();
		worldToObj.rotate(-rotation);
		worldToObj.translate(-center.getX(),-center.getY());
		worldToObj.transform(pt, pt); //transform pt to object coordinates
		
		double limit = (this.size/2); //how far point can be from center of square
		double px = pt.getX();
		double py = pt.getY();
		
		return ((-limit<=px && px<=limit) && (-limit<=py && py<=limit));
	}
}
