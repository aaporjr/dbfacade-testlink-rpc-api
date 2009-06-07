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
package org.dbfacade.testlink.eclipse.plugin.views.tree;


import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;


public class ViewLabelProvider extends LabelProvider
{
	private IConfigurationElement configElement;
	private Image folder;
	private Image bombed;
	private Image running_auto;
	private Image wait_manual_feedback;
	private Image passed;
	private Image failed;
	private Image blocked;
	private Image auto_exec_bad;
	private Image auto_exec_good;
	private Image manual_exec;
	private Image projectClose;
	private Image projectOpen;
	private Image projectSwitch;

	public ViewLabelProvider(
		IConfigurationElement cfg)
	{
		this.configElement = cfg;
		projectOpen = getImage("icons/drawer_open.png");
		projectSwitch = getImage("icons/drawer_switch.png");
		projectClose = getImage("icons/drawer.png");
		folder = getImage("icons/folder_yellow.png");
		bombed = getImage("icons/bomb.png");
		running_auto = getImage("icons/wait.png");
		wait_manual_feedback = getImage("icons/hand.png");
		passed = getImage("icons/accept.png");
		failed = getImage("icons/cancel.png");
		blocked = getImage("icons/lock.png");
		auto_exec_bad = getImage("icons/page_white_delete.png");
		auto_exec_good = getImage("icons/page_white_cup.png");
		manual_exec = getImage("icons/page_manual.png");
	}
	
	public String getText(
		Object obj)
	{
		return obj.toString();
	}

	public Image getImage(
		Object obj)
	{
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		
		// Take care of action requests
		if ( obj instanceof Action) {
			Action a = (Action) obj;
		}
		
		
		// Take care of project tree requests
		if ( obj instanceof ProjectTree ) {
			ProjectTree tree = (ProjectTree) obj;
			if ( tree.isOpen() ) {
				return projectOpen;
			} else {
				return projectClose;
			}
		}
		
		// Take care of plan requests
		if ( obj instanceof PlanTree ) {
			return folder;
		}
		
		
		// Test care of test case requests
		if ( obj instanceof TestCaseLeaf ) {
			
			TestCaseLeaf tcLeaf = (TestCaseLeaf) obj;
			TestCase tc = tcLeaf.getTestCase();
			TestCaseExecutor te = tc.getExecutor();
			
			boolean isImageSet = true;
			if ( te != null ) {
				if ( te.getExecutionState() == TestCaseExecutor.STATE_BOMBED ) {
					return bombed;
				} else if ( te.getExecutionState() == TestCaseExecutor.STATE_RUNNING ) {
					if ( tc.isAutoExec() ) {
						return running_auto;
					} else {
						return wait_manual_feedback;
					}
				} else if ( te.getExecutionState() == TestCaseExecutor.STATE_COMPLETED ) {
					if ( te.getExecutionResult() == TestCaseExecutor.RESULT_BLOCKED ) {
						return blocked;
					} else if ( te.getExecutionResult() == TestCaseExecutor.RESULT_PASSED ) {
						return passed;
					} else {
						return failed;
					}
				} else {
					isImageSet = false;
				}
			} else {
				isImageSet = false;			
			}
			
			if ( !isImageSet ) {
				if ( tc.isAutoExec() ) {
					if ( te != null ) {
						return auto_exec_good;
					} else {
						return auto_exec_bad;
					}
				} else {
					return manual_exec;
				}
			}
		}
		
		// Default at the end
		if ( obj instanceof TreeParent ) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}
		
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
	
	private Image getImage(
		String strIcon)
	{
		ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
			configElement.getNamespace(), strIcon);

		if ( imageDescriptor == null ) {
			return null;
		}

		return JFaceResources.getResources().createImageWithDefault(imageDescriptor);
	}
}
