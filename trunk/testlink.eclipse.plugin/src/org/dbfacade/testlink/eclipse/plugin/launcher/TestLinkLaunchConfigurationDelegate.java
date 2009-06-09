/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     David Saff (saff@mit.edu) - bug 102632: [JUnit] Support for JUnit 4.    
 *     Daniel R Padilla - Modified for use with TestLink plugin
 *******************************************************************************/
package org.dbfacade.testlink.eclipse.plugin.launcher;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

public class TestLinkLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate
{
	private boolean fKeepAlive= false;
	private int fPort;
	private IMember[] fTestElements;


	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {

		// TODO Auto-generated method stub
		
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		monitor.beginTask(MessageFormat.format("{0}...", new String[]{configuration.getName()}), 5); //$NON-NLS-1$
		// check for cancellation
		if (monitor.isCanceled()) {
			return;
		}

		try {
			
			/*
			if (mode.equals(JUnitLaunchConfigurationConstants.MODE_RUN_QUIETLY_MODE)) {
				launch.setAttribute(JUnitLaunchConfigurationConstants.ATTR_NO_DISPLAY, "true"); //$NON-NLS-1$
				mode = ILaunchManager.RUN_MODE;
			}
			*/

			//monitor.subTask(JUnitMessages.JUnitLaunchConfigurationDelegate_verifying_attriburtes_description);

			try {
				preLaunchCheck(configuration, launch, new SubProgressMonitor(monitor, 2));
			} catch (CoreException e) {
				if (e.getStatus().getSeverity() == IStatus.CANCEL) {
					monitor.setCanceled(true);
					return;
				}
				throw e;
			}
			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}

			/*
			fKeepAlive= mode.equals(ILaunchManager.DEBUG_MODE) && configuration.getAttribute(JUnitLaunchConfigurationConstants.ATTR_KEEPRUNNING, false);
			*/
			
			fKeepAlive= mode.equals(ILaunchManager.DEBUG_MODE);

			
			// fPort= evaluatePort();
			
			/*
			launch.setAttribute(JUnitLaunchConfigurationConstants.ATTR_PORT, String.valueOf(fPort));
			*/

			// fTestElements= evaluateTests(configuration, new SubProgressMonitor(monitor, 1));

			String mainTypeName= verifyMainTypeName(configuration);
			IVMRunner runner= getVMRunner(configuration, mode);

			File workingDir = verifyWorkingDirectory(configuration);
			String workingDirName = null;
			if (workingDir != null) {
				workingDirName= workingDir.getAbsolutePath();
			}

			// Environment variables
			String[] envp= getEnvironment(configuration);

			ArrayList vmArguments= new ArrayList();
			ArrayList programArguments= new ArrayList();
			collectExecutionArguments(configuration, vmArguments, programArguments);

			// VM-specific attributes
			Map vmAttributesMap= getVMSpecificAttributesMap(configuration);

			// Classpath
			String[] classpath= getClasspath(configuration);

			// Create VM config
			VMRunnerConfiguration runConfig= new VMRunnerConfiguration(mainTypeName, classpath);
			runConfig.setVMArguments((String[]) vmArguments.toArray(new String[vmArguments.size()]));
			runConfig.setProgramArguments((String[]) programArguments.toArray(new String[programArguments.size()]));
			runConfig.setEnvironment(envp);
			runConfig.setWorkingDirectory(workingDirName);
			runConfig.setVMSpecificAttributesMap(vmAttributesMap);

			// Bootpath
			runConfig.setBootClassPath(getBootpath(configuration));

			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}

			// done the verification phase
			monitor.worked(1);

			/*
			monitor.subTask(JUnitMessages.JUnitLaunchConfigurationDelegate_create_source_locator_description);
			*/
			
			// set the default source locator if required
			setDefaultSourceLocator(launch, configuration);
			monitor.worked(1);

			// Launch the configuration - 1 unit of work
			runner.run(runConfig, launch, monitor);

			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}
		} finally {
			// fTestElements= null;
			monitor.done();
		}

		
	}
	

	/**
	 * Performs a check on the launch configuration's attributes. If an attribute contains an invalid value, a {@link CoreException}
	 * with the error is thrown.
	 *
	 * @param configuration the launch configuration to verify
	 * @param launch the launch to verify
	 * @param monitor the progress monitor to use
	 * @throws CoreException an exception is thrown when the verification fails
	 */
	protected void preLaunchCheck(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		try {
			@SuppressWarnings("unused")
			IJavaProject javaProject= getJavaProject(configuration);
			
			/*
			if ((javaProject == null) || !javaProject.exists()) {
				informAndAbort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_invalidproject, null, IJavaLaunchConfigurationConstants.ERR_NOT_A_JAVA_PROJECT);
			}
			if (!TestSearchEngine.hasTestCaseType(javaProject)) {
				informAndAbort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_junitnotonpath, null, IJUnitStatusConstants.ERR_JUNIT_NOT_ON_PATH);
			}
			*/
			
			/*
			ITestKind testKind= getTestRunnerKind(configuration);
			boolean isJUnit4Configuration= TestKindRegistry.JUNIT4_TEST_KIND_ID.equals(testKind.getId());
			if (isJUnit4Configuration && ! TestSearchEngine.hasTestAnnotation(javaProject)) {
				informAndAbort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_junit4notonpath, null, IJUnitStatusConstants.ERR_JUNIT_NOT_ON_PATH);
			}
			*/
		} finally {
			monitor.done();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate#verifyMainTypeName(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public String verifyMainTypeName(ILaunchConfiguration configuration) throws CoreException {
		//return "org.eclipse.jdt.internal.junit.runner.RemoteTestRunner"; //$NON-NLS-1$
		return "org.dbfacade.testlink.eclipse.plugin.Runner";
	}


	/**
	 * Collects all VM and program arguments. Implementors can modify and add arguments.
	 *
	 * @param configuration the configuration to collect the arguments for
	 * @param vmArguments a {@link List} of {@link String} representing the resulting VM arguments
	 * @param programArguments a {@link List} of {@link String} representing the resulting program arguments
	 * @exception CoreException if unable to collect the execution arguments
	 */
	protected void collectExecutionArguments(ILaunchConfiguration configuration, List<String> vmArguments, List<String> programArguments) throws CoreException {

		// add program & VM arguments provided by getProgramArguments and getVMArguments
		String pgmArgs= getProgramArguments(configuration);
		String vmArgs= getVMArguments(configuration);
		ExecutionArguments execArgs= new ExecutionArguments(vmArgs, pgmArgs);
		vmArguments.addAll(Arrays.asList(execArgs.getVMArgumentsArray()));
		programArguments.addAll(Arrays.asList(execArgs.getProgramArgumentsArray()));

		// String testFailureNames= configuration.getAttribute(JUnitLaunchConfigurationConstants.ATTR_FAILURES_NAMES, ""); //$NON-NLS-1$

		programArguments.add("-version"); //$NON-NLS-1$
		programArguments.add("3"); //$NON-NLS-1$

		programArguments.add("-port"); //$NON-NLS-1$
		programArguments.add(String.valueOf(fPort));

		if (fKeepAlive)
			programArguments.add(0, "-keepalive"); //$NON-NLS-1$

		//ITestKind testRunnerKind= getTestRunnerKind(configuration);

		programArguments.add("-testLoaderClass"); //$NON-NLS-1$
		// programArguments.add(testRunnerKind.getLoaderClassName());
		programArguments.add("-loaderpluginname"); //$NON-NLS-1$
		// programArguments.add(testRunnerKind.getLoaderPluginId());

		IMember[] testElements = fTestElements;

		// a test name was specified just run the single test
		if (testElements.length == 1) {
			if (testElements[0] instanceof IMethod) {
				IMethod method= (IMethod) testElements[0];
				programArguments.add("-test"); //$NON-NLS-1$
				programArguments.add(method.getDeclaringType().getFullyQualifiedName()+':'+method.getElementName());
			} else if (testElements[0] instanceof IType) {
				IType type= (IType) testElements[0];
				programArguments.add("-classNames"); //$NON-NLS-1$
				programArguments.add(type.getFullyQualifiedName());
			} else {
				// informAndAbort(JUnitMessages.JUnitLaunchConfigurationDelegate_error_wrong_input, null, IJavaLaunchConfigurationConstants.ERR_UNSPECIFIED_MAIN_TYPE);
			}
		} else if (testElements.length > 1) {
			// String fileName= createTestNamesFile(testElements);
			// programArguments.add("-testNameFile"); //$NON-NLS-1$
			// programArguments.add(fileName);
		}
		//if (testFailureNames.length() > 0) {
		//	programArguments.add("-testfailures"); //$NON-NLS-1$
		//	programArguments.add(testFailureNames);
		//}
	}



}
