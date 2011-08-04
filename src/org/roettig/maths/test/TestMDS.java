/**
 * 
 */
package org.roettig.maths.test;

import java.io.IOException;

import org.roettig.maths.matrix.DiagonalIterator;
import org.roettig.maths.matrix.ElementIterator;
import org.roettig.maths.matrix.MMatrix;
import org.roettig.maths.mva.MDS;

import Jama.Matrix;
import junit.framework.TestCase;

/**
 * @author roettig
 *
 */
public class TestMDS extends TestCase
{
	public void testMDS() throws IOException
	{
		Matrix X  = MMatrix.load(this.getClass().getResource("/data/uk.dat").getFile());
		Matrix D2 = MDS.computeSquaredDistanceMatrix(X);
		int N = D2.getColumnDimension();
		assertEquals(N,11);
		DiagonalIterator iter = new DiagonalIterator(D2);
		for(Double d: iter)
		{
			assertEquals(d ,0.0, 1e-8);
		}

		assertEquals(D2.get(0,1) ,14980.45, 1e-4);
		assertEquals(D2.get(0,N-1) ,3694.16, 1e-4);
		assertEquals(D2.get(N-2,N-1) , 4674.919999999998, 1e-4);

		int n = X.getRowDimension();
		int k = X.getColumnDimension();
		Matrix Xtrain = X.getMatrix(1,n-1,0,k-1);
		Matrix Xtst   = X.getMatrix(0,0,0,k-1);
		Xtrain.print(5,2);
		D2 = MDS.computeSquaredDistanceMatrix(Xtrain);
		D2.print(5,2);
		Matrix D2tst = MDS.computeSquaredDistanceMatrix(Xtrain,Xtst);
		MDS mds = new MDS(D2);
		mds.compute();
		Matrix E1 = mds.embed(D2);
		assertEquals(E1.get(0,0),-88.01, 1e-2);
		assertEquals(E1.get(0,1),-90.67, 1e-2);
		assertEquals(E1.get(9,0),-140.72, 1e-2);
		assertEquals(E1.get(9,1),-47.17, 1e-2);
		E1.print(5,2);
		Matrix E2 = mds.embed(D2tst);
		E2.print(5,2);
		assertEquals(E2.get(0,0),-154.64, 1e-2);
		assertEquals(E2.get(0,1),12.00, 1e-2);

	}
}
