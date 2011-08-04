package org.roettig.maths.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import Jama.Matrix;

/**
 * DiagonalIterator implements Iterable&lt;Double&gt;-interface for all diagonal matrix elements.
 * 
 * @author roettig
 *
 */
public class DiagonalIterator implements Iterable<Double>
{
	private Matrix M;

	private ArrayList<Double> diag;

	/**
	 * 
	 * @param M : target matrix
	 */
	public DiagonalIterator(Matrix M)
	{
		this.M = M;
	}

	/**
	 * yields Double-iterator over diagonal elements of matrix.
	 * 
	 */
	@Override
	public Iterator<Double> iterator()
	{
		int C = M.getColumnDimension();
		int R = M.getRowDimension();
		int K = Math.min(R,C);
		diag = new ArrayList<Double>(); 
		for(int c=0;c<K;c++)
		{
			diag.add( M.get(c, c));
		}
		return diag.iterator();
	}

}
