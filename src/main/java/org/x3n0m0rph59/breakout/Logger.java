package org.x3n0m0rph59.breakout;

public final class Logger {

	public static void log(String text, int verbosityLevel) {		
		if (Config.getInstance().isDebugging() || Config.getInstance().isVerbose()) {
			
			if (Config.getInstance().getVerbosityLevel() >= verbosityLevel)
				System.out.println(text);
		}
	}
}
