package org.dbfacade.testlink.eclipse.plugin.preferences;


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.eclipse.plugin.Activator;
import org.dbfacade.testlink.tc.autoexec.TestPlanPrepare;
import org.eclipse.core.runtime.Preferences;


public class TestLinkPreferences
{
	private Preferences prefStore = Activator.getDefault().getPluginPreferences();
	private boolean useResultReporting;
	private String defaultProject; 
	private String devKey;
	private String testLinkURL;	
	private String testCaseCreator;
	private String testPlanPrepareClass;
	private String externalPath;
	private TestLinkAPIClient testLinkAPIClient;
	
	public TestLinkPreferences()
	{
		useResultReporting = prefStore.getBoolean(
			PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST);
		defaultProject = prefStore.getString(PreferenceConstants.P_DEFAULT_PROJECT_NAME);
		devKey = prefStore.getString(PreferenceConstants.P_DEV_KEY);
		testLinkURL = prefStore.getString(PreferenceConstants.P_TESTLINK_API_URL);
		testCaseCreator = prefStore.getString(
			PreferenceConstants.P_TEST_CASE_CREATION_USER);
		testPlanPrepareClass = prefStore.getString(
			PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS);
		externalPath = prefStore.getString(
			PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE);
		testLinkAPIClient = new TestLinkAPIClient(getDevKey(), getTestLinkURL());					
	}
	
	public boolean useResultReporting()
	{
		return useResultReporting;
	}
	
	public String getDefaultProject()
	{
		return defaultProject;
	}

	public String getDevKey()
	{
		return devKey;
	}
	
	public String getTestLinkURL()
	{
		return testLinkURL;
	}
	
	public String getTestCaseCreator()
	{
		return testCaseCreator;
	}
	
	public String getTestPlanPrepareClass()
	{
		return testPlanPrepareClass;
	}
	
	public String getExternalPath()
	{
		return externalPath;
	}
	
	public TestLinkAPIClient getTestLinkAPIClient()
	{
		return testLinkAPIClient;
	}
	
	public TestPlanPrepare getTestPlanPrepare() throws Exception {
		TestPlanPrepare tpp = (TestPlanPrepare) Class.forName(testPlanPrepareClass).newInstance();
		return tpp;
	}
}