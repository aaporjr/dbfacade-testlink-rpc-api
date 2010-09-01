/*
 * Daniel R Padilla
 *
 * Copyright (c) 2009, Daniel R Padilla
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package testlink.api.java.client.tc.autoexec.server;


import java.util.HashMap;
import java.util.Map;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.tc.autoexec.TestPlanPrepare;
import testlink.api.java.client.tc.autoexec.annotation.handlers.LoadAnnotatedTestClass;

/**
 * The class in in progress and not yet developed.
 * 
 * @author Daniel Padilla
 *
 */
public class TestClassRunner
{
	public static final String P_REPORT_RESULTS_AFTER_TEST = "-tlReportflag";
	public static final String P_DEFAULT_PROJECT_NAME = "-tlProject";
	public static final String P_DEV_KEY = "-tlDevKey";
	public static final String P_TESTLINK_URL = "-tlURL";
	public static final String P_TEST_CASE_CREATION_USER = "-tlUser";
	public static final String P_TESTLINK_TEST_CLASS = "-tlTestClass";
	public static final String P_OPTIONAL_EXTERNAL_CONFIG_PATH = "-tlExternalPath";
	public static final String P_PORT = "-tlPort";


	public static void main(
		String[] args) throws Exception
	{
		try {
				
			// Get args
			Map argMap = getArgs(args);
			
			int port = new Integer((String) argMap.get(P_PORT)).intValue();
			String devKey = (String) argMap.get(P_DEV_KEY);
			String url = (String) argMap.get(P_TESTLINK_URL) + "/lib/api/xmlrpc.php";
			String testClass = (String) argMap.get(P_TESTLINK_TEST_CLASS);
			String defaultTestCaseUser = (String) argMap.get(P_TEST_CASE_CREATION_USER);
			String externalDir = (String) argMap.get(P_OPTIONAL_EXTERNAL_CONFIG_PATH);
			
			
			TestLinkAPIClient apiClient = new TestLinkAPIClient(devKey, url);
			TestPlanPrepare prep = new LoadAnnotatedTestClass(testClass);		
					
			ExecutionServer server = new ExecutionServer(
					port,
					apiClient,
					prep,
					defaultTestCaseUser,
					externalDir);
			
			server.start();
				
		} catch ( Exception e ) {
			e.printStackTrace();
			ExecutionProtocol.debug("The launch failed due to an exception.");
		} 
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
				if ( args[i].equals(P_TESTLINK_TEST_CLASS) ) {
					i++;
					argMap.put(P_TESTLINK_TEST_CLASS, args[i]);
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
}
