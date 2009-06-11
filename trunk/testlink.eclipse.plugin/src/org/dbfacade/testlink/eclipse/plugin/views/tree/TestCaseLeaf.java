package org.dbfacade.testlink.eclipse.plugin.views.tree;


import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.eclipse.plugin.views.HtmlMessageText;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.dbfacade.testlink.tc.autoexec.TestProject;
import org.dbfacade.testlink.tc.autoexec.TestSuite;


public class TestCaseLeaf extends TreeNode implements TestCase, HtmlMessageText
{
	public static final String AUTOMATED_AND_INCOMPLETE = "(I) ";
	public static final String AUTOMATED_WITH_EXECUTOR = "(A) ";
	public static final String MANUAL = "(M) ";
	private TestCase testCase;
	private String leafType;
	
	public TestCaseLeaf(
		String testCaseName)
	{
		super(testCaseName);
	}
	
	public TestCaseLeaf(
		TestCase tc)
	{
		super(tc.getTestCaseName());
		if ( tc.isAutoExec() ) {
			TestCaseExecutor te = tc.getExecutor();
			if ( te == null ) {
				leafType = AUTOMATED_AND_INCOMPLETE;
			} else {
				leafType = AUTOMATED_WITH_EXECUTOR;
			}
		} else {
			leafType = MANUAL;
		}
		this.setName(leafType + this.getName());
		this.testCase = tc;
	}
	
	public String getTestType()
	{
		return leafType;
	}
	
	public TestCase getTestCase()
	{
		return testCase;
	}
	
	/*
	 * Implement the test case in particular for the ExecuteTestCases
	 */
	

	public void initNewCase(
		TestProject testProject,
		TestSuite testSuite,
		String caseName) throws TestLinkAPIException
	{
		testCase.initNewCase(testProject, testSuite, caseName);
	}
	
	public void initExistingCase(
		TestProject testProject,
		TestSuite testSuite,
		Map testCaseInfo) throws TestLinkAPIException
	{
		testCase.initExistingCase(testProject, testSuite, testCaseInfo);
	}
	
	public String getProjectName()
	{
		return testCase.getProjectName();
	}

	public Integer getProjectID()
	{
		return testCase.getProjectID();
	}
	
	public String getSuiteName()
	{
		return testCase.getSuiteName();
	}
	
	public Integer getSuiteID()
	{
		return testCase.getSuiteID();
	}
	
	public String getTestCaseName()
	{
		return testCase.getTestCaseName();
	}
	
	public void setTestCaseName(
		String caseName)
	{
		testCase.setTestCaseName(caseName);
	}
	
	public String getTestCaseVisibleID()
	{
		return testCase.getTestCaseVisibleID();
	}
	
	public Integer getTestCaseInternalID()
	{
		return testCase.getTestCaseInternalID();
	}
	
	public String getTestCaseSummary()
	{
		return testCase.getTestCaseSummary();
	}
	
	/**
	 * Set the test case summary information.
	 * 
	 * @param summary
	 */
	public void setTestCaseSummary(
		String summary)
	{
		testCase.setTestCaseSummary(summary);
	}
	
	public String getTestCaseSteps()
	{
		return testCase.getTestCaseSteps();
	}
	
	public void setTestCaseSteps(
		String steps)
	{
		testCase.setTestCaseSteps(steps);
	}
	
	public String getTestCaseExpectedResults()
	{
		return testCase.getTestCaseExpectedResults();
	}
	
	public void setTestCaseExpectedResults(
		String expectedResults)
	{
		testCase.setTestCaseExpectedResults(expectedResults);
	}
	
	public String getTestCaseCustomFieldContents(
		String fieldName)
	{
		return testCase.getTestCaseCustomFieldContents(fieldName);
	}
	
	public void setTestCaseCustomFieldContents(
		String fieldName,
		String contents)
	{
		testCase.setTestCaseCustomFieldContents(fieldName, contents);
	}
	
	public int getExecOrder()
	{
		return testCase.getExecOrder();
	}
	
	public void setExecOrder(
		int order)
	{
		testCase.setExecOrder(order);
	}
	
	public boolean isManualExec()
	{
		return testCase.isManualExec();
	}
	
	public boolean isAutoExec()
	{
		return testCase.isAutoExec();
	}
	
	public void setExecTypeManual()
	{
		testCase.setExecTypeManual();
	}
	
	public void setExecTypeAuto()
	{
		testCase.setExecTypeAuto();
	}
	
	public String getVersion()
	{
		return testCase.getVersion();
	}
	
	public boolean isLowImportance()
	{
		return testCase.isLowImportance();
	}
	
	public boolean isMediumImportance()
	{
		return testCase.isMediumImportance();
	}
	
	public boolean isHighImportance()
	{
		return testCase.isHighImportance();
	}
	
	public void setToLowImportance()
	{
		testCase.setToLowImportance();
	}
	
	public void setToMediumImportance()
	{
		testCase.setToMediumImportance();
	}
	
	public void setToHighImportance()
	{
		testCase.setToHighImportance();
	}
	
	public boolean isActive()
	{
		return testCase.isActive();
	}
	
	public void addToTestLink(
		TestLinkAPIClient apiClient,
		String loginUserName) throws TestLinkAPIException
	{
		testCase.addToTestLink(apiClient, loginUserName);
	}
	
	public TestCaseExecutor getExecutor()
	{
		return testCase.getExecutor();
	}
	
	public void setExecutor(
		TestCaseExecutor executor)
	{
		testCase.setExecutor(executor);
	}
	
	public void setHtmlText(String html) {
		
	}
	
	public String getHtmlText() {
		return displayHtml();
	}
	
	public String displayHtml()
	{
		String  detail = "No detail information available";
		if ( testCase != null ) {
			detail = HtmlMessageText.OPEN_HTML_DOC + "<b>Name:</b></p><p>" + testCase.getTestCaseName()
			+ "</p>" + "<p><b>Summary:</b></p>"
				+ "<p>" + testCase.getTestCaseSummary() + "</p>" + "<p><b>Steps:</b></p>"
				+ "<p>" + testCase.getTestCaseSteps() + "</p>" + "<p><b>Expected Results:</b></p><p>"
				+ testCase.getTestCaseExpectedResults() + HtmlMessageText.CLOSE_HTML_DOC;
		}
		return detail;
	}
}
