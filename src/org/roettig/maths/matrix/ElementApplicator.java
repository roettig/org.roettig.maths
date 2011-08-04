package org.roettig.maths.matrix;

import Jama.Matrix;

/**
 * ElementApplicator applies a functor to any matrix element.
 *  
 * @author roettig
 *
 */
public class ElementApplicator
{
	private ElementFunctor f;
	
	/**
	 * 
	 * @param f : ElementFunctor
	 */
	public ElementApplicator( ElementFunctor f)
	{
		this.f = f;
	}

	/**
	 * applies ElementFunctor f to any matrix element.
	 * 
	 * @param M : target matrix
	 */
	public void apply(Matrix M)
	{
		int R = M.getRowDimension();
		int C = M.getColumnDimension();
		
		for(int r=0;r<R;r++)
		{
			for(int c=0;c<C;c++)
			{
				double d = f.calculate( r, c, M.get(r, c) );
				M.set(r,c,d);
			}
		}
	}
}
