/*
 * Database Facade
 *
 * Copyright (c) 2009, Database Facade
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.dbfacade.testlink.tc.autoexec;


import java.util.Iterator;
import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.api.client.TestLinkAPIHelper;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;


/**
 * Currently not supported (class stub).
 * <p>
 * The class is used to manage the execution of automated test cases
 * associated with a test plan.
 * 
 * @author Daniel Padilla
 *
 */
public class TestPlanManager
{
	private boolean isAPIReachable = false;
	private boolean isReportResultsOn = false;
	private TestProject testProject;
	private String testPlanName = null;
	private Integer testPlanID = null;
	private TestLinkAPIClient apiClient;
	private TestCaseRegistry testCaseRegistry = new TestCaseRegistry();
	private String testCaseClass = "org.dbfacade.testlink.tc.autoexec.ExecutableTestCase";
	
	/**
	 * Creates an offline version of the project manager for
	 * running test offline.
	 */
	public TestPlanManager() {
		isAPIReachable=false;
		isReportResultsOn=false;
		this.testProject = TestProject.getOffLineProject();
		testPlanName = "Offline plan";
	}
	
	/**
	 * Creates an offline version of the project manager for
	 * running test offline.
	 */
	public TestPlanManager(String projectName, String planName) {
		isAPIReachable=false;
		isReportResultsOn=false;
		this.testProject = TestProject.getOffLineProject(projectName);
		testPlanName = planName;
	}
	
	
	/**
	 * Currently not supported (constructor stub).
	 * <p>
	 * When the TestPlanManager is instantiated then it retrieves all the
	 * test cases that are defined as automated test cases in the test plan.
	 * <p>
	 * Reporting of test results is turned on if the API is reachable. If the
	 * API is not reachable the system defaults to offline mode.
	 * 
	 * @param projectName
	 * @param planName
	 */
	public TestPlanManager(
		String projectName,
		String planName,
		String devKey,
		String urlToAPI)
	{
		checkAPIReachability(devKey, urlToAPI, projectName, planName);
		if ( isAPIReachable ) {
			init(projectName, planName);
		}
	}
	
	/**
	 * Currently not supported (constructor stub).
	 * <p>
	 * When the TestPlanManager is instantiated then it retrieves all the
	 * test cases that are defined as automated test cases in the test plan.
	 * <p>
	 * The cases are instantiated using the class that is passed to the constructor.
	 * <p>
	 * Reporting of test results is turned on if the API is reachable. If the
	 * API is not reachable the system defaults to offline mode.
	 *
	 * @param projectName
	 * @param planName
	 * @param testCaseImplementionClass
	 */
	public TestPlanManager(
		String projectName,
		String planName,
		String testCaseClass,
		String devKey,
		String urlToAPI)
	{
		checkAPIReachability(devKey, urlToAPI, projectName, planName);
		this.testCaseClass = testCaseClass;
		if ( isAPIReachable ) {
			init(projectName, planName);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTestPlanName() {
		return testPlanName;
	}
	
	/**
	 * Can the project manager access the API
	 * 
	 * @return
	 */
	public boolean isOffline() {
		return !(isAPIReachable);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isReportResultsOn() {
		return isReportResultsOn;
	}
	
	/**
	 * 
	 */
	public void trunOnResultReporting() {
		isReportResultsOn = true;
	}
	
	/**
	 * 
	 */
	public void trunOffResultReporting() {
		isReportResultsOn = false;
	}

	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Adds a test case to the a project test suite if the test case does
	 * not exist and then it it adds it to the test plan.
	 * 
	 * If the test case exists it replaces the test case class obejct instance
	 * with the new instance.
	 * 
	 * @param testCase
	 * @throws TestLinkAPIException
	 */
	public void putTestCase(
		TestCase testCase) throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case by name or visible id. The visible identifier can be
	 * viewed when looking at a test case suite tree in the TestLink web application.
	 * 
	 * @param caseNameOrVisibleID
	 * @return TestCase if found otherwise null
	 */
	public TestCase getTestCase(
		String caseNameOrVisibleID) throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the test case by internal identifier.
	 * 
	 * @param caseID
	 * @return TestCase if found otherwise null
	 */
	public TestCase getTestCase(
		Integer caseID) throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get an iterator for all the existing case names in this plan.
	 * 
	 * @return Iterator of test case internal identifiers
	 */
	public Iterator getTestCaseNames() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get an iterator for all the existing case visible identifiers in this plan.
	 * 
	 * @return Iterator of test case internal identifiers
	 */
	public Iterator getTestCaseVisibleIDs() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get an iterator for all the existing case internal identifiers in this plan.
	 * 
	 * @return Iterator of test case internal identifiers
	 */
	public Iterator getTestCaseInternalIDs() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Execute the test cases for the instantiated plan and record the test case
	 * results. The execution records a failure for any test case that returns an 
	 * undefined result or throws an exception during execution.
	 * 
	 * @throws TestLinkAPIException
	 */
	public void executeTestCases() throws TestLinkAPIException
	{
		throw new TestLinkAPIException("The method is not currently supported.");
	}
	
	/*
	 * Check to see if the TestLINK API can be accessed and the project exists
	 */
	private void checkAPIReachability(
		String devKey,
		String url,
		String projectName,
		String planName) 
	{
		
		apiClient = new TestLinkAPIClient(devKey, url);
		
		// First ping
		try {
			apiClient.ping();
			isAPIReachable = true;
		} catch ( Exception e ) {
			isAPIReachable = false;
		}
		
		// Check if the test project exists
		Integer projectID = null;
		if ( isAPIReachable ) {
			try {
				projectID = TestLinkAPIHelper.getProjectID(apiClient, projectName);
				if ( projectID == null ) {
					throw new Exception("Could not find the project.");
				} 
				Map projectInfo = TestLinkAPIHelper.getProjectInfo(apiClient, projectID);
				testProject = new TestProject(projectInfo);
			} catch ( Exception e ) {
				isAPIReachable = false;
				createDummyOfflineInfo(projectName, planName);
			}
		}
		
		// Check if the test plan exists
		if ( isAPIReachable ) {
			try {
				testPlanID = TestLinkAPIHelper.getPlanID(apiClient, projectID, planName);
				if ( testPlanID == null ) {
					throw new Exception("Could not find the project.");
				} 
			} catch ( Exception e ) {
				isAPIReachable = false;
				createDummyOfflineInfo(projectName, planName);
			}
		}

	}
	
	/*
	 * Private methods
	 */
	
	/*
	 * Method is called by the constructor and it finds all the test cases in a 
	 * test plan and then instantiates the classes using the initExistingCase()
	 * method. Once the case is instantiated it is stored in a list.
	 */
	private void init(String projectName, String planName)
	{
		try {
			TestLinkAPIResults caseList = apiClient.getCasesForTestPlan(testPlanID);
			for (int i=0; i < caseList.size(); i++) {
				Map caseInfo = caseList.getData(i);
				TestCase tc = getTestCaseInstance(caseInfo);
				testCaseRegistry.put(tc);
			}
			isReportResultsOn=true;
			testPlanName = planName;
		} catch (Exception e) {
			isAPIReachable = false;
			createDummyOfflineInfo(projectName, planName);
		}
	}
	
	/*
	 * Create an instance of the requested test case class
	 */
	private TestCase getTestCaseInstance(Map testCaseInfo) throws Exception {
		boolean isCreated=false;
		String tcSuiteName = (String) testCaseInfo.get(TestLinkAPIConst.API_RESULT_TC_SUITE);
		TestCase tc = (TestCase) Class.forName(testCaseClass).newInstance();
		TestLinkAPIResults suites = apiClient.getTestSuitesForTestPlan(testPlanID);
		for (int i=0; i < suites.size(); i++) {
			Map suiteInfo = suites.getData(i);
			String pSuiteName = (String) suiteInfo.get(TestLinkAPIConst.API_RESULT_NAME);
			if ( tcSuiteName.equals(pSuiteName) ) {
				TestSuite testSuite = new TestSuite(suiteInfo);
				tc.initExistingCase(testProject, testSuite, testCaseInfo);
				isCreated = true;
			}
		}
		
		if ( isCreated ) {
			return tc;
		} else {
			throw new TestLinkAPIException("Unable to find test suite name in the test plan " + tcSuiteName);
		}
	}
	
	/*
	 * Make sure we are offline with an offline project
	 */
	private void createDummyOfflineInfo(String projectName, String planName)
	{
		isAPIReachable = false;
		this.testProject = TestProject.getOffLineProject(projectName);
		testPlanName = planName;
	}
}
