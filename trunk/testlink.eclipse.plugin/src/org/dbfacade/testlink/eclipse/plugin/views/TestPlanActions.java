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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;


public class TestPlanActions
{
	private Action openProject;
	private Action closeProject;
	private Action switchProject;
	private Action executeTestDefault;
	private Action executeTestNoReport;
	private Action executeTestReport;

	public void makeActions(
		TreeViewer viewer,
		Action doubleClickAction,
		ViewLabelProvider labels)
	{
		final TreeViewer finalViewer = viewer;
		
		openProject = new Action()
		{
			public void run()
			{
				showMessage(finalViewer,
					"The open project action is not implemented at this time.");
			}
		};
		openProject.setText("Open Project");
		openProject.setToolTipText("Open an additional TestLink project.");
		openProject.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		closeProject = new Action()
		{
			public void run()
			{
				showMessage(finalViewer,
					"The close project action is not implemented at thsi time.");
			}
		};
		closeProject.setText("Close Project");
		closeProject.setToolTipText("Close this project.");
		closeProject.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		switchProject = new Action()
		{
			public void run()
			{
				showMessage(finalViewer,
					"The switch project actions is not implemented at this time.");
			}
		};
		switchProject.setText("Switch Project");
		switchProject.setToolTipText(
			"Close this project and open a new project in its place.");
		switchProject.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		executeTestDefault = new Action()
		{
			public void run()
			{
				showMessage(finalViewer, "Test have been executed");
			}
		};
		executeTestDefault.setText("Execute test (Default reporting).");
		executeTestDefault.setToolTipText(
			"Execute the test and use the default results reporting flag.");
		executeTestDefault.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		executeTestNoReport = new Action()
		{
			public void run()
			{
				showMessage(finalViewer, "Test have been executed.");
			}
		};
		executeTestNoReport.setText("Execute test (No reporting)");
		executeTestNoReport.setToolTipText(
			"Execute the test and do not report the results to the TestLink database.");
		executeTestNoReport.setImageDescriptor(
			PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		executeTestReport = new Action()
		{
			public void run()
			{
				showMessage(finalViewer, "Test have been executed.");
			}
		};
		executeTestReport.setText("Execute test (Report)");
		executeTestReport.setToolTipText(
			"Execute the test and report the results to the TestLink database regardless of default setting.");
		executeTestReport.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		doubleClickAction = new Action()
		{
			public void run()
			{
				ISelection selection = finalViewer.getSelection();
				Object obj = ((IStructuredSelection) selection).getFirstElement();
				showMessage(finalViewer, "Double-click detected on " + obj.toString());
			}
		};
	}
	
	/**
	 * We do not need these actions. This are the local down
	 * arrow that is found in view menus. Eventually they may 
	 * make a comeback as the plugin gets additional functionality.
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

	public void fillContextMenu(
		Object node,
		DrillDownAdapter drillDownAdapter,
		IMenuManager manager)
	{
		if ( node instanceof ProjectTree ) {
			manager.add(openProject);
			manager.add(closeProject);
			manager.add(switchProject);
		} else if ( node instanceof PlanTree ){
			manager.add(executeTestDefault);
			manager.add(executeTestNoReport);
			manager.add(executeTestReport);
		}
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	public void fillLocalToolBar(
		DrillDownAdapter drillDownAdapter,
		IToolBarManager manager)
	{/*
		 manager.add(action5);
		 manager.add(action6);
		 manager.add(new Separator());
		 drillDownAdapter.addNavigationActions(manager);
		 */}

	public void showMessage(
		TreeViewer viewer,
		String message)
	{
		MessageDialog.openInformation(viewer.getControl().getShell(), "TestLink View",
			message);
	}
}
