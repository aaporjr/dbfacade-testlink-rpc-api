package testlink.api.java.client.junit.autoexec;


import testlink.api.java.client.tc.autoexec.EmptyExecutor;


public class PassExecutor extends EmptyExecutor
{

	public PassExecutor()
	{
		super(RESULT_PASSED);
	}
}
