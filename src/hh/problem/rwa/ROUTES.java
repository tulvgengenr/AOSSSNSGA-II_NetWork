package hh.problem.rwa;

import java.util.ArrayList;
import java.util.List;


public class ROUTES {
	public List<Integer[][]> ROUTES = new ArrayList<>();
	private String filepath;
	//private GraphCompute graphCompute = new GraphCompute();

	private List<double[][]> wlist;
	
	public void BulidRoutes(int s,int[] d){
		int len = d.length;
		Integer[][] array =null;
		List<Integer[]> list = new ArrayList<>();;
		for(double[][] w:wlist){
			list.clear();
			GraphComputation.getMST(w, s, d);
			list = GraphComputation.getSP();
			int index= 0;
			 array = new Integer[len][];
			for(Integer[] i:list){
				array[index]=i;
				index++;
			}
			ROUTES.add(array);
		}
	}
	
	public List<Integer[][]> getROUTES() {
		return ROUTES;
	}
	
	public ROUTES(int s,int[] d,String filename) {
		filepath = filename;
		WeightMatrix w = new WeightMatrix(filepath);
		wlist = w.getWrtMat();
		BulidRoutes(s, d);
	}

	public static void main(String[] args) {

	}
}
