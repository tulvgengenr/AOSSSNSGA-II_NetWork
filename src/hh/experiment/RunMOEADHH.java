package hh.experiment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.moeaframework.Instrumenter;
import org.moeaframework.analysis.collector.InstrumentedAlgorithm;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.PopulationIO;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.util.TypedProperties;

import hh.creditassigment.CreditDefFactory;
import hh.creditassigment.ICreditAssignment;
import hh.hyperheuristics.AOSNSGAII;
import hh.hyperheuristics.HHFactory;
import hh.hyperheuristics.IHyperHeuristic;
import hh.hyperheuristics.MOEADHH;
import hh.nextheuristic.INextHeuristic;
import hh.problem.rwa.RWA;

public class RunMOEADHH {

	public static void main(String[] args){
		RWA problem = new RWA(3);
		String probName = "MOEADHH-1";//0.0060
		String probNamerandom = "MOEADHH-random";
		String probName2 = "MOEADHH-2";//0.0020
		String probName3 = "MOEADHH-3";//0.0020
		String probName4 = "MOEADHH-4";//0.0020
		//String probName5 = "MOEADHH-umIe+pmIe";
		String probName6 = "MOEADHH-5";//1.0000e-03
		String probName7 = "MOEADHH-6";//0.0020
		String probName8 = "MOEADHH-7";//0.0020
		String probName9 = "MOEADHH-8";//0.0050
		String probName10 = "MOEADHH-9";// 0
		//String probName11 = "MOEADHH-rmIe";
		
		
		int populationSize = 120;
        int maxEvaluations = 10000;
        
        Properties properties = new Properties();
        properties.put("populationSize", populationSize);
        String selectorStr = "FRRMAB";
        String selectorStr2 = "Random";
        //String selectorStr3 = "PM";
        //String selectorStr4 = "AP";
        
        properties.put("HH", selectorStr);
        properties.put("HH2", selectorStr2);
        String CredDefstr="SIDoPF";/*"CSDoPF"*/
        
        properties.put("CredDef", CredDefstr);
        properties.put("saveFolder", "results1");
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
        
        
        ArrayList<Variation> heuristics = new ArrayList<>();
        OperatorFactory of = OperatorFactory.getInstance();

          heuristics.add(of.getVariation("1x+pmIe", prop, problem));//0.0050
          heuristics.add(of.getVariation("2x+pmIe", prop, problem));//0.0030
          heuristics.add(of.getVariation("ux+pmIe", prop, problem));//0.0060
          //heuristics.add(of.getVariation("umIe+pmIe", prop, problem));
          heuristics.add(of.getVariation("sbxIe+pmIe", prop, problem));//0.0030
          heuristics.add(of.getVariation("deIe+pmIe", prop, problem));//0.0020
          heuristics.add(of.getVariation("pcxIe+pmIe", prop, problem));//0.0030
          heuristics.add(of.getVariation("undxIe+pmIe", prop, problem));//0.0080
          heuristics.add(of.getVariation("spxIe+pmIe", prop, problem));//0.0020
          //heuristics.add(of.getVariation("rmIe", prop, problem));
        
          ArrayList<Variation> heuristics2 = new ArrayList<>();
          heuristics2.add(of.getVariation("1x+pmIe", prop, problem));
          ArrayList<Variation> heuristics3 = new ArrayList<>();
          heuristics3.add(of.getVariation("2x+pmIe", prop, problem));
          ArrayList<Variation> heuristics4 = new ArrayList<>();
          heuristics4.add(of.getVariation("ux+pmIe", prop, problem));
//          ArrayList<Variation> heuristics5 = new ArrayList<>();
//          heuristics5.add(of.getVariation("umIe+pmIe", prop, problem));
          ArrayList<Variation> heuristics6 = new ArrayList<>();
          heuristics6.add(of.getVariation("sbxIe+pmIe", prop, problem));
          ArrayList<Variation> heuristics7 = new ArrayList<>();
          heuristics7.add(of.getVariation("deIe+pmIe", prop, problem));
          ArrayList<Variation> heuristics8 = new ArrayList<>();
          heuristics8.add(of.getVariation("pcxIe+pmIe", prop, problem));
          ArrayList<Variation> heuristics9 = new ArrayList<>();
          heuristics9.add(of.getVariation("undxIe+pmIe", prop, problem));
          ArrayList<Variation> heuristics10 = new ArrayList<>();
          heuristics10.add(of.getVariation("spxIe+pmIe", prop, problem));
//          ArrayList<Variation> heuristics11 = new ArrayList<>();
//          heuristics11.add(of.getVariation("rmIe", prop, problem));
          
        Initialization initialization = new RandomInitialization(problem,populationSize);
        
        ICreditAssignment creditDef;
        IHyperHeuristic hh;
        IHyperHeuristic hhh;
        IHyperHeuristic hh2;
        IHyperHeuristic hh3;
        IHyperHeuristic hh4;
        IHyperHeuristic hh5;
        IHyperHeuristic hh6;
        IHyperHeuristic hh7;
        IHyperHeuristic hh8;
        IHyperHeuristic hh9;
        IHyperHeuristic hh10;
        IHyperHeuristic hh11;
		try {
			creditDef = CreditDefFactory.getInstance().getCreditDef(prop.getString("CredDef", null), prop, problem);
			INextHeuristic selector = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics);
			INextHeuristic selectors = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH2", null), prop, heuristics);
			
			INextHeuristic selector2 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics2);
			INextHeuristic selector3 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics3);
			INextHeuristic selector4 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics4);
			//INextHeuristic selector5 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics5);
			INextHeuristic selector6 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics6);
			INextHeuristic selector7 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics7);
			INextHeuristic selector8 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics8);
			INextHeuristic selector9 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics9);
			INextHeuristic selector10 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics10);
			//INextHeuristic selector11 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics11);
			
			hh = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector, creditDef); 
			hhh = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectors, creditDef);
			
			hh2 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector2, creditDef); 
			hh3 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector3, creditDef); 
			hh4 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector4, creditDef); 
//			hh5 = new MOEADHH(problem, neighborhoodSize, initialization,
//	                delta, eta, updateUtility, selector5, creditDef); 
			hh6 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector6, creditDef); 
			hh7 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector7, creditDef); 
			hh8 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector8, creditDef); 
			hh9 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector9, creditDef); 
			hh10 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selector10, creditDef); 
//			hh11 = new MOEADHH(problem, neighborhoodSize, initialization,
//	                delta, eta, updateUtility, selector11, creditDef); 
			
			InstrumentedAlgorithm instAlgorithm = instrument(hh,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmhhh = instrument(hhh,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm2 = instrument(hh2,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm3 = instrument(hh3,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm4 = instrument(hh4,maxEvaluations,problem);
			//InstrumentedAlgorithm instAlgorithm5 = instrument(hh5,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm6 = instrument(hh6,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm7 = instrument(hh7,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm8 = instrument(hh8,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm9 = instrument(hh9,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm10 = instrument(hh10,maxEvaluations,problem);
			//InstrumentedAlgorithm instAlgorithm11 = instrument(hh11,maxEvaluations,problem);
			
			String path ="E:\\exp\\experiment results"; 
			String filenameall = path + File.separator + prop.getString("saveFolder","results1") + File.separator + "all";
			File file = new File(filenameall+".txt");
			
			System.out.println("Starting " + hh.getNextHeuristicSupplier() + CredDefstr + " on " + probName + " with pop size: " + properties.get("populationSize"));
	        long startTime = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm.isTerminated() && (instAlgorithm.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	            	instAlgorithm.step();
	                if (instAlgorithm.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh.terminate();
	        
	        long finishTime = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime - startTime) / 1000) + "s");

	        hh.setName(String.valueOf(System.nanoTime()));
	        
	        
	        String filename = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh.getNextHeuristicSupplier() + "_" + hh.getCreditDefinition() + "_" + hh.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        
	        
	   
			
			System.out.println("Starting " + hhh.getNextHeuristicSupplier() + CredDefstr + " on " + probNamerandom + " with pop size: " + properties.get("populationSize"));
	        long startTimerandom = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmhhh.isTerminated() && (instAlgorithmhhh.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	            	instAlgorithmhhh.step();
	                if (instAlgorithmhhh.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmhhh.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hhh.terminate();
	        
	        long finishTimerandom = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimerandom - startTimerandom) / 1000) + "s");

	        hhh.setName(String.valueOf(System.nanoTime()));
	        
	        
	        String filenamerandom = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamerandom + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hhh.getNextHeuristicSupplier() + "_" + hhh.getCreditDefinition() + "_" + hhh.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmhhh.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamerandom + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        
	        System.out.println("Starting " + hh2.getNextHeuristicSupplier() + CredDefstr + " on " + probName + " with pop size: " + properties.get("populationSize"));
	        long startTime2 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm2.isTerminated() && (instAlgorithm2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm2.step();
	                if (instAlgorithm2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh2.terminate();
	        
	        long finishTime2 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime2 - startTime2) / 1000) + "s");

	        hh2.setName(String.valueOf(System.nanoTime()));
	        
	        String filename2 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName2 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh2.getNextHeuristicSupplier() + "_" + hh2.getCreditDefinition() + "_" + hh2.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename2 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        
	        System.out.println("Starting " + hh3.getNextHeuristicSupplier() + CredDefstr + " on " + probName3 + " with pop size: " + properties.get("populationSize"));
	        long startTime3 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm3.isTerminated() && (instAlgorithm3.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm3.step();
	                if (instAlgorithm3.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm3.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh3.terminate();
	        
	        long finishTime3 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime3 - startTime3) / 1000) + "s");

	        hh3.setName(String.valueOf(System.nanoTime()));
	        
	        String filename3 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName3 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh3.getNextHeuristicSupplier() + "_" + hh3.getCreditDefinition() + "_" + hh3.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm3.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename3 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        
	        System.out.println("Starting " + hh4.getNextHeuristicSupplier() + CredDefstr + " on " + probName4 + " with pop size: " + properties.get("populationSize"));
	        long startTime4 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm4.isTerminated() && (instAlgorithm4.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm4.step();
	                if (instAlgorithm4.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm4.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh4.terminate();
	        
	        long finishTime4 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime4 - startTime4) / 1000) + "s");

	        hh4.setName(String.valueOf(System.nanoTime()));
	        
	        String filename4 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName4 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh4.getNextHeuristicSupplier() + "_" + hh4.getCreditDefinition() + "_" + hh4.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm4.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename4 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
//	        System.out.println("Starting " + hh5.getNextHeuristicSupplier() + CredDefstr + " on " + probName5 + " with pop size: " + properties.get("populationSize"));
//	        long startTime5 = System.currentTimeMillis();
//	        try {
//	            while (!instAlgorithm5.isTerminated() && (instAlgorithm5.getNumberOfEvaluations() < maxEvaluations)) {
//	                System.out.println("-----");
//	            	instAlgorithm5.step();
//	                if (instAlgorithm5.getNumberOfEvaluations() % 100 == 0) {
//	                    System.out.println(instAlgorithm5.getNumberOfEvaluations());
//	                }
//	            }
//	        } catch (Exception ex) {
//	            ex.printStackTrace();
//	        }
//
//	        
//	        hh5.terminate();
//	        
//	        long finishTime5 = System.currentTimeMillis();
//	        System.out.println("Done with optimization. Execution time: " + ((finishTime5 - startTime5) / 1000) + "s");
//
//	        hh5.setName(String.valueOf(System.nanoTime()));
//	        
//	        String filename5 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName5 + "_" // + problem.getNumberOfObjectives()+ "_"
//	                + hh5.getNextHeuristicSupplier() + "_" + hh5.getCreditDefinition() + "_" + hh5.getName();
//	        
//	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//	            NondominatedPopulation ndPop = instAlgorithm5.getResult();
//	            System.out.println(ndPop.size());
//	            
//	            try {
//	                PopulationIO.writeObjectives(new File(filename5 + ".txt"), ndPop);
//	                PopulationIO.writeObjectives(file, ndPop);
//	            } catch (IOException ex) {
//	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//	            }
//	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        System.out.println("Starting " + hh6.getNextHeuristicSupplier() + CredDefstr + " on " + probName6 + " with pop size: " + properties.get("populationSize"));
	        long startTime6 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm6.isTerminated() && (instAlgorithm6.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm6.step();
	                if (instAlgorithm6.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm6.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh6.terminate();
	        
	        long finishTime6 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime6 - startTime6) / 1000) + "s");

	        hh6.setName(String.valueOf(System.nanoTime()));
	        
	        String filename6 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName6 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh6.getNextHeuristicSupplier() + "_" + hh6.getCreditDefinition() + "_" + hh6.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm6.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename6 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        System.out.println("Starting " + hh7.getNextHeuristicSupplier() + CredDefstr + " on " + probName7 + " with pop size: " + properties.get("populationSize"));
	        long startTime7 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm7.isTerminated() && (instAlgorithm7.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm7.step();
	                if (instAlgorithm7.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm7.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh7.terminate();
	        
	        long finishTime7 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime7 - startTime7) / 1000) + "s");

	        hh7.setName(String.valueOf(System.nanoTime()));
	        
	        String filename7 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName7 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh7.getNextHeuristicSupplier() + "_" + hh7.getCreditDefinition() + "_" + hh7.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm7.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename7 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        System.out.println("Starting " + hh8.getNextHeuristicSupplier() + CredDefstr + " on " + probName8 + " with pop size: " + properties.get("populationSize"));
	        long startTime8 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm8.isTerminated() && (instAlgorithm8.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm8.step();
	                if (instAlgorithm8.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm8.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh8.terminate();
	        
	        long finishTime8 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime8 - startTime8) / 1000) + "s");

	        hh8.setName(String.valueOf(System.nanoTime()));
	        
	        String filename8 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName8 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh8.getNextHeuristicSupplier() + "_" + hh8.getCreditDefinition() + "_" + hh8.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm8.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename8 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        System.out.println("Starting " + hh9.getNextHeuristicSupplier() + CredDefstr + " on " + probName9 + " with pop size: " + properties.get("populationSize"));
	        long startTime9 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm9.isTerminated() && (instAlgorithm9.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm9.step();
	                if (instAlgorithm9.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm9.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh9.terminate();
	        
	        long finishTime9 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime9 - startTime9) / 1000) + "s");

	        hh9.setName(String.valueOf(System.nanoTime()));
	        
	        String filename9 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName9 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh9.getNextHeuristicSupplier() + "_" + hh9.getCreditDefinition() + "_" + hh9.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm9.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename9 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        System.out.println("Starting " + hh10.getNextHeuristicSupplier() + CredDefstr + " on " + probName10 + " with pop size: " + properties.get("populationSize"));
	        long startTime10 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm10.isTerminated() && (instAlgorithm10.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("-----");
	            	instAlgorithm10.step();
	                if (instAlgorithm10.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm10.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh10.terminate();
	        
	        long finishTime10 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTime10 - startTime10) / 1000) + "s");

	        hh10.setName(String.valueOf(System.nanoTime()));
	        
	        String filename10 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName10 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + hh10.getNextHeuristicSupplier() + "_" + hh10.getCreditDefinition() + "_" + hh10.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm10.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename10 + ".txt"), ndPop);
	                PopulationIO.writeObjectives(file, ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
//	        System.out.println("Starting " + hh11.getNextHeuristicSupplier() + CredDefstr + " on " + probName11 + " with pop size: " + properties.get("populationSize"));
//	        long startTime11 = System.currentTimeMillis();
//	        try {
//	            while (!instAlgorithm11.isTerminated() && (instAlgorithm11.getNumberOfEvaluations() < maxEvaluations)) {
//	                System.out.println("-----");
//	            	instAlgorithm11.step();
//	                if (instAlgorithm11.getNumberOfEvaluations() % 100 == 0) {
//	                    System.out.println(instAlgorithm11.getNumberOfEvaluations());
//	                }
//	            }
//	        } catch (Exception ex) {
//	            ex.printStackTrace();
//	        }
//
//	        
//	        hh11.terminate();
//	        
//	        long finishTime11 = System.currentTimeMillis();
//	        System.out.println("Done with optimization. Execution time: " + ((finishTime11 - startTime11) / 1000) + "s");
//
//	        hh11.setName(String.valueOf(System.nanoTime()));
//	        
//	        String filename11 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName11 + "_" // + problem.getNumberOfObjectives()+ "_"
//	                + hh11.getNextHeuristicSupplier() + "_" + hh11.getCreditDefinition() + "_" + hh11.getName();
//	        
//	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
//	            NondominatedPopulation ndPop = instAlgorithm11.getResult();
//	            System.out.println(ndPop.size());
//	            
//	            try {
//	                PopulationIO.writeObjectives(new File(filename11 + ".txt"), ndPop);
//	                PopulationIO.writeObjectives(file, ndPop);
//	            } catch (IOException ex) {
//	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
//	            }
//	        }
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorCreditHistory"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorSelectionHistory"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getProperty("saveOperatorQualityHistory"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	protected static InstrumentedAlgorithm instrument(Algorithm alg,int maxEvaluations,RWA prob) {
        

        Instrumenter instrumenter = new Instrumenter().withFrequency(maxEvaluations / 100)
                .withProblem(prob)
                .attachElapsedTimeCollector();

        return instrumenter.instrument(alg);
    }

}
