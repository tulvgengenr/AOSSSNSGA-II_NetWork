package hh.problem.rwa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.moeaframework.core.PRNG;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.problem.AbstractProblem;
import org.moeaframework.problem.AnalyticalProblem;

import jmetal.encodings.variable.Int;

public class RWA extends AbstractProblem implements AnalyticalProblem,Problem{

	private static int s;
	private static int[] d;
//	private static final int s = 1;
//	public static final int[] d = {3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35,37,39,40,41,43,45,47,49,51,53,55,57,59,61,63,65,67,69};
	private ROUTES routes;
	private List<Integer[][]> routeTables;
	private  int[][] NETWORK = Network1.getNetwork();
	private  int[][] AvailbleBandwith=Network1.getAvailbleBandwith();
	private  double[][] delay = Network1.getDelay();
	private  double[][] PL = Network1.getPL();
	private  int[] Lambda = Network1.Lambda;
	private  int[][] A = Network1.getA();
	private  int[][] B = Network1.getB();
	private  int[][] C = Network.C;
	private double[][] weight;
	private int Vnum = Network1.NodeAmount;
	private int Lnum = Network1.LinkNum;
	private static final int FAI=100;
	//private static final int BR=1155; -----nsfnet
	private static final int BR=900;
	private int DR;
	private static final double PLR=0.9;
	private static final int RequiredW=95;
	//int max=0;
	//ArrayList<int[][]> list = null;
//	public ArrayList<int[][]> getList()
//	{
//		return list;
//	}
	public static void main(String[] args) {

		for(int i=0;i<50;i++)
		{
			System.out.println(PRNG.nextInt(1, 10));
		}
	}
	public RWA(int numberOfObjectives) {
		super(d.length, numberOfObjectives);
		
	}
	
	public RWA( int numberOfObjectives,int max) {
		super(d.length, numberOfObjectives);
		//this.max=max;
	}

	public RWA( int numberOfObjectives, int max, Network1 network2){
		super(network2.d.length, numberOfObjectives);
		d = network2.d;
		s = network2.s;
		DR = network2.DR;
		routes = new ROUTES(s, d, network2.file);
		Integer[][] array = routes.ROUTES.get(0);
		routeTables = routes.getROUTES();
		weight = network2.getWeight();
	}
	//int i=0;
	@Override
	public void evaluate(Solution solution) {
		//i++;
		
		
//		for(int i=0;i<solution.getNumberOfVariables();i++){
//			System.out.print(solution.getVariable(i)+" ");
//		}
//		System.out.println();
		int len = solution.getNumberOfVariables();		
		int[] variables = new int[len];
		IntegerVariable integerVariable=null;
		for(int i=0;i<len-1;i++){
			integerVariable = (IntegerVariable)solution.getVariable(i);
			variables[i] = integerVariable.getValue();
		}
		Integer[][] temp = new Integer[len][];
		double[][] mat = new double[Vnum][Vnum];
		
		for(int i=0;i<len;i++){
			//System.out.println(variables[i]);
			temp = routeTables.get(variables[i]);
			Integer[] path  = temp[i];
			
//			System.out.println("---------------------------");
//			for(Integer p:path){
//				System.out.print(p+" ");
//			}
//			System.out.println();
//			System.out.println("---------------------------");
			
			for(int j=0;j<path.length-1;j++){
				mat[path[j]][path[j+1]]=1;
				mat[path[j+1]][path[j]]=1;
			}
		}
					
		for(int i=0;i<Vnum;i++){
			for(int j=0;j<Vnum;j++){
				mat[i][j] = mat[i][j]*weight[i][j];
			}
		}
		
//		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//		for(int i=0;i<mat.length;i++){
//			for(int j=0;j<mat[i].length;j++){
//				System.out.print(mat[i][j]+" ");
//			}
//			System.out.println();/
//		}
//		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
		int[][] matres = GraphComputation.getMST(mat, s, d);
//		if(i==max){
//			list = new ArrayList<>();
//			list.add(matres);
//		}
		
//		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//		for(int i=0;i<matres.length;i++){
//			for(int j=0;j<matres[i].length;j++){
//				System.out.print(matres[i][j]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
		List<Integer[]> sp = GraphComputation.getSP();
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		for(Integer[] l:sp){
//			for(int i=0;i<l.length;i++){
//				System.out.print(l[i]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		
		
		int[] LambdaList = new int[Lnum];
		for(int s=0;s<LambdaList.length;s++){
			LambdaList[s] = -1;
		}
		int wavelengthcost = 0;
		int biannum = 0;
		Random random = new Random();
		for(int t1=0;t1<Vnum;t1++){
			for(int t2=t1;t2<Vnum;t2++){
				if(matres[t1][t2]==1){
					biannum ++;
					if(NETWORK[t1][t2]==0)
						continue;
					int[] l = A[NETWORK[t1][t2]-1];
					/*
					int number = PRNG.nextInt(1, 10);
					int sum = 0;
					int k=0;
					for(;k<l.length;k++){
						if(Lambda[k]<RequiredW){
							number=number-l[k];
                            continue;
						}
						sum=sum+l[k];
                        if(l[k]==0)
                            continue;
                        if(sum>=number)
                                break;
					}
					*/
					int k = (random.nextInt(10));
					while(l[k]==0)
					{
						k = (random.nextInt(10));
					}
					LambdaList[NETWORK[t1][t2]-1]=k;
					wavelengthcost=wavelengthcost+B[NETWORK[t1][t2]-1][k];
				}
			}
		}
		
		
		
		double channelrateres = 0.0;
		int nn=0;
		for(int t=0;t<LambdaList.length;t++){
			if(LambdaList[t]!=-1){
				nn=nn+1;
				int channelrate=RequiredW/Lambda[LambdaList[t]];
				channelrateres=channelrateres+channelrate;
			}
		}
		 channelrateres=channelrateres/nn;
		 
		 int[][] C1= C;
		 int traversecost=0;
         double DT=0;
         double PLT=0;
         for(int r1=0;r1<sp.size();r1++){
        	 List<Integer> linkList = new ArrayList<>();
        	 Integer[] path = sp.get(r1);   
        	 double PLP = 1;
        	 double DP = 0;
        	 for(int r2=0;r2<path.length-1;r2++){
        		 linkList.add(NETWORK[path[r2]][path[r2+1]]-1);
        		 DP=DP+delay[path[r2]][path[r2+1]];
                 PLP=PLP*(1-PL[path[r2]][path[r2+1]]);
        	 }
        	 for(int r3=0;r3<linkList.size()-1;r3++){
        		 traversecost=traversecost+C1[LambdaList[linkList.get(r3)]][LambdaList[linkList.get(r3+1)]];
        		 C1[LambdaList[linkList.get(r3)]][LambdaList[linkList.get(r3+1)]]=0;
        	 }
        	 if (DP>DT) DT=DP;
        	 PLP=1-PLP;
             if(PLP>PLT) PLT=PLP;            
         }
        
         int[][] BBBT = new int[Vnum][Vnum];
         for(int i=0;i<Vnum;i++){
        	 for(int j=0;j<Vnum;j++){
        		 BBBT[i][j] = matres[i][j]*(AvailbleBandwith[i][j]-FAI);
        	 }
         }
         
         for(int k1=0;k1<Vnum;k1++){
        	 for(int k2=0;k2<Vnum;k2++){
        		 if(BBBT[k1][k2]==0){
        			 BBBT[k1][k2]=Integer.MAX_VALUE;
        		 }
        	 }
         }
         int BBBTminum = Integer.MAX_VALUE;
         for(int k1=0;k1<Vnum;k1++){
        	 for(int k2=0;k2<Vnum;k2++){
        		 if(BBBT[k1][k2]<BBBTminum){
        			 BBBTminum = BBBT[k1][k2];
        		 }
        	 }
         }
         int wavesplitcost=biannum*10;
         int totalwavecost=wavelengthcost+traversecost+wavesplitcost;
         double tB,tD,tPL;
         if(BR>BBBTminum){
             tB=0.1;
         }else {
			tB = 0;
		}
         if(DR<DT){
             tD=0.1;
         }else {
			tD = 0;
		}
         if(PLR<PLT){
             tPL=0.1;
         }else {
			tPL = 0;
		}
           
         
      
         double[] f = new double[numberOfObjectives];
//         f[0] = (1+tB+tD+tPL)*DT;
//         f[1] = (1+tB+tD+tPL)*totalwavecost;
//         f[2] = (1+tB+tD+tPL)*(1-channelrateres);
         
         f[0] = (1+tB+tD+tPL)*DT;
         f[1] = (1+tB+tD+tPL)*totalwavecost;
         f[2] = (1-channelrateres);
         
         solution.setObjectives(f);
	}

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(numberOfVariables, numberOfObjectives, numberOfConstraints);
		for (int i = 0; i < numberOfVariables; i++) {
			solution.setVariable(i, new IntegerVariable(0, routeTables.size()-1));
		}

		return solution;
	}

	@Override
	public Solution generate() {
		Solution solution = newSolution();
		
		for (int i = 0; i < numberOfVariables; i++) {
			((IntegerVariable)solution.getVariable(i)).setValue(PRNG.nextInt(0, routeTables.size()-1));
		}

		evaluate(solution);

		return solution;
	}

}
