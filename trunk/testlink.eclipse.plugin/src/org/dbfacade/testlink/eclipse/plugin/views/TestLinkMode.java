package org.dbfacade.testlink.eclipse.plugin.views;


import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * Since TestLink can run on the workbench or run
 * in Application Window mode then here is were all
 * the differences between modes are stored.
 * 
 * @author Daniel Padilla
 *
 */
public class TestLinkMode
{
	public static final short WORKBENCH_MODE = 0;
	public static final short APPLICATION_MODE = 1;
	public static short mode = WORKBENCH_MODE;
	
	// TestLink Application runner sets these variables
	public static String iconsURL;

	/**
	 * Return the active ImageDescriptior
	 * 
	 * @param strIcon
	 * @return
	 */
	public static ImageDescriptor getImageDescriptor(
		IConfigurationElement configElement,
		String strIcon)
	{
		ImageDescriptor imageDescriptor = null;
		if ( isWorkbench() && configElement != null && strIcon != null ) {
			try {
				imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(
					configElement.getNamespaceIdentifier(), strIcon);
				return imageDescriptor;
			} catch ( Exception e ) {
				return null;
			}
		} else {
			return null;
		}
	}
				 
	/**
	 * True if the mode is workbench
	 * 
	 * @return
	 */
	public static boolean isWorkbench()
	{
		return (TestLinkMode.mode == TestLinkMode.WORKBENCH_MODE);
	}
}
