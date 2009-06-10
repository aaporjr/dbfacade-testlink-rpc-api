package org.dbfacade.testlink.eclipse.plugin.launcher;

	
/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 * Sebastian Davids: sdavids@gmx.de bug: 26293, 27889
 * David Saff (saff@mit.edu) - bug 102632: [JUnit] Support for JUnit 4.
 * Daniel R Padilla Modified for TestLink
 *******************************************************************************/

import org.dbfacade.testlink.eclipse.plugin.handlers.ChooseProjectHandler;
import org.dbfacade.testlink.eclipse.plugin.handlers.ChooseTestLinkProjectHandler;
import org.dbfacade.testlink.eclipse.plugin.handlers.SelectorHandler;
import org.dbfacade.testlink.eclipse.plugin.handlers.SelectorWidget;
import org.dbfacade.testlink.eclipse.plugin.preferences.PreferenceConstants;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * The launch configuration tab for TestLink.
 * <p>
 * This class may be instantiated but is not intended to be subclassed.
 * </p>
 * @since 3.3
 */
public class TestLinkLaunchConfigurationTab extends AbstractLaunchConfigurationTab
{
	// Widgets
	SelectorWidget project;
	ChooseTestLinkProjectHandler testLinkHandler;
	SelectorWidget testLinkProject;
	SelectorWidget testLinkKey;
	SelectorWidget testLinkURL;
	SelectorWidget testLinkExternalPath;
	SelectorWidget testLinkPrepClass;
	
	// TestLink API Access
	private String tlProject;
	private String tlDevKey;
	private String tlUrl;

	/**
	 * Called during eclipse startup
	 */
	public void createControl(
		Composite parent)
	{ 
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 3;
		
		comp.setLayout(topLayout);
 
		createSingleTestSection(comp);   
		
		Dialog.applyDialogFont(comp);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
			"I_have_no_context_if_at_this_time");
	}

	/**
	 * Tab title on the form
	 */
	public String getName()
	{
		return "TestLink Plan Execution";
	}

	/**
	 * Called at startup of "Run configurations"
	 */
	public void initializeFrom(
		ILaunchConfiguration config)
	{ 
		// Initialize from a saved copy
		setFromWorkingConfig(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
			project.getWidgetText(), config);
		setFromWorkingConfig(PreferenceConstants.P_DEFAULT_PROJECT_NAME,
			testLinkProject.getWidgetText(), config);
		setFromWorkingConfig(PreferenceConstants.P_DEV_KEY, testLinkKey.getWidgetText(),
			config);
		setFromWorkingConfig(PreferenceConstants.P_TESTLINK_URL,
			testLinkURL.getWidgetText(), config);
		setFromWorkingConfig(PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS,
			testLinkPrepClass.getWidgetText(), config);
		setFromWorkingConfig(PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE,
			testLinkExternalPath.getWidgetText(), config);
		
		// If nothing is set provide the defaults
		TestLinkPreferences pref = new TestLinkPreferences();
		setFromPreference(PreferenceConstants.P_DEFAULT_PROJECT_NAME,
			testLinkProject.getWidgetText(), pref.getDefaultProject());
		setFromPreference(PreferenceConstants.P_DEV_KEY, testLinkKey.getWidgetText(),
			pref.getDevKey());
		setFromPreference(PreferenceConstants.P_TESTLINK_URL,
			testLinkURL.getWidgetText(), pref.getTestLinkAPIURL());
		setFromPreference(PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS,
			testLinkPrepClass.getWidgetText(), pref.getTestPlanPrepareClass());
		setFromPreference(PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE,
			testLinkExternalPath.getWidgetText(), pref.getExternalPath());
		
		// Initialize local variables needed for later
		try {
			tlProject = config.getAttribute(PreferenceConstants.P_DEFAULT_PROJECT_NAME, "");
			tlDevKey = config.getAttribute(PreferenceConstants.P_DEV_KEY, "");
			tlUrl = config.getAttribute(PreferenceConstants.P_TESTLINK_URL, "");
			if ( testLinkHandler != null ) {
				testLinkHandler.setAPIAccess(tlProject, tlDevKey, tlUrl);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	/**
	 * Listener for form changes
	 */
	public void performApply(
		ILaunchConfigurationWorkingCopy config)
	{
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
			project.getWidgetText().getText());
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "");
		config.setAttribute(PreferenceConstants.P_DEFAULT_PROJECT_NAME,
			testLinkProject.getWidgetText().getText());
		config.setAttribute(PreferenceConstants.P_DEV_KEY,
			testLinkKey.getWidgetText().getText());
		config.setAttribute(PreferenceConstants.P_TESTLINK_URL,
			testLinkURL.getWidgetText().getText());
		config.setAttribute(PreferenceConstants.P_DEFAULT_TESTPLAN_PREP_CLASS,
			testLinkPrepClass.getWidgetText().getText());
		config.setAttribute(PreferenceConstants.P_OPTIONAL_EXTERNAL_CONFIG_FILE,
			testLinkExternalPath.getWidgetText().getText());

		/*
		 try {
		 mapResources(config);
		 } catch (CoreException e) {
		 JUnitPlugin.log(e.getStatus());
		 }
		 */
	}

	/**
	 * Still do not know
	 */
	public void setDefaults(
		ILaunchConfigurationWorkingCopy config)
	{
		IJavaElement javaElement = getContext();
		if ( javaElement != null ) {
			initializeJavaProject(javaElement, config);
		} else {}
	}
     
	/*
	 * Private methods
	 */
	private void initializeJavaProject(
		IJavaElement javaElement,
		ILaunchConfigurationWorkingCopy config)
	{
		IJavaProject javaProject = javaElement.getJavaProject();
		String name = null;
		if ( javaProject != null && javaProject.exists() ) {
			name = javaProject.getElementName();
		}
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, name);
	}
	
	/*
	 * Returns the current Java element context from which to initialize
	 * default settings, or <code>null</code> if none.
	 *
	 * @return Java element context.
	 */
	private IJavaElement getContext()
	{
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if ( activeWorkbenchWindow == null ) {
			return null;
		}
		IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
		if ( page != null ) {
			ISelection selection = page.getSelection();
			if ( selection instanceof IStructuredSelection ) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				if ( !ss.isEmpty() ) {
					Object obj = ss.getFirstElement();
					if ( obj instanceof IJavaElement ) {
						return (IJavaElement) obj;
					}
					if ( obj instanceof IResource ) {
						IJavaElement je = JavaCore.create((IResource) obj);
						if ( je == null ) {
							IProject pro = ((IResource) obj).getProject();
							je = JavaCore.create(pro);
						}
						if ( je != null ) {
							return je;
						}
					}
				}
			}
			IEditorPart part = page.getActiveEditor();
			if ( part != null ) {
				IEditorInput input = part.getEditorInput();
				return (IJavaElement) input.getAdapter(IJavaElement.class);
			}
		}
		return null;
	}
    
	/*
	 * Set single test section
	 */
	private void createSingleTestSection(
		Composite comp)
	{        
		SelectorHandler projectHandler = new ChooseProjectHandler(getShell());
		project = new SelectorWidget(comp, "Eclipse Project:", "Select Eclipse Project",
			projectHandler, this.getLaunchConfigurationDialog());
		
		testLinkHandler = new ChooseTestLinkProjectHandler(getShell(), tlProject, tlDevKey,
			tlUrl);
		testLinkProject = new SelectorWidget(comp, "TestLink Project:",
			"Select TestLink Project", testLinkHandler, this.getLaunchConfigurationDialog());
		
		testLinkKey = new SelectorWidget(comp, "Dev Key:", null, null,
			this.getLaunchConfigurationDialog());
		
		testLinkURL = new SelectorWidget(comp, "TestLink URL:", null, null,
			this.getLaunchConfigurationDialog());
		
		testLinkExternalPath = new SelectorWidget(comp, "External Path:", null, null,
			this.getLaunchConfigurationDialog());
          

		/*
		 * Eventually we want to search for classes but for now leave it as text
		 *
		 *		SelectorHandler prepClassHandler = new SearchClassHandler(getShell());
		 * testLinkPrepClass = new SelectorWidget(comp, "TestPlanPrepareClass implementer:",
		 * "Select class", prepClassHandler, this.getLaunchConfigurationDialog());
		 */
		testLinkPrepClass = new SelectorWidget(comp, "TestPlanPrepareClass implementer:",
				null, null, this.getLaunchConfigurationDialog());

	} 
	
	/*
	 * Initialize the variables from this class using what comes back from
	 * the configuration that is returned by the caller to the configuration
	 * objects that exist.
	 */
	private void setFromWorkingConfig(
		String key,
		Text text,
		ILaunchConfiguration config)
	{
		if ( text == null || key == null || config == null ) {
			return;
		}
		String value = ""; 
		try {
			value = config.getAttribute(key, ""); 
		} catch ( CoreException ce ) {}
		text.setText(value);
	}
	
	/*
	 * Initialize the variables from this class using what comes back from
	 * the configuration that is returned by the caller to the configuration
	 * objects that exist.
	 */
	private void setFromPreference(
		String key,
		Text text,
		String value)
	{
		if ( text == null || key == null || value == null ) {
			return;
		}
		if ( text.getText() == null || text.getText() == "" ) {
			text.setText(value);
		}
	}
 
}
