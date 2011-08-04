/**
 * 
 */
package org.roettig.maths.util;

/**
 * 
 * Simple pair class.
 * 
 * @author roettig
 *
 */
public class Pair<T1,T2>
{
	private T1 first;
	private T2 second;

	public Pair(T1 _first, T2 _second)
	{
		first = _first; second = _second;
	}

	/** 
	 * get first element of pair.   
	 * 
	 * @return first element of pair
	 */
	public T1 getFirst()
	{
		return first;
	}


	/** 
	 * set first element of pair.   
	 * 
	 * @param value for first element
	 */
	public void setFirst(T1 first)
	{
		this.first = first;
	}

	/** 
	 * get second element of pair.   
	 * 
	 * @return second element of pair
	 */
	public T2 getSecond()
	{
		return second;
	}

	/** 
	 * set second element of pair.   
	 * 
	 * @param value for second element
	 */
	public void setSecond(T2 second)
	{
		this.second = second;
	}

	public String toString()
	{
		return "("+first+","+second+")";
	}

}
