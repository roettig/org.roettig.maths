/**
 * 
 */
package org.roettig.maths.test;


import java.util.List;
import java.util.Vector;
import org.roettig.maths.statistics.Statistics;
import org.roettig.maths.statistics.Statistics.ConfusionMatrix;
import junit.framework.TestCase;

/**
 * @author roettig
 *
 */
public class TestStatistics extends TestCase
{
	
	public void testVar() throws Exception
	{
		List<Double> vec = new Vector<Double>();
		vec.add(1.0);
		vec.add(2.0);
		vec.add(3.0);
		vec.add(4.0);
		assertEquals( "var([1,2,3,4]) should be 1.6666666666666667", 1.6666666666666667, Statistics.var(vec),  1e-5);
	}

	public void testPearson() throws Exception
	{
		List<Double> y = new Vector<Double>();
		List<Double> yp = new Vector<Double>();
		y.add(1.0);yp.add(1.0);
		y.add(2.0);yp.add(1.0);
		y.add(3.0);yp.add(3.0);
		y.add(4.0);yp.add(4.0);
		assertEquals( "corr([1,2,3,4],[1,1,3,4]) should be 0.8962962962962963", 0.8962962962962963, Statistics.pearsonCorr(y, yp), 1e-5);
	}

	public void testStd() throws Exception
	{
		List<Double> vec = new Vector<Double>();
		vec.add(1.0);
		vec.add(2.0);
		vec.add(3.0);
		vec.add(4.0);
		assertEquals( "std([1,2,3,4]) should be 1.2909944487358056", 1.2909944487358056, Statistics.std(vec), 1e-5);
	}

	public void testMean() throws Exception
	{
		List<Double> vec = new Vector<Double>();
		vec.add(1.0);
		vec.add(2.0);
		vec.add(3.0);
		vec.add(4.0);
		assertEquals( "mean([1,2,3,4]) should be 2.5", 2.5, Statistics.mean(vec), 1e-5);
	}

	public void testAccuracy() throws Exception
	{
		List<Double> yt = new Vector<Double>();
		yt.add(1.0);
		yt.add(2.0);
		yt.add(1.0);
		
		List<Double> yp = new Vector<Double>();
		yp.add(1.0);
		yp.add(2.0);
		yp.add(1.0);
		
		assertEquals( "accuracy should be 1.0", 1.0, Statistics.accuracy(yp, yt), 1e-8);
		yp.set(1,1.0);
		
		assertEquals( "accuracy should be 2/3", 2.0/3.0, Statistics.accuracy(yp, yt), 1e-8);
		yp.set(2,2.0);
		yp.set(0,2.0);
		
		assertEquals( "accuracy should be 0", 0.0, Statistics.accuracy(yp, yt), 1e-8);
		yt.set(0,2.0);
		
		assertEquals( "accuracy should be 1/3", 1.0/3.0, Statistics.accuracy(yp, yt), 1e-8);
	}
	
	public void testConfusionMatrix()
	{
		List<Double> yp = new Vector<Double>();
		List<Double> yt = new Vector<Double>();

		Double C1 = new Double(1.0);
		Double C2 = new Double(2.0);
		Double C3 = new Double(3.0);
		
		/*
	          pred
 		C   1   2   3

 		1   8   1   2

 true	2   1   7   2

 		3   0   2   6
		 */

		// 2 x (2,3)
		yt.add(C2);yp.add(C3);
		yt.add(C2);yp.add(C3);


		// 2 x (1,3)
		yt.add(C1);yp.add(C3);
		yt.add(C1);yp.add(C3);

		// 2 x (3,2)
		yt.add(C3);yp.add(C2);
		yt.add(C3);yp.add(C2);

		// 1 x (1,2)
		yt.add(C1);yp.add(C2);

		// 0 x (3,1)

		// 1 x (2,1)
		yt.add(C2);yp.add(C1);


		// 8 x (1,1)
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);

		// 7 x (2,2)
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);

		// 6 x (3,3)
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		
		ConfusionMatrix conf = Statistics.calcConfusionMatrix(C1, yp, yt);
		assertEquals(8,conf.TP);
		assertEquals(13,conf.TN);
		assertEquals(1,conf.FP);
		assertEquals(3,conf.FN);
		
		conf = Statistics.calcConfusionMatrix(C2, yp, yt);
		
		assertEquals(7,conf.TP);
		assertEquals(14,conf.TN);
		assertEquals(3,conf.FP);
		assertEquals(3,conf.FN);
		
		conf = Statistics.calcConfusionMatrix(C3, yp, yt);
		
		assertEquals(6,conf.TP);
		assertEquals(15,conf.TN);
		assertEquals(4,conf.FP);
		assertEquals(2,conf.FN);
		
	}

	public void testSensSpec()
	{
		Double pos = new Double(1.0);
		Double neg = new Double(2.0);
		
		List<Double> yp = new Vector<Double>();
		List<Double> yt = new Vector<Double>();
		/*
        			pred
        			  p   n
				  C   1   2

		true	p 1   8   1

 				n 2   2   7
 				
 				iff 1 == positive
 				TP = 8 TN = 7 FP = 2 FN = 1
 				Sn = 8/(8+1) = 0.88889
 				Sp = 7/(7+2) = 0.77778
 				Pr = 8/(8+2) = 0.80000

		*/
		
		// 1 x (1,2)
		yt.add(pos);yp.add(neg);

		// 1 x (2,1)
		yt.add(neg);yp.add(pos);
		yt.add(neg);yp.add(pos);

		// 8 x (1,1)
		yt.add(pos);yp.add(pos);
		yt.add(pos);yp.add(pos);
		yt.add(pos);yp.add(pos);
		yt.add(pos);yp.add(pos);
		yt.add(pos);yp.add(pos);
		yt.add(pos);yp.add(pos);
		yt.add(pos);yp.add(pos);
		yt.add(pos);yp.add(pos);
		
		
		// 7 x (2,2)
		yt.add(neg);yp.add(neg);
		yt.add(neg);yp.add(neg);
		yt.add(neg);yp.add(neg);
		yt.add(neg);yp.add(neg);
		yt.add(neg);yp.add(neg);
		yt.add(neg);yp.add(neg);
		yt.add(neg);yp.add(neg);
		
				
		List<Double> sens_spec =  Statistics.calcSensSpec(pos, yp, yt);
		
		assertEquals(0.88889,sens_spec.get(0),1e-4);
		assertEquals(0.77779,sens_spec.get(1),1e-4);
		
		
		sens_spec =  Statistics.calcSensSpec(yp, yt);
		double mean = (sens_spec.get(0)+sens_spec.get(1))/2.0;
		assertEquals(0.83333,mean,1e-4);		

	}
	
	
	
	public void testPrecRec()
	{
		List<Double> yp = new Vector<Double>();
		List<Double> yt = new Vector<Double>();

		Double C1 = new Double(1.0);
		Double C2 = new Double(2.0);
		Double C3 = new Double(3.0);
		
		/*
	          pred
 		C   1   2   3

 		1   8   1   2

 true	2   1   7   2

 		3   0   2   6
		 */

		// 2 x (2,3)
		yt.add(C2);yp.add(C3);
		yt.add(C2);yp.add(C3);


		// 2 x (1,3)
		yt.add(C1);yp.add(C3);
		yt.add(C1);yp.add(C3);

		// 2 x (3,2)
		yt.add(C3);yp.add(C2);
		yt.add(C3);yp.add(C2);

		// 1 x (1,2)
		yt.add(C1);yp.add(C2);

		// 0 x (3,1)

		// 1 x (2,1)
		yt.add(C2);yp.add(C1);


		// 8 x (1,1)
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);
		yt.add(C1);yp.add(C1);

		// 7 x (2,2)
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);
		yt.add(C2);yp.add(C2);

		// 6 x (3,3)
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);
		yt.add(C3);yp.add(C3);


		List<Double> sens_spec =  Statistics.calcSensSpec(yp, yt);
		
		assertEquals(0.7257575757575757,sens_spec.get(0),1e-5);
		assertEquals(0.8471915081822203,sens_spec.get(1),1e-5);
		
		List<Double> prec_rec  =  Statistics.calcPrecRec(yp, yt);
		assertEquals(0.7296296296296295,prec_rec.get(0),1e-5);
		assertEquals(0.7257575757575757,prec_rec.get(1),1e-5);

		double f = Statistics.calcFMeasure(yp,yt);
		
		assertEquals(0.7276884518989781,f,1e-5);
	}
	
	public void testauROC() throws Exception
	{
		// http://www.rad.jhmi.edu/jeng/javarad/roc/JROCFITi.html 

		List<Double> ypNeg = new Vector<Double>();
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(1.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(2.0);
		ypNeg.add(3.0);
		ypNeg.add(3.0);
		ypNeg.add(3.0);
		ypNeg.add(4.0);
		ypNeg.add(5.0);


		List<Double> ypPos = new Vector<Double>();
		ypPos.add(1.0);
		ypPos.add(2.0);
		ypPos.add(2.0);
		ypPos.add(3.0);
		ypPos.add(3.0);
		ypPos.add(3.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(4.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);
		ypPos.add(5.0);

		assertEquals( "auROC should be 0.892", 0.892, Statistics.auROC(ypPos,ypNeg), 1e-5 );

		
	}
}
