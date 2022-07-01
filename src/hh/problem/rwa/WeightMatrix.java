package hh.problem.rwa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;






public class WeightMatrix {

	
	public static final int NUM_OBJ = 3;
	public static final int NUM_VECS = 10;
	
	public static double[][] getWrtVal(){
		double[][] weight = new double[NUM_VECS][NUM_OBJ];
		
		 String dataFileName;
		 
	     dataFileName = "W" + NUM_OBJ + "D_"+ NUM_VECS + ".dat";
	     try {
	            // Open the file
	            FileInputStream fis = new FileInputStream("weight" + File.separator
	                    + dataFileName);
	            InputStreamReader isr = new InputStreamReader(fis);
	            BufferedReader br = new BufferedReader(isr);

	            int j = 0;int k=0;
	            String aux = br.readLine();
	            while (aux != null) {
	                StringTokenizer st = new StringTokenizer(aux);
	                j = 0;
	                double[] wts = new double[NUM_OBJ];
	                while (st.hasMoreTokens()) {
	                    double value = (new Double(st.nextToken())).doubleValue();
	                    wts[j] = value;
	                    j++;
	                }

	                weight[k] = wts;
	                k++;
	                aux = br.readLine();
	            }
	            br.close();
	            
	        } catch (IOException | NumberFormatException e) {
	            System.out
	                    .println("initUniformWeight: failed when reading for file: "
	                            + "weight" + File.separator + dataFileName);
	            
	        }
	     
	     return weight;
	}

	public WeightMatrix(String filename){
		network = new Network1(filename);
	}
		
	//private static Network network = new Network();
	private static Network1 network;
	public static List<double[][]> getWrtMat(){
		double[][] weight = getWrtVal();
		List<double[][]> wrtMat = new ArrayList<>();
		double[][] res; 
		double[][] avbwn = network.getAvailbleBandwithNorm();
		double[][] delayn = network.getDealyNorm();
		double[][] pln = network.getPLNorm();
		int len=avbwn.length;
		double avw;
		double dew;
		double plw;
		for(int i=0;i<NUM_VECS;i++){
			avw = weight[i][0];
			dew = weight[i][1];
			plw = weight[i][2];
			res = new double[len][len];
			for(int j=0;j<len;j++){
				for(int k=0;k<len;k++){
					res[j][k] = avbwn[j][k]*avw + delayn[j][k]*dew + pln[j][k]*plw;
				}
			}
			wrtMat.add(res);
		}
		return wrtMat;
	}
}
