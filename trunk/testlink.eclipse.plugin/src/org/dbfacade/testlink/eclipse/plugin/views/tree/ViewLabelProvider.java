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


import java.net.URL;

import org.dbfacade.testlink.eclipse.plugin.views.TestLinkAction;
import org.dbfacade.testlink.eclipse.plugin.views.TestLinkMode;
import org.dbfacade.testlink.tc.autoexec.TestCase;
import org.dbfacade.testlink.tc.autoexec.TestCaseExecutor;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


public class ViewLabelProvider extends LabelProvider
{
	private IConfigurationElement configElement;
	private Image plan_ready;
	private Image plan_passed;
	private Image plan_failed;
	private Image plan_inactive;
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
	
	// Image Files
	private String fileProjectOpen = "icons/drawer_open.png";
	private String fileProjectClose = "icons/drawer.png";
	private String fileProjectSwitch = "icons/drawer_switch.png";
	private String fileExec = "icons/exec.gif";
	private String filePrep = "icons/new_con.gif";

	public ViewLabelProvider(
		IConfigurationElement cfg)
	{
		this.configElement = cfg;
		projectOpen = getImage(fileProjectOpen);
		projectClose = getImage(fileProjectClose);
		plan_ready = getImage("icons/folder_yellow.png");
		plan_passed = getImage("icons/folder.png");
		plan_failed = getImage("icons/folder_red.png");
		plan_inactive = getImage("icons/inactive.png");
		bombed = getImage("icons/bomb.png");
		running_auto = getImage("icons/wait.png");
		wait_manual_feedback = getImage("icons/hand.png");
		passed = getImage("icons/accept.png");
		failed = getImage("icons/cancel.gif");
		blocked = getImage("icons/lock.png");
		auto_exec_bad = getImage("icons/page_white_delete.png");
		auto_exec_good = getImage("icons/page_white_cup.png");
		manual_exec = getImage("icons/page_manual.png");
	}
	
	public URL getImageURL(
		String strIcon)
	{
		URL u = this.getClass().getResource(strIcon);
		return u;
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
			PlanTree tree = (PlanTree) obj;
			if ( !tree.isActive() ) {
				return plan_inactive;
			} else if ( tree.hasTestFailed() && tree.hasTestRun() ) {
				return plan_failed;
			} else if ( tree.hasTestPassed() && tree.hasTestRun() ) {
				return plan_passed;
			} else {
				return plan_ready;
			}
		}
		
		// Test care of test case requests
		if ( obj instanceof TestCaseLeaf ) {
			
			TestCaseLeaf tcLeaf = (TestCaseLeaf) obj;
			TestCase tc = tcLeaf.getTestCase();
			if ( tc != null ) {
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
						} else if ( te.getExecutionResult()
							== TestCaseExecutor.RESULT_PASSED ) {
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
					if ( tcLeaf.getTestType() == TestCaseLeaf.AUTOMATED_WITH_EXECUTOR ) {
						return auto_exec_good;
					} else if ( tcLeaf.getTestType()
						== TestCaseLeaf.AUTOMATED_AND_INCOMPLETE ) {
						return auto_exec_bad;
					} else {
						return manual_exec;
					}
				}
			} else {
				return auto_exec_bad;
			}
		}
		
		// Default at the end
		if ( obj instanceof TreeParentNode ) {
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
		}
		
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
	
	public ImageDescriptor getImageDescriptor(
		Object obj)
	{
		
		// Take care of action requests
		if ( obj instanceof TestLinkAction ) {
			TestLinkAction a = (TestLinkAction) obj;
			if ( a.getActionName().equals(TestLinkAction.OPEN_PROJECT) ) {
				return getImageDescriptor(fileProjectOpen);
			}
			if ( a.getActionName().equals(TestLinkAction.CLOSE_PROJECT) ) {
				return getImageDescriptor(fileProjectClose);
			}
			if ( a.getActionName().equals(TestLinkAction.SWITCH_PROJECT) ) {
				return getImageDescriptor(fileProjectSwitch);
			}
			if ( a.getActionName().startsWith(
				TestLinkAction.PLAN_EXEC_DEFAULT.substring(0, 5)) ) {
				return getImageDescriptor(fileExec);
			}
			if ( a.getActionName().equals(TestLinkAction.RESUBMIT_PREPARE) ) {
				return getImageDescriptor(filePrep);
			}
		}
		return null;

		/* PlatformUI.getWorkbench().getSharedImages().
		 getImageDescriptor(ISharedImages.IMG_TOOL_REDO); //IMG_OBJS_INFO_TSK); */
	}
	
	/*
	 * Private methods
	 */
	private ImageDescriptor getImageDescriptor(
		String strIcon)
	{
		try {
			ImageDescriptor imageDescriptor = TestLinkMode.getImageDescriptor(
				configElement, strIcon);
			if ( imageDescriptor == null ) {
				imageDescriptor = ImageDescriptor.createFromFile(this.getClass(), strIcon);
			}
			return imageDescriptor;
		} catch ( Exception e ) {
			return null;
		}
	}
	
	private Image getImage(
		String strIcon)
	{
		ImageDescriptor imageDescriptor = getImageDescriptor(strIcon);
		if ( imageDescriptor == null ) {
			return null;
		}
		return JFaceResources.getResources().createImageWithDefault(imageDescriptor);
	}
}
