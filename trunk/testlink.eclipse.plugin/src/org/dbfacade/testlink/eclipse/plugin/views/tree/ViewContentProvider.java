package org.dbfacade.testlink.eclipse.plugin.views.tree;


import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IViewSite;


public class ViewContentProvider implements IStructuredContentProvider, 
	ITreeContentProvider
{
	private TreeParent invisibleRoot;
	private IViewSite viewSite;
	
	public ViewContentProvider(IViewSite viewSite, TreeParent invisibleRoot) {
		this.viewSite = viewSite;
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
		if ( parent.equals(viewSite) ) {
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public Object getParent(
		Object child)
	{
		if ( child instanceof TreeObject ) {
			return ((TreeObject) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(
		Object parent)
	{
		if ( parent instanceof TreeParent ) {
			return ((TreeParent) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(
		Object parent)
	{
		if ( parent instanceof TreeParent ) {
			return ((TreeParent) parent).hasChildren();
		}
		return false;
	}
}
