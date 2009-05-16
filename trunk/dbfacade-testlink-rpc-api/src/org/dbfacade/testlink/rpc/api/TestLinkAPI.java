package org.dbfacade.testlink.rpc.api;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


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
	 * report test execution to TestLink API 
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 * @param String status 
	 */ 
	public void testCaseResult(int testCaseID, int testPlanID, String status) 
	{ 
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
		params.put(API_PARAM_TEST_CASE_ID, testCaseID);
		params.put(API_PARAM_TEST_PLAN_ID, testPlanID);
		params.put(API_PARAM_STATUS, status);
		executeRpcMethod(API_METHOD_REPORT_TEST_CASE, params);
	}
	
	
	/**
	 * Get test cases for test suite
	 * 
	 * @param int testCaseID 
	 * @param int testPlanID
	 * @param String status 
	 */ 
	public TestLinkAPIResults getCaseIDsByName(String testCaseName, String status) 
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
	{ 
		Hashtable<String, Object> params = new Hashtable<String, Object>();				
		params.put(API_PARAM_DEV_KEY, DEV_KEY);
		params.put(API_PARAM_TEST_PROJECT_ID, testProjectID);
		params.put(API_PARAM_TEST_SUITE_ID, testSuiteID);
		params.put(API_PARAM_DEPTH_FLAG, true);
		params.put(API_PARAM_DETAIL_TYPE, "full");
		return executeRpcMethod(API_METHOD_GET_TEST_CASES_FOR_SUITE, params);
	}
	
	/**
	 * Private method used to make rpc method calls
	 * 
	 * @param method
	 * @param params
	 */
	
	private TestLinkAPIResults executeRpcMethod(String method, Hashtable executionData)
	{
		TestLinkAPIResults results = new TestLinkAPIResults();
		ArrayList<Object> params = new ArrayList<Object>();
		try {
		    params.add(executionData);
			XmlRpcClient rpcClient = getRpcClient();
			Object[] result = (Object[]) rpcClient.execute("tl.reportTCResult", params);

			// Typically you'd want to validate the result here and probably do something more useful with it
			System.out.println("Result was:\n");				
			for ( int i = 0; i < result.length; i++ ) {
				Map item = (Map) result[i];
				results.add(item);
			}
		} catch ( XmlRpcException e ) {
			e.printStackTrace();
		}
		return results;
	}
	
	/**
	 * Create the rpc client to an xml rpc request can be made.
	 */
	private XmlRpcClient getRpcClient()
	{
		XmlRpcClient rpcClient = null;
		XmlRpcClientConfigImpl config;
		
		try {
			config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(SERVER_URL));
			rpcClient = new XmlRpcClient();
			rpcClient.setConfig(config);
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
		
		return rpcClient;		
	}
	
	/**
	 * User for testing against a dummy test plan 
	 * and project in the TestLink database 
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{ 
		TestLinkAPI api = new TestLinkAPI("xxxxxx", "URL");
		api.testCaseResult(4, 4, "b"); 
	} 
}

