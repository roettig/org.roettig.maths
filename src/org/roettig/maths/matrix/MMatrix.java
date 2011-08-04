package org.roettig.maths.matrix;

import java.io.*;
import java.util.*;
import Jama.Matrix;

/**
 * MMatrix is a convenience wrapper for Matrix.
 * 
 * @author roettig
 *
 */
public class MMatrix extends Matrix 
{

	public MMatrix(Matrix m)
	{
		super(m.getArrayCopy());
	}

	public MMatrix(int r, int c)
	{
		super(r, c);
	}

	public MMatrix(int r, int c, double val)
	{
		super(r, c, val);
	}

	/**
	 * saves matrix to file.
	 * 
	 * @param filename
	 * @throws IOException
	 */
	public void save(String filename) throws IOException
	{
		PrintWriter writer = new PrintWriter (filename);
		for(int i=0;i<this.getRowDimension();i++)
		{
			for(int j=0;j<this.getColumnDimension();j++)
			{
				writer.print(String.format(Locale.ENGLISH,"%+12.6f ",this.get(i,j)));
			}
			writer.println();
		}
		writer.close();
	}

	/**
	 * loads matrix from file.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static MMatrix load(String filename) throws IOException
	{
		Vector< Vector<Double> > data = new Vector< Vector<Double> >(); 
		Scanner lineScanner = new Scanner( new File(filename) ); 
		lineScanner.useDelimiter(System.getProperty("line.separator")); 

		while ( lineScanner.hasNext() )
		{
			String line = lineScanner.next();
			Scanner scanner = new Scanner(line);
			scanner.useLocale(Locale.ENGLISH);
			Vector<Double> row = new Vector<Double>();
			while( scanner.hasNextDouble() )
			{
				double d = scanner.nextDouble();
				row.add( d );
			}
			data.add( row );
		}
		lineScanner.close();

		int R = data.size();
		int C = data.get(0).size();
		MMatrix ret = new MMatrix(R,C,0.0);
		for(int r=0;r<R;r++)
		{
			for(int c=0;c<C;c++)
			{
				ret.set(r,c, data.get(r).get(c));
			}
		}
		return ret;
	}

	/**
	 * yields column iterator on matrix.
	 * 
	 * @param c : target column index
	 * 
	 * @return Double-iterator
	 */
	public Iterable<Double> columnIterator(int c)
	{
		return new ColumnIterator(this,c);
	}

	/**
	 * yields row iterator on matrix.
	 * 
	 * @param c : target row index
	 * 
	 * @return Double-iterator
	 */
	public Iterable<Double> rowIterator(int r)
	{
		return new RowIterator(this,r);
	}

	/**
	 * yields diagonal elements iterator.
	 * 
	 * @return
	 */
	public Iterable<Double> diagonalIterator()
	{
		return new DiagonalIterator(this);
	}

	public String toString(boolean pretty)
	{
		StringBuffer ret = new StringBuffer();
		if(pretty)
			ret.append("[matrix]\n");
		for(int i=0;i<this.getRowDimension();i++)
		{
			for(int j=0;j<this.getColumnDimension();j++)
			{
				ret.append(String.format(Locale.ENGLISH,"%+12.6f ",this.get(i,j)));
			}
			ret.append("\n");
		}
		if(pretty)
			ret.append("\n");
		return ret.toString();
	}

	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append("[matrix]\n");
		for(int i=0;i<this.getRowDimension();i++)
		{
			for(int j=0;j<this.getColumnDimension();j++)
			{
				ret.append(String.format(Locale.ENGLISH,"%+12.6f ",this.get(i,j)));
			}
			ret.append("\n");
		}
		ret.append("\n");
		return ret.toString();
	}

	/**
	 * equals the matrix to the matrix m (within given epsilon). 
	 * 
	 * @param m : matrix
	 * @param eps : epsilon cutoff
	 * @return
	 */
	public boolean equalsEps(Matrix m, double eps)
	{
		int R1 = m.getRowDimension();
		int C1 = m.getColumnDimension();
		int R2 = this.getRowDimension();
		int C2 = this.getColumnDimension();
		if(R1!=R2 || C1!=C2)
			return false;
		for(int i=0;i<R1;i++)
		{
			for(int j=0;j<C1;j++)
			{
				if(Math.abs(m.get(i,j)-this.get(i,j))>eps)
					return false;
			}
		}

		return true;
	}
}
