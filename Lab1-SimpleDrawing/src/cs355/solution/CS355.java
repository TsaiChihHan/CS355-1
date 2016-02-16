package cs355.solution;

import cs355.GUIFunctions;
import cs355.controller.Controller;

/**
 * This is the main class. The program starts here.
 * Make you add code below to initialize your model,
 * view, and controller and give them to the app.
 */
public class CS355 {

	/**
	 * This is where it starts.
	 * @param args = the command line arguments
	 */
	public static void main(String[] args) 
	{
		Controller c = Controller.instance();
		GUIFunctions.createCS355Frame(c, c.getView());

		GUIFunctions.setHScrollBarMax(2048);
		GUIFunctions.setVScrollBarMax(2048);
		
		GUIFunctions.setHScrollBarKnob(512);
		GUIFunctions.setVScrollBarKnob(512);

		GUIFunctions.setVScrollBarPosit(0);
		GUIFunctions.setHScrollBarPosit(0);

		GUIFunctions.refresh();
	}
}
