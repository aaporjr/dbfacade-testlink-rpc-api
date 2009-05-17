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
	
	public TestLinkAPIClient(String devKey, String url)
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
	public void reportTestCaseResult(int testCaseID, int testPlanID, String status) 
		throws TestLinkAPIException
	{ 
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_ID, testCaseID);
		setParam(params, REQUIRED, API_PARAM_TEST_PLAN_ID, testPlanID);
		setParam(params, REQUIRED, API_PARAM_STATUS, status);
		executeRpcMethod(API_METHOD_REPORT_TEST_RESULT, params);
	}
	
	/**
	 * Create a new project in TestLink. The method returns the id of
	 * the project if it successfully created otherwise it returns null.
	 * 
	 * @param projectName
	 * @param testCasePrefix
	 * @param description
	 */
	public Integer createTestProject(String projectName, String testCasePrefix, String description) 
		throws TestLinkAPIException
	{ 
		Integer projectID = null;
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_NAME, projectName);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_PREFIX, testCasePrefix);
		setParam(params, REQUIRED, API_PARAM_NOTES, description);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_PROJECT, params);
		if ( results.size() == 1 ) {
			Object id = results.getValue(0, API_RESULT_IDENTIFIER);
			if ( id != null ) {
				projectID = new Integer(id.toString());
			}
		} else {
			throw new TestLinkAPIException("Project " + projectName + " was not added");
		}
		return projectID;
	}
	
	/**
	 * Create top level test suite under a specific project name
	 * 
	 * @param projectName
	 * @param suiteName
	 * @param description
	 * @throws TestLinkAPIException
	 */
	public Integer createTestSuite(String projectName, String suiteName, String description) 
		throws TestLinkAPIException
	{
		Object id = null;
		TestLinkAPIResults results = getProjects();
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValue(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( projectName.equals(data.toString()) ) {
					id = results.getValue(i, API_RESULT_IDENTIFIER);
					break;
				}
			}
		}
		
		if ( id != null ) {
			Integer projectID = new Integer(id.toString());
			return createTestSuite(projectID, suiteName, description);
		}
		
		return null;
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
	public Integer createTestSuite(Integer projectID, String suiteName, String description) 
		throws TestLinkAPIException
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
		Boolean check)
		throws TestLinkAPIException
	{
		Integer suiteID=null;
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_PROJECT_ID, projectID.toString());
		setParam(params, REQUIRED, API_PARAM_TEST_SUITE_NAME, suiteName);
		setParam(params, REQUIRED, API_PARAM_DETAILS, description);
			setParam(params, OPTIONAL, API_PARAM_PARENT_ID, parentID.toString());
			setParam(params, OPTIONAL, API_PARAM_ORDER, order.toString());
			setParam(params, OPTIONAL, API_PARAM_CHECK_DUP_NAMES, check.toString());
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_SUITE, params);
		if ( results.size() == 1 ) {
			Object id = results.getValue(0, API_RESULT_IDENTIFIER);
			if ( id != null ) {
				suiteID = new Integer(id.toString());
			}
		}
		return suiteID;
	}
	
	public Integer createTestCase(
			Integer suiteID,
            String caseName,
            String description,
            String steps,
            String expectedResults,
            String order,
            Integer internalID,
            Boolean checkDuplicatedName,                        
            String actionOnDuplicatedName,
            String executionType,
            String importance) 
		throws TestLinkAPIException
	{
		Integer testCaseID=null;
		Hashtable<String, Object> params = new Hashtable<String, Object>();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_SUITE_ID, suiteID);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_NAME, caseName);
		setParam(params, REQUIRED, API_PARAM_SUMMARY, description);
		setParam(params, REQUIRED, API_PARAM_STEPS, steps);
		setParam(params, REQUIRED, API_PARAM_EXPECTED_RESULTS, expectedResults);
		setParam(params, OPTIONAL, API_PARAM_ORDER, order);
		setParam(params, OPTIONAL, API_PARAM_INTERNAL_ID, internalID);
		setParam(params, OPTIONAL, API_PARAM_CHECK_DUP_NAMES, checkDuplicatedName);
		setParam(params, OPTIONAL, API_PARAM_ACTION_DUP_NAME, actionOnDuplicatedName);
		setParam(params, OPTIONAL, API_PARAM_EXEC_TYPE, executionType);
		setParam(params, OPTIONAL, API_PARAM_IMPORTANCE, importance);
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_TEST_CASE, params);
		if ( results.size() == 1 ) {
			Object id = results.getValue(0, API_RESULT_IDENTIFIER);
			if ( id != null ) {
				testCaseID = new Integer(id.toString());
			}
		}
		return testCaseID;
	}

	/**
	 * Get all the test projects
	 */
	public TestLinkAPIResults getProjects() 
		throws TestLinkAPIException
	{
		Hashtable<String, Object> params = new Hashtable<String, Object>();	
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		return executeRpcMethod(API_METHOD_GET_PROJECTS, params);
	}
	
	/**
	 * Get test cases for test suite
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 * @param String status 
	 */ 
	public TestLinkAPIResults getCaseIDsByName(String testCaseName, String status) 
		throws TestLinkAPIException
	{ 
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
		setParam(params, REQUIRED, API_PARAM_DEV_KEY, DEV_KEY);
		setParam(params, REQUIRED, API_PARAM_TEST_CASE_NAME, testCaseName);
		return executeRpcMethod(API_METHOD_GET_TEST_CASE_IDS_BY_NAME, params);
	}
	
	/**
	 * Get test cases for test suite
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 * @param String status 
	 */ 
	public TestLinkAPIResults getCasesForTestSuite(int testProjectID, int testSuiteID, String status) 
		throws TestLinkAPIException
	{ 
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
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
	
	private TestLinkAPIResults executeRpcMethod(String method, Hashtable executionData)
		throws TestLinkAPIException
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
	private XmlRpcClient getRpcClient() 
		throws TestLinkAPIException
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
			Hashtable<String, Object> params, 
			boolean isRequired, 
			String paramName, 
			Object value)
		throws TestLinkAPIException
	{
		// If the value is required and it is null return exception
		if ( isRequired && value == null ) {
			throw new TestLinkAPIException("The required parameter " + paramName + " was not provided by the caller.");
		}
		
		// If the value is not required and it is null then return without assignment
		if ( !isRequired && value == null ) {
			return;
		}
		
		// Set the parameter for the XML-RPC call
		params.put(paramName, value);
	}
}

