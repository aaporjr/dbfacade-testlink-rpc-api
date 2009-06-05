/*
 * Database Facade
 *
 * Copyright (c) 2009, Database Facade
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

import org.dbfacade.testlink.api.client.TestLinkAPIException;

public interface TestCaseExecutor {
	public static final short UNKNOWN=-1;
	public static final short PASSED=0;
	public static final short FAILED=1;
	public static final short BLOCKED=2;
	
	/**
	 * Return the result state of the test case execution.
	 * 
	 * @return The result of the test case. Implementers should set the initial status to UNKNOWN.
	 */
	public short getExecutionResult();
	
	/**
	 * Set the results of the test from an external source.
	 * 
	 * @param result
	 */
	public void setExecutionResult(short result);
	
	/**
	 * Information about the results of the execution.
	 * 
	 * @return Information about the results of the execution.
	 */
	public String getExecutionNotes();
	
	/**
	 * Execute the test case that has been passed into the execute method.
	 * 
	 * @param testCase
	 * @throws TestLinkAPIException
	 */
	public void execute(TestCase testCase) throws TestLinkAPIException;
}
