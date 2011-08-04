package org.roettig.maths.matrix;

import Jama.Matrix;

/**
 * RowApplicator applies a ElementFunctor to any matrix element within a row.
 *  
 * @author roettig
 *
 */
public class RowApplicator
{
	private ElementFunctor f;
	
	public RowApplicator(ElementFunctor f)
	{
		this.f = f;
	}

	/**
	 * applies the ElementFunctor to r-th row of matrix M.
	 * 
	 * @param M : target matrix
	 * @param r : target row
	 */
	public void apply(Matrix M, int r)
	{
		
		int C = M.getColumnDimension();
		 
		for(int c=0;c<C;c++)
		{
			double d = f.calculate( r, c, M.get(r, c) );
			M.set(r,c,d);
		}
	}
}