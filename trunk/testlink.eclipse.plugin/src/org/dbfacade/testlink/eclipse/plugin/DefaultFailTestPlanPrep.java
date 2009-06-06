package org.dbfacade.testlink.eclipse.plugin;


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.tc.autoexec.EmptyExecutor;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;


public class DefaultFailTestPlanPrep implements TestPlanPrepare
{

	/**
	 * Optionally made available by callers to the interface
	 * 
	 * @param directory
	 */
	public void setExternalDirectory(
		String directory)
	{}
	
	/**
	 * Optionally made available by callers to the interface
	 * 
	 * @param user
	 */
	public void setTCUser(
		String user)
	{}
	
	/**
	 * This is just a dummy test executor creator	 
	 * 
	 * @param plan
	 * @return A test plan which has had the executors set for each test case.
	 * 
	 */
	public TestPlan adjust(
		TestLinkAPIClient apiClient,
		TestPlan plan) throws TestLinkAPIException
	{
		TestCase[] cases = plan.getTestCases();
		for ( int i = 0; i < cases.length; i++ ) {
			TestCase tc = cases[i];
			tc.setExecutor(new EmptyExecutor());
		}
		return plan;
	}
	
}
