package org.dbfacade.testlink.eclipse.plugin.views.tree;


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;
import org.eclipse.jface.viewers.TreeViewer;


public class PlanTree extends TreeParent
{
	private TestPlan plan;
	private TreeViewer viewer;
	
	/**
	 * Used to inform user no plans were acquired
	 * 
	 * @param planName
	 */
	public PlanTree(
		TreeViewer viewer, 
		String planName)
	{
		super(planName);
		this.viewer = viewer;
	}
	
	public boolean isOpen() {
		return (plan != null);
	}
	
	/**
	 * Create a new test plan node.
	 * 
	 * @param plan
	 */
	public PlanTree(
		TreeViewer viewer,
		TestPlan plan)
	{
		super(plan.getTestPlanName());
		this.plan = plan;
		this.viewer = viewer;
	}
	
	/**
	 * Get the test plan associated with this object.
	 * @return
	 */
	public TestPlan getTestPlan()
	{
		return plan;
	}
	
	/**
	 * Always return true since if we have no children we will return empty object.
	 */
	public boolean hasChildren()
	{
		return true;
	}
	
	/**
	 * Get the test cases for the test plan
	 */
	public void findChildren()
	{
		try {
			
			// Lazy load test cases
			TestLinkPreferences pref = new TestLinkPreferences();
			TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
			TestPlanPrepare prep = pref.getTestPlanPrepare();
			TestCase[] cases = this.plan.getTestCases();
			
			// Prepare the plan after test cases are loaded
			prep.setTCUser(pref.getTestCaseCreator());
			prep.setExternalPath(pref.getExternalPath());
			prep.adjust(apiClient, this.plan);
			
			// Add test cases to the tree
			for ( int i = 0; i < cases.length; i++ ) {
				TestCase tc = cases[i];
				TestCaseLeaf tcLeaf = new TestCaseLeaf(tc);
				this.addChild(tcLeaf);
			}

			readyTestCases();
			
		} catch ( Exception e ) {
			UserMsg.error(e, "Could not create the test cases.");
		}
		
		if ( children.size() == 0 ) {
			TestCaseLeaf tcLeaf = new TestCaseLeaf(
				"No test cases were acquired for test plan.");
			this.addChild(tcLeaf);
		}
	}
	
	public void readyTestCases() {
		for (int i=0; i < children.size(); i++) {
			TestCaseLeaf tcf = (TestCaseLeaf) children.get(i);
			TestCaseExecutor te = tcf.getTestCase().getExecutor();
			te.setExecutionState(TestCaseExecutor.STATE_READY);
			te.setExecutionResult(TestCaseExecutor.RESULT_UNKNOWN);
			viewer.update(tcf, null);
		}
	}
	
	public void resetTestCases() {
		for (int i=0; i < children.size(); i++) {
			TestCaseLeaf tcf = (TestCaseLeaf) children.get(i);
			TestCaseExecutor te = tcf.getTestCase().getExecutor();
			te.setExecutionState(TestCaseExecutor.STATE_RESET);
			te.setExecutionResult(TestCaseExecutor.RESULT_UNKNOWN);
			viewer.update(tcf, null);
		}
	}
}
