package org.dbfacade.testlink.tc.autoexec.example;


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;


public class RandomTestResultPrep implements TestPlanPrepare
{

	/**
	 * Optionally made available by callers to the interface
	 * 
	 * @param directory
	 */
	public void setExternalPath(
		String path)
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
	 * This is just a dummy test executor creator. This is not
	 * a good example since you do not want to run the test
	 * at creation. 
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
			TestCaseExecutor te = new RandomTestResultExecutor();
			tc.setExecutor(te);
		}
		return plan;
	}
	
}
