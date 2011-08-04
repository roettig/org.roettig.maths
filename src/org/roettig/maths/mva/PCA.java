package org.roettig.maths.mva;

import java.util.*;
import Jama.*;
import org.roettig.maths.matrix.*;
import org.roettig.maths.statistics.Statistics;

/**
 * 
 * PCA (principle component analysis) calculation on descriptor matrix.
 * 
 * <pre>
 * {@code
 * MMatrix X   = MMatrix.load("foods.mat");
 * PCA     pca = new PCA(X,3,true);
 * }
 * 
 * will compute a 3-dimensional PCA on the supplied descriptor matrix.
 * 
 * </pre>
 * @author roettig
 *
 */
public class PCA
{
	private int ncomp_;
		
	private MMatrix X_;
	
	private Matrix U;
	
	private Matrix S;
	
	private Matrix V;
	
	private MMatrix T;
	
	private Matrix TT;
	
	private Matrix Xres;
	private Matrix Xhat;
	private Matrix DModX;
	
	private Vector<Double> evals  = new Vector<Double>();

	/**
	 * Ctor for PCA.
	 * @param _X : descriptor matrix (each row is one datapoint)
	 * @param _ncomp : number of components to calculate
	 * @param standardize : should descriptor matrix be standardized ?
	 */
	public PCA(MMatrix X, int ncomp, boolean standardize)
	{
		ncomp_    = ncomp;
		int N = X.getRowDimension();
		// safely encapsulate matrix X
		X_ = new MMatrix(X);
		if(standardize)
			standardize(X_);  
		else
			center(X_);
		calcSVD();
	}


	/**
	 * standardize matrix.
	 * @param X
	 */
	private void standardize(MMatrix X)
	{
		ColumnCenterer.apply(X);
		ColumnScaler.apply(X);
	}

	/**
	 * center matrix.
	 * @param X
	 */
	private void center(MMatrix X)
	{
		ColumnCenterer.apply(X);
	}

	/**
	 * get Hotelling parameter for i-th dimension.
	 * 
	 * @param dim
	 * @return
	 */
	public double getHotellingParam(int dim)
	{
		//sqrt(var(y)*fc*(2*(I*I-1)./(I*(I-2))))
		//fcrit = 3
		//Matrix cv = colVars(T);
		double var = Statistics.var(T.columnIterator(dim));
		int I = X_.getRowDimension();
		int A = 2;
		double fc  = 3;
		double ret = Math.sqrt(var*fc*(2*(I*I-1)/(I*(I-2))));
		return ret;
	}

	/**
	 * calculate Singular Value Decomposition on descriptor matrix X. 
	 */
	private void calcSVD()
	{
		SingularValueDecomposition svd = new SingularValueDecomposition(X_);
		
		double[] d = svd.getSingularValues();
		
		S = Matrix.identity(d.length,d.length );
		
		for(int k=0;k<d.length;k++)
		{
			double ev = d[k];
			if(Math.abs(d[k])<1e-9)
				ev = 0.0;
			S.set(k,k,ev);
			evals.add(ev*ev);
		}

		int nVars    = X_.getColumnDimension();
		int nSamples = X_.getRowDimension();
		
		S  = S.getMatrix(0,ncomp_-1,0,ncomp_-1);
		U  = svd.getU().getMatrix(0,nSamples-1,0,ncomp_-1);
		V  = svd.getV().getMatrix(0,nVars-1,0,ncomp_-1);
		T  = new MMatrix( U.times(S) );
		
		Xhat = T.times(V.transpose());
		Xres = Xhat.minus(X_);
		
		//double resVar = Xres.norm2();
		//double totVar = X.norm2();
		//System.out.println("|resX|="+resVar+" |rel|="+(resVar/totVar));
		
		TT = (T.transpose()).times(T);
		
		calcDModX();
	}

	/**
	 * calculate distance to plane/model for each data point (DModX).
	 */
	private void calcDModX()
	{
		int nVars    = X_.getColumnDimension();
		int nSamples = X_.getRowDimension();
		DModX = new Matrix(nSamples,1,0.0);
		for(int i=0;i<nSamples;i++)
		{
			double res = 0.0;
			for(int j=0;j<nVars;j++)
			{
				// calculate row sum of squares 
				res += Math.pow(Xres.get(i,j),2);
			}
			// calculate stdev(row_i)
			DModX.set(i,0,res/(nSamples-ncomp_-1));
		}
	}

	/**
	 * calculate goodness of fit for given dimensionality.
	 * 
	 * @param dim
	 * @return goodness of fit
	 */
	public double getGoodnessOfFit(int dim)
	{
		double s1 = 0.0;
		for(int i=0;i<evals.size();i++)
		{
			s1+=evals.get(i);
		}
		double s2 = 0.0;
		for(int i=0;i<dim;i++)
		{
			s2+=evals.get(i);
		}
		return s2/s1;
	}

	/**
	 * get residual variance of datapoints.
	 * 
	 * @return overall residual variance
	 */
	public double getVarE()
	{
		int R = Xres.getRowDimension();
		int C = Xres.getColumnDimension();
		List<Double> es = new Vector<Double>();
		for(int r=0;r<R;r++)
		{
			for(int c=0;c<C;c++)
			{
				es.add( Xres.get(r,c));
			}
		}
		double s =  Statistics.var(es);
		s*=(R*C-1);
		s/=(R-ncomp_-1)*(C-ncomp_);
		return s;
	}

	/**
	 * get DModX for i-th datapoint.
	 * 
	 * @param i
	 * @return
	 */
	public double getDModX(int i)
	{
		return DModX.get(i,0);
	}

	/**
	 * get Hotelling statistic for i-th datapoint.
	 * 
	 * @param i
	 * @return
	 */
	public double getTval(int i)
	{
		return TT.get(i,i);
	}

	/**
	 * get value of i-th eigenvalue.
	 *  
	 * @param i
	 * @return
	 */
	public double getEval(int i)
	{
		return evals.get(i);
	}

	/**
	 * get Matrix of scores (i.e. embeddings/projections of datapoints on pca plane)
	 * 
	 * @return matrix of scores
	 */
	public Matrix getScores()
	{
		// return T = U*S
		return T;
	}

	/**
	 * get loadings (i.e. eigenvectors, vectors spanning pca plane)
	 * 
	 * @return matrix of loadings
	 */
	public Matrix getLoadings()
	{
		// return eigenvectors V
		return V;
	}
	
}
