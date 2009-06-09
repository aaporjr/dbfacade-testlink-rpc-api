package org.dbfacade.testlink.eclipse.plugin.launcher;


public class TestLinkRunner
{

	public static void main(
		String[] args)
	{
		try {
			System.out.println("We have a TestLink launch.");
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}
