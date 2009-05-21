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
package org.dbfacade.testlink.rpc.api;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.apache.xmlrpc.XmlRpcException;
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
	public static String DEV_KEY; 
	public static String SERVER_URL; 
	
	public TestLinkAPIClient(
		String devKey,
		String url)
	{
		DEV_KEY = devKey;
		SERVER_URL = url;
	}
	
	/**
	 * Report test execution results to TestLink 
	 * application using the TestLink API 
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 * @param String status 
	 */ 
	public void reportTestCaseResult(
		int testCaseID,
		int testPlanID,
		String status) throws TestLinkAPIException
	{ 
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_ID, testCaseID);
		setParam(params, REQUIRED, API_PARAM_TEST_PLAN_ID, testPlanID);
		setParam(params, REQUIRED, API_PARAM_STATUS, status);
		executeRpcMethod(API_METHOD_REPORT_TEST_RESULT, params);
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
			throw new TestLinkAPIException("The project " + projectName + " was not found and the test suite " + suiteName + " could not be created.");
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
		return createTestCase(authorLoginName, projectID, suiteID, testCaseName, summary, steps, expectedResults, null,
			null, null, null, null, importance);
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
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_TEST_CASE, params);
		return getCreatedRecordIdentifier(results, API_RESULT_IDENTIFIER);
	}
	
	/*
	 * This method is not supported by the TestLink API. It is
	 * needed so that JUnit test could be run without human
	 * intervention but right now manual creation in the only
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
	 * intervention but right now manual creation in the only
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
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID.toString());
		setParam(params, REQUIRED, "testplanname", planName); // not suppirted by TestLink
		setParam(params, REQUIRED, API_PARAM_NOTES, description);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_TEST_PLAN, params);
		return getCreatedRecordIdentifier(results, API_RESULT_IDENTIFIER);
	}
	*/


	/**
	 * Get all the test projects
	 */
	public TestLinkAPIResults getProjects() throws TestLinkAPIException
	{
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		return executeRpcMethod(API_METHOD_GET_PROJECTS, params);
	}
	
	public TestLinkAPIResults getProjectTestPlans(String projectName) throws TestLinkAPIException
	{
		Integer projectID = TestLinkAPIHelper.getProjectID(this, projectName);
		return getProjectTestPlans(projectID);
	}
	
	
	public TestLinkAPIResults getProjectTestPlans(Integer projectID) throws TestLinkAPIException
	{
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID);
		return executeRpcMethod(API_METHOD_GET_PROJECT_TEST_PLANS, params);
	}
	
	
	/**
	 * Get all the first level project test suites by project name
	 * 
	 * @param projectName
	 * @return
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
	 * @return
	 * @throws TestLinkAPIException
	 */
	public TestLinkAPIResults getFirstLevelTestSuitesForTestProject(
		Integer projectID) throws TestLinkAPIException
	{
		Hashtable params = new Hashtable();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID);
		return executeRpcMethod(API_METHOD_GET_FIRST_LEVEL_SUITES_FOR_PROJECT, params);
	}
	
	/**
	 * Get test cases for test suite
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 * @param String status 
	 */ 
	public TestLinkAPIResults getCaseIDsByName(
		String testCaseName,
		String status) throws TestLinkAPIException
	{ 
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_NAME, testCaseName);
		return executeRpcMethod(API_METHOD_GET_TEST_CASE_IDS_BY_NAME, params);
	}
	
	/**
	 * Get test cases for test suite
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 */ 
	public TestLinkAPIResults getCasesForTestSuite(
		int testProjectID,
		int testSuiteID) throws TestLinkAPIException
	{ 
		Hashtable params = new Hashtable();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, testProjectID);
		setParam(params, REQUIRED, API_PARAM_TEST_SUITE_ID, testSuiteID);
		setParam(params, REQUIRED, API_PARAM_DEPTH_FLAG, true);
		setParam(params, REQUIRED, API_PARAM_DETAILS, "full");
		return executeRpcMethod(API_METHOD_GET_TEST_CASES_FOR_SUITE, params);
	}
	
	/**
	 * Private method used to make xml-rpc method calls
	 * to the TestLink api URL.
	 * 
	 * @param method
	 * @param params
	 */
	
	private TestLinkAPIResults executeRpcMethod(
		String method,
		Hashtable executionData) throws TestLinkAPIException
	{
		TestLinkAPIResults results = new TestLinkAPIResults();
		ArrayList<Object> params = new ArrayList<Object>();
		XmlRpcClient rpcClient = getRpcClient();
		
		try {
			params.add(executionData);
			Object[] result = (Object[]) rpcClient.execute(method, params);			
			for ( int i = 0; i < result.length; i++ ) {
				Map item = (Map) result[i];
				results.add(item);
			}
		} catch ( XmlRpcException e ) {
			throw new TestLinkAPIException("The call to the xml-rpc client failed.", e);
		}
		return results;
	}
	
	/**
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
		} catch ( MalformedURLException e ) {
			throw new TestLinkAPIException("Unable to create a XML-RPC client.", e);
		}
		
		return rpcClient;		
	}
	
	/**
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
		params.put(paramName, value.toString());
	}
	
	private Integer getCreatedRecordIdentifier(TestLinkAPIResults results, String idKey) throws TestLinkAPIException {
		Integer newID=null;
		if ( results.size() == 1 ) {
			Object id = results.getValueByName(0, idKey);
			if ( id != null ) {
				newID = new Integer(id.toString());
			} else {
				Map msg = results.getData(0);
				String failMsg = "Create failed since the identifier for new record was not retrieved.\nAPI Returned Data: [" + msg.toString() + "]";
				throw new TestLinkAPIException(failMsg);
			}
		}
		return newID;
	}
}

