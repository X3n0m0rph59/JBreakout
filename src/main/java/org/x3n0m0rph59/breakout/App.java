package org.x3n0m0rph59.breakout;

//import java.io.File;

/**
 * The main class of JBreakout which holds the program entrypoint
 * @author user
 */
public class App 
{	
	/** The one and only main window */
	private static MainWindow mainWindow;
	
	/**
	 * Program entrypoint
	 * @param args Command line arguments
	 */
    public static void main(String[] args)
    {
//    	System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
    	
    	Config.getInstance().parseCommandLine(args);
    	
    	mainWindow = new MainWindow();    	
    	mainWindow.show();	
    }

    /**
	 * Returns the one and only main window
	 * @return Reference to the main Window
	 */
	public static MainWindow getMainWindow() {
		return mainWindow;
	}
}
