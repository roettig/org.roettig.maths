package org.roettig.maths.matrix;

import Jama.Matrix;

/**
 * ColumnApplicator applies a ElementFunctor to any element within a column.
 * 
 * @author roettig
 *
 */
public class ColumnApplicator
{
	private ElementFunctor f_;
	
	/**
	 * 
	 * @param f : an ElementFunctor
	 */
	public ColumnApplicator(ElementFunctor f)
	{
		f_ = f;
	}

	/**
	 * applies the ElementFunctor to c-th column of matrix M.
	 * 
	 * @param M : target matrix
	 * @param c : target column
	 */
	public void apply(Matrix M, int c)
	{
		int R = M.getRowDimension();
		 
		for(int r=0;r<R;r++)
		{
			double d = f_.calculate(r, c, M.get(r, c) );
			M.set(r,c,d);
		}
	}
	
	/**
	 * applies the ElementFunctor to all columns of matrix M.
	 * 
	 * @param M : target matrix
	 */
	public void apply(Matrix M)
	{
		int R = M.getRowDimension();
		int C = M.getColumnDimension();
		
		for(int c=0;c<C;c++)
		{
			for(int r=0;r<R;r++)
			{
				double d = f_.calculate(r, c, M.get(r, c) );
				M.set(r,c,d);
			}
		}
	}
}