package org.dbfacade.testlink.eclipse.plugin.views;


import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.handlers.ExecuteTestListener;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.tree.PlanTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewLabelProvider;
import org.dbfacade.testlink.tc.autoexec.ExecuteTestCases;
import org.dbfacade.testlink.tc.autoexec.TestCase;
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
				executeTestCases(obj, null, false);
			} else if ( actionName.equals(PLAN_EXEC_NO_REPORT) ) {
				executeTestCases(obj, new Boolean(false), false);
			} else if ( actionName.equals(PLAN_EXEC_REPORT) ) {
				executeTestCases(obj, new Boolean(true), false);
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
		Boolean reportFlag,
		boolean runInBackground)
	{
		PlanTree tree = null;
		ExecuteTestCases exec = null;
		TestLinkPreferences pref = null;
		
		try {
			tree = (PlanTree) obj;
			pref = new TestLinkPreferences();
			TestCase[] cases = tree.getChildrenAsTestCases();
			exec = new ExecuteTestCases(pref.getTestLinkAPIClient(), tree.getTestPlan(),
					cases, null, "org.dbfacade.testlink.eclipse.plugin.views.ManualExecutor");
			ExecuteTestListener listener = new ExecuteTestListener(tree);
			exec.addListener(listener);
		} catch ( Exception e ) {
			UserMsg.error(e,
				"The execution of the test case is not possible on the selected node.");
			return;
		}
		
		try {	
			tree.setName(tree.getName() + " (Testing inprogress)");
			TestLinkView.refresh(tree);
			
			if ( reportFlag != null ) {
				exec.executeTestCases(reportFlag, runInBackground);
			} else {
				exec.executeTestCases(pref.useResultReporting(), runInBackground);
			}
		} catch ( Exception e ) {
			UserMsg.error(e,
				"The execution of the test cases failed and did not complete.");
		}
	}

	private void resubmitPlanToPrepare(
		Object obj)
	{
		try {
			PlanTree tree = (PlanTree) obj;
			tree.prepareTestPlanCases();
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
