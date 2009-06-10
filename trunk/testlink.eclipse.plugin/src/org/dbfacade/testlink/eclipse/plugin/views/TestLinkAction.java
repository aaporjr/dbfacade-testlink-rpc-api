package org.dbfacade.testlink.eclipse.plugin.views;


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;
import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.handlers.ExecuteTestListener;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.tree.PlanTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ProjectTree;
import org.dbfacade.testlink.eclipse.plugin.views.tree.ViewLabelProvider;
import org.dbfacade.testlink.tc.autoexec.ExecuteTestCases;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;


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
	public static final String REFRESH = "Refresh";

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
			if ( actionName.startsWith(PLAN_EXEC_DEFAULT) ) {
				executeTestCases(obj, null);
			} else if ( actionName.startsWith(PLAN_EXEC_NO_REPORT) ) {
				executeTestCases(obj, new Boolean(false));
			} else if ( actionName.startsWith(PLAN_EXEC_REPORT) ) {
				executeTestCases(obj, new Boolean(true));
			} else if ( actionName.equals(RESUBMIT_PREPARE) ) {
				resubmitPlanToPrepare(obj);
			} else if ( actionName.equals(REFRESH) ) {
				TestLinkView.refresh();
			} else if ( actionName.equals(OPEN_PROJECT) ) {
				 handleProjectAction(obj, false);
			} else if ( actionName.equals(SWITCH_PROJECT) ) {
				 handleProjectAction(obj, true);
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
		ExecuteTestCases exec = null;
		TestLinkPreferences pref = null;
		boolean runInBackground = false;
		
		try {
			tree = (PlanTree) obj;
			pref = new TestLinkPreferences();
			TestCase[] cases = tree.getChildrenAsTestCases();
			exec = new ExecuteTestCases(pref.getTestLinkAPIClient(), tree.getTestPlan(),
				cases, null, "org.dbfacade.testlink.eclipse.plugin.views.ManualExecutor");
			runInBackground = MessageDialog.openQuestion(
				TestLinkView.viewer.getControl().getShell(), "Execute Test Cases",
				"Run in the background (no refresh) ?");
			ExecuteTestListener listener = new ExecuteTestListener(tree, runInBackground);
			exec.addListener(listener);
		} catch ( Exception e ) {
			UserMsg.error(e,
				"The execution of the test case is not possible on the selected node.");
			return;
		}
		
		try {	
			if ( runInBackground ) {
				tree.setName(tree.getName() + " (Testing inprogress in background)");
			} else {
				tree.setName(tree.getName() + " (Testing inprogress)");
			}
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
			final PlanTree tree = (PlanTree) obj;
			MonitoredAction action = new MonitoredAction()
			{
				public void action() throws Exception
				{
					tree.prepareTestPlanCases();
				}
			};
			MonitorActionProgress runAction = 
				new MonitorActionProgress("Re-running the prepare operation on test plan " + tree.getName(),  action);
			runAction.startAndWait();
		} catch ( Exception e ) {
			UserMsg.error(e, "Could not prepare test plan again due to exception.");
		}
	}
	
	private void handleProjectAction(Object obj, boolean isSwitchAction) {
		ProjectTree tree=null;
		
		if ( obj instanceof ProjectTree ) {
			tree= (ProjectTree) obj;
		} else {
			return;
		}
		String currentProject = tree.getName();
		int p = 0;
		TestLinkAPIResults results = null;
		
		try {
			TestLinkPreferences pref = new TestLinkPreferences();
			TestLinkAPIClient api = new TestLinkAPIClient(pref.getDevKey(), pref.getTestLinkURL());
			results = api.getProjects();
			p = results.size();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		String projects[] = new String[p];
		
		for (int i=0; i < p; i++) {
			String project = (String) results.getValueByName(i, TestLinkAPIConst.API_RESULT_NAME);
			if ( project != null ) {
				projects[i] = project;
			}
		}
	
		ILabelProvider labelProvider = new LabelProvider();
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(TestLinkView.viewer.getControl().getShell(),
			labelProvider);
		dialog.setTitle("TestLink Project");
		dialog.setMessage("TestLink Projects");
		dialog.setElements(projects);
        
		if ( dialog.open() == Window.OK ) {
			String newProject = (String) dialog.getFirstResult();
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
