/**
 * 
 */
package org.roettig.maths.mva;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.roettig.maths.matrix.ElementApplicator;
import org.roettig.maths.matrix.ElementFunctor;
import org.roettig.maths.matrix.ElementIterator;
import org.roettig.maths.matrix.RowIterator;
import org.roettig.maths.statistics.Statistics;
import org.roettig.maths.util.Pair;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

/**
 * 
 * MDS (multi dimensional scaling analysis) calculation on descriptor matrix.
 * 
 * <pre>
 * {@code
 * 
 * Matrix X  = MMatrix.load("uk.dat");
 * Matrix D2 = MDS.computeSquaredDistanceMatrix(X);
 * 
 * MDS mds = new MDS(D2);
 * mds.compute();
 * 
 * }
 * 
 * </pre>
 * 
 * @author roettig
 *
 */
public class MDS
{
	public static Logger logger = Logger.getLogger("org.roettig.maths.mva.mds");

	private Matrix D2;
	private Matrix D2means;
	private double D2mean_tot;
	private boolean computed = false;
	private double  eps = 1e-4;
	private int dim;
	private Matrix B;
	private Matrix Q;
	private Matrix S;
	private Matrix X;

	/**
	 * Ctor for MDS.
	 * 
	 * @param _D2 : Matrix of squared distances between all datapoints
	 * 
	 */
	public MDS(Matrix _D2)
	{
		D2 = _D2;
	}

	/**
	 * get Gramian matrix.
	 * 
	 * @return Gramian matrix
	 */
	public Matrix getGramian()
	{
		return B;
	}

	/**
	 * get embedding of datapoints.
	 * 
	 * @return Matrix d-dimensional with corrdinates
	 */
	public Matrix getX()
	{
		return X;
	}

	/**
	 * get dimensionality of embedding.
	 * 
	 * @return dimensionality
	 */
	public int getDim()
	{
		return dim;
	}

	/**
	 * compute MDS.
	 * 
	 */
	public void compute()
	{
		int N = D2.getColumnDimension();

		// compute centering matrix H
		Matrix H = new Matrix(N,N);
		for(int i=0;i<N;i++)
		{
			for(int j=0;j<N;j++)
			{
				if(i==j)
					H.set(i,j, (N-1)/(N*1.0) );
				else
					H.set(i,j, -1.0/N );
			}
		}

		B = H.times(D2.times(H)).times(-0.5);
		
		SingularValueDecomposition svd = new SingularValueDecomposition(B);
		Matrix Ssqrt = svd.getS();

		double[] svs = svd.getSingularValues();
		dim = 0;
		for(Double d: svs)
		{
			logger.info("SV "+d);
			if(d>eps)
				dim++;
			else
				break;
		}
		logger.info("MDS dim="+dim);

		ElementApplicator appl = new ElementApplicator(new ElementFunctor(){
			@Override
			public double calculate(int i, int j, double d)
			{
				return Math.sqrt(d);
			}
		}
		);
		appl.apply(Ssqrt);
		X = svd.getU().times(Ssqrt);
		X = X.getMatrix(0, N-1, 0, dim-1);
		//X.print(5,2);
	}

	/**
	 * embed new test datapoints.
	 * 
	 * @param D2t: matrix with distances to training datapoints
	 * @return coordinates of embedded test points
	 *  
	 */
	public Matrix embed(Matrix D2t)
	{
		computeEmbeddingMeasures();
		int K = D2t.getRowDimension();
		int N = D2.getRowDimension();

		Matrix G   = new Matrix(K,N);
		for(int i=0;i<K;i++)
		{
			RowIterator iter = new RowIterator(D2t,i);
			double Dmn = Statistics.mean(iter);
			for(int j=0;j<N;j++)
			{
				double g = -0.5*(D2t.get(i, j)-D2means.get(j,0)-Dmn+D2mean_tot);
				G.set(i,j, g);
			}
		}
		//G.print(5,2);
		return G.times(Q).times(S);
	}

	public Matrix embedGramian(Matrix D2t)
	{
		computeEmbeddingMeasures();
		int K = D2t.getRowDimension();
		int N = D2.getRowDimension();
		Matrix G   = new Matrix(K,N);
		for(int i=0;i<K;i++)
		{
			RowIterator iter = new RowIterator(D2t,i);
			double Dmn = Statistics.mean(iter);
			for(int j=0;j<N;j++)
			{
				double g = -0.5*(D2t.get(i, j)-D2means.get(j,0)-Dmn+D2mean_tot);
				G.set(i,j, g);
			}
		}
		return G;
	}

	public void computeEmbeddingMeasures()
	{
		if(computed)
			return;

		computed = true;
		int N = D2.getColumnDimension();
		//
		D2means = new Matrix(N,1); 
		for(int i=0;i<N;i++)
		{
			RowIterator iter = new RowIterator(D2,i);
			double mn = Statistics.mean(iter);
			D2means.set(i,0, mn);
		}
		//D2means.print(5,3);

		ElementIterator iter = new ElementIterator(D2);
		D2mean_tot = Statistics.mean(iter);
		//System.out.println(D2mean_tot);

		// B = G
		EigenvalueDecomposition evd = new EigenvalueDecomposition(B);
		List<Double> evs = new ArrayList<Double>();
		for(Double d: evd.getRealEigenvalues())
		{
			evs.add(d);
		}
		List< Pair<Integer,Double>> sevs = Statistics.sort(evs, false);

		List<Integer> toFetch = new ArrayList<Integer>();

		evs = new ArrayList<Double>();
		for(Pair<Integer,Double> p: sevs)
		{
			//System.out.println(p);
			if(p.getSecond()>eps)
			{
				toFetch.add(p.getFirst());
				evs.add(p.getSecond());
			}
		}
		int[] idx = new int[toFetch.size()];
		int i=0;
		for(Integer it: toFetch)
		{
			idx[i]=it;
			i++;
		}
		Q = evd.getV().getMatrix(0, N-1, idx);
		//Q.print(5, 2);
		S = new Matrix(dim,dim);
		for(int j=0;j<dim;j++)
		{
			S.set(j, j, 1.0/Math.sqrt(evs.get(j)));
		}
		//S.print(8, 3);
	}

	public static Matrix computeSquaredDistanceMatrix(Matrix X)
	{
		Matrix D = computeDistanceMatrix(X);
		ElementApplicator appl = new ElementApplicator(new ElementFunctor(){

			@Override
			public double calculate(int i, int j, double d)
			{
				return d*d;
			}

		}); 
		appl.apply(D);
		return D;
	}

	public static Matrix computeDistanceMatrix(Matrix X)
	{
		int    N = X.getRowDimension();
		int    K = X.getColumnDimension();
		Matrix D = new Matrix(N,N);
		for(int i=0;i<N;i++)
		{
			for(int j=i;j<N;j++)
			{
				Matrix r1 = X.getMatrix(i, i, 0, K-1);
				Matrix r2 = X.getMatrix(j, j, 0, K-1);
				double d = r1.minus(r2).norm2();
				D.set(i, j, d);
				D.set(j, i, d);
			}
		}
		return D;
	}

	public static Matrix computeSquaredDistanceMatrix(Matrix X, Matrix Xtst)
	{
		Matrix D = computeDistanceMatrix(X,Xtst);
		ElementApplicator appl = new ElementApplicator(new ElementFunctor(){

			@Override
			public double calculate(int i, int j, double d)
			{
				return d*d;
			}

		}); 
		appl.apply(D);
		return D;
	}

	public static Matrix computeDistanceMatrix(Matrix X, Matrix Xtst)
	{
		int    N1 = X.getRowDimension();
		int    N2 = Xtst.getRowDimension();
		int    K = X.getColumnDimension();
		Matrix D = new Matrix(N2,N1);
		for(int i=0;i<N2;i++)
		{
			for(int j=0;j<N1;j++)
			{
				Matrix r1 = X.getMatrix(j, j, 0, K-1);
				Matrix r2 = Xtst.getMatrix(i, i, 0, K-1);
				double d = r1.minus(r2).norm2();
				D.set(i, j, d);
			}
		}
		return D;
	}

}
