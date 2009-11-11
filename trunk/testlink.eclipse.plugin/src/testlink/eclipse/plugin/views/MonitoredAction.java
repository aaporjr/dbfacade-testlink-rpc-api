package testlink.eclipse.plugin.views;

public class MonitoredAction extends Thread {
	private boolean hasCompleted=false;
	
	public final void run() {
		hasCompleted=false;
		try {
			action();
		} catch (Exception e) {
			hasCompleted=true;
		}
		hasCompleted=true;
	}
	
	public boolean hasCompletedProcessing() {
		return hasCompleted;
	}
	
	public void action() throws Exception {};
}
