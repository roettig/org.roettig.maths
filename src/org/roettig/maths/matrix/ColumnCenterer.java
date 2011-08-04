package org.roettig.maths.matrix;

import org.roettig.maths.statistics.Statistics;

import Jama.Matrix;

/**
 * ColumnCenterer centers matrix columns to zero mean.
 * 
 * @author roettig
 *
 */
public class ColumnCenterer
{
	/**
	 * center c-th column to zero mean.
	 *  
	 * @param M : target matrix
	 * @param c : target column
	 */
	public static void apply(Matrix M, int c)
	{
		ColumnIterator citer = new ColumnIterator(M,c);
		final double mean = Statistics.mean(citer);
		ColumnApplicator cent = new ColumnApplicator( new ElementFunctor()
		{
			@Override								    
			public double calculate(int i, int j, double d)
			{
				return d-mean;
			}
		});

		cent.apply(M,c);
	}

	/**
	 * center all columns in matrix to zero mean.
	 * 
	 * @param M : target matrix
	 */
	public static void apply(Matrix M)
	{
		int C = M.getColumnDimension();
		for(int c=0;c<C;c++)
			ColumnCenterer.apply(M,c);
	}
}
