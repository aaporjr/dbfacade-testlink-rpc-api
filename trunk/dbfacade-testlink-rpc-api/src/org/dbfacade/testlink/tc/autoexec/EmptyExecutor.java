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
package org.dbfacade.testlink.tc.autoexec;


import java.util.Random;

import org.dbfacade.testlink.api.client.TestLinkAPIException;


public class EmptyExecutor implements TestCaseExecutor
{
	public static final short MODE_BOMB = 1;
	public static final short MODE_TEST = 2;
	private short mode = MODE_BOMB;
	private short testState = STATE_READY;
	private short testResult = RESULT_FAILED; 
	
	public EmptyExecutor()
	{
		mode = MODE_BOMB;
	}
	
	public EmptyExecutor(
		short mode)
	{
		this.mode = mode;
	}
	
	/**
	 * Get the state of the execution
	 * 
	 * @return
	 */
	public short getExecutionState()
	{
		return testState;
	}
	
	/**
	 * Set the new state of the executor
	 *  
	 * @param newState
	 */
	public void setExecutionState(
		short newState)
	{
		this.testState = newState;
	}
	
	/**
	 * Return FAILED result state of the test case execution.
	 * 
	 * @return The result of the test case. Implementers should set the initial status to UNKNOWN.
	 */
	public short getExecutionResult()
	{
		return testResult;
	}

	/**
	 * Set the results of the test from an external source.
	 * 
	 * @param result
	 */
	public void setExecutionResult(
		short result)
	{
			testResult = result;
	}
	
	/**
	 * Information about the results of the execution.
	 * 
	 * @return Information about the results of the execution.
	 */
	public String getExecutionNotes()
	{
		return "Empty executor generated to report executor missing from test case.";
	}
	
	/**
	 * Execute the test case that has been passed into the execute method.
	 * 
	 * @param testCase
	 * @throws TestLinkAPIException
	 */
	public void execute(
		TestCase testCase) throws TestLinkAPIException
	{
		if ( mode == MODE_BOMB ) {
			setExecutionState(STATE_BOMBED);
			throw new TestLinkAPIException(
				"This is an empty executor to hold failed test results.");
		} else {
			Random rand = new Random();
			int v = rand.nextInt(10);
			if ( v < 2 ) {
				setExecutionState(STATE_READY);
			} else if ( v >= 2 && v < 4 ) {
				setExecutionState(STATE_RUNNING);
			} else if ( v >= 4 && v < 7 ) {
				setExecutionState(STATE_COMPLETED);
				if ( v == 4 ) {
					setExecutionResult(RESULT_FAILED);
				} else if ( v == 5 ) {
					setExecutionResult(RESULT_PASSED);
				} else {
					setExecutionResult(RESULT_BLOCKED);
				}
			} else if ( v >= 7 && v < 9 ) {
				setExecutionState(STATE_RESET);
			} else {
				setExecutionState(STATE_BOMBED);
			}
			
		}
	}

}
