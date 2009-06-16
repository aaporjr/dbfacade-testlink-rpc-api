package org.dbfacade.testlink.tc.autoexec;


import org.dbfacade.testlink.tc.autoexec.server.RemoteClientExecutor;


public class ExecuteTestCase
{
	
	/**
	 * Process the request locally
	 * 
	 * @param tc
	 * @param te
	 * @param port
	 * @throws Exception
	 */
	public static void execute(
		TestPlan tp,
		TestCase tc,
		TestCaseExecutor te) throws Exception
	{
		execute(tp, tc, te, -1);
	}

	/**
	 * The method runs a test case. This method can be independently called
	 * and by passing a port it is considered a remote request. So it must not 
	 * allow dependencies of a non-remote nature to seep into the method.
	 * 
	 * @param tc
	 * @param te
	 * @param port
	 * @return
	 */
	public static void execute(
		TestPlan tp,
		TestCase tc,
		TestCaseExecutor te,
		int port) throws Exception
	{
		try {
			if ( port < 1 ) {
				te.execute(tc);
			} else {
				try {
					RemoteClientExecutor rte = new RemoteClientExecutor(port, tp);
					rte.execute(tc);
					te.setExecutionNotes(rte.getExecutionNotes());
					te.setExecutionResult(rte.getExecutionResult());
					te.setExecutionState(rte.getExecutionState());
				} catch ( Exception e ) {
					te.setExecutionResult(TestCaseExecutor.RESULT_FAILED);
					te.setExecutionState(TestCaseExecutor.STATE_BOMBED);
					throw e;
				}
			}
		} catch ( Exception e ) {
			te.setExecutionResult(TestCaseExecutor.RESULT_FAILED);
			te.setExecutionState(TestCaseExecutor.STATE_BOMBED);
			throw e;
		}
	}

}
