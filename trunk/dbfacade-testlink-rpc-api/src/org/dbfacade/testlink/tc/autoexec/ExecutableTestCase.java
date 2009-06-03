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
package org.dbfacade.testlink.tc.autoexec;


import java.util.HashMap;
import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;


/**
 * Default implementation of the TestCase interface for test cases that
 * can be executed.
 * 
 * @author Daniel Padilla
 *
 */
public class ExecutableTestCase implements TestCase
{
	private TestProject testProject;
	private TestSuite testSuite;
	private String testCaseName;
	private String testCaseVisibleID;
	private Integer testCaseID;
	private Integer execOrder = new Integer(5000);
	private Integer execType = new Integer(TestLinkAPIConst.TESTCASE_EXECUTION_TYPE_MANUAL);
	private String testCaseSummary;
	private String testCaseSteps;
	private String testCaseExpectedResults;
	private String testCaseVersion;
	private String testCaseImportance = TestLinkAPIConst.MEDIUM;
	private Map custom = new HashMap();
	private TestCaseExecutor autoTestExecutor = null;
	private boolean isOpen = true;
	
	/**
	 * Using the data returned from the TestLink API the test
	 * case is initialized.
	 * 
	 * @param testCase
	 */
	public void initNewCase(
		TestProject projectInfo,
		TestSuite suiteInfo,
		String caseName) throws TestLinkAPIException
	{
		if ( projectInfo == null ) {
			throw new TestLinkAPIException(
				"The test project information cannot be initialized to null.");
		}
		if ( suiteInfo == null ) {
			throw new TestLinkAPIException(
				"The test suite information cannot be initialized to null.");
		}
		this.testProject = projectInfo;
		this.testSuite = suiteInfo;
		this.testCaseName = caseName;
	}
	
	/**
	 * Using the data returned from the TestLink API the test
	 * case is initialized.
	 * 
	 * @param testCase
	 */
	public void initExistingCase(
		TestProject projectInfo,
		TestSuite suiteInfo,
		Map testCaseInfo) throws TestLinkAPIException
	{
		if ( projectInfo == null ) {
			throw new TestLinkAPIException(
				"The test project information cannot be initialized to null.");
		}
		if ( suiteInfo == null ) {
			throw new TestLinkAPIException(
				"The test suite information cannot be initialized to null.");
		}
		if ( testCaseInfo == null ) {
			throw new TestLinkAPIException(
				"The test suite information cannot be initialized to null.");
		}
		this.testProject = projectInfo;
		this.testSuite = suiteInfo;
		
		// Identifier
		Object value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_IDENTIFIER);
		if ( value != null ) {
			this.testCaseID = new Integer(value.toString());
		} else {
			throw new TestLinkAPIException(
				"The test case identifier cannot be null for existing test case.");
		}
		
		// Name
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_NAME);
		if ( value != null ) {
			this.testCaseName = value.toString();
		} else {
			throw new TestLinkAPIException(
				"The test case identifier cannot be null for existing test case.");
		}
		
		// Execution Order
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_NODE_ORDER);
		if ( value != null ) {
			this.execOrder = new Integer(value.toString());
		} else {
			throw new TestLinkAPIException(
				"The test case identifier cannot be null for existing test case.");
		}
		
		// Visible ID
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_TC_EXTERNAL_ID);
		if ( value != null ) {
			this.testCaseVisibleID = this.testProject.getTestCasePrefix() + '-'
				+ value.toString();
		} else {
			throw new TestLinkAPIException(
				"The test case identifier cannot be null for existing test case.");
		}
		
		// Summary
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_SUMMARY);
		if ( value != null ) {
			this.testCaseSummary = value.toString();
		}
		
		// Execution Type
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_EXEC_TYPE);
		if ( value != null ) {
			this.execType = new Integer(value.toString());
		} else {
			throw new TestLinkAPIException(
				"The test case identifier cannot be null for existing test case.");
		}
		
		// Is Open
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_IS_OPEN); 
		if ( value != null ) {
			if ( new Integer(value.toString()).intValue() < 1 ) {
				isOpen = false;
			}
		}
		
		// Steps
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_STEPS);
		if ( value != null ) {
			this.testCaseSteps = value.toString();
		}
		
		// Suite verification
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_TC_SUITE);
		if ( value != null ) {
			if ( !testSuite.getSuiteName().equals(value.toString()) ) {
				throw new TestLinkAPIException(
					"The pass test suite name " + testSuite.getSuiteName()
					+ " does not match the suite name " + value.toString()
					+ " for the test case.");
			}
		} else {
			throw new TestLinkAPIException(
				"The test case identifier cannot be null for existing test case.");
		}
		
		// TC Version
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_VERSION);
		if ( value != null ) {
			this.testCaseVersion = value.toString();
		}
		
		// Expected results
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_EXPECTED_RESULTS);  
		if ( value != null ) {
			this.testCaseExpectedResults = value.toString();
		}
		
		// Importance
		value = testCaseInfo.get(TestLinkAPIConst.API_RESULT_IMPORTANCE);
		if ( value != null ) {
			this.testCaseImportance = value.toString();
		}
		
	}
	
	/**
	 * Get the name of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public String getProjectName()
	{
		return testProject.getProjectName();
	}
	
	/**
	 * Get the internal identifier of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getProjectID()
	{
		return testProject.getProjectID();
	}
	
	/**
	 * Get the name of the test suite with which the test case is associated.
	 * 
	 * @return
	 */
	public String getSuiteName()
	{
		return testSuite.getSuiteName();
	}
	
	/**
	 * Get the internal identifier of the test suite with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getSuiteID()
	{
		return testSuite.getSuiteID();
	}
	
	/**
	 * Get the name of the test case.
	 * 
	 * @return
	 */
	public String getTestCaseName()
	{
		return testCaseName;
	}
	
	/**
	 * Set the name of the test case.
	 * 
	 * @param caseName
	 */
	public void setTestCaseName(
		String caseName)
	{
		this.testCaseName = caseName;
	}
	
	/**
	 * Get the test case's visible identifier in the web application.
	 * 
	 * @return
	 */
	public String getTestCaseVisibleID()
	{
		return testCaseVisibleID;
	}
	
	/**
	 * Get the test case's internal identifier.
	 * 
	 * @return
	 */
	public Integer getTestCaseInternalID()
	{
		return testCaseID;
	}
	
	/**
	 * Get the test case summary information.
	 * 
	 * @return
	 */
	public String getTestCaseSummary()
	{
		return testCaseSummary;
	}
	
	/**
	 * Set the test case summary information.
	 * 
	 * @param summary
	 */
	public void setTestCaseSummary(
		String summary)
	{
		this.testCaseSummary = summary;
	}
	
	/**
	 * Get the execution steps for the test case.
	 * 
	 * @return
	 */
	public String getTestCaseSteps()
	{
		return testCaseSteps;
	}
	
	/**
	 * Set the execution steps for the test case.
	 * 
	 * @param steps
	 */
	public void setTestCaseSteps(
		String steps)
	{
		this.testCaseSteps = steps;
	}
	
	/**
	 * Get the test case execution expected results.
	 * 
	 * @return
	 */
	public String getTestCaseExpectedResults()
	{
		return testCaseExpectedResults;
	}
	
	/**
	 * Set the test case's expected results.
	 * 
	 * @param expectedResults
	 */
	public void setTestCaseExpectedResults(
		String expectedResults)
	{
		this.testCaseExpectedResults = expectedResults;
	}
	
	/**
	 * Get custom information.
	 * 
	 * @param fieldName
	 * @return
	 */
	public String getTestCaseCustomFieldContents(
		String fieldName)
	{
		return (String) this.custom.get(fieldName);
	}
	
	/**
	 * Set custom information.
	 * 
	 * @param fieldName
	 * @param contents
	 */
	public void setTestCaseCustomFieldContents(
		String fieldName,
		String contents)
	{
		this.custom.put(fieldName, contents);
	}
	
	/**
	 * Get the test case execution order within a plan.
	 * 
	 * @return
	 */
	public int getExecOrder()
	{
		return execOrder.intValue();
	}

	
	/**
	 * Set the execution order for the test case.
	 * 
	 * @param order
	 */
	public void setExecOrder(int order) {
		this.execOrder = order;
	}
	
	/**
	 * True if the test requires manual execution.
	 * 
	 * @return
	 */
	public boolean isManualExec()
	{
		return !(isAutoExec());
	}
	
	/**
	 * True if the test requires automated execution.
	 * 
	 * @return
	 */
	public boolean isAutoExec()
	{
		Integer auto = new Integer(TestLinkAPIConst.TESTCASE_EXECUTION_TYPE_AUTO);
		return (execType.intValue() == auto.intValue());
	}
	
	/**
	 * Set test case to manual execution
	 */
	public void setExecTypeManual()
	{
		execType = new Integer(TestLinkAPIConst.TESTCASE_EXECUTION_TYPE_MANUAL);
	}
	
	/**
	 * Set test case to automated execution
	 */
	public void setExecTypeAuto()
	{
		execType = new Integer(TestLinkAPIConst.TESTCASE_EXECUTION_TYPE_AUTO);
	}
	
	/**
	 * Get the version for the test case
	 * 
	 * @return
	 */
	public String getVersion()
	{
		return testCaseVersion;
	}
	
	/**
	 * True if the test case is of low importance
	 * 
	 * @return
	 */
	public boolean isLowImportance()
	{
		return(this.testCaseImportance.equals(TestLinkAPIConst.LOW));
	}
	
	/**
	 * True if the test case is of medium.
	 * 
	 * @return
	 */
	public boolean isMediumImportance()
	{
		return(this.testCaseImportance.equals(TestLinkAPIConst.MEDIUM));
	}
	
	/**
	 * True if the test case is of high importance.
	 * 
	 * @return
	 */
	public boolean isHighImportance()
	{
		return (!isLowImportance() && !isMediumImportance());
	}
	
	/**
	 * Set the test case to low importance
	 */
	public void setToLowImportance()
	{
		this.testCaseImportance = TestLinkAPIConst.LOW;
	}
	
	/**
	 * Set the test case to medium importance
	 */
	public void setToMediumImportance()
	{
		this.testCaseImportance = TestLinkAPIConst.MEDIUM;
	}
	
	/**
	 * Set the test case to high importance
	 */
	public void setToHighImportance()
	{
		this.testCaseImportance = TestLinkAPIConst.HIGH;
	}
	
	/**
	 * See test link documentation for the meaning of this flag
	 */
	public boolean isOpen() {
		return isOpen;
	}
	
	/**
	 * Get the test case's automated execution instance.
	 * @return
	 */
	public TestCaseExecutor getExecutor()
	{
		return autoTestExecutor;
	}
	
	/**
	 * Reqister the object that will be responsible for executing the test case automatically.
	 * 
	 * @param executor
	 */
	public void setExecutor(
		TestCaseExecutor executor)
	{
		this.autoTestExecutor = executor;
	}
	
}
