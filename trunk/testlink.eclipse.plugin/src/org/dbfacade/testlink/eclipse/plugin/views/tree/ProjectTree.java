package org.dbfacade.testlink.eclipse.plugin.views.tree;

import org.dbfacade.testlink.tc.autoexec.TestProject;

public class ProjectTree extends TreeParent {
	private TestProject project=null;
	
	/**
	 * Used for initial display
	 * 
	 * @param projectName
	 */
	public ProjectTree(String projectName) {
		super(projectName);
	}
	
	/**
	 * Use for full initializarion
	 * 
	 * @param project
	 */
	public ProjectTree(TestProject project) {
		super(project.getProjectName());
		this.project = project;
	}
	
	/**
	 * Get the project information
	 * 
	 * @return
	 */
	public TestProject getProject() {
		return project;
	}
	
	public void setProject(TestProject project) {
		this.project = project;
		this.setName(project.getProjectName());
	}
	
}
