package org.roettig.maths.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import org.roettig.maths.util.Pair;

public class Statistics
{
	public static class ConfusionMatrix
	{
		/**
		 * true positives.
		 */
		public int TP;
		
		/**
		 * true negatives.
		 */
		public int TN;
		
		/**
		 * false positives.
		 */
		public int FP;
		
		/**
		 * false negatives.
		 */
		public int FN;
		
		public String toString()
		{
			return String.format("TP: %3d TN: %3d FP: %3d FN: %3d N:%3d",TP,TN,FP,FN,(TP+TN+FP+FN));
		}
	}
	
	/** 
	 * computes the prediction accuracy.  
	 *  
	 * @param yp : the predicted values
	 * @param yt : the true values     
	 * 
	 * @return value of accuracy
	 */
	public static double accuracy(Collection<? extends Object> yp, Collection<? extends Object> yt) throws Exception
	{
		double sum = 0.0;
		
		if(yt.size()!=yp.size())
			throw new Exception("list sizes do not match");
		
		Iterator<? extends Object> yp_iter = yp.iterator();
		Iterator<? extends Object> yt_iter = yt.iterator();
		
		while(yp_iter.hasNext())
		{
			Object ypl = yp_iter.next();
			Object ytl = yt_iter.next();
			
			if(ypl.equals(ytl))
			{
				sum+=1.0;
			}
		}
		return sum/yt.size();
	}
	
	
	/** 
	 * computes the mean of a vector/Iterable. 
	 * 
	 * @param v : vector with values
	 *
	 * @return value of mean(v)
	 */
	public static double mean(Iterable<? extends Number> v)
	{
		double s = 0;
		int    N = 0;
		
		for(Number e:v)
		{
			s+= e.doubleValue();
			N++;
		}
		s/=N;
		
		return s;
	}

	/** 
	 * computes the bias-corrected sample variance of a vector/Iterable. 
	 * 
	 * @param v : vector with values
	 *
	 * @return value of var(v)
	 */
	public static double var(Iterable<? extends Number> v)
	{
		double s  = 0.0;
		double mn = mean(v);
		
		int N = 0;
		for(Number e:v)
		{
			s+=Math.pow(e.doubleValue()-mn,2);
			N++;
		}
		s/=N-1;
		
		return s;
	}

	/** 
	 * computes the standard deviation of a vector/Iterable. 
	 * 
	 * @param v : vector with values
	 *
	 * @return value of std(v)
	 */
	public static double std(Iterable<? extends Number> v)
	{
		return Math.sqrt(var(v));
	}

	/** 
	 * computes coefficient of determination (q^2) between yt and yp.
	 *
	 * @param yt : the true outcome
	 * @param yp : the predicted outcome
	 * 
	 * @return value of q2
	 */
	public static double q2(Collection<? extends Number> yt, Collection<? extends Number> yp)
	{
		double mn = mean(yt);
		Vector<Double> yv = new Vector<Double>();
		double su = 0.0;
		for(Number e: yt)
		{
			double d = e.doubleValue();
			su += Math.pow((d-mn),2.0);
			yv.add(d);
		}

		double so = 0.0;
		int k = 0;
		for(Number e: yp)
		{
			so += Math.pow((e.doubleValue()-yv.get(k)),2.0);
			k++;
		}

		return 1-(so/su);
	}

	/** 
	 * computes Pearson correlation between yt and yp.
	 *
	 * @param yt : the true outcome
	 * @param yp : the predicted outcome
	 * 
	 * @return value of q2
	 */
	public static double pearsonCorr(Collection<? extends Number> yt, Collection<? extends Number> yp)
	{
		double mny  = mean(yt);
		double mnyp = mean(yp);

		Vector<Double> Y = new Vector<Double>();
		double sxx = 0.0;
		for(Number e: yt)
		{
			double d = e.doubleValue();
			sxx += Math.pow((d-mny),2.0);
			Y.add(d);
		}

		Vector<Double> YP = new Vector<Double>();
		double syy = 0.0;
		for(Number e: yp)
		{
			double d = e.doubleValue();
			syy += Math.pow((d-mnyp),2.0);
			YP.add(d);
		}

		double sxy = 0.0;
		for(int i=0;i<YP.size();i++)
		{
			sxy+=(YP.get(i)-mnyp)*(Y.get(i)-mny);
		}
		sxy = Math.pow( sxy,2.0);
		return sxy/(sxx*syy);
	}

	/** 
	 * computes the mean squared error of prediction vs. true value. 
	 * 
	 * @param yp : the predicted values
	 * @param yt : the true values
	 * 
	 * @return value of mse
	 */
	public static double mse(Collection<? extends Number> yp, Collection<? extends Number> yt)
	{
		Vector<Double> yv = new Vector<Double>();
		for(Number e: yt)
		{
			double d = e.doubleValue();
			yv.add(d);
		}
		double s = 0.0;
		int k = 0;
		for(Number e: yp)
		{
			s += Math.pow((e.doubleValue()-yv.get(k)),2.0);
			k++;
		}   
		return s/k;
	}
	
	/** 
	 * computes confusion matrix (TP,FP, TN and FN values).   
	 *  
	 * @param pos : the positive class label
	 * @param yp : the predicted values
	 * @param yt : the true values 
	 * 
	 * @return ConfusionMatrix
	 */
	public static ConfusionMatrix calcConfusionMatrix(Object pos, Collection<? extends Object> Yp, Collection<? extends Object> Y)
	{
		int TP=0;
		int FP=0;
		int TN=0;
		int FN=0;
		
		Iterator<? extends Object> Y_iter  = Y.iterator();
		Iterator<? extends Object> Yp_iter = Yp.iterator();
		
		while(Y_iter.hasNext())
		{
			Object y  = Y_iter.next();
			Object yp = Yp_iter.next();

			if(y.equals(yp))
			{
				if(y.equals(pos))
				{
					TP++;
				}
				else
				{
					TN++;
				}
			}
			else
			{
				if(y.equals(pos))
				{
					FN++;
				}
				else
				{
					if(yp.equals(pos))
					{
						FP++;
					}
					else
					{
						// irrelevant (yp,yt) pair
					}
				}
			}
		}
	
		ConfusionMatrix conf = new ConfusionMatrix();
		conf.TP = TP;
		conf.FP = FP;
		conf.TN = TN;
		conf.FN = FN;

		return conf;
	}
	
	/** 
	 * computes confusion matrix (TP,FP, TN and FN values).  
	 *  
	 * @param pos : the positive class label
	 * @param neg : the negative class label
	 * @param yp : the predicted values
	 * @param yt : the true values
	 * 
	 * @return ConfusionMatrix
	 * 
	 */
	public static ConfusionMatrix calcConfusionMatrix(Object pos, Object neg, Collection<? extends Object> Yp, Collection<? extends Object> Y)
	{
		int TP=0;
		int FP=0;
		int TN=0;
		int FN=0;
		
		Iterator<? extends Object> Y_iter  = Y.iterator();
		Iterator<? extends Object> Yp_iter = Yp.iterator();
		
		while(Y_iter.hasNext())
		{
			Object y  = Y_iter.next();
			Object yp = Yp_iter.next();

			if(y.equals(yp))
			{
				if(y.equals(pos))
					TP++;
				else
					if(y.equals(neg))
						TN++;
			}
			else
			{
				if(y.equals(pos))
					FN++;
				else
					if(y.equals(neg))
						FP++;
			}
		}
	
		ConfusionMatrix conf = new ConfusionMatrix();
		conf.TP = TP;
		conf.FP = FP;
		conf.TN = TN;
		conf.FN = FN;

		return conf;
	}
	
	/** 
	 * computes sensitivity based on confusion matrix (TP,FP, TN and FN values).  
	 *  
	 * @param conf : confusion matrix
	 * 
	 * @return sensitivity Sn = TP / (TP + FN)
	 * 
	 */
	public static double calcSensitivity(ConfusionMatrix conf) 
	{
			// TP / (TP + FN)
			double ret = 0.0;
			
			int TP = conf.TP;
			int FN = conf.FN;
			
			if((TP+FN)>0)
				ret = (TP*1.0)/(TP+FN);
			
			return ret;
	}

	/** 
	 * computes specificity based on confusion matrix (TP,FP, TN and FN values).  
	 *  
	 * @param conf : confusion matrix
	 * 
	 * @return specificity Sp = TN / (FP + TN)
	 * 
	 */
	public static double calcSpecificity(ConfusionMatrix conf) 
	{
			// TN / (FP + TN)
			double ret = 0.0;
			
			int TN = conf.TN;
			int FP = conf.FP;
			
			if( (TN+FP)>0 )
				ret = (TN*1.0)/(TN+FP);
			
			return ret; 
	}
	
	/** 
	 * computes precision based on confusion matrix (TP,FP, TN and FN values).  
	 *  
	 * @param conf : confusion matrix
	 * 
	 * @return precision prec = TP / (FP + TP)
	 * 
	 */
	public static double calcPrecision(ConfusionMatrix conf) 
	{
			// TP / (FP + TP)
			double ret = 0.0;
			
			int TP = conf.TP;
			int FP = conf.FP;
			
			if((TP+FP)>0)
				ret = (TP*1.0)/(TP+FP);
			
			return ret;
	}
	
	/** 
	 * computes sensitivity and specificity based upon predicted/true values.  
	 * 
	 * @param pos : positive label
	 * @param yp  : predicted values
	 * @param yt  : true values
	 * 
	 * @return sensitivity and specificity
	 * 
	 */
	public static List<Double> calcSensSpec(Object pos, Collection<? extends Object> yp, Collection<? extends Object> yt)
	{
		List<Double> ret    = new ArrayList<Double>();
	
		ConfusionMatrix conf = Statistics.calcConfusionMatrix(pos, yp, yt);

		double Sn = Statistics.calcSensitivity(conf);
		double Sp = Statistics.calcSpecificity(conf);
		
		ret.add(Sn);
		ret.add(Sp);
		
		return ret;
	}

	/** 
	 * computes averaged sensitivity and specificity based upon predicted/true values.  
	 * 
	 * Note: In two class case one label is randomly picked as positive class label.  
	 *   
	 * @param yp : predicted values
	 * @param yt : true values
	 * 
	 * @return sensitivity and specificity
	 * 
	 */
	public static List<Double> calcSensSpec(Collection<? extends Object> yp, Collection<? extends Object> yt)
	{
		List<Double> ret    = new ArrayList<Double>();
		Set<Object>  labels = new HashSet<Object>();
		
		for(Object lab: yt)
		{
			labels.add(lab);
		}
		
		double sens = 0.0;
		double spec = 0.0;
		int    k    = 0;
		int    C	= labels.size();
		
		if(C==2)
		{
			ConfusionMatrix conf = Statistics.calcConfusionMatrix(labels.iterator().next(), yp, yt);
			double Sn = Statistics.calcSensitivity(conf);
			double Sp = Statistics.calcSpecificity(conf);
			ret.add(Sn);
			ret.add(Sp);
		}
		else
		{
			// average over all labels ..
			for(Object lab: labels)
			{
				ConfusionMatrix conf = Statistics.calcConfusionMatrix(lab, yp, yt);
				double Sn = Statistics.calcSensitivity(conf);
				double Sp = Statistics.calcSpecificity(conf);
				sens += Sn;
				spec += Sp; 
				k++;
			}
		
			ret.add(sens/k);
			ret.add(spec/k);
		}
		
		return ret;
	}
	
	/** 
	 * computes (alternative) averaged sensitivity and specificity based upon predicted/true values.  
	 * 
	 * averages over all possible  pairs of pos/neg  0.5*C*(C-1)
	 *  
	 * @param yp : predicted values
	 * @param yt : true values
	 * 
	 * @return sensitivity and specificity
	 * 
	 */
	public static List<Double> calcSensSpecAlt(Collection<? extends Object> yp, Collection<? extends Object> yt)
	{
		List<Double> ret    = new ArrayList<Double>();
		Set<Object>  labels = new HashSet<Object>();
		
		for(Object lab: yt)
		{
			labels.add(lab);
		}
		
		List<Object> labels_ = new ArrayList<Object>();
		for(Object lab: labels)
			labels_.add(lab);
		
		int C = labels_.size();
				
		double sens = 0.0;
		double spec = 0.0;
		int    k    = 0;

		for(int i=0;i<C;i++)
		{
			for(int j=i+1;j<C;j++)
			{
				Object pos = labels_.get(i);
				Object neg = labels_.get(j);
				
				ConfusionMatrix conf = Statistics.calcConfusionMatrix(pos, neg, yp, yt);
				
				double Sn = Statistics.calcSensitivity(conf);
				double Sp = Statistics.calcSpecificity(conf);
				
				sens += Sn;
				spec += Sp; 
				k++;
			}
		}
		
		ret.add(sens/k);
		ret.add(spec/k);
		
		return ret;
	}

	
	/** 
	 * computes precision and recall based upon predicted/true values.  
	 * 
	 * @param pos : positive label
	 * @param yp  : predicted values
	 * @param yt  : true values
	 * 
	 * @return precision and recall
	 * 
	 */
	public static List<Double> calcPrecRec(Object pos, Collection<? extends Object> yp, Collection<? extends Object> yt)
	{
		List<Double> ret    = new ArrayList<Double>();
	
		ConfusionMatrix conf = Statistics.calcConfusionMatrix(pos, yp, yt);

		double prec = Statistics.calcPrecision(conf);
		double rec  = Statistics.calcSensitivity(conf);
		
		ret.add(prec);
		ret.add(rec);
		
		return ret;
	}

	/** 
	 * computes averaged precision and recall based upon predicted/true values.  
	 *  
	 * Note: In two class case one label is randomly picked as positive class label.
	 *   
	 * @param yp : predicted values
	 * @param yt : true values
	 * 
	 * @return precision and recall
	 * 
	 */
	public static List<Double> calcPrecRec(Collection<? extends Object> yp, Collection<? extends Object> yt)
	{
		List<Double> ret    = new ArrayList<Double>();
		Set<Object>  labels = new HashSet<Object>();
		
		for(Object lab: yt)
		{
			labels.add(lab);
		}
		
		double prec = 0.0;
		double rec  = 0.0;
		int    k    = 0;
		int    C    = labels.size();
		
		if(C==2)
		{
			ConfusionMatrix conf = Statistics.calcConfusionMatrix(labels.iterator().next(), yp, yt);
			double prec_ = Statistics.calcPrecision(conf);
			double rec_  = Statistics.calcSensitivity(conf);
			
			ret.add(prec_);
			ret.add(rec_);
		}
		else
		{
			// average over all labels ..
			for(Object lab: labels)
			{
				ConfusionMatrix conf = Statistics.calcConfusionMatrix(lab, yp, yt);
				double prec_ = Statistics.calcPrecision(conf);
				double rec_  = Statistics.calcSensitivity(conf);
				prec += prec_;
				rec  += rec_; 
				k++;
			}

			ret.add(prec/k);
			ret.add(rec/k);
		}
		
		return ret;
	}
	
	/** 
	 * computes area under the ROC. 
	 * 
	 * Scores/predictions are assumed to be higher for positive cases. 
	 * 
	 * @param ypPos : the scalar prediction for positive labeled cases
	 * @param ypNeg : the scalar prediction for negative labeled cases
	 * 
	 * @return value of auROC
	 */
	public static double auROC(Collection<? extends Comparable> ypPos, Collection<? extends Comparable> ypNeg)
	{
		// positives scores should be the greater ones 
		double auROC = 0.0;
		int    N     = 0;
		for(Comparable cPos: ypPos)
		{
			for(Comparable cNeg: ypNeg)
			{
				N+=1;
				int cmp = cPos.compareTo(cNeg);
				if(cmp>0)
				{
					auROC+=1.0;
				}
				if(cmp==0)
				{
					auROC+=0.5;
				}
			}
		}
		return auROC/N;
	}
	

	/** 
	 * computes FMeasure based upon predicted/true values  
	 *  
	 * @param yp : predicted values
	 * @param Yt : true values
	 * 
	 * @return F-measure
	 * 
	 */
	public static double calcFMeasure(Collection<? extends Object> yp, Collection<? extends Object> yt)
	{
		double F = 0.0;
		List<Double> precrec = calcPrecRec(yp,yt);
		double prec = precrec.get(0);
		double rec  = precrec.get(1);
		if((prec+rec)>0)
			F = 2.0*(prec*rec)/(prec+rec);
		else
			F = 0.0;
		return F;
	}

	public static <T extends Comparable<T>> List< Pair<Integer,T> > sort(Collection<T> toSort, boolean ascending)
	{
		List< Pair<Integer,T> > data = new ArrayList< Pair<Integer,T> >();
		int i=0;
		for(T t: toSort)
		{
			data.add( new Pair<Integer,T>(i,t) );
			i++;
		}
		final boolean asc = ascending; 
		Comparator<Pair<Integer,T> > cmp = new Comparator<Pair<Integer,T> >(){

			@Override
			public int compare(Pair<Integer, T> x, Pair<Integer, T> y)
			{
				if(asc)
					return x.getSecond().compareTo(y.getSecond());
				else
					return -x.getSecond().compareTo(y.getSecond());
			}

		};

		Collections.sort(data,cmp);
		return data;
	}

}
