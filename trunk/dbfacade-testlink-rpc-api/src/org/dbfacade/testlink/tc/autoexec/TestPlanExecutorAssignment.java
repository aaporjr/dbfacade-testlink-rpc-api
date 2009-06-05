package org.dbfacade.testlink.tc.autoexec;

/**
 * An implementer of the interface is responsible for providing a
 * list of test plan manager instances that are fully initialized
 * and ready for automated execution.
 * 
 * @author Daniel Padilla
 *
 */
public interface TestPlanExecutorAssignment {

	/**
	 *  Assign the executors for a TestPlanManager. The purpose of
	 *  this method is to allow a generic loader to call a custom
	 *  loader that is responsible for assigning the executors.
	 *  
	 * @param manager
	 * @return A test plan which has had the executors set for each test case.
	 * 
	 */
	public TestPlan assignExecutors(TestPlan manager);
}
