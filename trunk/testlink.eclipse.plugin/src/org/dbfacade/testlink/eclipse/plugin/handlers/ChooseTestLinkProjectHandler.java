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


import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIResults;
import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;


public class ChooseTestLinkProjectHandler extends SelectorHandler
{	
	private String currentProject;
	private String devKey;
	private String url;
	
	public ChooseTestLinkProjectHandler(
		Shell shell,
		String currentProject,
		String devKey,
		String url)
	{
		super(shell);
		this.currentProject = currentProject;
		this.devKey = devKey;
		this.url = url;
	}

	public void handle(
		Label wLabel,
		Text text,
		Button button)
	{
		super.handle(wLabel, text, button);    
		String projectName = chooseTestLinkProject();
		text.setText(projectName);
	}
	
	public void setAPIAccess(
		String currentProject,
		String devKey,
		String url)
	{
		this.currentProject = currentProject;
		this.devKey = devKey;
		this.url = url;
	}
	
	/*
	 * Realize a Java Project selection dialog and return the first selected project,
	 * or null if there was none.
	 */
	private String chooseTestLinkProject()
	{
		try {
			int p = 0;
			TestLinkAPIResults results = null;
		
			try {
				TestLinkAPIClient api = new TestLinkAPIClient(devKey, url);
				results = api.getProjects();
				p = results.size();
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			String projects[] = new String[p];
		
			for ( int i = 0; i < p; i++ ) {
				String project = (String) results.getValueByName(i,
					TestLinkAPIConst.API_RESULT_NAME);
				if ( project != null ) {
					projects[i] = project;
				}
			}
	
			ILabelProvider labelProvider = new LabelProvider();
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(),
				labelProvider);
			dialog.setTitle("TestLink Project");
			dialog.setMessage("TestLink Projects");
			dialog.setElements(projects);
        
			if ( dialog.open() == Window.OK ) {
				currentProject = (String) dialog.getFirstResult();
			}
			return currentProject;
		} catch ( Exception e ) {
			UserMsg.error(e, "Unable to select TestLink projects.");
			return currentProject;
		}
	}
    
}
