package org.dbfacade.testlink.eclipse.plugin.views.tree;

import org.dbfacade.testlink.tc.autoexec.TestProject;

public class ProjectTree extends TreeParent {
	private TestProject project;
	
	public ProjectTree(TestProject project) {
		super(project.getProjectName());
		this.project = project;
	}
	
	public TestProject getProject() {
		return project;
	}
}
