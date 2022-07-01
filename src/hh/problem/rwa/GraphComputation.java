package hh.problem.rwa;

import java.util.ArrayList;
import java.util.List;

public class GraphComputation {
	public static List<Integer[]> SP = new ArrayList<>();
	public static List<Integer[]> getSP() {
		return SP;
	}
	public static int[][] getMST(double[][] mat,int s,int[] d){
	
		int len =d.length;
		int num = mat.length;
		int[][] res = new int[num][num];
		for(int i=0;i<num;i++){
			for(int j=0;j<num;j++){
				if(i!=j&&mat[i][j]==0){
					mat[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~");
//		for(int i=0;i<mat.length;i++){
//			for(int j=0;j<mat[i].length;j++){
//				System.out.print(mat[i][j]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		double[][] weights = new double[num][3];
		for(int i=0;i<num;i++){
			weights[i][0] = s;
			weights[i][1] = mat[s][i];
			weights[i][2] = i;
		}
		
		weights[s][1] = 0;
		
		 for (int i = 0; i < num; i++) {
	        	//System.out.println("~~~~~~~~~~~~~~~~~  :  "+i);
	            // 由于从start开始的，因此不需要再对第start个顶点进行处理。
	            if(s == i)
	                continue;
	            int j = 0;
	            int k = 0;
	            double min = Double.POSITIVE_INFINITY;
	            
	            while (j < num) {
	                // 若weights[j]=0，意味着"第j个节点已经被排序过"(或者说已经加入了最小生成树中)。
	                if (weights[j][1] != 0 && weights[j][1] < min) {
	                    min = weights[j][1];
	                    k = j;
	                }
	                j++;
	            }
	            //System.out.println("k: "+k);
	            weights[k][1] = 0;
	 
	            for (j = 0 ; j < num; j++) {
	                // 当第j个节点没有被处理，并且需要更新时才被更新。
	                if (weights[j][1] != 0 && mat[k][j] < weights[j][1])
	                    {
	                		weights[j][1] = mat[k][j];
	                		weights[j][0] = k;
	                		weights[j][2] = j;
	                    }
	            }
	        }
		 
//		 System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//		 for(int i=0;i<weights.length;i++){
//			 for(int j=0;j<weights[i].length;j++){
//				 System.out.print(weights[i][j]+" ");
//			 }
//			 System.out.println();
//		 }
//		 System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		 double[][] matrix = new double[num][num];
		 for(int i=0;i<weights.length;i++){
			 matrix[(int)weights[i][0]][(int)weights[i][2]] = mat[(int)weights[i][0]][(int)weights[i][2]];
			 matrix[(int)weights[i][2]][(int)weights[i][0]] = mat[(int)weights[i][0]][(int)weights[i][2]];
		 }
		 
//		 System.out.println("####################################");
//			for(int i=0;i<matrix.length;i++){
//				for(int j=0;j<matrix[i].length;j++){
//					System.out.print(matrix[i][j]+" ");
//				}
//				System.out.println();
//			}
//			System.out.println("######################");
//		 
		 
		 int[][] resultmat = new int[num][num];
		 SP.clear();
		 for(int i=0;i<d.length;i++){
			 List<Integer> list = getShortestPath(matrix, s, d[i]);
			 Object[] array = list.toArray();
			 Integer[] array2 = new Integer[array.length];
			 for(int r=0;r<array.length;r++){
				 array2[r] = (Integer)array[r];
			 }
			 SP.add(array2);
			 for(int j=0;j<list.size()-1;j++){
				 resultmat[list.get(j)][list.get(j+1)] = 1;
				 resultmat[list.get(j+1)][list.get(j)] = 1;
			 }
		 }
		 
		 return resultmat;
	}
	
	public static List<Integer> getShortestPath(double[][] mat,int s,int d){
		int vexNum = mat.length;
//		int[] farthestPrevHop = new int[vexNum];
//		int[] farthestNextHop = new int[vexNum];
//		for(int i=0;i<vexNum;i++){
//			farthestPrevHop[i]=i;
//			farthestNextHop[i]=i;
//		}
		
//		System.out.println("------------------------");
		List<Integer> shortestPath = new ArrayList<>();
		for(int i=0;i<mat.length;i++){
			for(int j=0;j<mat[i].length;j++){
				if(i!=j&&mat[i][j]==0){
					mat[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
		boolean[] flag = new boolean[vexNum];
        int[] prev = new int[vexNum];
        double[] dist = new double[vexNum];
        
        for (int i = 0; i < vexNum; i++) {
            flag[i] = false;          // 顶点i的最短路径还没获取到。
            prev[i] = s;              // 顶点i的前驱顶点为0。
            dist[i] = mat[s][i];  // 顶点i的最短路径为"顶点vs"到"顶点i"的权。
        }
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~");
        flag[s] = true;
        dist[s] = 0;
        
        int k=0;
        for (int i = 1; i < vexNum; i++) {
        	
        	double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < vexNum; j++) {
                if (flag[j]==false && dist[j]<min) {
                    min = dist[j];
                    k = j;
                }
            }
//            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            flag[k] = true;
            
            for (int j = 0; j < vexNum; j++) {
                double tmp = (mat[k][j]==Double.POSITIVE_INFINITY ? Double.POSITIVE_INFINITY : (min + mat[k][j]));
                if (flag[j]==false && (tmp<dist[j]) ) {
                    dist[j] = tmp;
                    prev[j] = k;
                }
            }
        	
//            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$");	
        }
        
        if(prev[d]!=0){
//        	 System.out.println("######################");
        	int t = d;
        	shortestPath.add(d);
        	while(t!=s){
//        		 System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^");
        		int p = prev[t];
        		shortestPath.add(0, p);

//	            if(mat[t][farthestPrevHop[t]]<mat[t][p]){
//	            	farthestPrevHop[t] = p;
//	            }
//	            if(mat[p][farthestNextHop[p]]<mat[p][t]){
//	            	farthestNextHop[p] = t;
//	            }
//            	
            	t=p;
        	}
        }
//        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//        for(int i=0;i<prev.length;i++){
//        	System.out.print(prev[i]+" ");
//        }
        
//        System.out.println("\n@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		return shortestPath;
	}
}
