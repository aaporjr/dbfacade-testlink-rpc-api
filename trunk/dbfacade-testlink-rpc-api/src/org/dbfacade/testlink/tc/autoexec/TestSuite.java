package org.dbfacade.testlink.tc.autoexec;

import java.util.Map;

import org.dbfacade.testlink.api.client.TestLinkAPIConst;
import org.dbfacade.testlink.api.client.TestLinkAPIException;

/**
 * Intended to hold the information of an existing test suite in
 * the TestLink database. That is why the only way to instantiate
 * the class is using a Map to pass the suite information returned 
 * by the TestLink API.
 * 
 * @author Daniel Padilla
 *
 */
public class TestSuite {
	private String suiteName;
	private Integer suiteID;
	private boolean isOfflineVersion=false;

	/**
	 * Used to create offline dummy suite
	 */
	private TestSuite() {
		suiteName = "Offline suite";
		suiteID = new Integer(-1);
		isOfflineVersion = true;
	}
	
	/**
	 * Returns a new copied instance of the test suite
	 * 
	 * @param other
	 */
	public TestSuite(TestSuite other) {
		
		if ( suiteName != null ) {
			this.suiteName = new String(other.suiteName);
		}
		
		if ( suiteID != null ) {
			this.suiteID = new Integer(other.suiteID.intValue());
		}
		
		this.isOfflineVersion=other.isOfflineVersion;
	}
	
	/**
	 * Constructs a TestSuite instance when provided with information
	 * about the the suite using a Map result from the TestLink API
	 * for a suite. 
	 * 
	 * @param suiteInfo
	 * @throws TestLinkAPIException
	 */
	public TestSuite(
		Map suiteInfo) throws TestLinkAPIException
	{
		if ( suiteInfo == null ) {
			throw new TestLinkAPIException("The TestSuite class object instance could not be created.");
		}
		
		// Suite Name
		Object value = suiteInfo.get(TestLinkAPIConst.API_RESULT_NAME);
		if ( value == null ) {
			throw new TestLinkAPIException(
				"The setter does not allow null values for suite name.");
		} else {
			this.suiteName = value.toString();
		}
		
		// Identifier
		value = suiteInfo.get(TestLinkAPIConst.API_RESULT_IDENTIFIER);
		if ( value == null ) {
			throw new TestLinkAPIException(
			"The setter does not allow null values for suite identifier.");
		} else {
			this.suiteID = new Integer(value.toString());
		}
		
	}
	
	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the name of the suite with which the test case is associated.
	 * 
	 * @return
	 */
	public String getSuiteName()
	{
		return suiteName;
	}
	

	/**
	 * Currently not supported (method stub).
	 * <p>
	 * Get the internal identifier of the suite with which the test case is associated.
	 * 
	 * @return
	 */
	public Integer getSuiteID()
	{
		return suiteID;
	}
	
	/**
	 * True if it is the offline version of a suite
	 * 
	 * @return
	 */
	public boolean isOfflineVersion() {
		return isOfflineVersion;
	}
	
	/**
	 * Returns an offline dummy version of the TestSuite
	 * @return
	 */
	public static TestSuite getOfflineTestSuite() {
		return new TestSuite();
	}

}
