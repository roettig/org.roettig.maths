package org.roettig.maths.matrix;

import org.roettig.maths.statistics.Statistics;

import Jama.Matrix;

/**
 * ColumnScaler scales columns in a matrix to have a standard deviation of 1.
 * 
 * @author roettig
 *
 */
public class ColumnScaler
{
	/**
	 * scale c-th column to standard deviation of 1.
	 * 
	 * @param M : target matrix
	 * @param c : target column
	 * 
	 */
	public static void apply(Matrix M, int c)
	{
		ColumnIterator citer = new ColumnIterator(M,c); 
		final double s = Math.sqrt(Statistics.var(citer));
		if(s>0)
		{
			ColumnApplicator cent = new ColumnApplicator( new ElementFunctor()
			{
				@Override								    
				public double calculate(int i, int j, double d)
				{
					return d/s;
				}
			});
			cent.apply(M,c);
		}
	}

	/**
	 * scale all columns to standard deviation of 1.
	 * 
	 * @param M : target matrix
	 */
	public static void apply(Matrix M)
	{
		int C = M.getColumnDimension();
		for(int c=0;c<C;c++)
			ColumnScaler.apply(M,c);
	}
}