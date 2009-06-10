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
package org.dbfacade.testlink.eclipse.plugin.views;


import org.dbfacade.testlink.eclipse.plugin.views.tree.PlanTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ProjectTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;


public class TestLinkActions
{
	private Action openProject;
	private Action closeProject;
	private Action switchProject;
	private Action executeTestDefault;
	private Action executeTestNoReport;
	private Action executeTestReport;
	private Action resubmitPreparation;
	private Action refresh;

	/**
	 * Create all the actions for the view.
	 * 
	 * @param viewer
	 * @param doubleClickAction
	 * @param labels
	 */
	public void makeActions(
		Action doubleClickAction,
		ViewLabelProvider labels)
	{
		refresh = new TestLinkAction(labels, TestLinkAction.REFRESH,
		"Refresh the tree node.");
		
		openProject = new TestLinkAction(labels, TestLinkAction.OPEN_PROJECT,
			"Open an additional TestLink project.");
	
		closeProject = new TestLinkAction(labels, TestLinkAction.CLOSE_PROJECT,
			"Close this project.");

		switchProject = new TestLinkAction(labels, TestLinkAction.SWITCH_PROJECT,
			"Close this project and open a new project in its place.");

		executeTestDefault = new TestLinkAction(labels,
			TestLinkAction.PLAN_EXEC_DEFAULT,
			"Execute the test and use the default results reporting flag.");
		
		executeTestNoReport = new TestLinkAction(labels,
			TestLinkAction.PLAN_EXEC_NO_REPORT,
			"Execute the test and do not report the results to the TestLink database.");
	
		executeTestReport = new TestLinkAction(labels,
			TestLinkAction.PLAN_EXEC_REPORT,
			"Execute the test and report the results to the TestLink database regardless of default setting.");

		executeTestDefault = new TestLinkAction(labels,
				TestLinkAction.PLAN_EXEC_DEFAULT,
				"Execute the test and use the default results reporting flag.");
		
		resubmitPreparation = new TestLinkAction(labels,
				TestLinkAction.RESUBMIT_PREPARE,
				"Resubmits the plan to the preparation class defined in the preferences dialog.");
		
		doubleClickAction = new TestLinkAction(labels, TestLinkAction.DOUBLE_CLICK,
			null);
	}
	
	/**
	 * We do not need these actions. These are the local down
	 * arrow that is found in view menus. Eventually they may 
	 * make a come back as the plugin gets additional functionality.
	 * 
	 * @param manager
	 */
	public void fillLocalPullDown(
		IMenuManager manager)
	{/*
		 manager.add(action1);
		 manager.add(new Separator());
		 manager.add(action2);
		 */}

	/**
	 * These are the menu options for each of the tree node types.
	 * 
	 * @param node
	 * @param drillDownAdapter
	 * @param manager
	 */
	public void fillContextMenu(
		Object node,
		DrillDownAdapter drillDownAdapter,
		IMenuManager manager)
	{
		if ( node instanceof ProjectTree ) {
			manager.add(openProject);
			// TODO: Eventually it is needed but not a priority
			// manager.add(closeProject);
			manager.add(switchProject);
		} else if ( node instanceof PlanTree ) {
			manager.add(resubmitPreparation);
			manager.add(executeTestDefault);
			manager.add(executeTestNoReport);
			manager.add(executeTestReport);
		}
		manager.add(new Separator());
		manager.add(refresh);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	/**
	 * We do not need these actions. These actions live on the
	 * view tool bar. Eventually they may  make a come back as  
	 * the plugin gets additional functionality.
	 * 
	 * @param manager
	 */
	public void fillLocalToolBar(
		DrillDownAdapter drillDownAdapter,
		IToolBarManager manager)
	{/*
		 manager.add(action5);
		 manager.add(action6);
		 manager.add(new Separator());
		 drillDownAdapter.addNavigationActions(manager);
		 */}

}
