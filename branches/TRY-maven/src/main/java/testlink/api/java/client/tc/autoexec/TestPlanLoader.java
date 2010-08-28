package testlink.api.java.client.tc.autoexec;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIConst;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIHelper;
import testlink.api.java.client.TestLinkAPIResults;


public class TestPlanLoader
{
	private Map plans;
	
	/**
	 * Loads all the test plans for a project by name.
	 * 
	 * @param projectName
	 */
	public TestPlanLoader(
		String projectName,
		TestLinkAPIClient apiClient)
	{
		try {
			fillPlanList(apiClient, projectName);
		} catch ( Exception e ) {
			plans = new HashMap();
		}
	}
	
	/**
	 * Loads a single test plan by project name and test plan name
	 * 
	 * @param projectName
	 * @param planName
	 */
	public TestPlanLoader(
		String projectName,
		TestLinkAPIClient apiClient,
		String planName)
	{
		try {
			plans = new HashMap();
			Integer projectID = TestLinkAPIHelper.getProjectID(apiClient, projectName);
			Integer planID = TestLinkAPIHelper.getPlanID(apiClient, projectID, planName);
			if ( planID != null ) {
				loadPlan(apiClient, projectName, planName, planID);
			}
		} catch ( Exception e ) {
			plans = new HashMap();
		}
	}
	
	/**
	 * Loads all the test plans for a project by name and executes the adjust method for each plan.
	 * 
	 * @param projectName
	 */
	public TestPlanLoader(
		TestLinkAPIClient apiClient,
		String projectName,
		String defaultTestCaseUser,
		String externalDir,
		TestPlanPrepare prep) throws TestLinkAPIException
	{
		this(projectName, apiClient);
		preparePlans(apiClient, prep, defaultTestCaseUser, externalDir);
	}
	
	/**
	 * Loads a single test plan by project name and test plan name and executes the adjust method for each plan.
	 * 
	 * @param projectName
	 * @param planName
	 */
	public TestPlanLoader(
		TestLinkAPIClient apiClient,
		String projectName,
		String planName,
		String defaultTestCaseUser,
		String externalDir,
		TestPlanPrepare prep) throws TestLinkAPIException
	{
		this(projectName, apiClient, planName);
		preparePlans(apiClient, prep, defaultTestCaseUser, externalDir);
	}
	
	/**
	 * Returns the list of test plans.
	 * 
	 * @return
	 */
	public Map getPlans()
	{
		return plans;
	}
	
	/**
	 * Get all the plan identifiers in the list
	 * 
	 * @return
	 */
	public Iterator getPlanIDs() {
		return plans.keySet().iterator();
	}
	
	/**
	 * Get test plan
	 */
	public TestPlan getPlan(Object id) {
		return (TestPlan) plans.get(id);
	}
	
	/**
	 * Print out the results
	 * 
	 * @param results
	 */
	public String toString()
	{
		String ret = "";
		Iterator keys = plans.keySet().iterator();
		while ( keys.hasNext() ) {
			Object key = keys.next();
			TestPlan plan = (TestPlan) plans.get(key);
			if ( plan != null ) {
				ret += plan.getTestPlanName() + "\n";
			}
		}
		return ret;
	}
	
	/*
	 * Private methods
	 */
	private void fillPlanList(
		TestLinkAPIClient apiClient,
		String projectName) throws TestLinkAPIException 
	{
		plans = new HashMap();
		TestLinkAPIResults results = apiClient.getProjectTestPlans(projectName);
		for ( int i = 0; i < results.size(); i++ ) {
			Map data = results.getData(i);
			Object id = data.get(TestLinkAPIConst.API_RESULT_IDENTIFIER);
			Object name = data.get(TestLinkAPIConst.API_RESULT_NAME);
			if ( id != null && name != null ) {
				Integer planID = new Integer(id.toString());
				loadPlan(apiClient, projectName, name.toString(), planID);
			}
		}
	}
	
	private void loadPlan(
		TestLinkAPIClient apiClient,
		String projectName,
		String planName,
		Integer planID)
	{
		TestPlan plan = new TestPlan(apiClient, projectName, planName);
		plans.put(planID, plan);
	}
	
	private void preparePlans(
		TestLinkAPIClient apiClient,
		TestPlanPrepare prep,
		String defaultTestCaseUser,
		String externalDir) throws TestLinkAPIException
	{
		Iterator keys = plans.keySet().iterator();
		while ( keys.hasNext() ) {
			Object key = keys.next();
			TestPlan plan = (TestPlan) plans.get(key);
			if ( plan != null ) {
				prep.setExternalPath(externalDir);
				prep.setTCUser(defaultTestCaseUser);
				prep.adjust(apiClient, plan);
			}
		}
	}
}
