package testlink.api.java.client.junit.client;


import java.util.HashMap;
import java.util.Iterator;


public class RunExceptionResults
{
	private HashMap<String,
		Exception> results = new HashMap();
	private boolean hasFailure=false;
	private Exception latestFailure=null;
	private String latestFailedVersion;

	public void addResult(
		String version,
		Exception e)
	{
		if ( e != null ) {
			hasFailure = true;
			latestFailure=e;
			latestFailedVersion=version;
		}
		results.put(version, e);
	}

	public Iterator getVersions()
	{
		return results.keySet().iterator();
	}

	public Exception getResultException(
		String version)
	{
		return (Exception) results.get(version);
	}
	
	public boolean containsFailure() {
		return hasFailure;
	}
	
	public Exception getLatestFailure() {
		return latestFailure;
	}
	
	public String getLatestFailedVersion() {
		return latestFailedVersion;
	}
}
