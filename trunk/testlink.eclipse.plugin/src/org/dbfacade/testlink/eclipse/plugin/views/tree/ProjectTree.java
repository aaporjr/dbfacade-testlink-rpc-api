package org.dbfacade.testlink.eclipse.plugin.views.tree;


import java.util.Iterator;
import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIClient;
import org.dbfacade.testlink.eclipse.plugin.UserMsg;
import org.dbfacade.testlink.eclipse.plugin.preferences.TestLinkPreferences;
import org.dbfacade.testlink.eclipse.plugin.views.HtmlMessageText;
import org.dbfacade.testlink.tc.autoexec.TestPlan;
import org.dbfacade.testlink.tc.autoexec.TestPlanLoader;
import org.dbfacade.testlink.tc.autoexec.TestProject;


;


public class ProjectTree extends TreeParentNode
{
	public static final String OPEN_TREE_PLACE_HOLDER = "Right click to open project";
	public static final String UNABLE_TO_OPEN_PREFIX = "Unable to open project: ";
	private String projectName;
	private TestProject project = null;
	
	/**
	 * Used for initial display
	 * 
	 * @param projectName
	 */
	public ProjectTree(
		String projectName)
	{
		super(projectName);
		this.projectName = projectName;
	}
	
	/**
	 * Use for full initializarion
	 * 
	 * @param project
	 */
	public ProjectTree(
		TestProject project)
	{
		super(project.getProjectName());
		this.project = project;
		setProjectName(project.getProjectName());
	}
	
	public boolean isOpen()
	{
		return (project != null);
	}
	
	/**
	 * Get the project information
	 * 
	 * @return
	 */
	public TestProject getProject()
	{
		return project;
	}
	
	public void setProject(
		TestProject project)
	{
		this.project = project;
		setProjectName(projectName);
	}
	
	public void setProjectName(
		String projectName)
	{
		if ( project != null ) {
			if ( project.isActive() ) {
				projectName = projectName + " {Active}";
			} else {
				projectName = projectName + " {Inactive}";
			}
		}
		this.setName(projectName);
	}
	
	/**
	 * When a project is not a place holder project always return true.
	 * <p>
	 * Note: If there are no children we will open a no test plans message.
	 */
	public boolean hasChildren()
	{
		if ( isOpenProjectPlaceholderNode() ) {
			return false;
		} else {
			return true;
		}
	}
	
	public void findChildren()
	{
		try {
			TestLinkPreferences pref = new TestLinkPreferences();
			TestLinkAPIClient apiClient = pref.getTestLinkAPIClient();

			String pn = projectName;
			if ( project != null ) {
				pn = project.getProjectName();
			}
			TestPlanLoader loader = new TestPlanLoader(pn, apiClient);
			Map plans = loader.getPlans();
		
			Iterator planIDs = plans.keySet().iterator();
			while ( planIDs.hasNext() ) {
				Object planID = planIDs.next();
				TestPlan plan = (TestPlan) plans.get(planID);
				PlanTree planRoot = new PlanTree(plan);
				this.addChild(planRoot);
			}
		} catch ( Exception e ) {
			UserMsg.error(e, "Failed to find the children for the project.");
		}
		
		if ( children.size() == 0 ) {
			PlanTree planRoot = new PlanTree(PlanTree.EMPTY_PROJECT);
			this.addChild(planRoot);
		}
	}
	
	public boolean isOpenProjectPlaceholderNode()
	{
		return (this.getName().equals(OPEN_TREE_PLACE_HOLDER)
			|| this.getName().startsWith(UNABLE_TO_OPEN_PREFIX) || !(project.isActive()));
	}
	
	public String displayHtml()
	{
		String detail = "No detail information is available.";
		if ( project != null ) {
			detail = HtmlMessageText.OPEN_HTML_DOC + "<b>Name:</b></p><p>" + project.getProjectName() 
			+ "</p><p>" + "<b>Description:</b></p><p>"
				+ project.getProjectDescription() + HtmlMessageText.CLOSE_HTML_DOC;
		}
		return detail;
	}
	
}
