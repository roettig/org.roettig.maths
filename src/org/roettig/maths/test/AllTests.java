package org.roettig.maths.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite
{
	public static Test suite()
	{
		TestSuite mySuite = new TestSuite( "Test-Suite" );
		mySuite.addTestSuite( org.roettig.maths.test.TestStatistics.class );
		mySuite.addTestSuite( org.roettig.maths.test.TestMatrix.class );
		return mySuite;
	}
}
