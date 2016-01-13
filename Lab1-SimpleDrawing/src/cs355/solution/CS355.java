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
		Controller c = new Controller();
		GUIFunctions.createCS355Frame(c, c.getView());

		GUIFunctions.refresh();
	}
}
