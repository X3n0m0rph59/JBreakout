package org.x3n0m0rph59.breakout;


public class App 
{	
	private static MainWindow mainWindow;
	
    public static void main( String[] args )
    {
    	mainWindow = new MainWindow();    	
    	mainWindow.show();
    }

	public static MainWindow getMainWindow() {
		return mainWindow;
	}
}
