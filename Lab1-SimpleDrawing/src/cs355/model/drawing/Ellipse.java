package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Add your ellipse code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Ellipse extends Shape {

	// The center of this shape.

	// The width of this shape.
	private double width;

	// The height of this shape.
	private double height;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param center the center of the new shape.
	 * @param width the width of the new shape.
	 * @param height the height of the new shape.
	 */
	public Ellipse(Color color, Point2D.Double center, double width, double height) {

		// Initialize the superclass.
		super(color, center);
		super.setShapeType(Shape.type.ELLIPSE);

		// Set fields.
		this.width = width;
		this.height = height;
	}

	/**
	 * Getter for this shape's width.
	 * @return this shape's width as a double.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Setter for this shape's width.
	 * @param width the new width.
	 */
	public void setWidth(double width) {
		this.width = width;
	}

	/**
	 * Getter for this shape's height.
	 * @return this shape's height as a double.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Setter for this shape's height.
	 * @param height the new height.
	 */
	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public boolean pointInShape(Double pt, double tolerance)
	{
		//check bounding box before transforming
		double boundingHeight = (width * Math.abs(Math.sin(rotation))) + (height * Math.abs(Math.cos(rotation)));
		double boundingWidth = (width * Math.abs(Math.cos(rotation))) + (height * Math.abs(Math.sin(rotation)));
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
		
		return (Math.pow((pt.getX()), 2) / Math.pow((width/2), 2)) + (Math.pow((pt.getY()), 2) / Math.pow((height/2), 2)) <= 1;
	}
}
