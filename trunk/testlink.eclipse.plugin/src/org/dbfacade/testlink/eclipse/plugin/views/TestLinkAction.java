package org.dbfacade.testlink.eclipse.plugin.views;


import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;


public class TestLinkAction extends Action
{
	
	// public
	public static final String OPEN_PROJECT = "Open Project";
	public static final String CLOSE_PROJECT = "Close Project";
	public static final String SWITCH_PROJECT = "Switch Projects";
	public static final String PLAN_EXEC_DEFAULT = "Execute Test (Default reporting)";
	public static final String PLAN_EXEC_NO_REPORT = "Execute Test (No reporting)";
	public static final String PLAN_EXEC_REPORT = "Execute Test (Report results)";
	public static final String RESUBMIT_PREPARE = "Resubmit to preparation step";
	public static final String DOUBLE_CLICK = "doble click";

	// private
	private TreeViewer viewer;
	private String actionName;
	
	public TestLinkAction(
		TreeViewer viewer,
		ViewLabelProvider labelProvider,
		String actionName,
		String actionToolTip)
	{
		this.actionName = actionName;
		this.viewer = viewer;
		if ( !actionName.equals(DOUBLE_CLICK) ) {
			setText(actionName);
			setToolTipText(actionToolTip);
			setImageDescriptor(labelProvider.getImageDescriptor(this));
		}
	}
	
	public String getActionName()
	{
		return actionName;
	}
	
	public void run()
	{
		try {
			ISelection selection = viewer.getSelection();
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			showMessage(viewer,
				"The " + actionName + " action for " + obj.toString()
				+ " is not implemented at this time.");
		} catch ( Exception e ) {
			UserMsg.error(e,
				"Unable to perform the action " + actionName + " due to an exception.");
		}
	}
	
	public void showMessage(
		TreeViewer viewer,
		String message)
	{
		MessageDialog.openInformation(viewer.getControl().getShell(), "TestLink View",
			message);
	}
}
