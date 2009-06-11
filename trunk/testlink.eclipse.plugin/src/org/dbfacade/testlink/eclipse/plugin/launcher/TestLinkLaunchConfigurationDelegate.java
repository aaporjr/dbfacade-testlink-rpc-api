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

import org.dbfacade.testlink.eclipse.plugin.preferences.PreferenceConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

public class TestLinkLaunchConfigurationDelegate extends AbstractJavaLaunchConfigurationDelegate
{
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
			
			// check for cancellation
			if (monitor.isCanceled()) {
				return;
			}

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
	

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate#verifyMainTypeName(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public String verifyMainTypeName(ILaunchConfiguration configuration) throws CoreException {
		return "org.dbfacade.testlink.eclipse.plugin.launcher.TestLinkRunner";
	}


	/**
	 * Collects all VM and program arguments. Implementors can modify and add arguments.
	 *
	 * @param configuration the configuration to collect the arguments for
	 * @param vmArguments a {@link List} of {@link String} representing the resulting VM arguments
	 * @param programArguments a {@link List} of {@link String} representing the resulting program arguments
	 * @exception CoreException if unable to collect the execution arguments
	 */
	protected void collectExecutionArguments(ILaunchConfiguration config, List<String> vmArguments, List<String> programArguments) throws CoreException {

		// Standard code to initialize paramters
		String pgmArgs= getProgramArguments(config);
		String vmArgs= getVMArguments(config);
		ExecutionArguments execArgs= new ExecutionArguments(vmArgs, pgmArgs);
		vmArguments.addAll(Arrays.asList(execArgs.getVMArgumentsArray()));
		programArguments.addAll(Arrays.asList(execArgs.getProgramArgumentsArray()));
		
		// Initialize from a saved copy of the configuration
		setParamFromConfig(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
			 config, programArguments);
		setParamFromConfig(PreferenceConstants.P_DEFAULT_PROJECT_NAME,
			 config, programArguments);
		setParamFromConfig(PreferenceConstants.P_DEV_KEY, 
			config, programArguments);
		setParamFromConfig(PreferenceConstants.P_TESTLINK_URL,
			 config, programArguments);
		setParamFromConfig(PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS,
			config, programArguments);
		setParamFromConfig(PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE,
			 config, programArguments);

		
	}
	
	/*
	 * Initialize the variables from this class using what comes back from
	 * the configuration that is returned by the caller to the configuration
	 * objects that exist.
	 */
	private void setParamFromConfig(
		String key,
		ILaunchConfiguration config,
		List<String> programArguments)
	{
		if ( key == null || config == null ) {
			return;
		}
		String value = ""; 
		try {
			value = config.getAttribute(key, ""); 
		} catch ( CoreException ce ) {}
		
		if ( value != null ) {
			programArguments.add(key);
			programArguments.add(value.toString()); 
		}
	}



}
