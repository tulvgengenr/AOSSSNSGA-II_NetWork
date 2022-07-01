package hh.experiment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.NSGAII;
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

import hh.creditassigment.CreditDefFactory;
import hh.creditassigment.ICreditAssignment;
import hh.hyperheuristics.HHFactory;
import hh.hyperheuristics.IHyperHeuristic;
import hh.hyperheuristics.MOEADHH;
import hh.nextheuristic.INextHeuristic;
import hh.problem.rwa.RWA;

public class RunMOEADHHvs {
	public static void main(String[] args) {
		RWA problem = new RWA(3);
		String probNamefrrmba = "MOEADHH-frrmba";
		String probNamerandom = "MOEADHH-random";
		String probNamepm = "MOEADHH-pm";
		String probNameap = "MOEADHH-ap";
		String probNamede1 = "MOEADHH-frrmbade1";
		String probNamede2 = "MOEADHH-frrmbade2";
		String probNamede3 = "MOEADHH-frrmbade3";
		String probNamede4 = "MOEADHH-frrmbade4";
		
		int populationSize = 120;
        int maxEvaluations = 10000;
        
        Properties properties = new Properties();
        properties.put("populationSize", populationSize);
        String selectorStrf = "FRRMAB";
        String selectorStrr = "Random";
        String selectorStrp = "PM";
        String selectorStra = "AP";
        
        properties.put("HH-frrmba", selectorStrf);
        properties.put("HH-random", selectorStrr);
        properties.put("HH-pm", selectorStrp);
        properties.put("HH-ap", selectorStra);
        
        String CredDefstr="SIDoPF";/*"CSDoPF"*/
        
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
        
//        heuristics.add(of.getVariation("2x+pmIe", prop, problem));
//        heuristics.add(of.getVariation("sbxIe+pmIe", prop, problem));
        heuristics.add(of.getVariation("deIe", prop, problem));
        heuristics.add(of.getVariation("deIe2", prop, problem));
        heuristics.add(of.getVariation("deIe3", prop, problem));
        heuristics.add(of.getVariation("deIe4", prop, problem));
        heuristics.add(of.getVariation("1x+umIe", prop, problem));
//        heuristics.add(of.getVariation("undxIe+pmIe", prop, problem));
        //heuristics.add(of.getVariation("spxIe+pmIe", prop, problem));
        
        
        ArrayList<Variation> heuristics2 = new ArrayList<>();
        heuristics2.add(of.getVariation("deIe", prop, problem));
        ArrayList<Variation> heuristics3 = new ArrayList<>();
        heuristics3.add(of.getVariation("deIe2", prop, problem));
        ArrayList<Variation> heuristics4 = new ArrayList<>();
        heuristics4.add(of.getVariation("deIe3", prop, problem));
        ArrayList<Variation> heuristics5 = new ArrayList<>();
        heuristics5.add(of.getVariation("deIe4", prop, problem));
        
        Initialization initialization = new RandomInitialization(problem,populationSize);
        
        NondominatedSortingPopulation population = new NondominatedSortingPopulation();
        TournamentSelection selection = new TournamentSelection(2,
                new ChainedComparator(new ParetoDominanceComparator(),
                        new CrowdingComparator()));
        EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.5);
        GAVariation ga = new GAVariation(of.getVariation("1x", prop, problem), of.getVariation("umIe", prop, problem));
        
        ICreditAssignment creditDef;
        IHyperHeuristic frrmba;
        IHyperHeuristic random;
        IHyperHeuristic pm;
        IHyperHeuristic ap;
        IHyperHeuristic de1;
        IHyperHeuristic de2;
        IHyperHeuristic de3;
        IHyperHeuristic de4;
        NSGAII nsgaii;
        try {
			creditDef = CreditDefFactory.getInstance().getCreditDef(prop.getString("CredDef", null), prop, problem);
			INextHeuristic selectorfrrmba = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-frrmba", null), prop, heuristics);
			INextHeuristic selectorrandom = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-random", null), prop, heuristics);
			INextHeuristic selectorpm = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-pm", null), prop, heuristics);
			INextHeuristic selectorap = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-ap", null), prop, heuristics);
			INextHeuristic selectorrandomde1 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-frrmba", null), prop, heuristics2);
			INextHeuristic selectorrandomde2 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-frrmba", null), prop, heuristics3);
			INextHeuristic selectorrandomde3 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-frrmba", null), prop, heuristics4);
			INextHeuristic selectorrandomde4 = HHFactory.getInstance().getHeuristicSelector(prop.getString("HH-frrmba", null), prop, heuristics5);
			
			
			
			frrmba = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorfrrmba, creditDef); 
			random = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorrandom, creditDef);			
			pm = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorpm, creditDef);
			ap = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorap, creditDef);
			de1 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorrandomde1, creditDef);
			de2 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorrandomde2, creditDef);
			de3 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorrandomde3, creditDef);
			de4 = new MOEADHH(problem, neighborhoodSize, initialization,
	                delta, eta, updateUtility, selectorrandomde4, creditDef);
			nsgaii = new NSGAII(problem, population, archive, selection, ga, initialization);
			
			
			InstrumentedAlgorithm instAlgorithmfrrmba = instrument(frrmba,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmrandom = instrument(random,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmpm = instrument(pm,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmap = instrument(ap,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmde1 = instrument(de1,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmde2 = instrument(de2,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmde3 = instrument(de3,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmde4 = instrument(de4,maxEvaluations,problem);
			InstrumentedAlgorithm instAlgorithmnsga2 = instrument(nsgaii,maxEvaluations,problem);
			
			System.out.println("Starting " + frrmba.getNextHeuristicSupplier() + CredDefstr + " on " + probNamefrrmba + " with pop size: " + properties.get("populationSize"));
	        long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmfrrmba.isTerminated() && (instAlgorithmfrrmba.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmfrrmba.step();
	                if (instAlgorithmfrrmba.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmfrrmba.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        frrmba.terminate();
	        
	        long finishTimefrrmba = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimefrrmba - startTimefrrmba) / 1000) + "s");

	        frrmba.setName(String.valueOf(System.nanoTime()));
	        
	        String path ="E:\\exp\\experiment results"; 
	        String filenamef = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamefrrmba + "_" // + problem.getNumberOfObjectives()+ "_"
	                + frrmba.getNextHeuristicSupplier() + "_" + frrmba.getCreditDefinition() + "_" + frrmba.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmfrrmba.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamef + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting " + random.getNextHeuristicSupplier() + CredDefstr + " on " + probNamerandom + " with pop size: " + properties.get("populationSize"));
	        long startTimerandom = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmrandom.isTerminated() && (instAlgorithmrandom.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmrandom.step();
	                if (instAlgorithmrandom.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmrandom.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        random.terminate();
	        
	        long finishTimerandom = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimerandom - startTimerandom) / 1000) + "s");

	        random.setName(String.valueOf(System.nanoTime()));
	        
	        
	        String filenamerandom = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamerandom + "_" // + problem.getNumberOfObjectives()+ "_"
	                + random.getNextHeuristicSupplier() + "_" + random.getCreditDefinition() + "_" + random.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmrandom.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamerandom + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        
	        System.out.println("Starting " + pm.getNextHeuristicSupplier() + CredDefstr + " on " + probNamepm + " with pop size: " + properties.get("populationSize"));
	        long startTimepm = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmpm.isTerminated() && (instAlgorithmpm.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmpm.step();
	                if (instAlgorithmpm.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmpm.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        pm.terminate();
	        
	        long finishTimepm = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimepm - startTimepm) / 1000) + "s");

	        pm.setName(String.valueOf(System.nanoTime()));
	        
	         
	        String filenamep = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamepm + "_" // + problem.getNumberOfObjectives()+ "_"
	                + pm.getNextHeuristicSupplier() + "_" + pm.getCreditDefinition() + "_" + pm.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmpm.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamep + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }

	        
	        System.out.println("Starting " + ap.getNextHeuristicSupplier() + CredDefstr + " on " + probNameap + " with pop size: " + properties.get("populationSize"));
	        long startTimeap = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmap.isTerminated() && (instAlgorithmap.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmap.step();
	                if (instAlgorithmap.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmap.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        ap.terminate();
	        
	        long finishTimeap = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimeap - startTimeap) / 1000) + "s");

	        ap.setName(String.valueOf(System.nanoTime()));
	        
	         
	        String filenamea = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNameap + "_" // + problem.getNumberOfObjectives()+ "_"
	                + ap.getNextHeuristicSupplier() + "_" + ap.getCreditDefinition() + "_" + ap.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmap.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamea + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting " + de1.getNextHeuristicSupplier() + CredDefstr + " on " + probNamede1 + " with pop size: " + properties.get("populationSize"));
	        long startTimede1 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmde1.isTerminated() && (instAlgorithmde1.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmde1.step();
	                if (instAlgorithmde1.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmde1.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        de1.terminate();
	        
	        long finishTimede1 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimede1 - startTimede1) / 1000) + "s");

	        de1.setName(String.valueOf(System.nanoTime()));
	        
	        String filenamed1 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamede1 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + de1.getNextHeuristicSupplier() + "_" + de1.getCreditDefinition() + "_" + de1.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmde1.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamed1 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
	        System.out.println("Starting " + de2.getNextHeuristicSupplier() + CredDefstr + " on " + probNamede2 + " with pop size: " + properties.get("populationSize"));
	        long startTimede2 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmde2.isTerminated() && (instAlgorithmde2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmde2.step();
	                if (instAlgorithmde2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmde2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        de2.terminate();
	        
	        long finishTimede2 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimede2 - startTimede2) / 1000) + "s");

	        de2.setName(String.valueOf(System.nanoTime()));
	        
	        String filenamed2 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamede2 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + de2.getNextHeuristicSupplier() + "_" + de2.getCreditDefinition() + "_" + de2.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmde2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamed2 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting " + de3.getNextHeuristicSupplier() + CredDefstr + " on " + probNamede3 + " with pop size: " + properties.get("populationSize"));
	        long startTimede3 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmde3.isTerminated() && (instAlgorithmde3.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmde3.step();
	                if (instAlgorithmde3.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmde3.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        de3.terminate();
	        
	        long finishTimede3 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimede3 - startTimede3) / 1000) + "s");

	        de3.setName(String.valueOf(System.nanoTime()));
	        
	        String filenamed3 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamede3 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + de3.getNextHeuristicSupplier() + "_" + de3.getCreditDefinition() + "_" + de3.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmde3.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamed3 + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting " + de4.getNextHeuristicSupplier() + CredDefstr + " on " + probNamede4 + " with pop size: " + properties.get("populationSize"));
	        long startTimede4 = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmde4.isTerminated() && (instAlgorithmde4.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmde4.step();
	                if (instAlgorithmde4.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmde4.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        de4.terminate();
	        
	        long finishTimede4 = System.currentTimeMillis();
	        System.out.println("Done with optimization. Execution time: " + ((finishTimede4 - startTimede4) / 1000) + "s");

	        de4.setName(String.valueOf(System.nanoTime()));
	        
	        String filenamed4 = path + File.separator + prop.getString("saveFolder","results1") + File.separator + probNamede4 + "_" // + problem.getNumberOfObjectives()+ "_"
	                + de4.getNextHeuristicSupplier() + "_" + de4.getCreditDefinition() + "_" + de4.getName();
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmde4.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamed4+ ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        System.out.println("Starting nsgaii-rwa" );
	        //long startTimefrrmba = System.currentTimeMillis();
	        try {
	            while (!instAlgorithmnsga2.isTerminated() && (instAlgorithmnsga2.getNumberOfEvaluations() < maxEvaluations)) {
	                System.out.println("*****");
	                instAlgorithmnsga2.step();
	                if (instAlgorithmnsga2.getNumberOfEvaluations() % 100 == 0) {
	                    System.out.println(instAlgorithmnsga2.getNumberOfEvaluations());
	                }
	            }
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	        
	        nsgaii.terminate();
	        
	        //long finishTimefrrmba = System.currentTimeMillis();
	        //System.out.println("Done with optimization. Execution time: " + ((finishTimefrrmba - startTimefrrmba) / 1000) + "s");

	        //frrmba.setName(String.valueOf(System.nanoTime()));
	        
	        //String path ="E:\\exp\\experiment results"; 
	        String filenamen = path + File.separator + prop.getString("saveFolder","results1") + File.separator + "nsga2-rwa"; 
	        
	        if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
	            NondominatedPopulation ndPop = instAlgorithmnsga2.getResult();
	            System.out.println(ndPop.size());
	            
	            try {
	                PopulationIO.writeObjectives(new File(filenamen + ".txt"), ndPop);
	                
	            } catch (IOException ex) {
	                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        
	        
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
