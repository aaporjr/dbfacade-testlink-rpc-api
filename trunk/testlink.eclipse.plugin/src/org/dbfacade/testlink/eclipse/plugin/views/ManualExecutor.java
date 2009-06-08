package org.dbfacade.testlink.eclipse.plugin.views;

import org.dbfacade.testlink.tc.autoexec.EmptyExecutor;
import org.dbfacade.testlink.tc.autoexec.TestCase;

public class ManualExecutor extends EmptyExecutor {

	public void execute(TestCase tc) {
		this.setExecutionState(STATE_RUNNING);
		try {
			ManualResultsInput.getManualTestResults(tc, this);
		} catch (Exception e) {
			this.setExecutionResult(RESULT_FAILED);
			this.setExecutionState(STATE_BOMBED);
			return;
		}
		this.setExecutionState(STATE_COMPLETED);
	}
}
