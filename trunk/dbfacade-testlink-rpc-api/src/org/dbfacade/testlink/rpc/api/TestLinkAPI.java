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
public class TestLinkAPI implements TestLinkAPIConst
{
	public static String DEV_KEY; 
	public static String SERVER_URL; 
	
	public TestLinkAPI(String devKey, String url)
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
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
		params.put(API_PARAM_TEST_CASE_ID, testCaseID);
		params.put(API_PARAM_TEST_PLAN_ID, testPlanID);
		params.put(API_PARAM_STATUS, status);
		executeRpcMethod(API_METHOD_REPORT_TEST_RESULT, params);
	}
	
	/**
	 * Create a new project in TestLink
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
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
		params.put(API_PARAM_TEST_PROJECT_NAME, projectName);
		params.put(API_PARAM_TEST_CASE_PREFIX, testCasePrefix);
		params.put(API_PARAM_NOTES, description);
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
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
		params.put(API_PARAM_TEST_PROJECT_ID, projectID.toString());
		params.put(API_PARAM_TEST_SUITE_NAME, suiteName);
		params.put(API_PARAM_DETAILS, description);
		if ( parentID != null ) {
			params.put(API_PARAM_PARENT_ID, parentID.toString());
		}
		if ( order != null ) {
			params.put(API_PARAM_ORDER, order.toString());
		}
		if ( check != null ) {
			params.put(API_PARAM_CHECK_DUP_NAMES, check.toString());
		}
		TestLinkAPIResults results = executeRpcMethod(API_METHOD_CREATE_SUITE, params);
		if ( results.size() == 1 ) {
			Object id = results.getValue(0, API_RESULT_IDENTIFIER);
			if ( id != null ) {
				projectID = new Integer(id.toString());
			}
		}
		return projectID;
	}

	/**
	 * Get all the test projects
	 */
	public TestLinkAPIResults getProjects() 
		throws TestLinkAPIException
	{
		Hashtable<String, Object> params = new Hashtable<String, Object>();	
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
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
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
		params.put(API_PARAM_TEST_CASE_NAME, testCaseName);
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
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
		params.put(API_PARAM_TEST_PROJECT_ID, testProjectID);
		params.put(API_PARAM_TEST_SUITE_ID, testSuiteID);
		params.put(API_PARAM_DEPTH_FLAG, true);
		params.put(API_PARAM_DETAILS, "full");
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
}

