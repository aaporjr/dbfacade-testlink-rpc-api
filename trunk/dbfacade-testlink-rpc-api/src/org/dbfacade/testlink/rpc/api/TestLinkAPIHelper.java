package org.dbfacade.testlink.rpc.api;

public class TestLinkAPIHelper implements TestLinkAPIConst {

	public static Integer getProjectIDByName(TestLinkAPIClient apiClient, String projectName) 
		throws TestLinkAPIException
	{
		Integer projectID = null;
		Object id = null;
		TestLinkAPIResults results = apiClient.getProjects();
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
			projectID = new Integer(id.toString());
		}
		
		return projectID;
	}
}
