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
package testlink.eclipse.plugin.views.tree;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;


public class ViewContentProvider implements IStructuredContentProvider, 
	ITreeContentProvider
{
	private TreeParentNode invisibleRoot;
	private IViewSite viewSite;
	
	public ViewContentProvider(
		IViewSite viewSite,
		TreeParentNode invisibleRoot)
	{
		this.viewSite = viewSite;
		this.invisibleRoot = invisibleRoot;
	}
	
	public ViewContentProvider(
		TreeParentNode invisibleRoot)
	{
		this.viewSite = null;
		this.invisibleRoot = invisibleRoot;
	}

	public void inputChanged(
		Viewer v,
		Object oldInput,
		Object newInput)
	{}

	public void dispose()
	{}

	public Object[] getElements(
		Object parent)
	{
		if ( viewSite != null ) {
			if ( parent.equals(viewSite) ) {
				return getChildren(invisibleRoot);
			}
		} else {
			if ( parent.equals(invisibleRoot) ) {
				return getChildren(invisibleRoot);
			}
		}
		return getChildren(parent);
	}

	public Object getParent(
		Object child)
	{
		if ( child instanceof TreeNode ) {
			return ((TreeNode) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(
		Object parent)
	{
		if ( parent instanceof TreeParentNode ) {
			return ((TreeParentNode) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(
		Object parent)
	{
		if ( parent instanceof TreeParentNode ) {
			return ((TreeParentNode) parent).hasChildren();
		}
		return false;
	}
}
