/**
 * 
 */
package org.roettig.maths.test;

import org.roettig.maths.matrix.MMatrix;
import org.roettig.maths.matrix.MatrixHelper;

import Jama.Matrix;

import junit.framework.TestCase;

/**
 * @author roettig
 *
 */
public class TestIterator extends TestCase
{
	public void testRowIterator() throws Exception
	{
		MMatrix m = new MMatrix(3,3,0.0);
		m.set(0, 0, 1.0);
		m.set(0, 1, 2.0);
		m.set(0, 2, 3.0);
		m.set(1, 0, 4.0);
		m.set(1, 1, 5.0);
		m.set(1, 2, 6.0);
		m.set(2, 0, 7.0);
		m.set(2, 1, 8.0);
		m.set(2, 2, 9.0);
		System.out.println(m.toString());
		
		Matrix D = MatrixHelper.getRowSumMatrix(m);
		
		assertEquals("",D.get(0,0),6.0);
		assertEquals("",D.get(1,1),15.0);
		assertEquals("",D.get(2,2),24.0);
	}
}