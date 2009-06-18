package org.dbfacade.testlink.eclipse.plugin.launcher;


import java.util.Map;

import org.dbfacade.testlink.eclipse.plugin.Activator;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ProjectTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.TreeNode;
import org.dbfacade.testlink.eclipse.plugin.views.tree.TreeParentNode;
import org.dbfacade.testlink.tc.autoexec.server.ExecutionRunner;
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
						Map argMap = ExecutionRunner.getArgs(args);
						String projectName = (String) argMap.get(ExecutionRunner.P_DEFAULT_PROJECT_NAME);
						String port = (String) argMap.get(ExecutionRunner.P_PORT);

						
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
						if ( replaceChild) {
							invisibleRoot.removeChild(tree);
							TestLinkView.testLinkTree.addProject(visibleRoot, projectName);
						} else {
							TestLinkView.testLinkTree.addProject(visibleRoot, projectName);
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

}

