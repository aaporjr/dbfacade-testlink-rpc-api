package org.dbfacade.testlink.tc.autoexec;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.api.client.TestLinkAPIHelper;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;


public class TestPlanLoader
{
	private Map<Integer,
		TestPlan> plans;
	
	/**
	 * Loads all the test plans for a project by name.
	 * 
	 * @param projectName
	 */
	public TestPlanLoader(
		TestLinkAPIClient apiClient,
		String projectName)
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
		TestLinkAPIClient apiClient,
		String projectName,
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
		TestPlanPrepare prep) throws TestLinkAPIException
	{
		this(apiClient, projectName);
		preparePlans(prep);
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
		TestPlanPrepare prep) throws TestLinkAPIException
	{
		this(apiClient, projectName, planName);
		preparePlans(prep);
	}
	
	/**
	 * Returns the list of test plans.
	 * 
	 * @return
	 */
	public Map<Integer,
		TestPlan> getPlans()
	{
		return plans;
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
			TestPlan plan = plans.get(key);
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
		TestPlanPrepare prep) throws TestLinkAPIException
	{
		Iterator keys = plans.keySet().iterator();
		while ( keys.hasNext() ) {
			Object key = keys.next();
			TestPlan plan = plans.get(key);
			if ( plan != null ) {
				prep.adjust(plan);
			}
		}
	}
}
