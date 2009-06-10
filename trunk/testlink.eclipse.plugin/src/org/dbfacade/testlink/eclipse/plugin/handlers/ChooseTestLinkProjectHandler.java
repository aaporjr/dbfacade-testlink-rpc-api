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
package org.dbfacade.testlink.eclipse.plugin.handlers;


import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;


public class ChooseTestLinkProjectHandler extends SelectorHandler
{	
	public ChooseTestLinkProjectHandler(
		Shell shell)
	{
		super(shell);
	}

	public void handle(
		Label wLabel,
		Text text,
		Button button)
	{
		super.handle(wLabel, text, button);
		IJavaProject project = chooseJavaProject();
		if ( project == null ) {
			return;
		}
    
		String projectName = project.getElementName();
		text.setText(projectName);
	}
	
	/*
	 * Realize a Java Project selection dialog and return the first selected project,
	 * or null if there was none.
	 */
	private IJavaProject chooseJavaProject()
	{
		IJavaProject[] projects;
		try {
			projects = JavaCore.create(getWorkspaceRoot()).getJavaProjects();
		} catch ( JavaModelException e ) {
			// JUnitPlugin.log(e.getStatus());
			projects = new IJavaProject[0];
		}
        
		ILabelProvider labelProvider = new JavaElementLabelProvider(
			JavaElementLabelProvider.SHOW_DEFAULT);
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(),
			labelProvider);
		dialog.setTitle("Blah (choose project");
		dialog.setMessage("Bleh (choose project)");
		dialog.setElements(projects);
        
		IJavaProject javaProject = getJavaProject();
		if ( javaProject != null ) {
			dialog.setInitialSelections(new Object[] { javaProject });
		}
		if ( dialog.open() == Window.OK ) {
			return (IJavaProject) dialog.getFirstResult();
		}
		return null;
	}
    
	/*
	 * Convenience method to get the workspace root.
	 */
	private IWorkspaceRoot getWorkspaceRoot()
	{
		return ResourcesPlugin.getWorkspace().getRoot();
	}
    
	/*
	 * Return the IJavaProject corresponding to the project name in the project name
	 * text field, or null if the text does not match a project name.
	 */
	private IJavaProject getJavaProject()
	{
		String projectName = text.getText().trim();
		if ( projectName.length() < 1 ) {
			return null;
		}
		return getJavaModel().getJavaProject(projectName);
	}
    
	/*
	 * Convenience method to get access to the java model.
	 */
	private IJavaModel getJavaModel()
	{
		return JavaCore.create(getWorkspaceRoot());
	}
    
}
