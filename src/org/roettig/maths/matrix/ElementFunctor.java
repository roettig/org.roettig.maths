package org.roettig.maths.matrix;

/**
 * The ElementFunctor interface is the base for any functors modifying matrix elements.
 * 
 * 
 * @author roettig
 *
 */
public interface ElementFunctor
{
	/**
	 * 
	 * @param i : row index 
	 * @param j : column index
	 * @param d : current value of element
	 * 
	 * @return new value of element
	 */
    double calculate(int i, int j, double d);
}
