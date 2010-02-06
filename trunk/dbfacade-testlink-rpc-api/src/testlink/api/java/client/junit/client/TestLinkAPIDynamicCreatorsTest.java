/*
 * Daniel R Padilla
 *
 * Copyright (c) 2009, Daniel R Padilla
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
package testlink.api.java.client.junit.client;


import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.TestLinkAPIConst;
import testlink.api.java.client.TestLinkAPIException;
import testlink.api.java.client.TestLinkAPIHelper;
import testlink.api.java.client.TestLinkAPIResults;
import testlink.api.java.client.junit.constants.TestConst;


/**
 * 
 * Test used to verify the TestLink API Java implementation.
 * 
 * If you are going to use these tests it is recommended that 
 * you do it against a test installation of the TestLink system
 * especially the database since these test will create and
 * delete entries in the database.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkAPIDynamicCreatorsTest implements TestLinkAPIConst, TestConst
{	
	// The api instance
	public static HashMap<String,
	TestLinkAPIClient> apiList = new HashMap();
	public static TestLinkAPIClient api = null;
	public static String version;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		api = new TestLinkAPIClient(userKey, api182URL, true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{}

	/**
	 * Test method create project
	 */
	@Test
	public void testCreateProject()
	{
		try {
			Integer id = api.createTestProject(JUNIT_DYNAMIC_PROJECT, JUNIT_DYNAMIC_PREFIX,
				JUNIT_DYNAMIC_PROJECT + " created by JUnit test.");
			if ( id == null ) {
				throw new Exception("Unable to create project.");
			}
		} catch ( Exception e ) {
			fail("Failed to create a project.");
		}
	}
	
	/**
	 * Test method that gets the created projects
	 */
	@Test
	public void testGetProjects()
	{
		try {
			TestLinkAPIResults results = api.getProjects();
			if ( results == null || results.size() == 0 ) {
				throw new Exception(
					"Failed to get at least the project that would have been created by the test.");
			}
		} catch ( Exception e ) {
			fail("Establishing a connection caused a failure");
		}
	}
	
	/**
	 * Test method create test suite
	 */
	@Test
	public void testCreateTestSuite()
	{
		try {
			Integer id = api.createTestSuite(JUNIT_DYNAMIC_PROJECT, JUNIT_DYNAMIC_SUITE,
				"This suite was created by a JUnit test.");
			if ( id == null ) {
				throw new Exception(
					"Failed to create a test suite for project " + JUNIT_DYNAMIC_SUITE); 
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to create a test suite.");
		}
	}
	
	/**
	 * Test method create test case
	 */
	@Test
	public void testCreateTestCase()
	{
		try {
			Integer id = api.createTestCase(
				"admin",
				JUNIT_DYNAMIC_PROJECT, 
				JUNIT_DYNAMIC_SUITE,
				JUNIT_DYNAMIC_CASE, 
				"JUnit created summary.",
				"JUnit created steps.",
				"JUnit created expected results.", 
				HIGH);
			if ( id == null || id.intValue() == 0 ) {
				throw new Exception(
					"Failed to create a test case for project " + JUNIT_DYNAMIC_CASE); 
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to create a test suite.");
		}
	}
	
	/**
	 * Test method create test case with all parameters
	 */
	@Test
	public void testCreateTestCaseWithAllParameters()
	{
		try {
			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_DYNAMIC_PROJECT);
			Integer suiteID = TestLinkAPIHelper.getSuiteID(api, JUNIT_DYNAMIC_PROJECT, JUNIT_DYNAMIC_SUITE);
			Integer id = api.createTestCase(
				"admin",
				projectID, 
				suiteID,
				JUNIT_DYNAMIC_CASE, 
				"JUnit created summary.",
				"JUnit created steps.",
				"JUnit created expected results.", 
				new Integer(2),
				null,
				new Boolean(true),
				ACTION_BLOCK_ON_DUP,
				TESTCASE_EXECUTION_TYPE_MANUAL,
				HIGH);
			if ( id == null ) {
				throw new Exception(
					"Failed to create a test case for project " + JUNIT_DYNAMIC_CASE); 
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to create a test suite.");
		}
	}
	
	/**
	 * Test method create test case with all parameters
	 */
	@Test
	public void testCreateTestCaseWithAllParametersAuto()
	{
		try {
			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_DYNAMIC_PROJECT);
			Integer suiteID = TestLinkAPIHelper.getSuiteID(api, JUNIT_DYNAMIC_PROJECT, JUNIT_DYNAMIC_SUITE);
			Integer id = api.createTestCase(
				"admin",
				projectID, 
				suiteID,
				JUNIT_DYNAMIC_CASE, 
				"JUnit created summary.",
				"JUnit created steps.",
				"JUnit created expected results.", 
				new Integer(3),
				null,
				new Boolean(true),
				ACTION_GEN_NEW_ON_DUP,
				TESTCASE_EXECUTION_TYPE_AUTO,
				MEDIUM);
			if ( id == null ) {
				throw new Exception(
					"Failed to create a test case for project " + JUNIT_DYNAMIC_CASE); 
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to create a test suite.");
		}
	}
	
	/**
	 * Test method create test plan. 
	 * 
	 * TestLink does not offer a createTestPlan procedure at
	 * this time so it will need to be created manually. This
	 * procedure will test of the project, suite and cases so
	 * that the test plan can be setup.
	 * 
	 */
	@Test
	public void testCreateTestPlan()
	{
		try {

			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_STATIC_PROJECT);
			if ( projectID == null ) {
				projectID = api.createTestProject(JUNIT_STATIC_PROJECT, "mytest",
						JUNIT_STATIC_PROJECT + " created by JUnit test.");
			} 
			
			Integer suiteID = TestLinkAPIHelper.getSuiteID(api, JUNIT_STATIC_PROJECT, JUNIT_STATIC_SUITE);
			if ( suiteID == null ) {
				suiteID = api.createTestSuite(JUNIT_STATIC_PROJECT, JUNIT_STATIC_SUITE,
				"This suite was created by a JUnit test.");
			}
			
			Integer caseID = TestLinkAPIHelper.getCaseIDByName(api, projectID, suiteID, JUNIT_STATIC_CASE);
			
			if ( caseID == null ) {
				caseID = api.createTestCase(
					"admin",
					JUNIT_STATIC_PROJECT, 
					JUNIT_STATIC_SUITE,
					JUNIT_STATIC_CASE, 
					"JUnit created summary.",
					"JUnit created steps.",
					"JUnit created expected results.", 
					HIGH);
			}
			
			/*
			 * TestLink API does not offer test plan creation so check that
			 * the test plan has been manually created for future testing.
			 */
			Integer planID = TestLinkAPIHelper.getPlanID(api, projectID, JUNIT_STATIC_TEST_PLAN);
			if ( planID == null || planID.intValue() == 0 ) {
				throw new TestLinkAPIException("The JUnit test plan was not found.");
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to check test plan.");
		}
	}
	
	/**
	 * Test method create build
	 */
	@Test
	public void testCreateBuild()
	{
		try {
			Integer id = api.createBuild(JUNIT_STATIC_PROJECT, JUNIT_STATIC_TEST_PLAN, JUNIT_DYNAMIC_BUILD,
				JUNIT_DYNAMIC_BUILD + " created by JUnit test.");
			if ( id == null ) {
				throw new Exception("Unable to create project.");
			}
		} catch ( Exception e ) {
			fail("Failed to create a build.");
		}
	}
	
	/**
	 * Test method add test case to test plan
	 */
	@Test
	public void testAddTestCaseToTestPlan()
	{
		try {
			Integer id = api.createTestCase(
					"admin",
					JUNIT_STATIC_PROJECT, 
					JUNIT_STATIC_SUITE,
					JUNIT_DYNAMIC_CASE, 
					"JUnit created summary.",
					"JUnit created steps.",
					"JUnit created expected results.", 
					HIGH);
			if ( id == null ) {
				throw new Exception("Unable to add test case to test plan suite.");
			}
			TestLinkAPIResults added = api.addTestCaseToTestPlan(JUNIT_STATIC_PROJECT, JUNIT_STATIC_TEST_PLAN, JUNIT_DYNAMIC_CASE);
			if ( added == null ) {
				throw new Exception("Unable to add test case to test plan.");
			}
		} catch ( Exception e ) {
			fail("Failed to create a build.");
		}
	}
	
	/**
	 * Test method add test case (static plan case) to test plan
	 */
	@Test
	public void testAddTestPlanCaseToTestPlan()
	{
		try {
			Integer id = api.createTestCase(
					"admin",
					JUNIT_STATIC_PROJECT, 
					JUNIT_STATIC_SUITE,
					JUNIT_STATIC_CASE, 
					"JUnit created summary.",
					"JUnit created steps.",
					"JUnit created expected results.", 
					HIGH);
			if ( id == null ) {
				throw new Exception("Unable to add test case to test plan suite.");
			}
			TestLinkAPIResults added = api.addTestCaseToTestPlan(JUNIT_STATIC_PROJECT, JUNIT_STATIC_TEST_PLAN, JUNIT_STATIC_CASE);
			if ( added == null ) {
				throw new Exception("Unable to add test case to test plan.");
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to create a build.");
		}
	}
	
	
	/**
	 * Test reporting a test result by project, plan and case name
	 */
	@Test
	public void testReportTestCaseResult()
	{
		try {
			TestLinkAPIResults results = api.reportTestCaseResult(
					JUNIT_STATIC_PROJECT,
					JUNIT_STATIC_TEST_PLAN,
					JUNIT_DYNAMIC_CASE, 
					null,
					"The test was by JUnit run on " + new Date().toString(),
					TEST_PASSED);
			if ( results == null ) {
				throw new Exception("Unable to add test case to test plan suite.");
			}
		} catch ( Exception e ) {
			fail("Failed to create a build.");
		}
	}
	
	/**
	 * Test reporting a test result by project, plan and case name (static plan case)
	 */
	@Test
	public void testReportTestPlanCaseResult()
	{
		try {
			TestLinkAPIResults results = api.reportTestCaseResult(
					JUNIT_STATIC_PROJECT,
					JUNIT_STATIC_TEST_PLAN,
					JUNIT_STATIC_CASE, 
					null,
					"The test was by JUnit run on " + new Date().toString(),
					TEST_PASSED);
			if ( results == null ) {
				throw new Exception("Unable to add test case to test plan suite.");
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to create a build.");
		}
	}
	
	
	/**
	 * Test reporting a test result by project, plan build, and case name
	 */
	@Test
	public void testReportTestCaseResultByBuild()
	{
		try {
			String caseVisibleID = TestLinkAPIHelper.getCaseVisibleID(api, JUNIT_STATIC_PROJECT, JUNIT_DYNAMIC_CASE);
			TestLinkAPIResults results = api.reportTestCaseResult(
					JUNIT_STATIC_PROJECT,
					JUNIT_STATIC_TEST_PLAN,
					caseVisibleID, 
					JUNIT_DYNAMIC_BUILD,
					"The test was by JUnit run on " + new Date().toString(),
					TEST_PASSED);
			if ( results == null ) {
				throw new Exception("Unable to add test case to test plan suite.");
			}
		} catch ( Exception e ) {
			fail("Failed to create a build.");
		}
	}
	
	
}

