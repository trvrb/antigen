import java.io.*;
import java.util.*;
import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import cern.colt.matrix.linalg.*;

public class SimplePCA {

    public static void main(String[] args) {
    
  		double[][] input = {{2.5,2.4},{0.5,0.7},{2.2,2.9},{1.9,2.2},{3.1,3},{2.3,2.7},{2,1.6},{1,1.1},{1.5,1.6},{1.1,0.9}};
    	
		cern.jet.math.Functions F = cern.jet.math.Functions.functions; 
		cern.colt.matrix.linalg.Algebra alg = new cern.colt.matrix.linalg.Algebra();
			
		DoubleMatrix2D values = new DenseDoubleMatrix2D(input);
		int n = values.rows();
		int m = values.columns();
		
		System.out.println("Original data:");
		print(values);
		
		// calculate mean for each dimension
		double[] means = new double[m];
		for (int j = 0; j < m; j++) {
			means[j] = (values.viewColumn(j)).zSum() / (double) n;
		}
		
		// substract out the mean for each dimension
		DoubleMatrix2D centered = new DenseDoubleMatrix2D(values.toArray());
		for (int j = 0; j < m; j++) {
			(centered.viewColumn(j)).assign(F.minus(means[j]));
		}
		
		System.out.println("Center transformed:");
		print(centered);
		
		// find covariance matrix
		DoubleMatrix2D cov = alg.mult(centered.viewDice(), centered);
		double denom = 1 / ((double) n - 1);
		cov.assign(F.mult(denom));
		
		System.out.println("Covariance matrix:");
		print(cov);
		
		// find the Eigen decomposition of the covariance matrix
		EigenvalueDecomposition eigenSystem = new EigenvalueDecomposition(cov);
		
		DoubleMatrix1D eigenValues = eigenSystem.getRealEigenvalues();
		
		System.out.println("Eigenvalues:");
		print(eigenValues);
		
		DoubleMatrix2D eigenVectors = eigenSystem.getV();
		
		System.out.println("Eigenvectors:");
		print(eigenVectors);		
		
		// indices of top eigenvalues
		int firstIndex = indexOfMaximum(eigenValues);
		double firstValue = eigenValues.get(firstIndex);
		int secondIndex = indexOfNextToMaximum(eigenValues);
		double secondValue = eigenValues.get(secondIndex);
		
		System.out.println("Top eigenvalues:");
		System.out.println(firstIndex + " " + secondIndex);	
		System.out.println();
		
		// constructing feature vectors from ordered eigenvectors		
		
		DoubleMatrix1D firstVector = eigenVectors.viewColumn(firstIndex);
		
		System.out.println("First eigenvector:");
		print(firstVector);
		
		DoubleMatrix1D secondVector = eigenVectors.viewColumn(secondIndex);
		
		System.out.println("Second eigenvector:");
		print(secondVector);		
		
		DoubleMatrix2D featureVectors = new DenseDoubleMatrix2D(m,m);
		(featureVectors.viewColumn(0)).assign(firstVector);	
		(featureVectors.viewColumn(1)).assign(secondVector);			
		
		System.out.println("Feature vectors:");
		print(featureVectors);				
		
		// projecting original data onto new coordinate system
		
		DoubleMatrix2D projected = (alg.mult(featureVectors.viewDice(),centered.viewDice())).viewDice(); 
	
		System.out.println("Projected data:");	
		print(projected);
    
    }
	
	public static double[][] project(double[][] input) {
	
		cern.jet.math.Functions F = cern.jet.math.Functions.functions; 
		cern.colt.matrix.linalg.Algebra alg = new cern.colt.matrix.linalg.Algebra();
			
		DoubleMatrix2D values = new DenseDoubleMatrix2D(input);
		int n = values.rows();
		int m = values.columns();
				
		// calculate mean for each dimension
		double[] means = new double[m];
		for (int j = 0; j < m; j++) {
			means[j] = (values.viewColumn(j)).zSum() / (double) n;
		}
		
		// substract out the mean for each dimension
		DoubleMatrix2D centered = new DenseDoubleMatrix2D(values.toArray());
		for (int j = 0; j < m; j++) {
			(centered.viewColumn(j)).assign(F.minus(means[j]));
		}
				
		// find covariance matrix
		DoubleMatrix2D cov = alg.mult(centered.viewDice(), centered);
		double denom = 1 / ((double) n - 1);
		cov.assign(F.mult(denom));
				
		// find the Eigen decomposition of the covariance matrix
		EigenvalueDecomposition eigenSystem = new EigenvalueDecomposition(cov);		
		DoubleMatrix1D eigenValues = eigenSystem.getRealEigenvalues();		
		DoubleMatrix2D eigenVectors = eigenSystem.getV();
				
		// indices of top eigenvalues
		int firstIndex = indexOfMaximum(eigenValues);
		double firstValue = eigenValues.get(firstIndex);
		int secondIndex = indexOfNextToMaximum(eigenValues);
		double secondValue = eigenValues.get(secondIndex);
		
		// constructing feature vectors from ordered eigenvectors

		DoubleMatrix1D firstVector = eigenVectors.viewColumn(firstIndex);
		DoubleMatrix1D secondVector = eigenVectors.viewColumn(secondIndex);	
		DoubleMatrix2D featureVectors = new DenseDoubleMatrix2D(m,m);
		(featureVectors.viewColumn(0)).assign(firstVector);	
		(featureVectors.viewColumn(1)).assign(secondVector);			
		
		// projecting original data onto new coordinate system	
		
		DoubleMatrix2D projected = (alg.mult(featureVectors.viewDice(),centered.viewDice())).viewDice(); 
	
		return projected.toArray();
			
	}	
	
	public static void print(DoubleMatrix2D values) {
		int m = values.rows();
		int n = values.columns();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				System.out.printf("%.4f ", values.get(i,j));
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void print(DoubleMatrix1D values) {
		for (int i = 0; i < values.size(); i++) {
			System.out.printf("%.4f ", values.get(i));
		}
		System.out.println();
		System.out.println();
	}	
	
	public static int indexOfMaximum(DoubleMatrix1D values) {
		int index = 0;
		double max = 0;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) > max) {
				max = values.get(i);
				index = i;
			}
		}
		return index;
	}
	
	public static int indexOfNextToMaximum(DoubleMatrix1D values) {
		int index = 0;
		double max = 0;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) > max) {
				max = values.get(i);
			}
		}
		double nextToMax = 0;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) > nextToMax && values.get(i) < max) {
				nextToMax = values.get(i);
				index = i;
			}
		}
		return index;
	}	
	
}