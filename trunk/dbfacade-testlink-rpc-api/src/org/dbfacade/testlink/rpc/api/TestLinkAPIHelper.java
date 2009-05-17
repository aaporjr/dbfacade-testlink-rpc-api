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
	 * Find the project id for the named project provided in the parameter.
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
	 * Get the suite id by project name and suite name.
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
	 * Get the suite if by project id and suite name
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
		TestLinkAPIResults results = apiClient.getFirstLevelTestSuitesForTestProject(projectID);
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
}
