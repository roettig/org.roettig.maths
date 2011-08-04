package org.roettig.maths.matrix;

import java.util.ArrayList;
import java.util.Iterator;
import Jama.Matrix;

/**
 * ElementIterator implements Iterable&lt;Double&gt;-interface for all matrix elements.
 * 
 * @author roettig
 *
 */
public class ElementIterator implements Iterable<Double>
{
	private Matrix M;

	private ArrayList<Double> col;
	
	/**
	 * 
	 * @param M : target matrix
	 * 
	 */
	public ElementIterator(Matrix M)
	{
		this.M = M;
	}

	/**
	 * yields an Double-iterator over all matrix elements.
	 */
	@Override
	public Iterator<Double> iterator()
	{
		int R = M.getRowDimension();
		int C = M.getColumnDimension();
		
		col = new ArrayList<Double>(); 
		
		for(int r=0;r<R;r++)
		{
			for(int c=0;c<C;c++)
			{
				col.add( M.get(r, c));
			}
		}
		
		return col.iterator();
	}
}