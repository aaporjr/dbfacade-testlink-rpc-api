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
package org.dbfacade.testlink.api.client;


import java.net.URL;
import java.util.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


/**
 * The TestLinkAPI class is the principal interface
 * to the TestLink API. The class has been created 
 * to return results in easy to manage Java structures.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkAPIClient implements TestLinkAPIConst
{

	/* Cache Variables 
	 * 
	 * The way the client has been written it accesses
	 * the API many times for the same information so
	 * the following is a way to cache the information
	 * if the user of the api chooses to cache.
	 */
	boolean useCache = false;
	TestLinkAPIResults projects = null;
	Map plans = null;
	Map firstLevelSuites = null;
	Map casesForSuite = null;
	Map casesForPlan = null;
	Map buildsForPlan = null;
	
	/* API Initialization variables */
	public static String DEV_KEY; 
	public static String SERVER_URL; 
	
	/**
	 * Constructor. The client cache capabilities
	 * are turned off by default.
	 * 
	 * @param devKey
	 * @param url
	 */
	public TestLinkAPIClient(
		String devKey,
		String url)
	{
		DEV_KEY = devKey;
		SERVER_URL = url;
	}
	
	/**
	 * Constructor that can be used to enable or disable 
	 * the api cache. The API ignores all external changes 
	 * that can be made to a project when the cache is
	 * enabled. It keeps track of changes made by the client
	 * object instance and manages the cache accordingly.
	 * 
	 * @param devKey
	 * @param url
	 * @param useCache
	 */
	public TestLinkAPIClient(
		String devKey,
		String url,
		boolean useCache)
	{
		DEV_KEY = devKey;
		SERVER_URL = url;
		this.useCache = useCache;
	}
	
	public TestLinkAPIResults about() throws TestLinkAPIException {
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_REPORT_TEST_RESULT, params);
		return results;
	}
		
	/**
	 * Report a test execution result for a test case by test
	 * project name and test plan name. The system is allowed
	 * to guess on the latest build for the test case.
	 * 
	 * @param projectName			Required
	 * @param testPlanName			Required
	 * @param testCaseName			Required
	 * @param testResultStatus		Required
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults reportTestCaseResult(
		String projectName,
		String testPlanName,
		String testCaseName,
		String execNotes,
		String testResultStatus) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		if ( projectID == null ) {
			throw new TestLinkAPIException(
				"The project " + projectName + " was not found and the test case "
				+ testCaseName + " could not be accessed to report a test result.");
		}
		Integer planID = TestLinkAPIHelper.getPlanID(this, projectID, testPlanName);
		if ( planID == null ) {
			throw new TestLinkAPIException(
				"The plan " + testPlanName + " was not found and the test case "
				+ testCaseName + " could not be accessed to report a test result.");
		}
		Integer caseID = TestLinkAPIHelper.getCaseID(this, projectID, testCaseName);
		if ( caseID == null ) {
			throw new TestLinkAPIException(
				"The test case " + testCaseName + " was not found and the test case"
				+ " could not be accessed to report a test result against plan "
				+ testPlanName + ".");
		}

		TestLinkAPIResults results = reportTestCaseResult(planID, caseID, null,
			execNotes, testResultStatus);
		return results;
	}
	
	/**
	 * Report a test execution result for a test case by test
	 * project name and test plan name for a specific build.
	 * 
	 * If the build is left as null then the system is allowed 
	 * to guess on the latest build for the test case.
	 * 
	 * @param projectName			Required
	 * @param testPlanName			Required
	 * @param testCaseVisibleID		Required (Test Case ID on web-page tree)
	 * @param buildName				Optional
	 * @param testResultStatus		Required
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults reportTestCaseResult(
		String projectName,
		String testPlanName,
		String testCaseVisibleID,
		String buildName,
		String execNotes,
		String testResultStatus) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		if ( projectID == null ) {
			throw new TestLinkAPIException(
				"The project " + projectName + " was not found and the test case "
				+ testCaseVisibleID + " could not be accessed to report a test result.");
		}
		Integer planID = TestLinkAPIHelper.getPlanID(this, projectID, testPlanName);
		if ( planID == null ) {
			throw new TestLinkAPIException(
				"The plan " + testPlanName + " was not found and the test case "
				+ testCaseVisibleID + " could not be accessed to report a test result.");
		}
		Integer caseID = TestLinkAPIHelper.getCaseIDByVisibleID(this, projectID,
			testCaseVisibleID);
		if ( caseID == null ) {
			throw new TestLinkAPIException(
				"The test case identifier " + caseID + " was not found and the test case "
				+ testCaseVisibleID
				+ " could not be accessed to report a test result to test plan "
				+ testPlanName + ".");
		}
		
		Integer buildID = null;
		if ( buildName != null ) {
			buildID = TestLinkAPIHelper.getBuildID(this, planID, buildName);
			if ( buildID == null ) {
				throw new TestLinkAPIException(
					"The build name " + buildName + " was not found in test plan "
					+ testPlanName + " and the test result for test case " + testCaseVisibleID + " could not be recorded.");
			}
		}
		
		TestLinkAPIResults results = reportTestCaseResult(planID, caseID, buildID,
			execNotes, testResultStatus);
		return results;
	}
	
	/**
	 * Report a test execution result for a test case by test
	 * plan identifier and test case identifier for a specific 
	 * build identifier.
	 * 
	 * If the build identifier is not provided then the system
	 * is allowed to guess on the latest build for the test case.
	 * 
	 * @param testPlanID				Required
	 * @param testCaseID				Required
	 * @param buildID					Optional
	 * @param String testResultStatus	Required 
	 */ 
	public TestLinkAPIResults reportTestCaseResult(
		Integer testPlanID,
		Integer testCaseID,
		Integer buildID,
		String execNotes,
		String  testResultStatus) throws TestLinkAPIException
	{ 
		Boolean guess = new Boolean(true);
		if ( buildID != null ) {
			guess = new Boolean(false);
		}
		TestLinkAPIResults results = reportTestCaseResult(testPlanID, testCaseID, buildID,
			null, guess, execNotes, testResultStatus);
		return results;
	}
	
	/**
	 * This method supports the TestLink API set of parameters
	 * that can be used to report a test case result. 
	 * 
	 * @param testPlanID				Required
	 * @param testCaseID
	 * @param buildID
	 * @param bugID
	 * @param execNotes
	 * @param testResultStatus
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults reportTestCaseResult(
		Integer testPlanID,
		Integer testCaseID,
		Integer buildID,
		Integer bugID,
		Boolean guess,
		String  execNotes,
		String  testResultStatus) throws TestLinkAPIException
	{ 
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PLAN_ID, testPlanID);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_ID, testCaseID);
		setParam(params, OPTIONAL, API_PARAM_BUILD_ID, buildID);
		setParam(params, OPTIONAL, API_PARAM_BUG_ID, bugID);
		setParam(params, OPTIONAL, API_PARAM_GUESS, guess);
		setParam(params, OPTIONAL, API_PARAM_NOTES, execNotes);
		setParam(params, REQUIRED, API_PARAM_STATUS, testResultStatus);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_REPORT_TEST_RESULT, params);
		if ( hasError(results) ) {
			String empty = "Nothing was reported back when recording a test case execution result.";
			String notEmpty = "An error was reported while recording a test case execution result.";
			if ( results.size() < 1 ) {
				throw new TestLinkAPIException(empty);
			}
			Map data = results.getData(0);
			throw new TestLinkAPIException(notEmpty + " Results Message: [" + data.toString() + "]");
		}
		return results;
	}
	
	/**
	 * Create a new project in TestLink. 
	 * 
	 * @param projectName
	 * @param testCasePrefix
	 * @param description
	 */
	public Integer createTestProject(
		String projectName,
		String testCasePrefix,
		String description) throws TestLinkAPIException
	{ 
		initCache();
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_NAME, projectName);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_PREFIX, testCasePrefix);
		setParam(params, REQUIRED, API_PARAM_NOTES, description);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_PROJECT, params);
		return getCreatedRecordIdentifier(results, API_RESULT_IDENTIFIER);
	}
	
	/**
	 * Create top level test suite under a specific project name
	 * 
	 * @param projectName
	 * @param suiteName
	 * @param description
	 * @throws TestLinkAPIException
	 */
	public Integer createTestSuite(
		String projectName,
		String suiteName,
		String description) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		if ( projectID != null ) {
			return createTestSuite(projectID, suiteName, description);
		} else {
			throw new TestLinkAPIException(
				"The project " + projectName + " was not found and the test suite "
				+ suiteName + " could not be created.");
		}
	}
	
	/**
	 * Create top level test suite under a specific project identifier
	 * 
	 * @param projectID
	 * @param suiteName
	 * @param description
	 * @return
	 * @throws TestLinkAPIException
	 */
	public Integer createTestSuite(
		Integer projectID,
		String suiteName,
		String description) throws TestLinkAPIException
	{
		return createTestSuite(projectID, suiteName, description, null, null, null);
	}
	
	/**
	 * 
	 * Create a test suite at any level using the project identifier and
	 * the parent suite identifier information.
	 * 
	 * @param projectID
	 * @param suiteName
	 * @param description
	 * @param parentID
	 * @param order
	 * @param check
	 * @return
	 * @throws TestLinkAPIException
	 */
	public Integer createTestSuite(
		Integer projectID, 
		String suiteName,
		String description,
		Integer parentID,
		Integer order,
		Boolean check) throws TestLinkAPIException
	{
		initCache();
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID.toString());
		setParam(params, REQUIRED, API_PARAM_TEST_SUITE_NAME, suiteName);
		setParam(params, REQUIRED, API_PARAM_DETAILS, description);
		setParam(params, OPTIONAL, API_PARAM_PARENT_ID, parentID);
		setParam(params, OPTIONAL, API_PARAM_ORDER, order);
		setParam(params, OPTIONAL, API_PARAM_CHECK_DUP_NAMES, check);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_SUITE, params);
		return getCreatedRecordIdentifier(results, API_RESULT_IDENTIFIER);
	}
	
	/**
	 * Create a test case by project name and suite name. 
	 * 
	 * @param projectName
	 * @param suiteName
	 * @param testCaseName
	 * @param summary
	 * @param steps
	 * @param expectedResults
	 * @param importance
	 * @return
	 */
	public Integer createTestCase(
		String authorLoginName,
		String projectName, 
		String suiteName,
		String testCaseName,
		String summary,
		String steps,
		String expectedResults,
		String importance) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		Integer suiteID = TestLinkAPIHelper.getSuiteID(this, projectName, suiteName);
		return createTestCase(authorLoginName, projectID, suiteID, testCaseName, summary,
			steps, expectedResults, null, null, null, null, null, importance);
	}
	
	/**
	 * Create a new test case using all the variables that are provided by
	 * the TestLink API. For more information on the parameters refer to the 
	 * TestLink API documentation.
	 * 
	 * @param suiteID
	 * @param caseName
	 * @param summary
	 * @param steps
	 * @param expectedResults
	 * @param order
	 * @param internalID
	 * @param checkDuplicatedName
	 * @param actionOnDuplicatedName
	 * @param executionType
	 * @param importance
	 * @return The new test case identifier or null if unsuccessful
	 * @throws TestLinkAPIException
	 */
	public Integer createTestCase(
		String authorLoginName,
		Integer projectID,
		Integer suiteID,
		String caseName,
		String summary,
		String steps,
		String expectedResults,
		Integer order,
		Integer internalID,
		Boolean checkDuplicatedName,                        
		String actionOnDuplicatedName,
		String executionType,
		String importance) throws TestLinkAPIException
	{
		initCache();
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_AUTHOR_LOGIN, authorLoginName);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID);
		setParam(params, REQUIRED, API_PARAM_TEST_SUITE_ID, suiteID);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_NAME, caseName);
		setParam(params, REQUIRED, API_PARAM_SUMMARY, summary);
		setParam(params, REQUIRED, API_PARAM_STEPS, steps);
		setParam(params, REQUIRED, API_PARAM_EXPECTED_RESULTS, expectedResults);
		setParam(params, OPTIONAL, API_PARAM_ORDER, order);
		setParam(params, OPTIONAL, API_PARAM_INTERNAL_ID, internalID);
		setParam(params, OPTIONAL, API_PARAM_CHECK_DUP_NAMES, checkDuplicatedName);
		setParam(params, OPTIONAL, API_PARAM_ACTION_DUP_NAME, actionOnDuplicatedName);
		setParam(params, OPTIONAL, API_PARAM_EXEC_TYPE, executionType);
		setParam(params, OPTIONAL, API_PARAM_IMPORTANCE, importance);
		executeRpcMethod(API_METHOD_CREATE_TEST_CASE, params);
		// The id returned in results is the id within
		// the project we want the actual test case id
		return TestLinkAPIHelper.getCaseID(this, projectID, suiteID, caseName);
	}
	
	/*
	 * This method is not supported by the TestLink API. It is
	 * needed so that JUnit test could be run without human
	 * intervention but right now manual creation is the only
	 * way to get to all the other test.
	 * 
	 * 
	 * @param projectName
	 * @param planName
	 * @param description
	 * @throws TestLinkAPIException
	 *
	 public Integer createTestPlan(
	 String projectName,
	 String planName,
	 String description) throws TestLinkAPIException
	 {
	 Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
	 if ( projectID != null ) {
	 return createTestPlan(projectID, planName, description);
	 } else {
	 throw new TestLinkAPIException("The project " + projectName + " was not found and the test plan " + planName + " could not be created.");
	 }
	 }
	 */
	
	/*
	 * This method is not supported by the TestLink API. It is
	 * needed so that JUnit test could be run without human
	 * intervention but right now manual creation is the only
	 * way to get to all the other test.
	 * 
	 * @param projectName
	 * @param planName
	 * @param description
	 * @throws TestLinkAPIException
	 *
	 public Integer createTestPlan(
	 Integer projectID,
	 String planName,
	 String description) throws TestLinkAPIException
	 {
	 initCache();
	 Hashtable params = new Hashtable();				
	 setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
	 setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID.toString());
	 setParam(params, REQUIRED, "testplanname", planName); // not supported by TestLink
	 setParam(params, REQUIRED, API_PARAM_NOTES, description);
	 TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_TEST_PLAN, params);
	 return getCreatedRecordIdentifier(results, API_RESULT_IDENTIFIER);
	 }
	 */


	/**
	 * The method creates a build under the provided project name and test plan.
	 * 
	 * @param projectName
	 * @param planName
	 * @param buildName
	 * @param buildNotes
	 * @return
	 * @throws TestLinkAPIException
	 */
	public Integer createBuild(
		String projectName,
		String planName,
		String buildName,
		String buildNotes) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		if ( projectID == null ) {
			throw new TestLinkAPIException(
				"The project " + projectName + " was not found and the build " + buildName
				+ " could not be created.");
		}
		Integer planID = TestLinkAPIHelper.getPlanID(this, projectID, planName);
		if ( planID != null ) {
			return createBuild(planID, buildName, buildNotes);
		} else {
			throw new TestLinkAPIException(
				"The plan " + planName + " was not found and the build " + buildName
				+ " could not be created.");
		}
	}
	
	/**
	 * The method creates a build under the test plan ID.
	 * 
	 * @param planID
	 * @param buildName
	 * @param buildNotes
	 * @return
	 * @throws TestLinkAPIException
	 */
	public Integer createBuild(
		Integer planID,
		String buildName,
		String buildNotes) throws TestLinkAPIException
	{
		initCache();
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PLAN_ID, planID);
		setParam(params, REQUIRED, API_PARAM_BUILD_NAME, buildName);
		setParam(params, REQUIRED, API_PARAM_BUILD_NOTES, buildNotes);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_BUILD, params);
		return getCreatedRecordIdentifier(results, API_RESULT_IDENTIFIER);
	}
	
	/**
	 * Appends that latest version of a test case to a test 
	 * plan with a default level of urgency. Can only handle
	 * test cases associated with first level suites.
	 * 
	 * 
	 * @param projectName
	 * @param planName
	 * @param testCaseName
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults addTestCaseToTestPlan(
		String projectName,
		String planName,
		String testCaseName) throws TestLinkAPIException
	{
		int maxNode = 0;
		TestLinkAPIResults cases = getCasesForTestPlan(projectName, planName);
		for ( int i = 0; i < cases.size(); i++ ) {
			Map data = cases.getData(i);
			Object node = data.get("execution_order");
			if ( node != null ) {
				try {
					Integer cn = new Integer(node.toString());
					if ( cn.intValue() > maxNode ) {
						maxNode = cn.intValue();
					}
				} catch ( Exception e ) {}
			}
		}
		maxNode++;
		TestLinkAPIResults results = addTestCaseToTestPlan(projectName, planName,
			testCaseName, new Integer(maxNode), null);	
		return results;
	}
	
	/**
	 * Appends that latest version of a test case to a test plan with
	 * a medium urgency. Can only handle test in first level suites.
	 * 
	 * @param projectName
	 * @param planName
	 * @param testCaseName
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults addTestCaseToTestPlan(
		String projectName,
		String planName,
		String testCaseName,
		Integer execOrder,
		String urgency) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		if ( projectID == null ) {
			throw new TestLinkAPIException(
				"The project " + projectName + " was not found and the test case "
				+ testCaseName + " could not be appended.");
		}
		Integer planID = TestLinkAPIHelper.getPlanID(this, projectID, planName);
		if ( planID == null ) {
			throw new TestLinkAPIException(
				"The plan " + planName + " was not found and the test case " + testCaseName
				+ " could not be appended.");
		}
		Integer caseID = TestLinkAPIHelper.getCaseID(this, projectID, testCaseName);
		if ( caseID == null ) {
			throw new TestLinkAPIException(
				"The test case " + testCaseName + " was not found and the test case"
				+ " could not be appended to test plan " + planName + ".");
		}
		Map caseInfo = TestLinkAPIHelper.getTestCaseInfo(this, projectID, caseID);
		if ( caseInfo == null ) {
			throw new TestLinkAPIException(
				"The test case identifier " + caseID + " was not found and the test case "
				+ testCaseName + " could not be appended to test plan " + planName + ".");
		}
		Map projectInfo = TestLinkAPIHelper.getProjectInfo(this, projectName);
		if ( projectInfo == null ) {
			throw new TestLinkAPIException(
				"The project information for " + projectName
				+ " was not found and the test case " + testCaseName
				+ " could not be appended to test plan " + planName + ".");
		}
		Object prefix = projectInfo.get("prefix");
		Object externalID = caseInfo.get(API_RESULT_TC_EXTERNAL_ID);
		String visibleTestCaseID = prefix.toString() + '-' + externalID.toString();
		Object version = caseInfo.get(API_PARAM_VERSION);
		TestLinkAPIResults results = addTestCaseToTestPlan(projectID, planID,
			visibleTestCaseID, new Integer(version.toString()), execOrder, urgency);	
		return results;
	}
	
	/**
	 * The method adds a test case from a project to the test plan.
	 * 
	 * @param projectID
	 * @param planID
	 * @param testCaseID
	 * @param version
	 * @param ExecOrder
	 * @param Urgency
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults addTestCaseToTestPlan(
		Integer projectID,
		Integer planID,
		String testCaseVisibleID,
		Integer version,
		Integer execOrder,
		String urgency) throws TestLinkAPIException
	{
		initCache();
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID);
		setParam(params, REQUIRED, API_PARAM_TEST_PLAN_ID, planID);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_ID_EXTERNAL, testCaseVisibleID);
		setParam(params, REQUIRED, API_PARAM_VERSION, version);
		setParam(params, OPTIONAL, API_PARAM_URGENCY, urgency);
		setParam(params, OPTIONAL, API_PARAM_EXEC_ORDER, execOrder);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_ADD_TEST_CASE_TO_PLAN,
			params);
		if ( results.size() < 1 ) {
			throw new TestLinkAPIException(
				"Could not add test case " + testCaseVisibleID + " to test plan id " + planID);
		}
		Map data = results.getData(0);
		if ( hasError(data) ) {
			throw new TestLinkAPIException(
				"Could not add test case " + testCaseVisibleID + " to test plan id " + planID
				+ ". Results Message: [" + data.toString() + "]");
		}
		return results;
	}
				
	/**
	 * Get a list of all the test projects
	 * 
	 * @return The results from the TestLink API as a list of Map entries
	 */
	public TestLinkAPIResults getProjects() throws TestLinkAPIException
	{
		if ( projects == null || useCache == false ) {
			Hashtable params = new Hashtable();	
			setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
			projects = executeRpcMethod(API_METHOD_GET_PROJECTS, params);
		}
		return projects;
	}
	
	public TestLinkAPIResults getProjectTestPlans(
		String projectName) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		return getProjectTestPlans(projectID);
	}
	
	/**
	 * Get the lest of test plans for a project
	 * 
	 * @param projectID
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults getProjectTestPlans(
		Integer projectID) throws TestLinkAPIException
	{
		boolean isCached = true;
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID);
		
		if ( plans == null || useCache == false ) {
			plans = new HashMap();
			isCached = false;
		} else if ( !plans.containsKey(projectID) ) {
			isCached = false;
		}
		
		if ( isCached == false ) {
			TestLinkAPIResults results = executeRpcMethod(
				API_METHOD_GET_PROJECT_TEST_PLANS, params);
			plans.put(projectID, results);		
		}
		
		return (TestLinkAPIResults) plans.get(projectID);
	}
	
	/**
	 * Get a list of builds for a test plan id
	 * 
	 * @param planID	Required
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults getBuildsForTestPlan(
		Integer planID) throws TestLinkAPIException
	{
		boolean isCached = true;
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PLAN_ID, planID);
			
		if ( buildsForPlan == null || useCache == false ) {
			buildsForPlan = new HashMap();
			isCached = false;
		} else if ( !buildsForPlan.containsKey(planID) ) {
			isCached = false;
		}
			
		if ( isCached == false ) {
			TestLinkAPIResults results = executeRpcMethod(API_METHOD_GET_BUILDS_FOR_PLAN,
				params);
			buildsForPlan.put(planID, results);		
		}
			
		return (TestLinkAPIResults) buildsForPlan.get(planID);
	}
	
	/**
	 * Get all the first level project test suites by project name
	 * 
	 * @param projectName
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults getFirstLevelTestSuitesForTestProject(
		String projectName) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		return getFirstLevelTestSuitesForTestProject(projectID);
	}
	
	/**
	 * Get all the first level project test suites by project id
	 * 
	 * @param projectID
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults getFirstLevelTestSuitesForTestProject(
		Integer projectID) throws TestLinkAPIException
	{
		boolean isCached = true;
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID);
		
		if ( firstLevelSuites == null || useCache == false ) {
			firstLevelSuites = new HashMap();
			isCached = false;
		} else if ( !firstLevelSuites.containsKey(projectID) ) {
			isCached = false;
		}
		
		if ( isCached == false ) {
			TestLinkAPIResults results = executeRpcMethod(
				API_METHOD_GET_FIRST_LEVEL_SUITES_FOR_PROJECT, params);
			firstLevelSuites.put(projectID, results);
		}
		
		return (TestLinkAPIResults) firstLevelSuites.get(projectID);
	}
	
	/**
	 * Get test cases for test suite
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 * @param String status 
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */ 
	public TestLinkAPIResults getCaseIDsByName(
		String testCaseName,
		String status) throws TestLinkAPIException
	{ 
		// TODO: Cache
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_NAME, testCaseName);
		return executeRpcMethod(API_METHOD_GET_TEST_CASE_IDS_BY_NAME, params);
	}
	
	/**
	 * Get test cases for test suite
	 * 
	 * @param int testProjectID		Required
	 * @param int testSuiteID		Required
	 * @return The results from the TestLink API as a list of Map entries
	 */ 
	public TestLinkAPIResults getCasesForTestSuite(
		Integer testProjectID,
		Integer testSuiteID) throws TestLinkAPIException
	{ 
		boolean isCached = true;
		String key = testProjectID.toString() + "-" + testSuiteID.toString();
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, testProjectID);
		setParam(params, REQUIRED, API_PARAM_TEST_SUITE_ID, testSuiteID);
		setParam(params, REQUIRED, API_PARAM_DEPTH_FLAG, true);
		setParam(params, REQUIRED, API_PARAM_DETAILS, "full");
		
		if ( casesForSuite == null || useCache == false ) {
			casesForSuite = new HashMap();
			isCached = false;
		} else if ( !casesForSuite.containsKey(key) ) {
			isCached = false;
		}
		
		if ( isCached == false ) {
			TestLinkAPIResults results = executeRpcMethod(
				API_METHOD_GET_TEST_CASES_FOR_SUITE, params);
			casesForSuite.put(key, results);
		}
		
		return (TestLinkAPIResults) casesForSuite.get(key);
	}
	
	/**
	 * 
	 * @param projectName
	 * @param planName
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults getCasesForTestPlan(
		String projectName,
		String planName
		) throws TestLinkAPIException
	{ 
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		if ( projectID == null ) {
			throw new TestLinkAPIException(
				"Could not get project identifier for " + projectName);
		}
		Integer planID = TestLinkAPIHelper.getPlanID(this, projectID, planName);
		if ( planID == null ) {
			throw new TestLinkAPIException("Could not get plan identifier for " + planName);
		}
		return getCasesForTestPlan(planID);
	}
	
	/**
	 * 
	 * @param testPlanID
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults getCasesForTestPlan(
		Integer testPlanID
		) throws TestLinkAPIException
	{ 
		return getCasesForTestPlan(testPlanID, null, null, null, null, null, null, null);
	}
	
	/**
	 * Get all the test cases associated with a test plan.
	 * 
	 * Note: Only the testPlanID is currently supported
	 * 
	 * @param testPlanID
	 * @param testCaseID
	 * @param buildID
	 * @param keywordID
	 * @param executed
	 * @param assignedTo
	 * @param execStatus
	 * @param execType
	 * @return The results from the TestLink API as a list of Map entries
	 * @throws TestLinkAPIException
	 */
	private TestLinkAPIResults getCasesForTestPlan(
		Integer testPlanID,
		Integer testCaseID,
		Integer buildID,
		Integer keywordID,
		String executed,
		String assignedTo,
		String execStatus,
		String execType
		) throws TestLinkAPIException
	{ 
		boolean isCached = true;
		
		// setup the key
		String key = "Plan:" + testPlanID.toString();
		
		key += "Case:";
		if ( testCaseID != null ) {
			key += testCaseID.toString();
		}
		
		key += "Build:";
		if ( buildID != null ) {
			key += buildID.toString();
		}
		
		key += "KW:";
		if ( keywordID != null ) {
			key += keywordID.toString();
		}
		
		key += "Exec:";
		if ( executed != null ) {
			key += executed;
		}
		
		key += "Assign:";
		if ( assignedTo != null ) {
			key += assignedTo;
		}
		
		key += "ES:";
		if ( execStatus != null ) {
			key += execStatus;
		}
		
		key += "ET:";
		if ( execType != null ) {
			key += execType;
		}
				
		// Setup hash parameters
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PLAN_ID, testPlanID);
		
		// TODO : Add all the optional parameters
		
		// Check the hash
		if ( casesForPlan == null || useCache == false ) {
			casesForPlan = new HashMap();
			isCached = false;
		} else if ( !casesForPlan.containsKey(key) ) {
			isCached = false;
		}
		
		if ( isCached == false ) {
			TestLinkAPIResults results = executeRpcMethod(
				API_METHOD_GET_TEST_CASES_FOR_PLAN, params);
			casesForPlan.put(key, results);
		}
		
		return (TestLinkAPIResults) casesForPlan.get(key);
	}
	
	/* =========================================== */
	
	/* Private Methods                */
	
	/* =========================================== */
	
	private void initCache()
	{
		projects = null;
		plans = null;
		firstLevelSuites = null;
		casesForSuite = null;
		casesForPlan = null;
		buildsForPlan = null;
	}
	
	/*
	 * Private method used to make xml-rpc method calls
	 * to the TestLink api URL.
	 */
	
	private TestLinkAPIResults executeRpcMethod(
		String method,
		Hashtable executionData) throws TestLinkAPIException
	{
		TestLinkAPIResults results = new TestLinkAPIResults();
		ArrayList<Object> params = new ArrayList<Object>();
		XmlRpcClient rpcClient = getRpcClient();
		int unknownResultTypeCnt = 0;
		
		try {
			params.add(executionData);
			Object[] rawResults = (Object[]) rpcClient.execute(method, params);			
			for ( int i = 0; i < rawResults.length; i++ ) {
				Object result = (Map) rawResults[i];
				if ( result instanceof Map ) {
					Map item = (Map) result;
					results.add(item);
				} else {
					unknownResultTypeCnt++;
					HashMap data = new HashMap();
					data.put(getUnknownKey(unknownResultTypeCnt), result);
					results.add(data);
				}
			}
		} catch ( Exception e ) {
			
			// Try without casting to an Object[] list since XML-RPC officially returns
			// an Object but TestLink API is known to return Object[] list and cast is
			// tried first and then if that does not work we go with Object.
			try {
				Object single = rpcClient.execute(method, params);	
				if ( single instanceof Map ) {
					results.add((Map) single);
				} else {
					unknownResultTypeCnt++;
					HashMap data = new HashMap();
					data.put(getUnknownKey(unknownResultTypeCnt), single);
					results.add(data);
				}
			} catch ( Exception ee ) {
				throw new TestLinkAPIException("The call to the xml-rpc client failed.", e);
			}
		}
		return results;
	}
	
	private static String getUnknownKey(
		int cnt)
	{
		String key = new Integer(cnt).toString();
		return "RESULT_NUM_" + key + "_OF_UNKNOWN_TYPE";
	}
	
	/*
	 * Create the rpc client to an xml rpc request can be made.
	 */
	private XmlRpcClient getRpcClient() throws TestLinkAPIException
	{
		XmlRpcClient rpcClient = null;
		XmlRpcClientConfigImpl config;
		
		try {
			config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(SERVER_URL));
			rpcClient = new XmlRpcClient();
			rpcClient.setConfig(config);
		} catch ( Exception e ) {
			throw new TestLinkAPIException("Unable to create a XML-RPC client.", e);
		}
		
		return rpcClient;		
	}
	
	/*
	 * Assign the parameter
	 */
	private void setParam(
		Hashtable params, 
		boolean isRequired, 
		String paramName, 
		Object value) throws TestLinkAPIException
	{
		// If the value is required and it is null return exception
		if ( isRequired && value == null ) {
			throw new TestLinkAPIException(
				"The required parameter " + paramName + " was not provided by the caller.");
		}
		
		// If the value is not required and it is null then return without assignment
		if ( !isRequired && value == null ) {
			return;
		}
		
		// Set the parameter for the XML-RPC call
		try {
			Integer intTypeValue = new Integer(value.toString());
			params.put(paramName, intTypeValue.intValue());
		} catch ( Exception e ) {
			params.put(paramName, value.toString());
		}
	}
	
	private Integer getCreatedRecordIdentifier(
		TestLinkAPIResults results,
		String idKey) throws TestLinkAPIException
	{
		Integer newID = null;
		if ( results.size() == 1 ) {
			Object id = results.getValueByName(0, idKey);
			if ( id != null ) {
				newID = new Integer(id.toString());
			} else {
				Map msg = results.getData(0);
				String failMsg = "Create failed since the identifier for new record was not retrieved.\nAPI Returned Data: ["
					+ msg.toString() + "]";
				throw new TestLinkAPIException(failMsg);
			}
		}
		return newID;
	}
	
	/*
	 * There seems to be no standard message coming from the 
	 * TestLink API to indicate there is an error. So this is 
	 * a kludge trying to figure out if the message is an error
	 * since it contains both a message and code.
	 * 
	 * Maybe it is the XML-RPC.
	 * 
	 */
	private boolean hasError(TestLinkAPIResults results) {
		if ( results.size() < 1 ) {
			return true;
		}
		return hasError(results.getData(0));
	}
	
	private boolean hasError(
		Map data)
	{
		String message = (String) data.get(API_RESULT_MESSAGE);
		Object code = data.get(API_RESULT_CODE);
		if ( message != null && code != null ) {
			if ( !message.toLowerCase().contains("success") ) {
				return true;
			}
		}
		return false;
	}
}

