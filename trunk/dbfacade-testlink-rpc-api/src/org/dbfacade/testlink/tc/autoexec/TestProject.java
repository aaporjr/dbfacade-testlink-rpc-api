package org.dbfacade.testlink.tc.autoexec;


import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;


/**
 * Intended to hold the information of an existing test project in
 * the TestLink database. That is why the only way to instantiate
 * the class is using a Map to pass the project information returned 
 * by the TestLink API.
 * 
 * @author Daniel Padilla
 * 
 */
public class TestProject
{
	private String projectName;
	private String tcPrefix;
	private Integer projectID;
	private Integer active = new Integer(1);
	private Integer auto = new Integer(1);
	private boolean isOfflineVersion=false;

	/**
	 * Used to create an offline dummy project
	 */
	private TestProject() {
		projectName = "Offline project";
		tcPrefix = "offline";
		projectID = new Integer(-1);
		isOfflineVersion=true;
	}
	
	/**
	 * Used to create an offline dummy project
	 */
	private TestProject(String projectName) {
		this.projectName = projectName;
		tcPrefix = "offline";
		projectID = new Integer(-1);
		isOfflineVersion=true;
	}
	
	
	/**
	 * Constructs a TestProject instance when provided with information
	 * about the the project using a Map result from the TestLink API
	 * for a project. 
	 * 
	 * @param projectInfo
	 * @throws TestLinkAPIException
	 */
	public TestProject(
		Map projectInfo) throws TestLinkAPIException
	{
		if ( projectInfo == null ) {
			throw new TestLinkAPIException(
				"The TestProject class object instance could not be created.");
		}
		
		// Project Name
		Object value = projectInfo.get(TestLinkAPIConst.API_RESULT_NAME);
		if ( value == null ) {
			throw new TestLinkAPIException(
				"The setter does not allow null values for project name.");
		} else {
			this.projectName = value.toString();
		}
		
		// Project Test Case prefix
		value = projectInfo.get(TestLinkAPIConst.API_RESULT_PREFIX);
		if ( value == null ) {
			throw new TestLinkAPIException(
				"The setter does not allow null values for project test case prefix.");
		} else {
			this.tcPrefix = value.toString();
		}
		
		// Identifier
		value = projectInfo.get(TestLinkAPIConst.API_RESULT_IDENTIFIER);
		if ( value == null ) {
			throw new TestLinkAPIException(
				"The setter does not allow null values for project identifier.");
		} else {
			this.projectID = new Integer(value.toString());
		}
		
		// Active
		value = projectInfo.get(TestLinkAPIConst.API_RESULT_ACTIVE);
		if ( value == null ) {
			active = new Integer(0);
		} else {
			active = new Integer(value.toString());
		}
		
		// Automation
		value = projectInfo.get(TestLinkAPIConst.API_RESULT_AUTO_OPTION);
		if ( value == null ) {
			auto = new Integer(0);
		} else {
			auto = new Integer(value.toString());
		}
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the name of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public String getProjectName()
	{
		return projectName;
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the internal identifier of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getProjectID()
	{
		return projectID;
	}
		
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the name of the project with which the test case is associated.
	 * 
	 * @return
	 */
	public String getTestCasePrefix()
	{
		return tcPrefix;
	}
	
	/**
	 * True if the project is active. 
	 * 
	 * @return
	 */
	public boolean isActive()
	{
		return (active.intValue() > 0);
	}
	
	/**
	 * True if the project allows automation.
	 * 
	 * @return
	 */
	public boolean allowsAutomation()
	{
		return (auto.intValue() > 0);
	}

	/**
	 * True if the test suite is the dummy offline version
	 * 
	 * @return
	 */
	public boolean isOfflineVersion() {
		return isOfflineVersion;
	}
	
	/**
	 * Return dummy offline version of a project.
	 * 
	 * @return
	 */
	public static TestProject getOffLineProject() {
		return new TestProject();
	}
	
	
	/**
	 * Return dummy offline version of a project with the parameter name
	 * 
	 * @return
	 */
	public static TestProject getOffLineProject(String projectName) {
		return new TestProject(projectName);
	}
}
