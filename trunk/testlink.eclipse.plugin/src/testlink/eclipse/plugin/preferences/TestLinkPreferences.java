package testlink.eclipse.plugin.preferences;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Preferences;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.tc.autoexec.TestPlanPrepare;
import testlink.eclipse.plugin.Activator;


public class TestLinkPreferences
{	
	// Alternate storage information
	private Map alternateStore = new HashMap(); 
	private boolean usePrefStore = true;

	// Class variables
	private Preferences prefStore;
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
		if ( usePrefStore ) {
			Activator activator = Activator.getDefault();
			if ( activator != null ) {
				prefStore = activator.getPluginPreferences();
				usePrefStore = true;
			}
			useResultReporting = prefStore.getBoolean(
				PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST);
			defaultProject = prefStore.getString(
				PreferenceConstants.P_DEFAULT_PROJECT_NAME);
			devKey = prefStore.getString(PreferenceConstants.P_DEV_KEY);
			testLinkURL = prefStore.getString(PreferenceConstants.P_TESTLINK_URL);
			testCaseCreator = prefStore.getString(
				PreferenceConstants.P_TEST_CASE_CREATION_USER);
			testPlanPrepareClass = prefStore.getString(
				PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS);
			externalPath = prefStore.getString(
				PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE);
			testLinkAPIClient = new TestLinkAPIClient(getDevKey(), getTestLinkAPIURL());	
		} else {
			Boolean value = (Boolean) alternateStore.get(
				PreferenceConstants.P_REPORT_RESULTS_AFTER_TEST);
			if ( value != null ) {
				useResultReporting = value.booleanValue();
			}
			defaultProject = (String) alternateStore.get(
				PreferenceConstants.P_DEFAULT_PROJECT_NAME);
			devKey = (String) alternateStore.get(PreferenceConstants.P_DEV_KEY);
			testLinkURL = (String) alternateStore.get(PreferenceConstants.P_TESTLINK_URL);
			testCaseCreator = (String) alternateStore.get(
				PreferenceConstants.P_TEST_CASE_CREATION_USER);
			testPlanPrepareClass = (String) alternateStore.get(
				PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS);
			externalPath = (String) alternateStore.get(
				PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE);
			testLinkAPIClient = new TestLinkAPIClient(getDevKey(), getTestLinkAPIURL());	
		}
	}
	
	public void setAlternateStore(
		Map store)
	{
		alternateStore = store;
		usePrefStore = false;
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
	
	public String getTestLinkAPIURL()
	{
		return getTestLinkURL() + "/lib/api/xmlrpc.php";
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
	
	public TestPlanPrepare getTestPlanPrepare() throws Exception
	{
		TestPlanPrepare tpp = (TestPlanPrepare) Class.forName(testPlanPrepareClass).newInstance();
		return tpp;
	}
}
