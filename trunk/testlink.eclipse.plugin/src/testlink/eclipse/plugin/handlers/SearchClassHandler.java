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
package testlink.eclipse.plugin.handlers;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SearchClassHandler extends SelectorHandler {

	public SearchClassHandler(Shell shell) {
		super(shell);
	}

    /*
     * Show a dialog that lists all main types
     */
	public void handle(
			Label wLabel,
			Text text,
			Button button)
		{
			super.handle(wLabel, text, button);
		Shell shell = getShell();

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
