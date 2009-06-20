package org.dbfacade.testlink.eclipse.plugin.launcher;


import java.util.HashMap;
import java.util.Map;

import org.dbfacade.testlink.eclipse.plugin.Activator;
import org.dbfacade.testlink.eclipse.plugin.preferences.PreferenceConstants;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ProjectTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.TreeNode;
import org.dbfacade.testlink.eclipse.plugin.views.tree.TreeParentNode;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * The static method is responsible for showing the TestLink view
 * at launch and adding the project that is being requested by the
 * launcher to the tree with and open port.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkShowViewAtLaunch
{
	public static final String VIEW_ID = "org.dbfacade.testlink.eclipse.plugin.views.TestLinkView";

	public static void show(final VMRunnerConfiguration runConfig) throws CoreException
	{
		try {
			Display display = Display.getDefault();
			display.syncExec(
				new Runnable()
			{
				public void run()
				{     
					final IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
					if ( workbenchWindow != null ) {
						IWorkbenchPage page = workbenchWindow.getActivePage();
						try {
							page.showView(VIEW_ID);
						} catch ( Exception e ) {
							e.printStackTrace();
						}
						
						String[] args = runConfig.getProgramArguments();
						Map argMap = getArgs(args);
						String projectName = (String) argMap.get(PreferenceConstants.P_DEFAULT_PROJECT_NAME);
						String portNum = (String) argMap.get(PreferenceConstants.P_PORT);

						
						// Add project
						TreeParentNode invisibleRoot = TestLinkView.testLinkTree.getInvisibleRoot();
						ProjectTree visibleRoot = null;
						TreeNode[] nodes = invisibleRoot.getChildren();
						TreeNode tree = null;
						boolean replaceChild = false;
						for (int i=0; i < nodes.length; i++) {
							ProjectTree tmp = (ProjectTree) nodes[i];
							if ( tmp.getProject().getProjectName().equals(projectName) ) {
								tree = tmp;
								replaceChild=true;
								break;
							}
						}
						
						// Setup the preferences for the tree (Exception #2)
						TestLinkPreferences prefs = new TestLinkPreferences();
						prefs.setAlternateStore(argMap);
						
						// Add the project to the tree
						int port = new Integer(portNum).intValue();
						if ( replaceChild) {
							invisibleRoot.removeChild(tree);
							TestLinkView.testLinkTree.addProject(visibleRoot, prefs, projectName, port);
						} else {
							TestLinkView.testLinkTree.addProject(visibleRoot, prefs, projectName, port);
						}
						TestLinkView.refresh();
					}
				}  
			});

		} catch ( Exception e ) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.OK,
				"Could not open the TestLink view.", e); 
			throw new CoreException(status);
		}
	}
	

	public static Map getArgs(
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
					argMap.put(PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST, new Boolean(flag(args[i])));
				}
				if ( args[i].equals(PreferenceConstants.P_TEST_CASE_CREATION_USER) ) {
					i++;
					argMap.put(PreferenceConstants.P_TEST_CASE_CREATION_USER, args[i]);
				}
				if ( args[i].equals(PreferenceConstants.P_PORT) ) {
					i++;
					argMap.put(PreferenceConstants.P_PORT, args[i]);
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

