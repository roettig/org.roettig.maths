package org.roettig.maths.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import Jama.Matrix;

/**
 * ColumnIterator implements Iterable&lt;Double&gt;-interface for matrix columns.
 * 
 * 
 * @author roettig
 *
 */
public class ColumnIterator implements Iterable<Double>
{
	private Matrix M_;
	private int c_;

	private ArrayList<Double> col;
	
	/**
	 * 
	 * @param M : target matrix
	 * @param c : column index
	 */
	public ColumnIterator(Matrix M, int c)
	{
		M_ = M;
		c_ = c;
	}

	/**
	 * yields Double-iterator over column elements. 
	 */
	@Override
	public Iterator<Double> iterator()
	{
		int R = M_.getRowDimension();
		col = new ArrayList<Double>();
		
		for(int r=0;r<R;r++)
		{
			col.add( M_.get(r, c_));
		}
		return col.iterator();
	}
}
