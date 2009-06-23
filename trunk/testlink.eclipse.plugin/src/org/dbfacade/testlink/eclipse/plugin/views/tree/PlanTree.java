package org.dbfacade.testlink.eclipse.plugin.views.tree;


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.HtmlMessageText;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;
import org.dbfacade.testlink.tc.autoexec.server.RemoteClientExecutor;


public class PlanTree extends TreeParentNode
{
	public static final String EMPTY_PROJECT = "No test plans acquired";
	private TestPlan plan;
	private boolean hasTestRun = false;
	private boolean hasTestFailed = false;
	private RemoteClientExecutor rte = null; 
	private boolean isPreped=false;
	
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
	 * Create a new test plan node.
	 * 
	 * @param plan
	 */
	public PlanTree(
		TestPlan plan,
		int port)
	{
		super(plan.getTestPlanName());
		this.plan = plan;
		if ( port > 0 && plan != null ) {
			rte = new RemoteClientExecutor(port, plan);
		}
	}
	
	public RemoteClientExecutor getRemoteClient()
	{
		return rte;
	}
	
	/**
	 * Send a shutdown request to the server
	 */
	public void shutdownRemoteTester()
	{
		if ( rte != null ) {
			rte.sendServerShutdownRequest();
		}
	}
	
	/**
	 * True if the test has run to completion
	 */
	public boolean hasTestRun()
	{
		return hasTestRun;
	}
	
	/**
	 * True if the test has run to completion
	 */
	public boolean setHasTestRun(
		boolean run)
	{
		return hasTestRun = run;
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
	
	/**
	 * True if the a single test did not pass
	 */
	public boolean setHasTestFailed(
		boolean failed)
	{
		return hasTestFailed = failed;
	}
	
	public boolean isOpen()
	{
		return (plan != null);
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
		if ( isEmptyProjectNode() ) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isEmptyProjectNode()
	{
		return this.getName().equals(EMPTY_PROJECT);
	}
	
	public TestCase[] getChildrenAsTestCases()
	{
		int count = children.size();
		TestCase cases[] = new TestCase[count];
		for ( int i = 0; i < children.size(); i++ ) {
			TestCase tc = (TestCase) children.get(i);
			cases[i] = tc;
		}
		return cases;
	}
	
	public boolean isActive()
	{
		if ( plan != null ) {
			return plan.isActive();
		} else {
			return true;
		}
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
		hasTestRun = false;
		hasTestFailed = false;
		TestCase[] cases = null;
		
		try {
			// Lazy load test cases
			cases = this.plan.getTestCases();
		} catch ( Exception e ) {
			UserMsg.error(e, "The test plan test cases could not be loaded and prepared.");
		}	

		// If the plan is in remote test mode do not
		// prepare the test cases locally. Prepare 
		// the cases remotely and get results.
		if ( rte != null ) {
			rte.sendPlanPrepareRequest(cases);
			if ( ! rte.isPreped() ) {
				isPreped=false;
				UserMsg.error("The test plan prepare failed on the remote server.");
			} else {
				isPreped = true;
			}
			return cases;
		} else {
			try {
				
				// Setup the apiClient
				TestLinkPreferences pref = getPreferences();
				TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();
		
				// Prepare the plan after test cases are loaded
				TestPlanPrepare prep = pref.getTestPlanPrepare();
				prep.setTCUser(pref.getTestCaseCreator());
				prep.setExternalPath(pref.getExternalPath());
				prep.adjust(apiClient, this.plan);
				isPreped=true;
			} catch ( Exception e ) {
				isPreped=false;
				UserMsg.error(e,
					"The test plan prepare class "
					+ getPreferences().getTestPlanPrepareClass()
					+ " was unable to prepare the plan.");
			}			
			return cases;
		}
	}
	
	/**
	 * True if the test plan has been successfully prepared.
	 * @return
	 */
	public boolean isPreped() {
		return isPreped;
	}
	
	/**
	 * This is GUI related at expand.
	 */
	public void readyTestCases()
	{
		TestCaseLeaf last = null;
		for ( int i = 0; i < children.size(); i++ ) {
			TestCaseLeaf tcf = (TestCaseLeaf) children.get(i);
            if ( tcf != null ) {
            	TestCaseExecutor te = tcf.getTestCase().getExecutor();
            	if ( te != null ) {
            		te.setExecutionState(TestCaseExecutor.STATE_READY);
            		te.setExecutionResult(TestCaseExecutor.RESULT_UNKNOWN);
            	}
            	last = tcf;
            }
		}
		
		if ( last != null ) {
			TestLinkView.refresh(last);
		}
	}

	public String displayHtml()
	{
		String  detail = "No detail information available";
		if ( plan != null ) {
			detail = HtmlMessageText.OPEN_HTML_DOC + "<p><b>Name:</b></p><p>"
				+ plan.getTestPlanName() + "</p><p><b>Description:</b></p><p>"
				+ plan.getPlanDescription() + HtmlMessageText.CLOSE_HTML_DOC;
		}
		return detail;
	}
}
