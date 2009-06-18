package org.dbfacade.testlink.tc.autoexec.server;


import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;


public class ExecutionRunner
{
	public static final String P_REPORT_RESULTS_AFTER_TEST = "-tlReportflag";
	public static final String P_DEFAULT_PROJECT_NAME = "-tlProject";
	public static final String P_DEV_KEY = "-tlDevKey";
	public static final String P_TESTLINK_URL = "-tlURL";
	public static final String P_TEST_CASE_CREATION_USER = "-tlUser";
	public static final String P_DEFAULT_TESTPLAN_PREP_CLASS = "-tlPrepClass";
	public static final String P_OPTIONAL_EXTERNAL_CONFIG_FILE = "-tlExternalPath";
	public static final String P_PORT = "-tlPort";

	public static void main(
		String[] args)
	{
		try {
				
			// Get args
			Map argMap = getArgs(args);
			printArgs(args, argMap);
			printClasspath();
				
		} catch ( Exception e ) {
			e.printStackTrace();
			System.out.println("The launch failed due to an exception.");
		} 
	}
	
		
	public static void printArgs(
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

	public static Map getArgs(
		String[] args)
	{
		Map argMap = new HashMap();
		argMap.put(P_REPORT_RESULTS_AFTER_TEST, false);
		try {
			for ( int i = 0; i < args.length; i++ ) {
				if ( args[i].equals(P_DEFAULT_PROJECT_NAME) ) {
					i++;
					argMap.put(P_DEFAULT_PROJECT_NAME, args[i]);
				}
				if ( args[i].equals(P_DEFAULT_TESTPLAN_PREP_CLASS) ) {
					i++;
					argMap.put(P_DEFAULT_TESTPLAN_PREP_CLASS, args[i]);
				}
				if ( args[i].equals(P_DEV_KEY) ) {
					i++;
					argMap.put(P_DEV_KEY, args[i]);
				}
				if ( args[i].equals(P_TESTLINK_URL) ) {
					i++;
					argMap.put(P_TESTLINK_URL, args[i]);
				}
				if ( args[i].equals(P_REPORT_RESULTS_AFTER_TEST) ) {
					i++;
					argMap.put(P_REPORT_RESULTS_AFTER_TEST, flag(args[i]));
				}
				if ( args[i].equals(P_TEST_CASE_CREATION_USER) ) {
					i++;
					argMap.put(P_TEST_CASE_CREATION_USER, args[i]);
				}
				if ( args[i].equals(P_PORT) ) {
					i++;
					argMap.put(P_PORT, args[i]);
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		return argMap;
	}
	
	/*
	 * Private methods
	 */
		
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
		
	public static void printClasspath()
	{
		 
		// Get the System Classloader
		ClassLoader sysClassLoader = ClassLoader.getSystemClassLoader();

		// Get the URLs
		URL[] urls = ((URLClassLoader) sysClassLoader).getURLs();

		for ( int i = 0; i < urls.length; i++ ) {
			System.out.println(urls[i].getFile());
		}       

	}
}
