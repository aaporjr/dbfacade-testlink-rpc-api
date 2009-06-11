package org.dbfacade.testlink.eclipse.plugin.launcher;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dbfacade.testlink.eclipse.plugin.preferences.PreferenceConstants;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkMode;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;


/**
 * Has the same plugin.xml parameters for command hardcoded
 * into the runner.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkRunner extends ApplicationWindow
{
	public static final String COMMAND_ID = "testlink.eclipse.plugin.commands.testlinkview";
	public static final String COMMAND_NAME = "TestLink Test Plan Execution";
	public static final String CATEGORY_ID = "testlink.eclipse.plugin.commands.category";
	public static final String CATEGORY_NAME = "TestLink";
	
	public TestLinkRunner()
	{
		super(null);
	}
	
	protected Control createContents(
		Composite parent)
	{
		parent.setSize(400, 600);
		getShell().setText("TestLink Test Plan Execution Runner");
		// Create window contents
		TestLinkView testLinkView = new TestLinkView();
		testLinkView.createTreeView(parent);
		Tree tree = TestLinkView.viewer.getTree();
		return tree;
	}

	public static void main(
		String[] args)
	{
		try {
			
			// Create window
			TestLinkRunner runner = new TestLinkRunner();
			TestLinkMode.mode = TestLinkMode.APPLICATION_MODE;
			TestLinkPreferences.setAlternateStore(getArgs(args));
			
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

	private static Map getArgs(
		String[] args)
	{
		Map argMap = new HashMap();
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
             
}
