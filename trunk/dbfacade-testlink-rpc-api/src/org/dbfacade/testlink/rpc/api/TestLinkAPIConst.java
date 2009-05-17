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
package org.dbfacade.testlink.rpc.api;

import java.util.Date;

/**
 * The constants used by the dbfacade implementation
 * of the TestLink API. The method names and execution 
 * parameter constants can be found in this file.
 * 
 * @author Daniel Padilla
 *
 */
public interface TestLinkAPIConst {
	
	/*
	 * Methods supported by TestLink 1.8.2 based on the PHP
	 * sample implementation of the TestLink API
	 */
	
	// Check about and alive methods
	public static final String API_METHOD_PING="tl.ping";
	public static final String API_METHOD_ABOUT="tl.about";
	
	// Create methods
	public static final String API_METHOD_CREATE_PROJECT="tl.createTestProject";
	public static final String API_METHOD_CREATE_SUITE="tl.createTestSuite";
	public static final String API_METHOD_CREATE_TEST_CASE="tl.createTestCase";
	public static final String API_METHOD_CREATE_BUILD="tl.createBuild";
	

	// Get methods
	public static final String API_METHOD_GET_PROJECTS="tl.getProjects";
	public static final String API_METHOD_GET_PROJECT_TEST_PLANS="tl.getProjectTestPlans";
	public static final String API_METHOD_GET_BUILDS_FOR_PLANS="tl.getBuildsForTestPlan";
	public static final String API_METHOD_GET_LAST_BUILDS_FOR_PLAN="tl.getLatestBuildForTestPlan";
	public static final String API_METHOD_GET_SUITES_FOR_PLAN="tl.getTestSuitesForTestPlan";
	public static final String API_METHOD_GET_TEST_CASES_FOR_PLAN="tl.getTestCasesForTestPlan";
    public static final String API_METHOD_GET_TEST_CASE_IDS_BY_NAME="tl.getTestCaseIDByName";
    public static final String API_METHOD_GET_TEST_CASES_FOR_SUITE="tl.getTestCasesForTestSuite";    
	public static final String API_METHOD_LAST_EXECUTION_RESULT="tl.getLastExecutionResult";
	public static final String API_METHOD_CASE_ATTACHMENTS="tl.getTestCaseAttachments";
	public static final String API_METHOD_TEST_CASE_CUSTOM_FIELD_DESIGN_VALUE="tl.getTestCaseCustomFieldDesignValue";
	public static final String API_METHOD_GET_FIRST_LEVEL_SUITE_FOR_PROJECT="tl.getFirstLevelTestSuitesForTestProject";
	
    
	// Set methods
	public static final String API_METHOD_ADD_TEST_CASE_TO_PLAN="tl.addTestCaseToTestPlan";
	public static final String API_METHOD_ASSIGN_REQUIREMENT="tl.assignRequirements";
	public static final String API_METHOD_SET_TEST_MODE="tl.setTestMode";
    public static final String API_METHOD_REPORT_TEST_RESULT="tl.reportTCResult";
	

    
    /*
     * Parameter names supported by TestLink 1.8.2 based
     * on PHP sample implementation of TestLinkAPI
     */
    public static final String API_PARAM_DEV_KEY="devKey";
    public static final String API_PARAM_TEST_PROJECT_ID="testprojectid";
    public static final String API_PARAM_TEST_PROJECT_NAME="testprojectname";
    public static final String API_PARAM_TEST_CASE_PREFIX="testcaseprefix";
    public static final String API_PARAM_TEST_SUITE_ID="testsuiteid";
    public static final String API_PARAM_TEST_SUITE_NAME="testsuitename";
    public static final String API_PARAM_TEST_CASE_ID="testcaseid";
    public static final String API_PARAM_TEST_CASE_NAME="testcasename";
    public static final String API_PARAM_TEST_PLAN_ID="testplanid";
    public static final String API_PARAM_STATUS="status";
    public static final String API_PARAM_DEPTH_FLAG="deep";
    public static final String API_PARAM_DETAILS="details";
    public static final String API_PARAM_NOTES="notes";
    public static final String API_PARAM_TEST_CASE_EXTERNAL= "testcaseexternalid";
    public static final String API_PARAM_BUILD_ID="buildid";
    public static final String API_PARAM_TIMESTAMP="timestamp";
    public static final String API_PARAM_GUESS="guess";
    public static final String API_PARAM_TEST_MODE="testmode";
    public static final String API_PARAM_BUILD_NAME="buildname";
    public static final String API_PARAM_BUILD_NOTES="buildnotes";
    public static final String API_PARAM_AUTOMATED="automated";
    public static final String API_PARAM_KEY_WORD_ID="keywordid";
    public static final String API_PARAM_EXECUTED="executed";
    public static final String API_PARAM_ASSIGNED_TO="assignedto";
    public static final String API_PARAM_EXECUTE__STATUS="executestatus";
    public static final String API_PARAM_CUST_FIELD_NAME="customfieldname";
    public static final String API_PARAM_SUMMARY="summary";
    public static final String API_PARAM_STEPS="steps";
    public static final String API_PARAM_EXPECTED_RESULTS="expectedresults";
    public static final String API_PARAM_AUTHOR_LOGIN="authorlogin";
    public static final String API_PARAM_EXEC_TYPE="executiontype";
    public static final String API_PARAM_IMPORTANCE="importance";
    public static final String API_PARAM_ORDER="order";
    public static final String API_PARAM_INTERNAL_ID="internalid";
    public static final String API_PARAM_CHECK_DUP_NAMES="checkduplicatedname";
    public static final String API_PARAM_ACTION_DUP_NAME="actiononduplicatedname";
    public static final String API_PARAM_KEYWORDS="keywords";
    public static final String API_PARAM_VERSION="version";
    public static final String API_PARAM_EXEC_ORDER="executionorder";
    public static final String API_PARAM_URGENCY="urgency";
    public static final String API_PARAM_REQUIREMENTS="requirements";
    public static final String API_PARAM_BUG_ID="bugid";		
    public static final String API_PARAM_PARENT_ID="parentid";
    
    /*
     * Values being returned in the map based on inspection and
     * unless documented may change it would have to be retested.
     */
    public static final String API_RESULT_IDENTIFIER="id";
    public static final String API_RESULT_NAME="name";
    
    /*
     * Readability constants
     */
    public static final boolean REQUIRED=true;
    public static final boolean OPTIONAL=false;
    
    
    /*
     * Variables used for testing
     */
    public static final String junitID = new Long(new Date().getTime()).toString();
	public static final String junitProject = "Test Project " + junitID;
	public static final String junitSuite = "Test Suite " + junitID;
	public static final String junitPrefix = junitID;
    
    
}
