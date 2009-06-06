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
	private Image drawer;
	private Image folder;
	private Image bombed;
	private Image running;
	private Image passed;
	private Image failed;
	private Image blocked;
	private Image auto_exec_bad;
	private Image auto_exec_good;
	private Image manual_exec;

	public ViewLabelProvider(
		IConfigurationElement cfg)
	{
		this.configElement = cfg;
		drawer = getImage("icons/drawer.png");
		folder = getImage("icons/folder.png");
		bombed = getImage("icons/bomb.png");
		running = getImage("icons/wait.png");
		passed = getImage("icons/accept.png");
		failed = getImage("icons/cancel.png");
		blocked = getImage("icons/lock.png");
		auto_exec_bad = getImage("icons/page.png");
		auto_exec_good = getImage("icons/page_white_cup.png");
		manual_exec = getImage("icons/hand.png");
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
		if ( obj instanceof ProjectTree ) {
			return drawer;
		}
		if ( obj instanceof PlanTree ) {
			return folder;
		}
		if ( obj instanceof TestCaseLeaf ) {
			
			TestCaseLeaf tcLeaf = (TestCaseLeaf) obj;
			TestCase tc = tcLeaf.getTestCase();
			TestCaseExecutor te = tc.getExecutor();
			
			boolean isImageSet = true;
			if ( te != null ) {
				if ( te.getExecutionState() == TestCaseExecutor.STATE_BOMBED ) {
					return bombed;
				} else if ( te.getExecutionState() == TestCaseExecutor.STATE_RUNNING ) {
					return running;
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
