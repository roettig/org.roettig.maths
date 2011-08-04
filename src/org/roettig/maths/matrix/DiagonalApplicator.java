package org.roettig.maths.matrix;


import Jama.Matrix;

/**
 * DiagonalApplicator applies an ElementFunctor to all diagonal elements of a matrix.
 * 
 * @author roettig
 *
 */
public class DiagonalApplicator
{
	private ElementFunctor f;
	
	/**
	 * 
	 * @param f : ElementFunctor
	 */
	public DiagonalApplicator(ElementFunctor f)
	{
		this.f = f;
	}

	/**
	 * applies ElementFunctor to all diagonal elements of matrix.
	 * 
	 * @param M : target matrix
	 * 
	 */
	public void apply(Matrix M)
	{
		int R = M.getRowDimension();
		int C = M.getColumnDimension();
		int mn = Math.min(R,C);

		for(int c=0;c<mn;c++)
		{
			double d = f.calculate( c, c, M.get(c, c) );
			M.set(c,c,d);
		}

	}
}