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
package org.dbfacade.testlink.eclipse.plugin.views;

import org.dbfacade.testlink.tc.autoexec.EmptyExecutor;
import org.dbfacade.testlink.tc.autoexec.TestCase;

public class ManualExecutor extends EmptyExecutor {

	public void execute(TestCase tc) {
		this.setExecutionState(STATE_RUNNING);
		try {
			ManualResultsInput.setManualTestResults(tc, this);
		} catch (Exception e) {
			this.setExecutionResult(RESULT_FAILED);
			this.setExecutionState(STATE_BOMBED);
			return;
		}
		this.setExecutionState(STATE_COMPLETED);
	}
}
