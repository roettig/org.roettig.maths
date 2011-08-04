package org.roettig.maths.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import Jama.Matrix;

/**
 * RowIterator implements Iterable&lt;Double&gt;-interface for matrix rows.
 * 
 * 
 * @author roettig
 *
 */
public class RowIterator implements Iterable<Double>
{
	private Matrix M;
	private int r;

	private ArrayList<Double> row;
	
	/**
	 * 
	 * @param M : target matrix
	 * @param r : target row index
	 */
	public RowIterator(Matrix M, int r)
	{
		this.M = M;
		this.r = r;
	}

	/**
	 * yields Double-iterator over row elements. 
	 */
	@Override
	public Iterator<Double> iterator()
	{
		int C = M.getColumnDimension();
		
		row = new ArrayList<Double>();
		
		for(int c=0;c<C;c++)
		{
			row.add( M.get(r, c));
		}
		return row.iterator();
	}
}
