package hh.problem.rwa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class Network1 {
	public static int NodeAmount; //14,28,40,70,100
	
	public static int LinkNum; //21,41,153,280,603
	
	public static int[][] Network;
	
	public static int[][] AvailbleBandwith;
	
	public static double[][] delay;
	
	public static double[][] PL;
	
	public static int[][] A;
	
	public static int[][] B;
	
	public static final int[][] C ={{0,34,20,78,109,105,32,74,62,114},
            {34,0,107,105,119,41,40,64,40,93},
            {20,107,0,94,97,75,34,32,115,94},
            {78,105,94,0,78,83,39,69,28,26},
            {109,119,97,78,0,23,24,106,30,106},
            {105,41,75,83,23,0,84,108,34,114},
            {32,40,34,39,24,84,0,47,36,119},
            {74,64,32,69,106,108,47,0,82,106},
            {62,40,115,28,30,34,36,82,0,99},
            {114,93,94,26,106,114,119,106,99,0}}; 
	
	public static final int[] Lambda={85,90,95,100,105,110,115,120,125,130};

	public String file;

	public int s;
	public int[] d;
	public int DR;
	public Network1(){
		getNetworkAndParamMat();
		Weight = new double[Network.length][Network.length];
		getNorm();
		BulidWeight();
	}
	public Network1(String filepath){
		file = filepath;
		ReadSAndD(filepath+"\\sd.txt");
		getNetworkAndParamMat();
		Weight = new double[Network.length][Network.length];
		getNorm();
		BulidWeight();
	}

	private void ReadSAndD(String filename){
		try {
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			String line="";
			line = br.readLine();
			String[] str1 = line.split(",");
			NodeAmount = Integer.parseInt(str1[0]);
			LinkNum = Integer.parseInt(str1[1]);

			s = Integer.parseInt(br.readLine());
			line = br.readLine();
			String[] str = line.split(",");
			d = new int[str.length];
			for(int j=0;j<str.length;j++){
				d[j] = Integer.parseInt(str[j]);
			}
			DR = Integer.parseInt(br.readLine());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static double[][] ReadFileForDouble(String filename){
		double[][] result = new double[NodeAmount][NodeAmount];
		try {
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			
			String line ="";
			int i=0;
			String[] str;
			while((line=br.readLine())!=null){
				str = line.split(",");
				for(int j=0;j<str.length;j++){
					result[i][j] = Double.parseDouble(str[j]);
				}
				i++;
			}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	private static int[][] ReadFileForInteger(String filename){
		int[][] result = new int[NodeAmount][NodeAmount];
		try {
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			
			String line ="";
			int i=0;
			String[] str;
			while((line=br.readLine())!=null){
				str = line.split(",");
				for(int j=0;j<str.length;j++){
					//System.out.println(str[j]);
					result[i][j] = Integer.parseInt(str[j]);
				}
				i++;
			}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		return result;
	}
	
	private static int[][] ReadFileA(String filename){
		int[][] result = new int[LinkNum][Lambda.length];
		try {
			FileInputStream fis = new FileInputStream(filename);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
			
			String line ="";
			int i=0;
			String[] str;
			while((line=br.readLine())!=null){
				str = line.split(",");
				for(int j=0;j<Lambda.length;j++){
					//System.out.println(str[j]);
					result[i][j] = Integer.parseInt(str[j]);
				}
				i++;
			}
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		Network1 network1 = new Network1("data\\Test4");
		System.out.println(Network1.LinkNum);
		System.out.println(Network1.NodeAmount);
		System.out.println(network1.s);
		for(int i=0;i<network1.d.length;i++)
			System.out.println(network1.d[i]);
	}
   
	private void getNetworkAndParamMat(){
		String networkfile = file+"\\network.txt";
		String bwfile = file+"\\bandwith.txt";
		String delayfile = file+"\\delay.txt";
		String plfile = file+"\\packetloss.txt";
		String Afile = file+"\\A.txt";
		String Bfile = file+"\\B.txt";
		Network = ReadFileForInteger(networkfile);
		AvailbleBandwith = ReadFileForInteger(bwfile);
		delay = ReadFileForDouble(delayfile);
		PL = ReadFileForDouble(plfile);
		A = ReadFileA(Afile);
		B = ReadFileA(Bfile);
	}
	
	public double[] findMax_Min(double[][] m){
		double[] max_min=new double[2];
 		double max = m[0][0];
		double min = m[0][0];
		for (int i=0;i<m.length;i++){
			for(int j=0;j<m[i].length;j++){
				if(m[i][j]>max){
					max = m[i][j];
				}
				if(m[i][j]<min){
					min = m[i][j];
				}
			}
		}
		max_min[0]=max;
		max_min[1]=min;
		return max_min;
	}
	
	public int[] findMax_Min(int[][] m){
		int[] max_min=new int[2];
 		int max = m[0][0];
		int min = m[0][0];
		for (int i=0;i<m.length;i++){
			for(int j=0;j<m[i].length;j++){
				if(m[i][j]>max){
					max = m[i][j];
				}
				if(m[i][j]<min){
					min = m[i][j];
				}
			}
		}
		max_min[0]=max;
		max_min[1]=min;
		return max_min;
	}


	public double[][] normalization(double[][] m){
		double[] max_min = findMax_Min(m);
		double[][] result = new double[m.length][m.length];
		for(int i=0;i<m.length;i++){
			for(int j=0;j<m[i].length;j++){
				result[i][j] = (m[i][j]-max_min[1])/(max_min[0]-max_min[1]);
			}
		}
		return result;
	}
	
	public double[][] normalization(int[][] m){
		int[] max_min = findMax_Min(m);
		double[][] result = new double[m.length][m.length];
		for(int i=0;i<m.length;i++){
			for(int j=0;j<m[i].length;j++){
				result[i][j] = ((double)m[i][j]-max_min[1])/(max_min[0]-max_min[1]);
			}
		}
		return result;
	}

	public double[][] AvailbleBandwithNorm = null;
	public double[][] DealyNorm = null;
	public double[][] PLNorm = null;
	public double[][] Weight = null;
	public void getNorm(){
		AvailbleBandwithNorm=normalization(AvailbleBandwith);
		DealyNorm = normalization(delay);
		PLNorm = normalization(PL);
	}
	
	public void BulidWeight(){
		
		for(int i=0;i<AvailbleBandwithNorm.length;i++){
			for(int j=0;j<AvailbleBandwithNorm[i].length;j++){
				Weight[i][j] = AvailbleBandwithNorm[i][j]+DealyNorm[i][j]+PLNorm[i][j];
			}
		}
	}
	
	public static int[][] getNetwork() {
		return Network;
	}
	public static int[][] getAvailbleBandwith() {
		return AvailbleBandwith;
	}
	public static double[][] getDelay() {
		return delay;
	}
	public static double[][] getPL() {
		return PL;
	}
	public static int[][] getA() {
		return A;
	}
	public static int[][] getB() {
		return B;
	}
	
	public double[][] getAvailbleBandwithNorm() {
		return AvailbleBandwithNorm;
	}
	
	public double[][] getDealyNorm() {
		return DealyNorm;
	}
	
	public double[][] getPLNorm() {
		return PLNorm;
	}
	public double[][] getWeight() {
		return Weight;
	}
	
	
}

        
        
         
         


        
