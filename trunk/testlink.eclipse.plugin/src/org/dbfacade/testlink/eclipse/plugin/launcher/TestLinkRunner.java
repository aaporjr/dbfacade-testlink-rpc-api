package org.dbfacade.testlink.eclipse.plugin.launcher;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.dbfacade.testlink.eclipse.plugin.preferences.PreferenceConstants;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkMode;
import org.eclipse.swt.widgets.Display;


/**
 * Has the same plugin.xml parameters for command hardcoded
 * into the runner.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkRunner
{
	
	public static void main(
		String[] args)
	{
		try {
			
			// Get args
			Map argMap = getArgs(args);
			printArgs(args, argMap);
			printClasspath();
			
			// Create window
			TestLinkRunnerWindow runner = new TestLinkRunnerWindow();
			TestLinkMode.mode = TestLinkMode.APPLICATION_MODE;
			TestLinkPreferences.setAlternateStore(argMap);
			
			// Display window
			runner.setBlockOnOpen(true);
			runner.open();
			Display.getCurrent().dispose();
			
		} catch ( Exception e ) {
			e.printStackTrace();
			System.out.println("The launch failed due to an exception.");
		} 
	}
	
	/*
	 * Private methods
	 */
	
	private static void printArgs(
		String[] args,
		Map argMap)
	{
		System.out.print("[");
		for ( int i = 0; i < args.length; i++ ) {
			System.out.print(args[i] + ", ");
		}
		System.out.print("]\n");
		System.out.println("\n" + argMap);
	}

	private static Map getArgs(
		String[] args)
	{
		Map argMap = new HashMap();
		argMap.put(PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST, false);
		try {
			for ( int i = 0; i < args.length; i++ ) {
				if ( args[i].equals(PreferenceConstants.P_DEFAULT_PROJECT_NAME) ) {
					i++;
					argMap.put(PreferenceConstants.P_DEFAULT_PROJECT_NAME, args[i]);
				}
				if ( args[i].equals(PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS) ) {
					i++;
					argMap.put(PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS, args[i]);
				}
				if ( args[i].equals(PreferenceConstants.P_DEV_KEY) ) {
					i++;
					argMap.put(PreferenceConstants.P_DEV_KEY, args[i]);
				}
				if ( args[i].equals(PreferenceConstants.P_TESTLINK_URL) ) {
					i++;
					argMap.put(PreferenceConstants.P_TESTLINK_URL, args[i]);
				}
				if ( args[i].equals(PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST) ) {
					i++;
					argMap.put(PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST,
						flag(args[i]));
				}
				if ( args[i].equals(PreferenceConstants.P_TEST_CASE_CREATION_USER) ) {
					i++;
					argMap.put(PreferenceConstants.P_TEST_CASE_CREATION_USER, args[i]);
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return argMap;
	}
	
	private static boolean flag(
		String arg)
	{
		if ( arg != null ) {
			if ( arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("Yes")
				|| arg.equalsIgnoreCase("Y") ) {
				return true;
			}
		}
		return false;
	}
	
	public static void printClasspath() {
	 
		// Get the System Classloader
		ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

		// Get the URLs
		URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();

		for ( int i = 0; i < urls.length; i++ ) {
			System.out.println(urls[i].getFile());
		}       

	}
} 
             
