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

import org.dbfacade.testlink.api.client.TestLinkAPIResults;

public interface TestCase {
	
	/**
	 * Using the data returned from the TestLink API the test
	 * case is initialized.
	 * 
	 * @param testCase
	 */
	public void initExistingCase(TestLinkAPIResults testCase);
	
	/*
	 * Project related information
	 */
	public String getProjectName();
	public void setProjectName(String projectName);
	public Integer getProjectID();
	public void setProjectID(Integer id);
	
	/*
	 * Suite related information
	 */
	public String getSuiteName();
	public void setSuiteName(String suiteName);
	public Integer getSuiteID();
	public void setSuiteID(Integer id);
	
	
	/*
	 * Test case related information
	 */
	public String getTestCaseName();
	public void setTestCaseName(String caseName);
	public String getTestCaseVisibleID();
	public void setTestCaseVisibleID(String visibleID);
	public Integer getTestCaseInternalID();
	public void setTestCaseInternalIDID(Integer id);
	public String getTestCaseSummary();
	public void setTestCaseSummary(String summary);
	public String getTestCaseSteps();
	public void setTestCaseSteps(String summary);
	public String getTestCaseExpectedResults();
	public void setTestCaseExpectedResults(String summary);
	public String getTestCaseCustomFieldContents(String fieldName);
	public void setTestCaseCustomFieldContents(String fieldName, String contents);
	
	/*
	 * Registration information
	 */
	public TestCaseExecutor getExecutor();
	public void setExecutor(TestCaseExecutor executor);
	
}
