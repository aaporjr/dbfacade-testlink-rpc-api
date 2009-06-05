package org.dbfacade.testlink.tc.autoexec;

import java.util.ArrayList;

public class TestPlanLoader {
	private ArrayList plans = new ArrayList();
	
	/**
	 * Loads all the test plans for a project.
	 * @param projectName
	 */
	public TestPlanLoader(String projectName) {
		
	}
	
	/**
	 * Loads a single test plan by project name and test plan name
	 * 
	 * @param projectName
	 * @param planName
	 */
	public TestPlanLoader(String projectName, String planName) {
		
	}
	
	/**
	 * Returns the list of test plans.
	 * 
	 * @return
	 */
	public ArrayList getPlans() {
		return plans;
	}
}
