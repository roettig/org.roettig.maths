/**
 * 
 */
package org.roettig.maths.matrix;

import java.io.IOException;

import Jama.Matrix;

/**
 * MatrixHelper provides some convenience methods.
 * 
 * @author roettig
 *
 */
public class MatrixHelper
{
	/**
	 * create (n,m) identity matrix.
	 *  
	 * @param n : #rows
	 * @param m _ #columns
	 * 
	 * @return identity matrix
	 */
	public static Matrix eye(int n, int m)
	{
		Matrix ret = new Matrix(n,m);
		int mn = Math.min(n, m);
		for(int i=0;i<mn;i++)
		{
			ret.set(i, i,1.0);
		}
		return ret;
	}

	/**
	 * create square identity matrix.
	 * 
	 * @param n : dimension
	 * 
	 * @return identity matrix 
	 */
	public static Matrix eye(int n)
	{
		return eye(n,n);
	}
	
	/**
	 * create (diagonal) matrix of row sums with row sums stored on diagonal.
	 * 
	 * @param m : matrix
	 * 
	 * @return row sum matrix
	 * 
	 * @throws Exception
	 */
	public static Matrix getRowSumMatrix(Matrix m) throws Exception
	{
		int    M = m.getColumnDimension();
		int    N = m.getRowDimension();
		Matrix D = new Matrix(N,M);
		for(int i=0;i<N;i++)
		{
			RowIterator rit = new RowIterator(m,i);
			double sum = 0.0;
			for(Double d: rit)
			{
				sum+=d;
			}
			D.set(i, i, sum);
		}
		return D;
	}
}
