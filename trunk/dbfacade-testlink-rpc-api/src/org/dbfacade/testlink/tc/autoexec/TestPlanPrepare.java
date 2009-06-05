package org.dbfacade.testlink.tc.autoexec;


import org.dbfacade.testlink.api.client.TestLinkAPIException;


/**
 * An implementer of this interface is expected to do the following:
 * 
 * Required:
 * 
 * 1)  Assign the executors for all the automated test cases in the test plan.  
 * 
 * Optional:
 * 
 *  1) Add additional test cases that are not listed in the plan. Especially helpful
 *  when the list of test cases is maintained and added to using programatic means.
 *  
 *  2) Replace the test cases with different TestCase interface implementations. 
 *  A good example would be when the default ExecutableTestCase could be replaced
 *  with CustomTestCase which extends ExecutableTestCase. There is an init procedure
 *  for in the interface to init from another test case.
 *  
 *  3) etc..
 *  
 * @author Daniel Padilla
 *
 */
public interface TestPlanPrepare
{

	/**
	 * Add additional test cases to the plan. This method
	 * create new TestCase instances and should add the
	 * test cases to the TestPlan using the 
	 *
	 *
	 * 
	 * @param manager
	 * @return A test plan which has had the executors set for each test case.
	 * 
	 */
	public TestPlan prepare(
		TestPlan plan) throws TestLinkAPIException;
}
