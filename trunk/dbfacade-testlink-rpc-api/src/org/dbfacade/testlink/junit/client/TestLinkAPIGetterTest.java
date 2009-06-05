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
package org.dbfacade.testlink.junit.client;


import static org.junit.Assert.fail;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;
import org.dbfacade.testlink.api.client.TestLinkAPIHelper;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;
import org.dbfacade.testlink.junit.constants.TestConst;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


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
public class TestLinkAPIGetterTest implements TestLinkAPIConst, TestConst
{	
	// The api instance
	public static TestLinkAPIClient api=null;
	
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
		if ( api == null ) {
			api = new TestLinkAPIClient(userKey, apiURL, true);
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{}

	/**
	 * Test method about
	 */
	@Test
	public void testAbout()
	{
		try {
			TestLinkAPIResults results = api.about();
			if ( results == null ) {
				throw new Exception("Unable to run about method.");
			}
			printResults("testAbout", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API about method.");
		}
	}
	
	/**
	 * Test method pint
	 */
	@Test
	public void testPing()
	{
		try {
			TestLinkAPIResults results = api.ping();
			if ( results == null ) {
				throw new Exception("Unable to run ping method.");
			}
			printResults("testPing", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API ping method.");
		}
	}
	
	/**
	 * Test method getProjects
	 */
	@Test
	public void testGetProjects()
	{
		try {
			TestLinkAPIResults results = api.getProjects();
			if ( results == null ) {
				throw new Exception("Unable to run getProject() method.");
			}
			printResults("testGetProjects", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getProjects() method.");
		}
	}
	
	/**
	 * Test method getFirstLevelTestSuitesForTestProject
	 */
	@Test
	public void testGetFirstLevelTestSuitesForTestProject()
	{
		try {
			TestLinkAPIResults results = api.getFirstLevelTestSuitesForTestProject(JUNIT_PLAN_PROJECT);
			if ( results == null ) {
				throw new Exception("Unable to run getFirstLevelTestSuitesForTestProject() method.");
			}
			printResults("testGetFirstLevelTestSuitesForTestProject", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getFirstLevelTestSuitesForTestProject() method.");
		}
	}
	
	/**
	 * Test method getProjectTestPlans
	 */
	@Test
	public void testGetProjectTestPlans()
	{
		try {
			TestLinkAPIResults results = api.getProjectTestPlans(JUNIT_PLAN_PROJECT);
			if ( results == null ) {
				throw new Exception("Unable to run getProjectTestPlans() method.");
			}
			printResults("testGetProjectTestPlans", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getProjectTestPlans() method.");
		}
	}
	
	/**
	 * Test method getBuildsForTestPlan
	 */
	@Test
	public void testGetBuildsForTestPlan()
	{
		try {
			TestLinkAPIResults results = api.getBuildsForTestPlan(JUNIT_PLAN_PROJECT, JUNIT_PLAN_NAME);
			if ( results == null ) {
				throw new Exception("Unable to run getBuildsForTestPlan() method.");
			}
			printResults("testGetBuildsForTestPlan", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getBuildsForTestPlan() method.");
		}
	}
	
	/**
	 * Test method getLatestBuildForTestPlan
	 */
	@Test
	public void testGetLatestBuildForTestPlan()
	{
		try {
			TestLinkAPIResults results = api.getLatestBuildForTestPlan(JUNIT_PLAN_PROJECT, JUNIT_PLAN_NAME);
			if ( results == null ) {
				throw new Exception("Unable to run getLatestBuildForTestPlan() method.");
			}
			printResults("testGetLatestBuildForTestPlans", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getLatestBuildForTestPlan() method.");
		}
	}
	
	/**
	 * Test method getTestSuitesForTestPlan
	 */
	@Test
	public void testGetTestSuitesForTestPlan()
	{
		try {
			TestLinkAPIResults results = api.getTestSuitesForTestPlan(JUNIT_PLAN_PROJECT, JUNIT_PLAN_NAME);
			if ( results == null ) {
				throw new Exception("Unable to run getTestSuitesForTestPlan() method.");
			}
			printResults("testGetTestSuitesForTestPlan", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getTestSuitesForTestPlan() method.");
		}
	}
	
	/**
	 * Test method getTestCaseIDByName
	 */
	@Test
	public void testGetTestCaseIDByName()
	{
		try {
			TestLinkAPIResults results = api.getTestCaseIDByName(JUNIT_PLAN_CASE);
			if ( results == null ) {
				throw new Exception("Unable to run getTestCaseIDByName() method.");
			}
			printResults("testGetTestCaseIDByName", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getTestCaseIDByName() method.");
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetLastExecutionResult()
	{
		try {
			TestLinkAPIResults results = api.getLastExecutionResult(JUNIT_PLAN_PROJECT, JUNIT_PLAN_NAME, JUNIT_PLAN_CASE);
			if ( results == null ) {
				throw new Exception("Unable to run getLastExecutionResult() method.");
			}
			printResults("testGetLastExecutionResult", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getLastExecutionResult() method.");
		}
	}
	
	/**
	 * Test method getCasesForTestPlan(projectName, planName)
	 */
	@Test
	public void testGetTestCasesByPlanName()
	{
		try {
			TestLinkAPIResults results = api.getCasesForTestPlan(JUNIT_PLAN_PROJECT, JUNIT_PLAN_NAME);
			if ( results == null ) {
				throw new Exception("Unable to run getCasesForTestPlan() method.");
			}
			printResults("testGetTestCasesByPlanName", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getCasesForTestPlan() method.");
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesManual()
	{
		try {
			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_PLAN_PROJECT);
			if ( projectID == null ) {
				throw new TestLinkAPIException(
					"Could not get project identifier for " + JUNIT_PLAN_PROJECT);
			}
			Integer planID = TestLinkAPIHelper.getPlanID(api, projectID, JUNIT_PLAN_NAME);
			if ( planID == null ) {
				throw new TestLinkAPIException("Could not get plan identifier for " + JUNIT_PLAN_NAME);
			}
			TestLinkAPIResults results = api.getCasesForTestPlan(
					planID,
					null,
					null,
					null,
					null,
					null,
					null,
					TESTCASE_EXECUTION_TYPE_MANUAL
					);
			if ( results == null ) {
				throw new Exception("Unable to run getCasesForTestPlan() method.");
			}
			printResults("testGetTestCasesPassedManual", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getCasesForTestPlan() method.");
		}
	}
	
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesAuto()
	{
		try {
			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_PLAN_PROJECT);
			if ( projectID == null ) {
				throw new TestLinkAPIException(
					"Could not get project identifier for " + JUNIT_PLAN_PROJECT);
			}
			Integer planID = TestLinkAPIHelper.getPlanID(api, projectID, JUNIT_PLAN_NAME);
			if ( planID == null ) {
				throw new TestLinkAPIException("Could not get plan identifier for " + JUNIT_PLAN_NAME);
			}
			TestLinkAPIResults results = api.getCasesForTestPlan(
					planID,
					null,
					null,
					null,
					null,
					null,
                    null,
                    TESTCASE_EXECUTION_TYPE_AUTO
					);
			if ( results == null ) {
				throw new Exception("Unable to run getCasesForTestPlan() method.");
			}
			printResults("testGetTestCasesPassedAuto", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getCasesForTestPlan() method.");
		}
	}
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesFailed()
	{
		try {
			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_PLAN_PROJECT);
			if ( projectID == null ) {
				throw new TestLinkAPIException(
					"Could not get project identifier for " + JUNIT_PLAN_PROJECT);
			}
			Integer planID = TestLinkAPIHelper.getPlanID(api, projectID, JUNIT_PLAN_NAME);
			if ( planID == null ) {
				throw new TestLinkAPIException("Could not get plan identifier for " + JUNIT_PLAN_NAME);
			}
			TestLinkAPIResults results = api.getCasesForTestPlan(
					planID,
					null,
					null,
					null,
					null,
					null,
					TEST_FAILED,
					null
					);
			if ( results == null ) {
				throw new Exception("Unable to run getCasesForTestPlan() method.");
			}
			printResults("testGetTestCasesFailed", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getCasesForTestPlan() method.");
		}
	}
	
	
	/**
	 * Test method getLastExecutionResult
	 */
	@Test
	public void testGetTestCasesPassed()
	{
		try {
			Integer projectID = TestLinkAPIHelper.getProjectID(api, JUNIT_PLAN_PROJECT);
			if ( projectID == null ) {
				throw new TestLinkAPIException(
					"Could not get project identifier for " + JUNIT_PLAN_PROJECT);
			}
			Integer planID = TestLinkAPIHelper.getPlanID(api, projectID, JUNIT_PLAN_NAME);
			if ( planID == null ) {
				throw new TestLinkAPIException("Could not get plan identifier for " + JUNIT_PLAN_NAME);
			}
			TestLinkAPIResults results = api.getCasesForTestPlan(
					planID,
					null,
					null,
					null,
					null,
					null,
					TEST_PASSED,
					null
					);
			if ( results == null ) {
				throw new Exception("Unable to run getCasesForTestPlan() method.");
			}
			printResults("testGetTestCasesPassed", results);
		} catch ( Exception e ) {
			e.printStackTrace();
			fail("Failed to run TestLink API getCasesForTestPlan() method.");
		}
	}
	
	
	private void printResults(String method, TestLinkAPIResults results) {
		System.out.println("\n----------------------------------------------------------------");
		System.out.println(method + " results: ");
		System.out.println(results.toString());
	}
	
	
}

