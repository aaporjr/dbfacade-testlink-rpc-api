package testlink.eclipse.plugin.views.tree;


import java.util.Iterator;
import java.util.Map;


import testlink.api.java.client.TestLinkAPIClient;
import testlink.api.java.client.tc.autoexec.TestPlan;
import testlink.api.java.client.tc.autoexec.TestPlanLoader;
import testlink.api.java.client.tc.autoexec.TestProject;
import testlink.api.java.client.tc.autoexec.server.RemoteClientConnection;
import testlink.api.java.client.tc.autoexec.server.RemoteClientListener;
import testlink.api.java.client.tc.autoexec.server.RemoteConnectionManager;
import testlink.eclipse.plugin.UserMsg;
import testlink.eclipse.plugin.preferences.TestLinkPreferences;
import testlink.eclipse.plugin.views.HtmlMessageText;
import testlink.eclipse.plugin.views.TestLinkView;



;


public class ProjectTree extends TreeParentNode implements RemoteClientListener
{
	public static final String OPEN_TREE_PLACE_HOLDER = "Right click to open project";
	public static final String UNABLE_TO_OPEN_PREFIX = "Unable to open project: ";
	private String projectName;
	private TestProject project = null;
	private int port = -1;
	private boolean isConnected = false;
	
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
	 * Use for full initialization
	 * 
	 * @param project
	 */
	public ProjectTree(
		TestProject project,
		TestLinkPreferences pref,
		int port)
	{
		super(project.getProjectName());
		this.project = project;
		setProjectName(project.getProjectName());
		setPreferences(pref);
		if ( port > 0 ) {
			this.port = port;
			this.isConnected = true;
			RemoteClientConnection conn = RemoteConnectionManager.getOrCreateConnection(port);
			conn.addListener(this);
		}
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
	 * Dispose of the parent and all descendants
	 * 
	 * @param child
	 */
	public void removeChild(
		TreeNode child)
	{

		if ( child instanceof PlanTree ) {
			PlanTree ptChild = (PlanTree) child;
			ptChild.shutdownRemoteTester();
		}
		
		if ( child instanceof TreeParentNode ) {
			TreeParentNode childInParentRole = (TreeParentNode) child;
			TreeNode[] grandchildren = childInParentRole.getChildren(true);
			for ( int i = 0; i < grandchildren.length; i++ ) {
				TreeNode grandchild = grandchildren[i];
				removeChild(grandchild);
			}
		}
		children.remove(child);
		child.setParent(null);
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
			TestLinkPreferences pref = getPreferences();
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
				PlanTree planRoot = new PlanTree(plan, port);
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
			detail = HtmlMessageText.OPEN_HTML_DOC + "<b>Name:</b></p><p>"
				+ project.getProjectName() + "</p><p>" + "<b>Description:</b></p><p>"
				+ project.getProjectDescription() + HtmlMessageText.CLOSE_HTML_DOC;
		}
		return detail;
	}
	
	/**
	 * Returns the port that should be used for remote testing
	 * 
	 * @return
	 */
	public int getPort()
	{
		return port;
	}
	
	/**
	 * The tree was built to make remote request.
	 * 
	 * @return
	 */
	public boolean isInRemoteMode()
	{
		return (port > 0);
	}
	
	/**
	 * Let the tree know that the remote server has
	 * been shutdown.
	 * 
	 */
	public void disconnect()
	{
		isConnected = false;
	}
	
	/**
	 * True if the tree has been notified of a disconnect.
	 * 
	 * @return
	 */
	public boolean isConnected()
	{
		if ( !isInRemoteMode() ) {
			return false;
		}
		return isConnected;
	}
	
	public void sentMessage(
		String output)
	{}

	public void receivedMessage(
		String input)
	{}
	
	public void receivedShutdown()
	{
		disconnect();
		TestLinkView.refresh();
	}
	
}
