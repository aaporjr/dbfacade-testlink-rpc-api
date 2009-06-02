package org.dbfacade.testlink.tc.autoexec;

/**
 * An implementer of the interface is responsible for providing a
 * list of test plan manager instances that are fully initialized
 * and ready for automated execution.
 * 
 * @author Daniel Padilla
 *
 */
public interface TestPlanManagerLoader {

	/**
	 * The method creates a list of TestPlanManager instances that have
	 * their test cases initialized and the executors registered for
	 * the automated test cases.
	 * 
	 * @param devKey
	 * @param urlTestLinkAPIHome
	 * @return
	 */
	public TestPlanManager[] load(String devKey, String urlTestLinkAPIHome);
}
