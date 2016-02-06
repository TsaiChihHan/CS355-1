package cs355.model.drawing;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

/**
 * Add your line code here. You can add fields, but you cannot
 * change the ones that already exist. This includes the names!
 */
public class Line extends Shape {

	// The ending point of the line.
	private Point2D.Double end;

	/**
	 * Basic constructor that sets all fields.
	 * @param color the color for the new shape.
	 * @param start the starting point.
	 * @param end the ending point.
	 */
	public Line(Color color, Point2D.Double start, Point2D.Double end) {

		// Initialize the superclass.
		super(color, start);
		super.setShapeType(Shape.type.LINE);

		// Set fields.
		this.end = end;
	}

	/**
	 * Getter for this Line's ending point.
	 * @return the ending point as a Java point.
	 */
	public Point2D.Double getEnd() {
		return end;
	}

	/**
	 * Setter for this Line's ending point.
	 * @param end the new ending point for the Line.
	 */
	public void setEnd(Point2D.Double end) {
		this.end = end;
	}

	@Override
	public boolean pointInShape(Double pt, double tolerance)
	{
		double x0 = pt.getX(); 
		double y0 = pt.getY(); 
		
		double x1 = center.getX(); 
		double y1 = center.getY();
		
		double x2 = end.getX(); 
		double y2 = end.getY();
		
		double distance = Math.abs(((y2-y1)*x0) - ((x2-x1)*y0) + (x2*y1) - (y2*x1))/Math.sqrt(Math.pow(y2-y1, 2)+Math.pow(x2-x1, 2));

		double slope = (y1-y2)/(x2-x1);
		double tangentSlope = -1/slope;
		double degree = Math.atan(tangentSlope);
		
		y0 += Math.sin(degree);
		x0 += Math.cos(degree);
		
		if(distance <= tolerance && (x0 <= (Math.max(x1, x2)+tolerance) && x0 >= (Math.min(x1, x2)-tolerance) && y0 <= (Math.max(y1, y2)+tolerance) && y0 >= (Math.min(y1, y2)-tolerance)))
		{
			return true;
		}
		return false;
	}
}
