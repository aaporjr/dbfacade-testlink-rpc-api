package org.dbfacade.testlink.eclipse.plugin.views.tree;


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.ManualExecutor;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.dbfacade.testlink.tc.autoexec.EmptyExecutor;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;


public class PlanTree extends TreeParent
{
	private TestPlan plan;
	private boolean hasTestRun = false;
	private boolean hasTestFailed = false;
	
	/**
	 * Used to inform user no plans were acquired
	 * 
	 * @param planName
	 */
	public PlanTree(
		String planName)
	{
		super(planName);
	}
	
	/**
	 * True if the test has run to completion
	 */
	public boolean hasTestRun()
	{
		return hasTestRun;
	}
	
	/**
	 * True if the all test passed
	 */
	public boolean hasTestPassed()
	{
		return !(hasTestFailed());
	}
	
	/**
	 * True if the a single test did not pass
	 */
	public boolean hasTestFailed()
	{
		return hasTestFailed;
	}
	
	public boolean isOpen()
	{
		return (plan != null);
	}
	
	/**
	 * Create a new test plan node.
	 * 
	 * @param plan
	 */
	public PlanTree(
		TestPlan plan)
	{
		super(plan.getTestPlanName());
		this.plan = plan;
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
		
			// Test plan
			TestCase[] cases = prepareTestPlanCases();
			
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
	
	/**
	 * Called by the non-GUI and GUI elements so the
	 * GUI side takes care of the catch for feedback.
	 * 
	 * @return
	 * @throws Exception
	 */
	public TestCase[] prepareTestPlanCases() throws Exception
	{
		// Lazy load test cases
		TestCase[] cases = this.plan.getTestCases();
		
		TestLinkPreferences pref = new TestLinkPreferences();
		TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
		TestPlanPrepare prep = pref.getTestPlanPrepare();
		
		// Prepare the plan after test cases are loaded
		prep.setTCUser(pref.getTestCaseCreator());
		prep.setExternalPath(pref.getExternalPath());
		prep.adjust(apiClient, this.plan);
		
		return cases;
	}
	
	public void readyTestCases()
	{
		for ( int i = 0; i < children.size(); i++ ) {
			TestCaseLeaf tcf = (TestCaseLeaf) children.get(i);
			TestCaseExecutor te = tcf.getTestCase().getExecutor();
			te.setExecutionState(TestCaseExecutor.STATE_READY);
			te.setExecutionResult(TestCaseExecutor.RESULT_UNKNOWN);
			TestLinkView.refresh(tcf);
		}
	}
	
	public void resetTestCases()
	{
		for ( int i = 0; i < children.size(); i++ ) {
			TestCaseLeaf tcf = (TestCaseLeaf) children.get(i);
			TestCaseExecutor te = tcf.getTestCase().getExecutor();
			te.setExecutionState(TestCaseExecutor.STATE_RESET);
			te.setExecutionResult(TestCaseExecutor.RESULT_UNKNOWN);
			TestLinkView.refresh(tcf);
		}
	}
	
	/**
	 * Execution of the test cases using default flag for reporting 
	 */
	public void executeTestCases()
	{
		TestLinkPreferences pref = new TestLinkPreferences();
		executeTestCases(pref.useResultReporting());
	}
	
	/**
	 * Execution of the test cases using flag for reporting 
	 * 
	 * @param reportResults
	 */
	public void executeTestCases(
		boolean reportResults)
	{
		hasTestRun = false;
		try {
			hasTestFailed = false;
			for ( int i = 0; i < children.size(); i++ ) {
			
				// Get the information
				TestCaseLeaf tcf = (TestCaseLeaf) children.get(i);
				TestCase tc = tcf.getTestCase();
				TestCaseExecutor te = tc.getExecutor();
				
				this.setName(
					plan.getTestPlanName() + " (Testing inprogress [Case: "
					+ tc.getTestCaseName() + "])");
				TestLinkView.refresh(this);
			
				// If no executor is registered then create empty and run empty
				if ( te == null ) {
					te = new EmptyExecutor();
				}
			
				// Execute the test case exception does not mean failure
				try {
					if ( tc.isManualExec() ) {
						te = new ManualExecutor();
						tc.setExecutor(te);
					} 
					te.execute(tc);
					if ( te.getExecutionResult() != TestCaseExecutor.RESULT_PASSED ) {
						hasTestFailed = true;
					}
				} catch ( Exception e ) {
					te.setExecutionResult(TestCaseExecutor.RESULT_FAILED);
					te.setExecutionState(TestCaseExecutor.STATE_BOMBED);
					hasTestFailed = true;
				}
			
				if ( reportResults ) {
					TestLinkPreferences pref = new TestLinkPreferences();
					TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
					try {
						apiClient.reportTestCaseResult(plan.getProject().getProjectName(),
							plan.getTestPlanName(), tc.getTestCaseName(), null, null, null);
					} catch ( Exception e ) {
						if ( tc != null ) {
							UserMsg.error(e,
								"Unable to report result for test case "
								+ tc.getTestCaseName());
						} else {
							UserMsg.error(e, "Unable to report result.");
						}
						hasTestFailed = true;
					}
				}
			
				TestLinkView.refresh(tcf);
			}
		} catch ( Exception e ) {
			hasTestFailed = true;
		}
		hasTestRun = true;
		
	}
}
