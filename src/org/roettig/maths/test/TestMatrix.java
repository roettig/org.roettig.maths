/**
 * 
 */
package org.roettig.maths.test;

import org.roettig.maths.matrix.ColumnCenterer;
import org.roettig.maths.matrix.ColumnScaler;
import org.roettig.maths.matrix.DiagonalApplicator;
import org.roettig.maths.matrix.ElementApplicator;
import org.roettig.maths.matrix.ElementFunctor;
import org.roettig.maths.matrix.MMatrix;
import org.roettig.maths.mva.PCA;
import junit.framework.TestCase;

/**
 * @author roettig
 *
 */
public class TestMatrix extends TestCase
{
	public void testMatrix() throws Exception
	{
		MMatrix m = new MMatrix(3,3,0.0);
		m.set(0, 0, 1.0);
		m.set(0, 1, 2.0);
		m.set(0, 2, 3.0);
		m.set(1, 0, 4.0);
		m.set(1, 1, 5.0);
		m.set(1, 2, 6.0);
		m.set(2, 0, 7.0);
		m.set(2, 1, 8.0);
		m.set(2, 2, 9.0);
		m.save("/tmp/mat");
		System.out.println(m.toString());

		double i = 1.0;
		for(Double d: m.columnIterator(0))
		{
			assertEquals( "element should be ", i , d , 1e-9);
			i+=3.0;
		}
		i = 3.0;
		for(Double d: m.columnIterator(2))
		{
			assertEquals( "element should be ", i , d , 1e-9);
			i+=3.0;
		}
		i = 1.0;
		for(Double d: m.rowIterator(0))
		{
			assertEquals( "element should be ", i , d , 1e-9);
			i+=1.0;
		}
		i = 7.0;
		for(Double d: m.rowIterator(2))
		{
			assertEquals( "element should be ", i , d , 1e-9);
			i+=1.0;
		}
		i = 1.0;
		for(Double d: m.diagonalIterator())
		{
			System.out.println(d);
			assertEquals( "element should be ", i , d , 1e-9);
			i+=4.0;
		}

		MMatrix x = new MMatrix(m);

		ElementApplicator doublediag = new ElementApplicator( new ElementFunctor()
		{
			@Override								    
			public double calculate(int i, int j, double d)
			{
				if(i==j)
				{
					return 2*d;
				}
				return d;
			}
		});
		doublediag.apply(m);
		System.out.println(m);


		//
		MMatrix D3 = new MMatrix(3,3,0.0);
		D3.set(0, 0, 1.0);
		D3.set(1, 1, 2.0);
		D3.set(2, 2, 3.0);
		DiagonalApplicator dapl = new DiagonalApplicator(new ElementFunctor(){
			@Override
			public double calculate(int i, int j, double d)
			{
				return d*d;
			}}); 
		dapl.apply(D3);
		assertEquals(D3.get(0,0), 1.0, 1e-8);
		assertEquals(D3.get(1,1), 4.0, 1e-8);
		assertEquals(D3.get(2,2), 9.0, 1e-8);

		assertEquals(D3.get(0,1), 0.0, 1e-8);
		assertEquals(D3.get(0,2), 0.0, 1e-8);
		assertEquals(D3.get(1,0), 0.0, 1e-8);
		assertEquals(D3.get(1,2), 0.0, 1e-8);
		assertEquals(D3.get(2,0), 0.0, 1e-8);
		assertEquals(D3.get(2,1), 0.0, 1e-8);

		//

		ColumnCenterer cc = new ColumnCenterer();
		cc.apply(x);

		System.out.println(x);

		MMatrix xc = new MMatrix(3,3,0.0);
		xc.set(0, 0, -3.0);
		xc.set(0, 1, -3.0);
		xc.set(0, 2, -3.0);
		xc.set(1, 0,  0.0);
		xc.set(1, 1,  0.0);
		xc.set(1, 2,  0.0);
		xc.set(2, 0,  3.0);
		xc.set(2, 1,  3.0);
		xc.set(2, 2,  3.0);

		assertEquals("s",xc.equalsEps(x, 1e-8),true);

		System.out.println(xc.equalsEps(m, 1e-8));

		ColumnScaler cs = new ColumnScaler();
		cs.apply(x);


		MMatrix xs = new MMatrix(3,3,0.0);
		xs.set(0, 0, -1.0);
		xs.set(0, 1, -1.0);
		xs.set(0, 2, -1.0);
		xs.set(1, 0,  0.0);
		xs.set(1, 1,  0.0);
		xs.set(1, 2,  0.0);
		xs.set(2, 0,  1.0);
		xs.set(2, 1,  1.0);
		xs.set(2, 2,  1.0);

		System.out.println(x);
		assertEquals("s",xs.equalsEps(x, 1e-8),true);

		MMatrix foods = MMatrix.load(this.getClass().getResource("/data/foods.dat").getFile());
		System.out.println(foods);
		ColumnCenterer.apply(foods);
		ColumnScaler.apply(foods);
		System.out.println(foods);

		PCA pca = new PCA(foods,3,true);

		// eigenvectors/loadings
		MMatrix V = new MMatrix(pca.getLoadings());
		// scores
		MMatrix S = new MMatrix(pca.getScores());

		/*
	System.out.println("########");
	System.out.println(pca.getHotellingParam(0));
	System.out.println(pca.getHotellingParam(1));
		 */


		assertEquals("first  eigenvalue should be 91.34233566503293",91.34233566503293,pca.getEval(0),1e-8);
		assertEquals("second eigenvalue should be 63.011833688379355",63.011833688379355,pca.getEval(1),1e-8);

		assertEquals("gof(1) should be 0.3044744522167764",0.3044744522167764,pca.getGoodnessOfFit(1),1e-8);
		System.out.println("gof="+pca.getGoodnessOfFit(1));
		assertEquals("gof(2) should be 0.5145138978447076",0.5145138978447076,pca.getGoodnessOfFit(2),1e-8);
		System.out.println("gof="+pca.getGoodnessOfFit(2));

		double varE = pca.getVarE();
		System.out.println("stdE="+Math.sqrt(varE));

		MMatrix Vref = MMatrix.load(this.getClass().getResource("/data/foodsV3.dat").getFile());
		MMatrix V3 = new MMatrix(V.getMatrix(0, 19, 0, 2));
		assertEquals("loadings do not match", Vref.equalsEps(V3, 1e-4), true);

		MMatrix Sref = MMatrix.load(this.getClass().getResource("/data/foodsS3.dat").getFile());
		MMatrix S3 = new MMatrix(S.getMatrix(0, 15, 0, 2));
		assertEquals("scores do not match", Sref.equalsEps(S3, 1e-4), true);



		// check normalized DModX values
		// DModX values > 2.5*sqrt(varE) are outliers
		assertEquals("normalized DModX(1) should be 0.6468042435399536",0.6468042435399536 , Math.sqrt(pca.getDModX(0)/varE),1e-8);
		assertEquals("normalized DModX(2) should be 0.8981077506558245",0.8981077506558245 , Math.sqrt(pca.getDModX(1)/varE),1e-8);
		assertEquals("normalized DModX(3) should be 1.0691563625268792",1.0691563625268792 , Math.sqrt(pca.getDModX(2)/varE),1e-8);
		assertEquals("normalized DModX(14) should be 1.145356566777296",1.145356566777296  , Math.sqrt(pca.getDModX(13)/varE),1e-8);
		assertEquals("normalized DModX(15) should be 0.9024400977057276",0.9024400977057276 , Math.sqrt(pca.getDModX(14)/varE),1e-8);
		assertEquals("normalized DModX(16) should be 0.8512781583224829",0.8512781583224829 , Math.sqrt(pca.getDModX(15)/varE),1e-8);





		System.out.println(Math.sqrt(pca.getDModX(0)/varE));
		System.out.println(Math.sqrt(pca.getDModX(1)/varE));
		System.out.println(Math.sqrt(pca.getDModX(2)/varE));
		System.out.println(Math.sqrt(pca.getDModX(3)/varE));
		System.out.println(Math.sqrt(pca.getDModX(4)/varE));
		System.out.println(Math.sqrt(pca.getDModX(5)/varE));
		System.out.println(Math.sqrt(pca.getDModX(6)/varE));
		System.out.println(Math.sqrt(pca.getDModX(7)/varE));
		System.out.println(Math.sqrt(pca.getDModX(8)/varE));
		System.out.println(Math.sqrt(pca.getDModX(9)/varE));
		System.out.println(Math.sqrt(pca.getDModX(10)/varE));
		System.out.println(Math.sqrt(pca.getDModX(11)/varE));	
		System.out.println(Math.sqrt(pca.getDModX(12)/varE));
		System.out.println(Math.sqrt(pca.getDModX(13)/varE));
		System.out.println(Math.sqrt(pca.getDModX(14)/varE));
		System.out.println(Math.sqrt(pca.getDModX(15)/varE));
	}
}
