package org.dbfacade.testlink.eclipse.plugin.views;


import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.views.tree.PlanTree;
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
	private String actionName;
	
	public TestLinkAction(
		ViewLabelProvider labelProvider,
		String actionName,
		String actionToolTip)
	{
		this.actionName = actionName;
		if ( !actionName.equals(DOUBLE_CLICK) ) {
			setText(actionName);
			setToolTipText(actionToolTip);
			setImageDescriptor(labelProvider.getImageDescriptor(this));
		}
		this.
	}
	
	public String getActionName()
	{
		return actionName;
	}
	
	public void run()
	{
		try {
			ISelection selection = TestLinkView.viewer.getSelection();
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if ( actionName.equals(PLAN_EXEC_DEFAULT) ) {
				executeTestCases(obj, null);
			} else if ( actionName.equals(PLAN_EXEC_NO_REPORT) ) {
				executeTestCases(obj, new Boolean(false));
			} else if ( actionName.equals(PLAN_EXEC_REPORT) ) {
				executeTestCases(obj, new Boolean(true));
			} else if ( actionName.equals(RESUBMIT_PREPARE) ) {
				resubmitPlanToPrepare(obj);
			} else {
				showMessage(TestLinkView.viewer,
					"The " + actionName + " action for " + obj.toString()
					+ " is not implemented at this time.");
			}
		} catch ( Exception e ) {
			UserMsg.error(e,
				"Unable to perform the action " + actionName + " due to an exception.");
		}
	}
	
	
	/*
	 * Private methods
	 */
	
	/*
	 * Casting done inside method because we want to
	 * trap and report all errors associated with running 
	 * the test cases.
	 * 
	 */
	private void executeTestCases(
		Object obj,
		Boolean reportFlag)
	{
		PlanTree tree = null;
		
		try {
			tree = (PlanTree) obj;
		} catch ( Exception e ) {
			UserMsg.error(e,
				"The execution of the test case is not possible on the selected node.");
			return;
		}
		
		try {		
			tree.setName(tree.getName() + " (Testing inprogress)");
			TestLinkView.refresh(tree);
			tree.resetTestCases();
			if ( reportFlag != null ) {
				tree.executeTestCases(reportFlag.booleanValue());
			} else {
				tree.executeTestCases();
			}
		} catch ( Exception e ) {
			UserMsg.error(e,
				"The execution of the test cases failed and did not complete.");
		}
		tree.setName(tree.getTestPlan().getTestPlanName());
		TestLinkView.refresh(tree);
	}
	
	private void resubmitPlanToPrepare(
		Object obj)
	{
		try {
			PlanTree tree = (PlanTree) obj;
			tree.prepareTestPlanCases();
			tree.resetTestCases();
			showMessage(TestLinkView.viewer,
				obj.toString() + " has completed preparation.");
		} catch ( Exception e ) {
			UserMsg.error(e, "Could not prepare test plan again due to exception.");
		}
	}
	
	private void showMessage(
		TreeViewer viewer,
		String message)
	{
		MessageDialog.openInformation(viewer.getControl().getShell(), "TestLink View",
			message);
	}
}
