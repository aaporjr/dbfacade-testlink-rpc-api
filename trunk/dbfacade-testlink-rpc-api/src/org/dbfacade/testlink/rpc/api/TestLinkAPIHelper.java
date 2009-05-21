package org.dbfacade.testlink.rpc.api;


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
		Integer projectID = null;
		Object id = null;
		TestLinkAPIResults results = apiClient.getProjects();
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValueByName(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( projectName.equals(data.toString()) ) {
					id = results.getValueByName(i, API_RESULT_IDENTIFIER);
					break;
				}
			}
		}
		
		if ( id != null ) {
			projectID = new Integer(id.toString());
		}
		
		return projectID;
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
		TestLinkAPIResults results = apiClient.getFirstLevelTestSuitesForTestProject(
			projectID);
		Object id = null;
		Integer suiteID = null;
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValueByName(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( suiteName.equals(data.toString()) ) {
					id = results.getValueByName(i, API_RESULT_IDENTIFIER);
					break;
				}
			}
		}
		
		if ( id != null ) {
			suiteID = new Integer(id.toString());
		}
		
		return suiteID;
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
		TestLinkAPIResults results = apiClient.getCasesForTestSuite(projectID, suiteID);
		Object id = null;
		Integer caseID = null;
		
		for ( int i = 0; i < results.size(); i++ ) {
			Object data = results.getValueByName(i, API_RESULT_NAME);
			if ( data != null ) {
				if ( caseName.equals(data.toString()) ) {
					id = results.getValueByName(i, API_RESULT_IDENTIFIER);
					break;
				}
			}
		}
		
		if ( id != null ) {
			caseID = new Integer(id.toString());
		}
		
		return caseID;
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
	
}
