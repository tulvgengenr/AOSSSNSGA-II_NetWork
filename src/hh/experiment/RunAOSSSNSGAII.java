package hh.experiment;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.MOEAD;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.ReferencePointNondominatedSortingPopulation;
import org.moeaframework.algorithm.SPEA2;
import org.moeaframework.analysis.collector.InstrumentedAlgorithm;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.PopulationIO;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.operator.GAVariation;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.util.TypedProperties;

import hh.IO.IOCreditHistory;
import hh.IO.IOQualityHistory;
import hh.IO.IOSelectionHistory;
import hh.creditassigment.CreditDefFactory;
import hh.creditassigment.ICreditAssignment;
import hh.hyperheuristics.AOSNSGAII;
import hh.hyperheuristics.AOSSSNSGAII;
import hh.hyperheuristics.HHFactory;
import hh.hyperheuristics.IHyperHeuristic;
import hh.moea.SteadyStateNSGAII;
import hh.nextheuristic.INextHeuristic;
import hh.problem.rwa.Network1;
import hh.problem.rwa.RWA;
import jmetal.core.Problem;
import jmetal.encodings.variable.Int;


public class RunAOSSSNSGAII {
    public static void writeRuntime(long x,String path){
        try{
            File file = new File(path);
            FileOutputStream fos = null;
            if(!file.exists()){
                file.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(file);//首次写入获取
            }else{
                //如果文件已存在，那么就在文件末尾追加写入
                fos = new FileOutputStream(file,true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
            }

            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//指定以UTF-8格式写入文件

            osw.write(Long.toString(x));
            osw.write('\n');

            osw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearInfoForFile(String fileName) {
        File file =new File(fileName);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){

        int populationSize = 120;
        int maxEvaluations = 10000;
        double Gnodes=Network1.NodeAmount;
        //int maxEvaluations =(int)(Gnodes*Math.pow(10, Glinks/Gnodes)*Gper*Math.pow(10, 2));
        //int maxEvaluations = 1;
        Properties properties = new Properties();
        properties.put("populationSize", populationSize);
        properties.put("saveFolder", "results2");
        properties.put("saveIndicators", "true");
        properties.put("saveFinalPopulation", "true");
        properties.put("saveOperatorCreditHistory", "true");
        properties.put("saveOperatorSelectionHistory", "true");
        properties.put("saveOperatorQualityHistory", "true");


        int neighborhoodSize = 20;

        double delta = 0.8;

        double eta = 2.0;

        int updateUtility = 30;

        TypedProperties prop = new TypedProperties(properties);

        String[] problems = new String[12];
        problems[0] = "data\\Test1";
        problems[1] = "data\\Test2";
        problems[2] = "data\\Test3";
        problems[3] = "data\\Test4";
        problems[4] = "data\\Test5";
        problems[5] = "data\\Test6";
        problems[6] = "data\\Test7";
        problems[7] = "data\\Test8";
        problems[8] = "data\\Test9";
        problems[9] = "data\\Test10";
        problems[10] = "data\\Test11";
        problems[11] = "data\\Test12";

        OperatorFactory of = OperatorFactory.getInstance();
        Initialization initialization ;
        NondominatedSortingPopulation population ;
        NondominatedSortingPopulation population1 ;
        TournamentSelection selection = new TournamentSelection(2,
                new ChainedComparator(new ParetoDominanceComparator(),
                        new CrowdingComparator()));
        EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.5);

//        for(int i=0;i<12;i++){
//        	String s1 = "result2\\r"+(i+1)+"\\runtime\\nsga2.txt";
//        	String s2 = "result2\\r"+(i+1)+"\\runtime\\nsga3.txt";
//        	String s3 = "result2\\r"+(i+1)+"\\runtime\\moead.txt";
//        	String s4 = "result2\\r"+(i+1)+"\\runtime\\ssnsga2.txt";
//        	String s5 = "result2\\r"+(i+1)+"\\runtime\\spea2.txt";
//			String s6 = "result2\\r"+(i+1)+"\\runtime\\aosnsga2.txt";
//			String s7 = "result2\\r"+(i+1)+"\\runtime\\aosssnsga2.txt";
//			String s8 = "result2\\r"+(i+1)+"\\runtime\\aosnsga3.txt";
//			clearInfoForFile(s1);
//			clearInfoForFile(s2);
//			clearInfoForFile(s3);
//			clearInfoForFile(s4);
//			clearInfoForFile(s5);
//			clearInfoForFile(s6);
//			clearInfoForFile(s7);
//			clearInfoForFile(s8);
//		}
//        按次数清空runtime
//        for(int j=0;j<10;j++){
//            String s1 = "result3\\num"+(j+1)+"\\runtime\\nsga2.txt";
//            String s2 = "result3\\num"+(j+1)+"\\runtime\\nsga3.txt";
//            String s3 = "result3\\num"+(j+1)+"\\runtime\\moead.txt";
//            String s4 = "result3\\num"+(j+1)+"\\runtime\\ssnsga2.txt";
//            String s5 = "result3\\num"+(j+1)+"\\runtime\\spea2.txt";
//            String s6 = "result3\\num"+(j+1)+"\\runtime\\aosnsga2.txt";
//            String s7 = "result3\\num"+(j+1)+"\\runtime\\aosssnsga2.txt";
//            String s8 = "result3\\num"+(j+1)+"\\runtime\\aosnsga3.txt";
//            clearInfoForFile(s1);
//            clearInfoForFile(s2);
//            clearInfoForFile(s3);
//            clearInfoForFile(s4);
//            clearInfoForFile(s5);
//            clearInfoForFile(s6);
//            clearInfoForFile(s7);
//            clearInfoForFile(s8);
//        }
        for(int j=0;j<1;j++){
            for(int i=0;i<12;i++) {
//				AOSSSNSGAII aosssnsgaiii;
                AOSNSGAII aosnsgaiii;
                AOSSSNSGAII aosssnsgaii;
                AOSNSGAII aosnsgaii;
                NSGAII nsgaiii;
                NSGAII nsgaii;
                MOEAD moead;
                SPEA2 spea2;
                SteadyStateNSGAII ssnsga2;

                Network1 network = new Network1(problems[i]);
                RWA problem = new RWA(3, maxEvaluations,network);
                GAVariation ga1 = new GAVariation(of.getVariation("1x", prop, problem), of.getVariation("umIe", prop, problem));

//				String directory = "result2\\r"+(i+1);
                String directory = "result3\\num"+(j+1);
                try {
                    initialization = new RandomInitialization(problem,populationSize);
                    population = new NondominatedSortingPopulation();
                    population1 = new ReferencePointNondominatedSortingPopulation(3, 2,1);



                    /**
                     * aosnsgaii补充部分start
                     */
                    String CredDefstr=/*"SIDoPF";*//*"CSDoPF"*/"OPDo";
                    properties.put("CredDef", CredDefstr);
                    String selectorStr = "AP";
                    //String selectorStr = "PM";
                    properties.put("HH", selectorStr);
                    ArrayList<Variation> heuristics = new ArrayList<>();//4种组合里面选择
                    heuristics.add(of.getVariation("1x+umIe", prop, problem));
//          		heuristics.add(of.getVariation("2x+umIe", prop, problem));
                    heuristics.add(of.getVariation("ux+umIe", prop, problem));
//          		heuristics.add(of.getVariation("umIe", prop, problem));
//          		heuristics.add(of.getVariation("umIe+pmIe", prop, problem));
                    heuristics.add(of.getVariation("sbxIe+pmIe", prop, problem));
//          		heuristics.add(of.getVariation("pcxIe+pmIe", prop, problem));
//          		heuristics.add(of.getVariation("undxIe+pmIe", prop, problem))
                    heuristics.add(of.getVariation("spxIe+pmIe", prop, problem));
//          		heuristics.add(of.getVariation("rmIe", prop, problem));
//          		heuristics.add(of.getVariation("deIe", prop, problem));
//          		heuristics.add(of.getVariation("deIe2", prop, problem));
//          		heuristics.add(of.getVariation("deIe3", prop, problem));
//          		heuristics.add(of.getVariation("deIe4", prop, problem))
                    ICreditAssignment creditDef = CreditDefFactory.getInstance().getCreditDef(prop.getString("CredDef", null), prop, problem);
                    INextHeuristic selector = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics);
//					aosssnsgaiii = new AOSSSNSGAII(problem,population1,archive,selection,initialization,);
//					aosnsgaiii = new AOSSSNSGAII(problem,population1,archive,selection,initialization);
//					aosssnsgaii = new AOSSSNSGAII(problem,population,archive,selection,initialization)
                    aosnsgaii = new AOSNSGAII(problem,population,archive,selection,initialization,selector,creditDef);
                    InstrumentedAlgorithm instAlgorithnmaosnsgaii = instrument(aosnsgaii,maxEvaluations,problem);
                    /**
                     * aosnsgaii补充部分end
                     */



                    /**
                     * aosssnsgaii补充部分start
                     */
                    aosssnsgaii = new AOSSSNSGAII(problem,population,archive,selection,initialization,selector,creditDef,directory+"\\op_all.txt");
                    InstrumentedAlgorithm instAlgorithmaosssnsgaii = instrument(aosssnsgaii,maxEvaluations,problem);
                    /**
                     * aosssnsgaii补充部分end
                     */
                    /**
                     * aosnsgaiii补充部分start
                     */
                    aosnsgaiii = new AOSNSGAII(problem,population1,archive,selection,initialization,selector,creditDef);
                    InstrumentedAlgorithm instAlgorithmaosnsgaiii = instrument(aosnsgaiii,maxEvaluations,problem);
                    /**
                     * aosnsgaiii补充部分end
                     */
                    nsgaiii = new NSGAII(problem, population1, archive, selection, ga1, initialization);
                    nsgaii = new NSGAII(problem, population, archive, selection, ga1, initialization);
                    moead = new MOEAD(problem, neighborhoodSize, initialization, ga1, delta, eta, updateUtility);
                    spea2 = new SPEA2(problem, initialization, ga1, 10, 1);
                    ssnsga2 = new SteadyStateNSGAII(problem, population, archive, selection, ga1, initialization);

                    InstrumentedAlgorithm instAlgorithmnsga3op1 = instrument(nsgaiii,maxEvaluations,problem);
                    InstrumentedAlgorithm instAlgorithmnsga2op1 = instrument(nsgaii,maxEvaluations,problem);
                    InstrumentedAlgorithm instAlgorithmmoead = instrument(moead,maxEvaluations,problem);
                    InstrumentedAlgorithm instAlgorithmspea2 = instrument(spea2,maxEvaluations,problem);
                    InstrumentedAlgorithm instAlgorithmssop1 = instrument(ssnsga2,maxEvaluations,problem);


                    /**
                     * aosnasgaiii
                     */
                    System.out.println("Strating "+ aosnsgaiii.getNextHeuristicSupplier()+CredDefstr+" on"+" AOSNSGAIII "+"with pop size: "+properties.get("populationSize"));
                    long startTimeiii = System.currentTimeMillis();
                    try{
                        while(!instAlgorithmaosnsgaiii.isTerminated() && (instAlgorithmaosnsgaiii.getNumberOfEvaluations()<maxEvaluations)){
                            instAlgorithmaosnsgaiii.step();
//							if(instAlgorithnmaosnsgaii.getNumberOfEvaluations()%100 == 0){
//								System.out.println(instAlgorithnmaosnsgaii.getNumberOfEvaluations());
//							}
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    long finishTimeiii = System.currentTimeMillis();
                    String pathiii = directory + "\\runtime\\aosnsga3.txt";
                    long ti = finishTimeiii-startTimeiii;
                    writeRuntime(ti,pathiii);
                    aosnsgaiii.terminate();
//					System.out.println("Done with optimization. Execution time: " + ((finishTimeiii - startTimeiii) / 1000) + "s");
                    aosnsgaiii.setName(String.valueOf(System.nanoTime()));
                    String filenameaosnsgaiii = directory+"\\aosnsga3"+File.separator+"aosnsga3-rwa";
                    if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
                        NondominatedPopulation ndPop = instAlgorithmaosnsgaiii.getResult();
                        System.out.println(ndPop.size());
                        try {
                            PopulationIO.writeObjectives(new File(filenameaosnsgaiii + (i+1) + (j+1) + ".txt"), ndPop);
                        } catch (IOException ex) {
                            Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
//					/**
//					 * nasgaiii
//					 */
//					System.out.println("Starting nsgaiii-rwa");
//					try {
//						long t1 = System.currentTimeMillis();
//						while (!instAlgorithmnsga3op1.isTerminated() && (instAlgorithmnsga3op1.getNumberOfEvaluations() < maxEvaluations)) {
//							//System.out.println("*****");
//							instAlgorithmnsga3op1.step();
////							if (instAlgorithmnsga3op1.getNumberOfEvaluations() % 100 == 0) {
////								System.out.println(instAlgorithmnsga3op1.getNumberOfEvaluations());
////							}
//						}
//						long t2 = System.currentTimeMillis();
//						long t = t2 - t1;
//						String path = directory + "\\runtime\\nsga3.txt";
//						writeRuntime(t,path);
//
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//					nsgaiii.terminate();
//					String filenamenop0 = directory + "\\nsga3" +  File.separator + "nsga3-rwa";
//					if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//						NondominatedPopulation ndPop = instAlgorithmnsga3op1.getResult();
//						System.out.println(ndPop.size());
//						try {
//							PopulationIO.writeObjectives(new File(filenamenop0 + Integer.toString(i+1)+Integer.toString (j+1)+ ".txt"), ndPop);
//						} catch (IOException ex) {
//							Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//						}
//					}
//					/**
//					 * nasgaii
//					 */
//					System.out.println("Starting nsgaii-rwa" );
//					try {
//						long t1=System.currentTimeMillis();
//						while (!instAlgorithmnsga2op1.isTerminated() && (instAlgorithmnsga2op1.getNumberOfEvaluations() < maxEvaluations)) {
//							//System.out.println("*****");
//							instAlgorithmnsga2op1.step();
////							if (instAlgorithmnsga2op1.getNumberOfEvaluations() % 100 == 0) {
////								System.out.println(instAlgorithmnsga2op1.getNumberOfEvaluations());
////							}
//						}
//						long t2=System.currentTimeMillis();
//						long t=t2-t1;
//						String path = directory + "\\runtime\\nsga2.txt";
//						writeRuntime(t,path);
//						System.out.println("*******************nsgaii:   "+t);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//					nsgaii.terminate();
//					String filenamenop1 = directory + "\\nsga2" + File.separator + "nsga2-rwa";
//					if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//						NondominatedPopulation ndPop = instAlgorithmnsga2op1.getResult();
//						System.out.println(ndPop.size());
//						try {
//							PopulationIO.writeObjectives(new File(filenamenop1 + (i+1) + (j+1) + ".txt"), ndPop);
//						} catch (IOException ex) {
//							Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//						}
//					}
//
//					/**
//					 * moead
//					 */
//					System.out.println("Starting moead-rwa" );
//					//long startTimefrrmba = System.currentTimeMillis();
//					try {
//						long t1=System.currentTimeMillis();
//						while (!instAlgorithmmoead.isTerminated() && (instAlgorithmmoead.getNumberOfEvaluations() < maxEvaluations)) {
//							//System.out.println("*****");
//							instAlgorithmmoead.step();
////							if (instAlgorithmmoead.getNumberOfEvaluations() % 100 == 0) {
////								System.out.println(instAlgorithmmoead.getNumberOfEvaluations());
////							}
//						}
//						long t2=System.currentTimeMillis();
//						long t=t2-t1;
//						String path = directory + "\\runtime\\moead.txt";
//						writeRuntime(t,path);
//						System.out.println("*******************moead:   "+t);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//					moead.terminate();
//					String filenamem = directory + "\\moead" + File.separator  + "moead-rwa";
//					if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//						NondominatedPopulation ndPop = instAlgorithmmoead.getResult();
//						System.out.println(ndPop.size());
//						try {
//							PopulationIO.writeObjectives(new File(filenamem + (i+1) + (j+1) + ".txt"), ndPop);
//
//						} catch (IOException ex) {
//							Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//						}
//					}
//					/**
//					 * spea2
//					 */
//					System.out.println("Starting spea2-rwa" );
//					try {
//						long t1=System.currentTimeMillis();
//						while (!instAlgorithmspea2.isTerminated() && (instAlgorithmspea2.getNumberOfEvaluations() < maxEvaluations)) {
//							//System.out.println("*****");
//							instAlgorithmspea2.step();
////							if (instAlgorithmspea2.getNumberOfEvaluations() % 100 == 0) {
////								System.out.println(instAlgorithmspea2.getNumberOfEvaluations());
////							}
//						}
//						long t2=System.currentTimeMillis();
//						long t=t2-t1;
//						String path = directory + "\\runtime\\spea2.txt";
//						writeRuntime(t,path);
//						System.out.println("*******************spea2:   "+t);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//					spea2.terminate();
//					String filenames = directory + "\\spea2" + File.separator + "spea2-rwa";
//					if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//						NondominatedPopulation ndPop = instAlgorithmspea2.getResult();
//						System.out.println(ndPop.size());
//						try {
//							PopulationIO.writeObjectives(new File(filenames + (i+1) + (j+1) + ".txt"), ndPop);
//
//						} catch (IOException ex) {
//							Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//						}
//					}
//
//					/**
//					 * ssnsgaii
//					 */
//					System.out.println("Starting SSNSGA2-rwa" );
//					try {
//						long t1=System.currentTimeMillis();
//						while (!instAlgorithmssop1.isTerminated() && (instAlgorithmssop1.getNumberOfEvaluations() < maxEvaluations)) {
//							//System.out.println("*****");
//							instAlgorithmssop1.step();
//							// System.out.println("$$$$$$"+instAlgorithmssop1.getNumberOfEvaluations());
////							if (instAlgorithmssop1.getNumberOfEvaluations() % 100 == 0) {
////								System.out.println(instAlgorithmssop1.getNumberOfEvaluations());
////							}
////	                if(instAlgorithmssop1.getNumberOfEvaluations()>maxEvaluations-100){
////	                	System.out.println("###################################");
////	                	ArrayList<int[][]> l = problem.getList();
////	                	//System.out.println(l.size());
////	                	for(int j=0;j<l.size();j++){
////	                		System.out.println("**************************************");
////	                		int[][] mat=l.get(j);
////	                		for(int k1=0;k1<mat.length;k1++){
////	                			for(int k2=k1;k2<mat.length;k2++){
////	                				if(mat[k1][k2]==1){
////	                					System.out.println(k1+" "+k2);
////	                				}
////	                			}
////	                		}
////	                	}
////	                }
//						}
//						long t2=System.currentTimeMillis();
//						long t=t2-t1;
//						String path = directory + "\\runtime\\ssnsga2.txt";
//						writeRuntime(t,path);
//						System.out.println("*******************SSNSGA2:   "+t);
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//					ssnsga2.terminate();
//					String filenamessop1 = directory + "\\ssnsga2" + File.separator + "ssnsga2-rwa";
//					if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//						NondominatedPopulation ndPop = instAlgorithmssop1.getResult();
//						System.out.println(ndPop.size());
//						try {
//							PopulationIO.writeObjectives(new File(filenamessop1 + (i+1) + (j+1) + ".txt"), ndPop);
//						} catch (IOException ex) {
//							Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//						}
//					}
//					/**
//					 * aosnsgaii
//					 */
//					System.out.println("Strating "+ aosnsgaii.getNextHeuristicSupplier()+CredDefstr+" on"+" AOSNSGAII "+"with pop size: "+properties.get("populationSize"));
//					long startTime = System.currentTimeMillis();
//					try{
//						while(!instAlgorithnmaosnsgaii.isTerminated() && (instAlgorithnmaosnsgaii.getNumberOfEvaluations()<maxEvaluations)){
//							instAlgorithnmaosnsgaii.step();
////							if(instAlgorithnmaosnsgaii.getNumberOfEvaluations()%100 == 0){
////								System.out.println(instAlgorithnmaosnsgaii.getNumberOfEvaluations());
////							}
//						}
//					}catch (Exception e){
//						e.printStackTrace();
//					}
//					long finishTime = System.currentTimeMillis();
//					String path = directory + "\\runtime\\aosnsga2.txt";
//					long t = finishTime-startTime;
//					writeRuntime(t,path);
//					aosnsgaii.terminate();
//					System.out.println("Done with optimization. Execution time: " + ((finishTime - startTime) / 1000) + "s");
//					aosnsgaii.setName(String.valueOf(System.nanoTime()));
//					String filenameaosnsgaii = directory+"\\aosnsga2"+File.separator+"aosnsga2-rwa";
//					if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//						NondominatedPopulation ndPop = instAlgorithnmaosnsgaii.getResult();
//						System.out.println(ndPop.size());
//						try {
//							PopulationIO.writeObjectives(new File(filenameaosnsgaii + (i+1) + (j+1) + ".txt"), ndPop);
//						} catch (IOException ex) {
//							Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//						}
//					}
//
//					/**
//					 * aosssnsgaii
//					 */
//					System.out.println("Starting AOSSSNSGA2-rwa" );
//					long startTime_aosssnsgaii = System.currentTimeMillis();
//					try{
//						while(!instAlgorithmaosssnsgaii.isTerminated() && (instAlgorithmaosssnsgaii.getNumberOfEvaluations()<maxEvaluations)){
//							instAlgorithmaosssnsgaii.step();
////							if(instAlgorithmaosssnsgaii.getNumberOfEvaluations() % 100 ==0){
////								System.out.println(instAlgorithmaosssnsgaii.getNumberOfEvaluations());
////							}
//						}
//					}catch (Exception e){
//						e.printStackTrace();
//					}
//					long finishTime_aosssnsgaii = System.currentTimeMillis();
//					String path_aosssnsgaii = directory + "\\runtime\\aosssnsga2.txt";
//					long t_aosssnsgaii = finishTime_aosssnsgaii-startTime_aosssnsgaii;
//					writeRuntime(t_aosssnsgaii,path_aosssnsgaii);
//					aosssnsgaii.terminate();
//					System.out.println("Done with optimization. Execution time: " + ((finishTime_aosssnsgaii - startTime_aosssnsgaii) / 1000) + "s");
//					aosnsgaii.setName(String.valueOf(System.nanoTime()));
//					String filenameaosssnsgaii = directory+"\\aosssnsga2"+File.separator+"aosnsssga2-rwa";
//					if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//						NondominatedPopulation ndPop = instAlgorithmaosssnsgaii.getResult();
//						System.out.println(ndPop.size());
//						try {
//							PopulationIO.writeObjectives(new File(filenameaosssnsgaii + (i+1) + (j+1) + ".txt"), ndPop);
//						} catch (IOException ex) {
//							Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//						}
//					}
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }



    protected static InstrumentedAlgorithm instrument(Algorithm alg,int maxEvaluations,RWA prob) {
        Instrumenter instrumenter = new Instrumenter().withFrequency(maxEvaluations / 100)
                .withProblem(prob)
                .attachElapsedTimeCollector();

        return instrumenter.instrument(alg);

    }
}
