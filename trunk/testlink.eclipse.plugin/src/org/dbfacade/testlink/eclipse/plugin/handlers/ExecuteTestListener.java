package org.dbfacade.testlink.eclipse.plugin.handlers;


import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkView;
import org.dbfacade.testlink.eclipse.plugin.views.tree.PlanTree;
import org.dbfacade.testlink.tc.autoexec.ExecuteTestCaseEvent;
import org.dbfacade.testlink.tc.autoexec.ExecuteTestCaseListener;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestPlan;


public class ExecuteTestListener implements ExecuteTestCaseListener
{
	PlanTree tree;
	boolean isBackgroundExecution = false;
	
	/**
	 * Register the test plan node as part of the listener
	 * 
	 * @param node
	 */
	public ExecuteTestListener(
		PlanTree node,
		boolean runInBackground)
	{
		tree = node;
		this.isBackgroundExecution=runInBackground;
	}

	/**
	 * Called before execution
	 * 
	 * @param event
	 */
	public void executionStart(
		ExecuteTestCaseEvent event)
	{
		tree.setHasTestFailed(false);
		tree.setHasTestRun(false);
	}

	/**
	 * Called if execution fails
	 * 
	 * @param event
	 */
	public void executionFailed(
		ExecuteTestCaseEvent event)
	{
		TestPlan tp = event.getTestPlan();
		tree.setName(tp.getTestPlanName());
		tree.setHasTestFailed(true);
		tree.setHasTestRun(true);
		if ( !isBackgroundExecution ) {
			TestLinkView.refresh(tree);
			TestLinkView.refresh(event.getTestCase());
			UserMsg.error(event.getFaulure(),
				"The execution of the test cases failed and did not complete.");
		}
	}
	
	/**
	 * Called if execution completes without an exception. This does not mean all test passed.
	 * 
	 * @param event
	 */
	public void executionSuccess(
		ExecuteTestCaseEvent event)
	{
		TestPlan tp = event.getTestPlan();
		tree.setName(tp.getTestPlanName());
		tree.setHasTestFailed(!(event.getExecutionPassStatus()));
		tree.setHasTestRun(true);
		if ( !isBackgroundExecution ) {
			TestLinkView.refresh(tree);
			TestLinkView.refresh(event.getTestCase());
		}
	}
	
	/**
	 * Called after the test cases has been reset as part of 
	 * preparation to start execution.
	 * 
	 * @param event
	 */
	public void testCasesReset(
		ExecuteTestCaseEvent event)
	{}
	
	/**
	 * Called before the test case runs
	 * 
	 * @param event
	 */
	public void testCaseStart(
		ExecuteTestCaseEvent event)
	{
		if ( !isBackgroundExecution ) {
			TestPlan tp = event.getTestPlan();
			TestCase tc = event.getTestCase();
			int c = ((event.getTotalCases() - event.getTotalRemainingCases()) + 1);
			tree.setName(
				tp.getTestPlanName() + " (Testing " + c + " of " + event.getTotalCases()
				+ " [" + tc.getTestCaseName() + "])");
			TestLinkView.refresh(tree);
		}
	}
	
	/**
	 * Called if a test case is found without a registered executor
	 * 
	 * @param event
	 */
	public void testCaseWithoutExecutor(
		ExecuteTestCaseEvent event)
	{}
	
	/**
	 * Called if report results to testlink api is enabled and it has
	 * and exception.
	 * 
	 * @param event
	 */
	public void testCaseReportResultsFailed(
		ExecuteTestCaseEvent event)
	{}
	
	/**
	 * Called at any time the test cases is being processed
	 * and has not reached completion and there is an execption.
	 * 
	 * @param event
	 */
	public void testCaseBombed(
		ExecuteTestCaseEvent event)
	{}
	
	/**
	 * Called when the test case has completed and the results
	 * have been registered.
	 * 
	 * @param event
	 */
	public void testCaseCompleted(
		ExecuteTestCaseEvent event)
	{
		if ( !isBackgroundExecution ) {
			TestLinkView.refresh(tree);
		}
	}
}
