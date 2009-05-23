package org.dbfacade.testlink.rpc.api;


import java.util.ArrayList;
import java.util.Map;


/**
 * This class should maintain visibility only to the package
 * and not outside of the package. The class contains a set of 
 * methods used to organize the code and help maintain the
 * TestAPIClient class focused on the XML-RPS TestLink API
 * method calls.
 * 
 */
public class TestLinkAPIHelper implements TestLinkAPIConst
{

	/**
	 * Get the project identifier by test project name.
	 * 
	 * @param apiClient
	 * @param projectName
	 * @return
	 * @throws TestLinkAPIException
	 */
	static Integer getProjectID(
		TestLinkAPIClient apiClient,
		String projectName) throws TestLinkAPIException
	{
		Map data = getProjectInfo(apiClient, projectName);
		return getIdentifier(data);
	}
	
	static Map getProjectInfo(
		TestLinkAPIClient apiClient,
		String projectName) throws TestLinkAPIException
	{
		TestLinkAPIResults results = apiClient.getProjects();
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValueByName(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( projectName.equals(data.toString()) ) {
					return results.getData(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the suite identifier by test project name and test suite name
	 * 
	 * @param apiClient
	 * @param projectName
	 * @param suiteName
	 * @return
	 * @throws TestLinkAPIException
	 */
	static Integer getSuiteID(
		TestLinkAPIClient apiClient, 
		String projectName, 
		String suiteName) throws TestLinkAPIException
	{
		Integer projectID = getProjectID(apiClient, projectName);
		return getSuiteID(apiClient, projectID, suiteName);
	}
	
	static Map getSuiteInfo(
		TestLinkAPIClient apiClient, 
		String projectName, 
		String suiteName) throws TestLinkAPIException
	{
		Integer projectID = getProjectID(apiClient, projectName);
		return getSuiteInfo(apiClient, projectID, suiteName);
	}
	
	/**
	 * Get the suite identifier by test project id and test suite name
	 * 
	 * @param apiClient
	 * @param projectID
	 * @param suiteName
	 * @return
	 * @throws TestLinkAPIException
	 */
	static Integer getSuiteID(
		TestLinkAPIClient apiClient, 
		Integer projectID, 
		String suiteName) throws TestLinkAPIException
	{
		Map data = getSuiteInfo(apiClient, projectID, suiteName);
		return getIdentifier(data);
	}
	
	/**
	 * Get the suite record information by test project id and test suite name
	 * @param apiClient
	 * @param projectID
	 * @param suiteName
	 * @return
	 * @throws TestLinkAPIException
	 */
	static Map getSuiteInfo(
		TestLinkAPIClient apiClient, 
		Integer projectID, 
		String suiteName) throws TestLinkAPIException
	{
		TestLinkAPIResults results = apiClient.getFirstLevelTestSuitesForTestProject(
			projectID);
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValueByName(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( suiteName.equals(data.toString()) ) {
					return results.getData(i);
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the test case identifier for a case name within a project.
	 * 
	 * @param apiClient
	 * @param projectID
	 * @param caseName
	 * @return
	 * @throws TestLinkAPIException
	 */
	static Integer getCaseID(
		TestLinkAPIClient apiClient,
		Integer projectID,
		String caseName) throws TestLinkAPIException
	{
		ArrayList cases = new ArrayList();
		TestLinkAPIResults results = apiClient.getFirstLevelTestSuitesForTestProject(
			projectID);
		for ( int i = 0; i < results.size(); i++ ) {
			Object id = results.getValueByName(i, API_RESULT_IDENTIFIER);
			if ( id != null ) {
				addAllMatchingCases(apiClient, cases, projectID,
					new Integer(id.toString()), caseName);
			}
		}
		Map data = getLatestVersionCaseID(cases);
		return getIdentifier(data);
	}
	
	/**
	 * Get the a test case identifier by test project id, suite id and test case name.
	 * 
	 * @param apiClient
	 * @param projectID
	 * @param suiteID
	 * @param caseName
	 * @return
	 * @throws TestLinkAPIException
	 */
	static Integer getCaseID(
		TestLinkAPIClient apiClient, 
		Integer projectID, 
		Integer suiteID,
		String caseName) throws TestLinkAPIException
	{
		ArrayList cases = new ArrayList();
		addAllMatchingCases(apiClient, cases, projectID, suiteID, caseName);
		Map data = getLatestVersionCaseID(cases);
		return getIdentifier(data);
	}
		
	/**
	 * Find the matching test case information and add it
	 * to the array list passes as a parameter.
	 * 
	 * @param apiClient
	 * @param cases
	 * @param projectID
	 * @param suiteID
	 * @param caseName
	 * @throws TestLinkAPIException
	 */
	static void addAllMatchingCases(
		TestLinkAPIClient apiClient, 
		ArrayList cases,
		Integer projectID, 
		Integer suiteID,
		String caseName) throws TestLinkAPIException
	{
		TestLinkAPIResults results = apiClient.getCasesForTestSuite(projectID, suiteID);
		Object id = null;
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValueByName(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( caseName.equals(data.toString()) ) {
					id = results.getValueByName(i, API_RESULT_IDENTIFIER);
					if ( id != null ) {
						cases.add(results.getData(i));
					}
				}
			}
		}
	}
	
	/**
	 * Find the matching test case information and add it
	 * to the array list passes as a parameter.
	 * 
	 * @param apiClient
	 * @param cases
	 * @param projectID
	 * @param suiteID
	 * @param caseName
	 * @throws TestLinkAPIException
	 */
	static Map getTestCaseInfo(
		TestLinkAPIClient apiClient, 
		Integer projectID, 
		Integer testCaseID) throws TestLinkAPIException
	{
		TestLinkAPIResults suites = apiClient.getFirstLevelTestSuitesForTestProject(
			projectID);
		for ( int i = 0; i < suites.size(); i++ ) {
			Object id = suites.getValueByName(i, API_RESULT_IDENTIFIER);
			if ( id != null ) {
				Integer suiteID = new Integer(id.toString());
				TestLinkAPIResults cases = apiClient.getCasesForTestSuite(projectID,
					suiteID);
				for ( int c = 0; c < cases.size(); c++ ) {
					id = cases.getValueByName(c, API_RESULT_IDENTIFIER);
					Integer currentTestCase = new Integer(id.toString());
					if ( currentTestCase.compareTo(testCaseID) == 0 ) {
						return (Map) cases.getData(c);
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the latest version for the test cases passed in the array.
	 * The method assumes these are the results of all cases within 
	 * a suite or project that matched a specific case name.
	 * 
	 * @param cases
	 * @return
	 */
	private static Map getLatestVersionCaseID(
		ArrayList cases)
	{
		int maxVersion = 0;
		
		// find the max version
		for ( int i = 0; i < cases.size(); i++ ) {
			Map data = (Map) cases.get(i);
			Object version = data.get("tcversion_id");
			if ( version != null ) {
				int cv = new Integer(version.toString()).intValue();
				if ( cv > maxVersion ) {
					maxVersion = cv;
				}
			}
		}
		
		// return the max version
		for ( int i = 0; i < cases.size(); i++ ) {
			Map data = (Map) cases.get(i);
			Object version = data.get("tcversion_id");
			if ( version != null ) {
				int cv = new Integer(version.toString()).intValue();
				if ( cv == maxVersion ) {
					return data;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get the a test case identifier by test project id, suite id and test case name.
	 * 
	 * @param apiClient
	 * @param projectID
	 * @param suiteID
	 * @param caseName
	 * @return
	 * @throws TestLinkAPIException
	 */
	static Integer getPlanID(
		TestLinkAPIClient apiClient, 
		Integer projectID, 
		String planName) throws TestLinkAPIException
	{
		TestLinkAPIResults results = apiClient.getProjectTestPlans(projectID);
		Object id = null;
		Integer planID = null;
		
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValueByName(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( planName.equals(data.toString()) ) {
					id = results.getValueByName(i, API_RESULT_IDENTIFIER);
					break;
				}
			}
		}
		
		if ( id != null ) {
			planID = new Integer(id.toString());
		}
		
		return planID;
	}
	
	private static Integer getIdentifier(
		Map data)
	{
		Integer identifier = null;
		Object id = null;
		if ( data == null ) {
			return identifier;
		}
		id = data.get(API_RESULT_IDENTIFIER);
		if ( id != null ) {
			identifier = new Integer(id.toString());
		}
		return identifier;
	}
}
