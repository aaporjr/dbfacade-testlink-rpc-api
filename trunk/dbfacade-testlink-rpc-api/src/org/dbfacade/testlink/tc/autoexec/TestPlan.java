/*
 * Daniel R Padilla
 *
 * Copyright (c) 2009, Daniel R Padilla
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


import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.api.client.TestLinkAPIHelper;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;


/**
 * The class is used to manage the execution of test cases
 * associated with a test plan.
 * <p>
 * The test plan manager has two modes of opertaion. There is the online mode 
 * and the offline mode. In order to enter online mode a valid DEV KEY and 
 * TestLink API url must be provided. See the TestLink documentation for more 
 * information about the dev key.
 * <p>
 * If the api is deemed to be unreachable no error is thrown. The test plan
 * manager just defaults into offline mode. The offline or online state
 * of a TestPlanManager instance is found by using isOffline() method.
 * 
 * @author Daniel Padilla
 *
 */
public class TestPlan
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
	public TestPlan()
	{
		isAPIReachable = false;
		isReportResultsOn = false;
		this.testProject = TestProject.getOffLineProject();
		testPlanName = "Offline plan";
	}
	
	/**
	 * Creates an offline version of the project manager for
	 * running test offline.
	 */
	public TestPlan(
		String projectName,
		String planName)
	{
		isAPIReachable = false;
		isReportResultsOn = false;
		this.testProject = TestProject.getOffLineProject(projectName);
		testPlanName = planName;
	}
	
	/**
	 * When the TestPlanManager is instantiated then it retrieves all the
	 * test cases that are defined as automated test cases in the test plan.
	 * <p>
	 * Reporting of test results is turned on if the API is reachable. If the
	 * API is not reachable the system defaults to offline mode.
	 * 
	 * @param projectName
	 * @param planName
	 */
	public TestPlan(
		TestLinkAPIClient apiClient,
		String projectName,
		String planName)
	{
		this.apiClient = apiClient;
		checkAPIReachability(this.apiClient, projectName, planName);
		if ( isAPIReachable ) {
			init(projectName, planName);
		}
	}
	
	/**
	 * When the TestPlanManager is instantiated then it retrieves all the
	 * test cases that are defined as automated test cases in the test plan.
	 * <p>
	 * Reporting of test results is turned on if the API is reachable. If the
	 * API is not reachable the system defaults to offline mode.
	 * 
	 * @param projectName
	 * @param planName
	 */
	public TestPlan(
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
	public TestPlan(
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
	 * Return the name of the test plan for this test plan manager.
	 * 
	 * @return
	 */
	public String getTestPlanName()
	{
		return testPlanName;
	}
	
	/**
	 * Can the project manager access the API
	 * 
	 * @return
	 */
	public boolean isOffline()
	{
		return !(isAPIReachable);
	}
	
	/**
	 * True if reporting of test result to the TestLink API is turned on. 
	 * 
	 * @return
	 */
	public boolean isReportResultsOn()
	{
		return isReportResultsOn;
	}
	
	/**
	 * Turn on report of test result to the TestLink API. 
	 */
	public void trunOnResultReporting()
	{
		isReportResultsOn = true;
	}
	
	/**
	 * Turn off report of test result to the TestLink API. 
	 */
	public void trunOffResultReporting()
	{
		isReportResultsOn = false;
	}

	/**
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
		TestCase testCase,
		String loginUserName) throws TestLinkAPIException
	{
		if ( !isOffline() ) {
			testCase.addToTestLink(apiClient, loginUserName);
			if ( !isCasePartOfPlan(testCase) ) {
				addTestCaseToPlan(testCase);
			}
		}
		testCaseRegistry.put(testCase);
	}
	
	/**
	 * 
	 * @param caseNameOrVisibleID
	 * @param executor
	 * @throws TestLinkAPIException
	 */
	public void setTestCaseExecutor(
		String caseNameOrVisibleID,
		TestCaseExecutor executor) 
	{
		TestCase testCase = testCaseRegistry.get(caseNameOrVisibleID);
		testCase.setExecutor(executor);
	}
	
	/**
	 * 
	 * @param caseID
	 * @param executor
	 * @throws TestLinkAPIException
	 */
	public void setTestCaseExecutor(
		Integer caseID,
		TestCaseExecutor executor) 
	{
		TestCase testCase = testCaseRegistry.get(caseID);
		testCase.setExecutor(executor);
	}
	
	/**
	 * Execute the test cases for the instantiated plan and record the test case
	 * results. The execution records a failure for any test case that returns an 
	 * undefined result or throws an exception during execution.
	 * 
	 * @throws TestLinkAPIException
	 */
	public void executeTestCases() throws TestLinkAPIException
	{
		for (int i=0; i < testCaseRegistry.size(); i++) {
			TestCase tc = testCaseRegistry.get(i);
			executeTestCase(tc);
		}
	}
	
	public void executeTestCase(
		TestCase testCase)
	{
		TestCaseExecutor executor = testCase.getExecutor();
		if ( executor == null ) {
			executor = new EmptyExecutor();
			testCase.setExecutor(executor);
		} else {
			try {
				executor.execute(testCase);
			} catch ( Exception e ) {
				executor.setExecutionResult(TestCaseExecutor.RESULT_FAILED);
			}
		}
	}
	
	public TestCase[] getTestCases() {
		return testCaseRegistry.toArray();
	}

	/*
	 * Private methods section
	 */
	
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
		checkAPIReachability(apiClient, projectName, planName);
	}
	
	private void checkAPIReachability(TestLinkAPIClient apiClient, String projectName, String planName)
	{
		
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
	 * Method is called by the constructor and it finds all the test cases in a 
	 * test plan and then instantiates the classes using the initExistingCase()
	 * method. Once the case is instantiated it is stored in a list.
	 */
	private void init(
		String projectName,
		String planName)
	{
		try {
			TestLinkAPIResults caseList = apiClient.getCasesForTestPlan(testPlanID);
			for ( int i = 0; i < caseList.size(); i++ ) {
				Map caseInfo = caseList.getData(i);
				TestCase tc = getTestCaseInstance(caseInfo);
				testCaseRegistry.put(tc);
			}
			isReportResultsOn = true;
			testPlanName = planName;
		} catch ( Exception e ) {
			isAPIReachable = false;
			createDummyOfflineInfo(projectName, planName);
		}
	}
	
	/*
	 * Create an instance of the requested test case class
	 */
	private TestCase getTestCaseInstance(
		Map testCaseInfo) throws Exception
	{
		boolean isCreated = false;
		String tcSuiteName = (String) testCaseInfo.get(
			TestLinkAPIConst.API_RESULT_TC_SUITE);
		TestCase tc = (TestCase) Class.forName(testCaseClass).newInstance();
		TestLinkAPIResults suites = apiClient.getTestSuitesForTestPlan(testPlanID);
		for ( int i = 0; i < suites.size(); i++ ) {
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
			throw new TestLinkAPIException(
				"Unable to find test suite name in the test plan " + tcSuiteName);
		}
	}
	
	/*
	 * Make sure we are offline with an offline project
	 */
	private void createDummyOfflineInfo(
		String projectName,
		String planName)
	{
		isAPIReachable = false;
		this.testProject = TestProject.getOffLineProject(projectName);
		testPlanName = planName;
	}
	
	/*
	 * Check to see if the test plan is part of the test case
	 */
	private boolean isCasePartOfPlan(
		TestCase tc)
	{
		try {
			TestLinkAPIResults results = apiClient.getCasesForTestPlan(testPlanID);
			for (int i=0; i < results.size(); i++) {
				Map caseInfo = results.getData(i);
				Object id = caseInfo.get(TestLinkAPIConst.API_RESULT_IDENTIFIER);
				if ( id != null ) {
					Integer caseID = new Integer(id.toString());
					if ( caseID.equals(tc.getTestCaseInternalID())) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	/*
	 * Add test case to test plan
	 */
	private void addTestCaseToPlan(
		TestCase tc)
	{
		try {
			apiClient.addTestCaseToTestPlan(testProject.getProjectID(), testPlanID, tc.getTestCaseVisibleID(), null, null, null);
		} catch (Exception e) {}
	}
}
