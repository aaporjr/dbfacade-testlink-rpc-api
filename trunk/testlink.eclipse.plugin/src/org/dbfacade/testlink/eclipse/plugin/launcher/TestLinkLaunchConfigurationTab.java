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
import org.dbfacade.testlink.eclipse.plugin.handlers.SearchClassHandler;
import org.dbfacade.testlink.eclipse.plugin.handlers.SelectorHandler;
import org.dbfacade.testlink.eclipse.plugin.handlers.SelectorWidget;
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
	private Text fProjText;
	private Text fPrepClass;
	   
	@Override
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

	@Override
	public String getName()
	{
		return "TestLink Plan Execution";
	}

	@Override
	public void initializeFrom(
		ILaunchConfiguration config)
	{ 
		updateProjectFromConfig(config);
		String containerHandle = ""; // $NON-NLS-1$
		try {} catch ( Exception ce ) {}
	}

	@Override
	public void performApply(
		ILaunchConfigurationWorkingCopy config)
	{}

	@Override
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
		SelectorWidget project = new SelectorWidget(comp, "Eclipse Project:",
			"Select project", projectHandler, this.getLaunchConfigurationDialog());
		fProjText = project.getWidgetText();
          
		SelectorHandler prepClassHandler = new SearchClassHandler(getShell());
		SelectorWidget prepClass = new SelectorWidget(comp, "TestPlanPrepareClass implementer:",
			"Select class", prepClassHandler, this.getLaunchConfigurationDialog());
		fPrepClass = prepClass.getWidgetText();
	} 
	
	/*
	 * Update project from configuration
	 */
	private void updateProjectFromConfig(
		ILaunchConfiguration config)
	{
		String projectName = ""; 
		try {
			projectName = config.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); 
		} catch ( CoreException ce ) {}
		fProjText.setText(projectName);
	}
 
}
