package hh.experiment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.MOEAD;
import org.moeaframework.algorithm.NSGAII;
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
import hh.problem.rwa.RWA;
import jmetal.core.Problem;


public class RunAOSNSGAII {
	public static void main(String[] args){
		RWA problem = new RWA(3);
		String probName = "AOSNSGAII";
		int populationSize = 120;
        int maxEvaluations = 10000;
        
        Properties properties = new Properties();
        properties.put("populationSize", populationSize);
        String selectorStr = "AP";
        //String selectorStr = "PM";
        properties.put("HH", selectorStr);
        String CredDefstr=/*"SIDoPF";*//*"CSDoPF"*/"OPDo";
        
        properties.put("CredDef", CredDefstr);
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
        
        ArrayList<Variation> heuristics = new ArrayList<>();
        OperatorFactory of = OperatorFactory.getInstance();

          heuristics.add(of.getVariation("1x+umIe", prop, problem));
//          heuristics.add(of.getVariation("2x+umIe", prop, problem));
          heuristics.add(of.getVariation("ux+umIe", prop, problem));
          //heuristics.add(of.getVariation("umIe", prop, problem));
          //heuristics.add(of.getVariation("umIe+pmIe", prop, problem));
//          
          heuristics.add(of.getVariation("sbxIe+pmIe", prop, problem));          
//          heuristics.add(of.getVariation("pcxIe+pmIe", prop, problem));
//          heuristics.add(of.getVariation("undxIe+pmIe", prop, problem));
          heuristics.add(of.getVariation("spxIe+pmIe", prop, problem));
//          //heuristics.add(of.getVariation("rmIe", prop, problem));
//          
 //         heuristics.add(of.getVariation("deIe", prop, problem));
//          heuristics.add(of.getVariation("deIe2", prop, problem));
//          heuristics.add(of.getVariation("deIe3", prop, problem));
//          heuristics.add(of.getVariation("deIe4", prop, problem));
          
          
          
          ArrayList<Variation> heuristics2 = new ArrayList<>();
          heuristics2.add(of.getVariation("1x+umIe", prop, problem));
          ArrayList<Variation> heuristics3 = new ArrayList<>();
          heuristics3.add(of.getVariation("ux+umIe", prop, problem));
          ArrayList<Variation> heuristics4 = new ArrayList<>();
          heuristics4.add(of.getVariation("sbxIe+pmIe", prop, problem));
          ArrayList<Variation> heuristics5 = new ArrayList<>();
          heuristics5.add(of.getVariation("spxIe+pmIe", prop, problem));
          
          
        Initialization initialization ;
        NondominatedSortingPopulation population ;
        TournamentSelection selection = new TournamentSelection(2,
                new ChainedComparator(new ParetoDominanceComparator(),
                        new CrowdingComparator()));
        EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.5);
        GAVariation ga1 = new GAVariation(of.getVariation("1x", prop, problem), of.getVariation("umIe", prop, problem));
        GAVariation ga2 = new GAVariation(of.getVariation("ux", prop, problem), of.getVariation("umIe", prop, problem));
        GAVariation ga3 = new GAVariation(of.getVariation("sbxIe", prop, problem), of.getVariation("pmIe", prop, problem));
        GAVariation ga4 = new GAVariation(of.getVariation("spxIe", prop, problem), of.getVariation("pmIe", prop, problem));
        
        //GAVariation de = new GAVariation(of.getVariation("deIe", prop, problem), of.getVariation("deIe", prop, problem));
        
        ICreditAssignment creditDef;
        IHyperHeuristic hh;
        IHyperHeuristic hh2;
        NSGAII nsgaiiop1;
        NSGAII nsgaiiop2;
        NSGAII nsgaiiop3;
        NSGAII nsgaiiop4;
        MOEAD moead;
        SPEA2 spea2;
        MOEAD moeadde;
        IHyperHeuristic op1;
        IHyperHeuristic op2;
        IHyperHeuristic op3;
        IHyperHeuristic op4;
        SteadyStateNSGAII ssnsga2op1;
        SteadyStateNSGAII ssnsga2op2;
        SteadyStateNSGAII ssnsga2op3;
        SteadyStateNSGAII ssnsga2op4;
        for(int i=6;i<=10;i++){
        String directory ="D:\\zy\\exp2\\70\\35\\"+i;
		try {
			initialization = new RandomInitialization(problem,populationSize);
			population = new NondominatedSortingPopulation();
			
			creditDef = CreditDefFactory.getInstance().getCreditDef(prop.getString("CredDef", null), prop, problem);
			INextHeuristic selector = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics);
			INextHeuristic selector2 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics2);
			INextHeuristic selector3 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics3);
			INextHeuristic selector4 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics4);
			INextHeuristic selector5 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH", null), prop, heuristics5);
			
			
			hh = new AOSNSGAII(problem, population, archive, selection,initialization, selector, creditDef);
			nsgaiiop1 = new NSGAII(problem, population, archive, selection, ga1, initialization);
			nsgaiiop2 = new NSGAII(problem, population, archive, selection, ga2, initialization);
			nsgaiiop3 = new NSGAII(problem, population, archive, selection, ga3, initialization);
			nsgaiiop4 = new NSGAII(problem, population, archive, selection, ga4, initialization);
			moead = new MOEAD(problem, neighborhoodSize, initialization, ga1, delta, eta, updateUtility);
			spea2 = new SPEA2(problem, initialization, ga1, 10, 1);
			moeadde = new MOEAD(problem, neighborhoodSize, initialization, of.getVariation("deIe", prop, problem), delta, eta, updateUtility);
			hh2 = new AOSSSNSGAII(problem, population, archive, selection, initialization, selector, creditDef,directory+"\\op_all.txt");
			op1 = new AOSSSNSGAII(problem, population, archive, selection, initialization, selector2, creditDef,directory+"\\op_1.txt");
			op2 = new AOSSSNSGAII(problem, population, archive, selection, initialization, selector3, creditDef,directory+"\\op_2.txt");
			op3 = new AOSSSNSGAII(problem, population, archive, selection, initialization, selector4, creditDef,directory+"\\op_3.txt");
			op4 = new AOSSSNSGAII(problem, population, archive, selection, initialization, selector5, creditDef,directory+"\\op_4.txt");
			ssnsga2op1 = new SteadyStateNSGAII(problem, population, archive, selection, ga1, initialization);
			ssnsga2op2 = new SteadyStateNSGAII(problem, population, archive, selection, ga2, initialization);
			ssnsga2op3 = new SteadyStateNSGAII(problem, population, archive, selection, ga3, initialization);
			ssnsga2op4 = new SteadyStateNSGAII(problem, population, archive, selection, ga4, initialization);
			
			
			InstrumentedAlgorithm instAlgorithm = instrument(hh,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmnsga2op1 = instrument(nsgaiiop1,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmnsga2op2 = instrument(nsgaiiop2,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmnsga2op3 = instrument(nsgaiiop3,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmnsga2op4 = instrument(nsgaiiop4,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmmoead = instrument(moead,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmspea2 = instrument(spea2,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmmoeadde = instrument(moeadde,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithm2 = instrument(hh2,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmop1 = instrument(op1,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmop2 = instrument(op2,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmop3 = instrument(op3,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmop4 = instrument(op4,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmssop1 = instrument(ssnsga2op1,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmssop2 = instrument(ssnsga2op2,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmssop3 = instrument(ssnsga2op3,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmssop4 = instrument(ssnsga2op4,maxEvaluations,problem);
			
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
	        
	       // String path ="E:\\exp\\experiment results"; 
//	        String filename = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probName + "_" // + problem.getNumberOfObjectives()+ "_"
//	                + hh.getNextHeuristicSupplier() + "_" + hh.getCreditDefinition() + "_" + hh.getName();
	        String filename = directory +  File.separator + "aosnsgaii-rwa";
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename + ".txt"), ndPop);
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
//	        if (Boolean.parseBoolean(prop.getString("saveOperatorCreditHistory", "true"))) {
//	            IOCreditHistory ioch = new IOCreditHistory();
//	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename + ".creditcsv",",");
//	        }
//
//	        if (Boolean.parseBoolean(prop.getString("saveOperatorSelectionHistory", "true"))) {
//	            IOSelectionHistory iosh = new IOSelectionHistory();
//	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename + ".hist");
//	        }
//
//	        //save operator quality history
//	        if (Boolean.parseBoolean(prop.getString("saveOperatorQualityHistory", "true"))) {
//	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename + ".qual");
//	        }
	        
	        
	        
	        System.out.println("Starting nsgaii-op1-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmnsga2op1.isTerminated() && (instAlgorithmnsga2op1.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmnsga2op1.step();
	                if (instAlgorithmnsga2op1.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmnsga2op1.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        nsgaiiop1.terminate();
	        
	        //long finishTimefrrmba = System.currentTimeMillis();
	        //System.out.println("Done with optimization. Execution time: " + ((finishTimefrrmba - startTimefrrmba) / 1000) + "s");

	        //frrmba.setName(String.valueOf(System.nanoTime()));
	        
	        //String path ="E:\\exp\\experiment results"; 
	        String filenamenop1 = directory +  File.separator +"nsgaii-op1-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmnsga2op1.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamenop1 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        System.out.println("Starting nsgaii-op2-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmnsga2op2.isTerminated() && (instAlgorithmnsga2op2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmnsga2op2.step();
	                if (instAlgorithmnsga2op2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmnsga2op2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        nsgaiiop2.terminate();
	        
	        
	        String filenamenop2 = directory +  File.separator +"nsgaii-op2-rwa";
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmnsga2op2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamenop2 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        System.out.println("Starting nsgaii-op3-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmnsga2op3.isTerminated() && (instAlgorithmnsga2op3.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmnsga2op3.step();
	                if (instAlgorithmnsga2op3.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmnsga2op3.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        nsgaiiop3.terminate();
	        
	        
	        String filenamenop3 = directory +  File.separator +"nsgaii-op3-rwa";
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmnsga2op3.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamenop3 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting nsgaii-op4-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmnsga2op4.isTerminated() && (instAlgorithmnsga2op4.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmnsga2op4.step();
	                if (instAlgorithmnsga2op4.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmnsga2op4.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        nsgaiiop4.terminate();
	       
	        String filenamenop4 = directory +  File.separator +"nsgaii-op4-rwa";
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmnsga2op4.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamenop4 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        
	        
	        System.out.println("Starting moead-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmmoead.isTerminated() && (instAlgorithmmoead.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmmoead.step();
	                if (instAlgorithmmoead.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmmoead.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        moead.terminate();
	        
	        //long finishTimefrrmba = System.currentTimeMillis();
	        //System.out.println("Done with optimization. Execution time: " + ((finishTimefrrmba - startTimefrrmba) / 1000) + "s");

	        //frrmba.setName(String.valueOf(System.nanoTime()));
	        
	        //String path ="E:\\exp\\experiment results"; 
	        String filenamem = directory +  File.separator + "moead-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmmoead.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamem + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        System.out.println("Starting spea2-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmspea2.isTerminated() && (instAlgorithmspea2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmspea2.step();
	                if (instAlgorithmspea2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmspea2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        spea2.terminate();
	        
	        //long finishTimefrrmba = System.currentTimeMillis();
	        //System.out.println("Done with optimization. Execution time: " + ((finishTimefrrmba - startTimefrrmba) / 1000) + "s");

	        //frrmba.setName(String.valueOf(System.nanoTime()));
	        
	        //String path ="E:\\exp\\experiment results"; 
	        String filenames = directory +  File.separator + "spea2-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmspea2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenames + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        System.out.println("Starting moeadde-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmmoeadde.isTerminated() && (instAlgorithmmoeadde.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmmoeadde.step();
	                if (instAlgorithmmoeadde.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmmoeadde.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        moeadde.terminate();
	        
	        //long finishTimefrrmba = System.currentTimeMillis();
	        //System.out.println("Done with optimization. Execution time: " + ((finishTimefrrmba - startTimefrrmba) / 1000) + "s");

	        //frrmba.setName(String.valueOf(System.nanoTime()));
	        
	        //String path ="E:\\exp\\experiment results"; 
	        String filenamed =directory +  File.separator + "moeadde-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmmoeadde.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamed + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting AOSSSNSGA2-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithm2.isTerminated() && (instAlgorithm2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithm2.step();
	                if (instAlgorithm2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithm2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        hh2.terminate();
	        
	        //long finishTimefrrmba = System.currentTimeMillis();
	        //System.out.println("Done with optimization. Execution time: " + ((finishTimefrrmba - startTimefrrmba) / 1000) + "s");

	        //frrmba.setName(String.valueOf(System.nanoTime()));
	        
	        //String path ="E:\\exp\\experiment results"; 
	        String filename2 = directory +  File.separator + "aosssnsga2-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithm2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filename2 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        if (Boolean.parseBoolean(prop.getString("saveOperatorCreditHistory", "true"))) {
	            IOCreditHistory ioch = new IOCreditHistory();
	            ioch.saveHistory(((IHyperHeuristic) hh).getCreditHistory(), filename2 + "_CreditHistory.txt",",");
	        }

	        if (Boolean.parseBoolean(prop.getString("saveOperatorSelectionHistory", "true"))) {
	            IOSelectionHistory iosh = new IOSelectionHistory();
	            iosh.saveHistory(((IHyperHeuristic) hh).getSelectionHistory(), filename2 + "_SelectionHistory.txt");
	        }

	        //save operator quality history
	        if (Boolean.parseBoolean(prop.getString("saveOperatorQualityHistory", "true"))) {
	            IOQualityHistory.saveHistory(((IHyperHeuristic) hh).getQualityHistory(), filename2 + "_QualityHistory.txt");
	        }
	        
	        System.out.println("Starting AOSSSNSGA2-op1-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmop1.isTerminated() && (instAlgorithmop1.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmop1.step();
	                if (instAlgorithmop1.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmop1.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        op1.terminate();
	       
	        String filenameop1 = directory +  File.separator + "aosssnsga2-op1-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmop1.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenameop1 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        System.out.println("Starting AOSSSNSGA2-op2-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmop2.isTerminated() && (instAlgorithmop2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmop2.step();
	                if (instAlgorithmop2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmop2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        op2.terminate();
	       
	        String filenameop2 = directory +  File.separator + "aosssnsga2-op2-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmop2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenameop2 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting AOSSSNSGA2-op3-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmop3.isTerminated() && (instAlgorithmop3.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmop3.step();
	                if (instAlgorithmop3.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmop3.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        op3.terminate();
	       
	        String filenameop3 = directory +  File.separator + "aosssnsga2-op3-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmop3.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenameop3 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting AOSSSNSGA2-op4-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmop4.isTerminated() && (instAlgorithmop4.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmop4.step();
	                if (instAlgorithmop4.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmop4.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        op4.terminate();
	       
	        String filenameop4 = directory +  File.separator + "aosssnsga2-op4-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmop4.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenameop4 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        System.out.println("Starting SSNSGA2-op1-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmssop1.isTerminated() && (instAlgorithmssop1.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmssop1.step();
	                if (instAlgorithmssop1.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmssop1.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        ssnsga2op1.terminate();
	       
	        String filenamessop1 = directory +  File.separator + "ssnsga2-op1-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmssop1.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamessop1 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting SSNSGA2-op2-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmssop2.isTerminated() && (instAlgorithmssop2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmssop2.step();
	                if (instAlgorithmssop2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmssop2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        ssnsga2op2.terminate();
	       
	        String filenamessop2 = directory +  File.separator + "ssnsga2-op2-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmssop2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamessop2 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting SSNSGA2-op3-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmssop3.isTerminated() && (instAlgorithmssop3.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmssop3.step();
	                if (instAlgorithmssop3.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmssop3.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        ssnsga2op3.terminate();
	       
	        String filenamessop3 = directory + File.separator + "ssnsga2-op3-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmssop3.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamessop3 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting SSNSGA2-op4-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmssop4.isTerminated() && (instAlgorithmssop4.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmssop4.step();
	                if (instAlgorithmssop4.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmssop4.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        ssnsga2op4.terminate();
	       
	        String filenamessop4 = directory + File.separator + "ssnsga2-op4-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmssop4.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamessop4 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
		} catch (IOException e) {
			e.printStackTrace();
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
