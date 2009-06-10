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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.util.PixelConverter;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;


/**
 * The launch configuration tab for TestLink.
 <p>
 * This class may be instantiated but is not intended to be subclassed.
 * </p>
 * @since 3.3
 */
@SuppressWarnings("restriction")
public class OrigWithContainerLaunchConfigurationTab extends AbstractLaunchConfigurationTab
{
	// Project
	 private Label fProjLabel;
     private Text fProjText;
     private Button fProjButton;
     
     // Container
	private Text fContainerText;
	private IJavaElement fContainerElement;
	private final ILabelProvider fJavaElementLabelProvider = new JavaElementLabelProvider();
	private Button fContainerSearchButton;
	private Button fTestContainerRadioButton;
	private Button fTestRadioButton;
	private Label fTestLabel;
	
	// whatever
    // Test class UI widgets
    private Text fTestText;
	private Button fSearchButton;
	//private final Image fTestIcon= createImage("obj16/test.gif"); //$NON-NLS-1$
	private Label fTestMethodLabel;
	   
		     
	@Override
	public void createControl(
		Composite parent)
	{ // TODO Auto-generated method stub
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 3;
		comp.setLayout(topLayout);
 
        createSingleTestSection(comp);
		createTestContainerSelectionGroup(comp);    
		
		Dialog.applyDialogFont(comp);
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "I_have_no_context_if_at_this_time");
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "What ever test link";
	}

	@Override
	public void initializeFrom(
		ILaunchConfiguration config)
	{ // TODO Auto-generated method stub
		updateProjectFromConfig(config);
		@SuppressWarnings("unused")
		String containerHandle = ""; // $NON-NLS-1$
		try {
			/*
			containerHandle = config.getAttribute(
				JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, ""); // $NON-NLS-1$
				*/
		} catch ( Exception ce ) {}
	}

	@Override
	public void performApply(
		ILaunchConfigurationWorkingCopy config)
	{ // TODO Auto-generated method stub
		if ( fContainerElement != null ) {
		config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
	       fContainerElement.getJavaProject().getElementName());
		}
	}

	@Override
	public void setDefaults(
		ILaunchConfigurationWorkingCopy config)
	{
		// TODO Auto-generated method stub
		IJavaElement javaElement = getContext();
		if ( javaElement != null ) {
			initializeJavaProject(javaElement, config);
		} else {// We set empty attributes for project & main type so that when one config is
			// config.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); //$NON-NLS-1$
			// config.setAttribute(JUnitLaunchConfigurationConstants.ATTR_TEST_CONTAINER, ""); //$NON-NLS-1$
		}
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
	 * Create container selection group
	 */
	private void createTestContainerSelectionGroup(
		Composite comp)
	{
		fTestContainerRadioButton = new Button(comp, SWT.RADIO);
		fTestContainerRadioButton.setText("Blah (TestLink contain button)");
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		fTestContainerRadioButton.setLayoutData(gd);
		fTestContainerRadioButton.addSelectionListener(new SelectionListener()
		{
			public void widgetSelected(
				SelectionEvent e)
			{/*
				 if (fTestContainerRadioButton.getSelection())
				 testModeChanged();
				 */}

			public void widgetDefaultSelected(
				SelectionEvent e)
			{}
		});
        
		fContainerText = new Text(comp, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 25;
		gd.horizontalSpan = 2;
		fContainerText.setLayoutData(gd);
		fContainerText.addModifyListener(new ModifyListener()
		{
			public void modifyText(
				ModifyEvent evt)
			{
				updateLaunchConfigurationDialog();
			}
		});
        
		fContainerSearchButton = new Button(comp, SWT.PUSH);
		fContainerSearchButton.setText("Blih (testlink stuff");
		fContainerSearchButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(
				SelectionEvent evt)
			{
				handleContainerSearchButtonSelected();
			}
		});
		setButtonGridData(fContainerSearchButton);
	}
    
	/*
	 * Handle container selection
	 */
	private void handleContainerSearchButtonSelected()
	{
		IJavaElement javaElement = chooseContainer(fContainerElement);
		if ( javaElement != null ) {
			setContainerElement(javaElement);
		}
	}

	/*
	 * Set the container
	 */
	private void setContainerElement(
		IJavaElement javaElement)
	{
		fContainerElement = javaElement;
		fContainerText.setText(getPresentationName(javaElement));
		// validatePage();
		updateLaunchConfigurationDialog();
	}
	
	/*
	 * Set single test section
	 */
    private void createSingleTestSection(Composite comp) {
        fTestRadioButton= new Button(comp, SWT.RADIO);
        fTestRadioButton.setText("Blah (test section)");
        GridData gd = new GridData();
        gd.horizontalSpan = 3;
        fTestRadioButton.setLayoutData(gd);
        fTestRadioButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
            	/*
                if (fTestRadioButton.getSelection())
                    testModeChanged();
                    */
            }
        });
        
        fProjLabel = new Label(comp, SWT.NONE);
        fProjLabel.setText("Bleh (test section)");
        gd= new GridData();
        gd.horizontalIndent = 25;
        fProjLabel.setLayoutData(gd);
        
        fProjText= new Text(comp, SWT.SINGLE | SWT.BORDER);
        fProjText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fProjText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent evt) {
                updateLaunchConfigurationDialog();
                fSearchButton.setEnabled(fTestRadioButton.getSelection() && fProjText.getText().length() > 0);
            }
        });
            
        fProjButton = new Button(comp, SWT.PUSH);
        fProjButton.setText("Blih (test section)");
        fProjButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent evt) {
                handleProjectButtonSelected();
            }
        });
        setButtonGridData(fProjButton);
        
        fTestLabel = new Label(comp, SWT.NONE);
        gd = new GridData();
        gd.horizontalIndent = 25;
        fTestLabel.setLayoutData(gd);
        fTestLabel.setText("Bhlo (Test section)");
        
    
        fTestText = new Text(comp, SWT.SINGLE | SWT.BORDER);
        fTestText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fTestText.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent evt) {
               // validatePage();
                updateLaunchConfigurationDialog();
            }
        });
        
        fSearchButton = new Button(comp, SWT.PUSH);
        fSearchButton.setEnabled(fProjText.getText().length() > 0);
        fSearchButton.setText("Blah, Blah, Blah (testlink)");
        fSearchButton.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent evt) {
                handleSearchButtonSelected();
            }
        });
        setButtonGridData(fSearchButton);
        
        new Label(comp, SWT.NONE);
        
        fTestMethodLabel= new Label(comp, SWT.NONE);
        fTestMethodLabel.setText(""); //$NON-NLS-1$
        gd= new GridData();
        gd.horizontalSpan = 2;
        fTestMethodLabel.setLayoutData(gd);
        
    }

    
	/*
	 * Choose container
	 */
	private IJavaElement chooseContainer(
		IJavaElement initElement)
	{
		Class[] acceptedClasses = new Class[] {
			IPackageFragmentRoot.class, IJavaProject.class, IPackageFragment.class };
		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(
			acceptedClasses, false)
		{
			public boolean isSelectedValid(
				Object element)
			{
				return true;
			}
		};
        
		acceptedClasses = new Class[] {
			IJavaModel.class, IPackageFragmentRoot.class, IJavaProject.class,
			IPackageFragment.class };
		ViewerFilter filter = new TypedViewerFilter(acceptedClasses)
		{
			public boolean select(
				Viewer viewer,
				Object parent,
				Object element)
			{
				if ( element instanceof IPackageFragmentRoot
					&& ((IPackageFragmentRoot) element).isArchive() ) {
					return false;
				}
				return super.select(viewer, parent, element);
			}
		};

		StandardJavaElementContentProvider provider = new StandardJavaElementContentProvider();
		ILabelProvider labelProvider = new JavaElementLabelProvider(
			JavaElementLabelProvider.SHOW_DEFAULT);
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(),
			labelProvider, provider);
		dialog.setValidator(validator);
		dialog.setComparator(new JavaElementComparator());
		dialog.setTitle("Blah (choose container)");
		dialog.setMessage("Bleh (choose container)");
		dialog.addFilter(filter);
		dialog.setInput(JavaCore.create(getWorkspaceRoot()));
		dialog.setInitialSelection(initElement);
		dialog.setAllowMultiple(false);
        
		if ( dialog.open() == Window.OK ) {
			Object element = dialog.getFirstResult();
			return (IJavaElement) element;
		}
		return null;
	}
	
    
    /*
     * Show a dialog that lets the user select a project. This in turn provides
     * context for the main type, allowing the user to key a main type name, or
     * constraining the search for main types to the specified project.
     */
    private void handleProjectButtonSelected() {
        IJavaProject project = chooseJavaProject();
        if (project == null) {
            return;
        }
        
        String projectName = project.getElementName();
        fProjText.setText(projectName);
    }
    
    /*
     * Show a dialog that lists all main types
  */
    private void handleSearchButtonSelected() {
    	@SuppressWarnings("unused")
		Shell shell = getShell();
     
        @SuppressWarnings("unused")
		IJavaProject javaProject = getJavaProject();
        
        /*
        IType[] types= new IType[0];
        boolean[] radioSetting= new boolean[2];
        try {
            // fix for 66922 Wrong radio behaviour when switching
// remember the selected radio button
radioSetting[0]= fTestRadioButton.getSelection();
            radioSetting[1]= fTestContainerRadioButton.getSelection();
            
            types= TestSearchEngine.findTests(getLaunchConfigurationDialog(), javaProject, getSelectedTestKind());
        } catch (InterruptedException e) {
            setErrorMessage(e.getMessage());
            return;
        } catch (InvocationTargetException e) {
            JUnitPlugin.log(e.getTargetException());
            return;
        } finally {
            fTestRadioButton.setSelection(radioSetting[0]);
            fTestContainerRadioButton.setSelection(radioSetting[1]);
        }

        SelectionDialog dialog = new TestSelectionDialog(shell, types);
        dialog.setTitle(JUnitMessages.JUnitLaunchConfigurationTab_testdialog_title);
        dialog.setMessage(JUnitMessages.JUnitLaunchConfigurationTab_testdialog_message);
        if (dialog.open() == Window.CANCEL) {
            return;
        }
        
        Object[] results = dialog.getResult();
        if ((results == null) || (results.length < 1)) {
            return;
        }
        IType type = (IType)results[0];
        
        if (type != null) {
            fTestText.setText(type.getFullyQualifiedName('.'));
            javaProject = type.getJavaProject();
            fProjText.setText(javaProject.getElementName());
        }
        */
    }
        
    
	
	/*
	 * Convenience method to get the workspace root.
	 */
	private IWorkspaceRoot getWorkspaceRoot()
	{
		return ResourcesPlugin.getWorkspace().getRoot();
	}
    
	private String getPresentationName(
		IJavaElement element)
	{
		return fJavaElementLabelProvider.getText(element);
	}
    
	/*
	 * Layout util
	 */
	private void setButtonGridData(
		Button button)
	{
		GridData gridData = new GridData();
		button.setLayoutData(gridData);
		Object gd = button.getLayoutData();
		if ( gd instanceof GridData ) {
			((GridData) gd).widthHint = getButtonWidthHint(button);
			((GridData) gd).horizontalAlignment = GridData.FILL;
		}
	}
    
	/*
	 * Returns a width hint for a button control.
	 * @param button the button for which to set the dimension hint
	 * @return the width hint
	 */
	private int getButtonWidthHint(
		Button button)
	{
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter.convertHorizontalDLUsToPixels(
			IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}
	
	/*
	 * Update project from configuration
	 */
    private void updateProjectFromConfig(ILaunchConfiguration config) {
        String projectName= ""; //$NON-NLS-1$
try {
            projectName = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""); 
} catch (CoreException ce) {
        }
        fProjText.setText(projectName);
    }
 
    /*
     * Return the IJavaProject corresponding to the project name in the project name
     * text field, or null if the text does not match a project name.
     */
    private IJavaProject getJavaProject() {
        String projectName = fProjText.getText().trim();
        if (projectName.length() < 1) {
            return null;
        }
        return getJavaModel().getJavaProject(projectName);
    }
    
    /*
     * Convenience method to get access to the java model.
     */
    private IJavaModel getJavaModel() {
        return JavaCore.create(getWorkspaceRoot());
    }
    
    /*
     * Realize a Java Project
selection dialog and return the first selected project,
     * or null if there was none.
     */
    private IJavaProject chooseJavaProject() {
        IJavaProject[] projects;
        try {
            projects= JavaCore.create(getWorkspaceRoot()).getJavaProjects();
        } catch (JavaModelException e) {
           // JUnitPlugin.log(e.getStatus());
            projects= new IJavaProject[0];
        }
        
        ILabelProvider labelProvider= new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
        ElementListSelectionDialog dialog= new ElementListSelectionDialog(getShell(), labelProvider);
        dialog.setTitle("Blah (choose project");
        dialog.setMessage("Bleh (choose project)");
        dialog.setElements(projects);
        
        IJavaProject javaProject = getJavaProject();
        if (javaProject != null) {
            dialog.setInitialSelections(new Object[] { javaProject });
        }
        if (dialog.open() == Window.OK) {
            return (IJavaProject) dialog.getFirstResult();
        }
        return null;
    }


}
