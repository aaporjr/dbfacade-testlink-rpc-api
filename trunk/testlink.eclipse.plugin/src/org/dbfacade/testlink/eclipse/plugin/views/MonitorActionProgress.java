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


import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;


/**
 * This class represents a long running operation
 */
public class MonitorActionProgress
{
	ProgressMonitorDialog progress;
	IRunnableWithProgress monitor;

	public MonitorActionProgress(
		String message,
		MonitoredAction thread)
	{
		progress = new ProgressMonitorDialog(TestLinkView.viewer.getControl().getShell());
		monitor = new MonitorAction(message, thread);
	}
	
	public void startAndWait() throws InvocationTargetException,
			InterruptedException
	{
		progress.run(true, true, monitor);
	}
	
}

	
class MonitorAction implements IRunnableWithProgress
{
	private static final int secs=15;
	private static final int INCREMENT = 1000;
	private static final int TOTAL_TIME = INCREMENT * secs;
	private static final int LAST_PUSH = INCREMENT * (secs / 10);
	private MonitoredAction action;
	private String message;
	
	public MonitorAction(
		String message,
		MonitoredAction thread)
	{
		action = thread;
		this.message = message;
	}
	
	/**
	 * Waits a average of 30 seconds for a task
	 * 
	 * @param monitor
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public void run(
		IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException
	{
		monitor.beginTask(message, TOTAL_TIME + LAST_PUSH);
		action.start();
		for ( int total = 0; total < TOTAL_TIME && !monitor.isCanceled()
			&& !action.hasCompletedProcessing(); total += INCREMENT ) {
			Thread.sleep(INCREMENT);
			monitor.worked(INCREMENT);
			total += INCREMENT;
		}
		
		// More than 30 seconds on this one so now we wait.
		while ( !action.hasCompletedProcessing() ) {
			Thread.sleep(200);
		}
		
		monitor.worked(LAST_PUSH);
		
		monitor.done();
		if ( monitor.isCanceled() ) {
			throw new InterruptedException("The operation was cancelled");
		}
	}
}
