package hh.experiment;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.moeaframework.analysis.sensitivity.EpsilonHelper;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Problem;
import org.moeaframework.core.spi.ProblemFactory;
import org.moeaframework.util.TypedProperties;

import hh.hyperheuristics.IHyperHeuristic;

public class RunBenchmark2 {
	 /**
     * pool of resources
     */
    private static ExecutorService pool;

    /**
     * List of future tasks to perform
     */
    private static ArrayList<Future<IHyperHeuristic>> futures;
    
    
    public static void main(String[] args) {
    	String[] problems = new String[]{"DTLZ1_3"};
		//String[] problems = new String[]{"DTLZ1_3","DTLZ2_3","DTLZ3_3","DTLZ4_3","DTLZ5_3","DTLZ6_3","DTLZ7_3"};
		 pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-1);
		 for (String problem : problems){
			 String path="E:\\exp\\experiment results\\";
				String probName = problem;
	            System.out.println(probName);
	            int numberOfSeeds = 2;	            
	            String[] algs = new String[]{"NSGAII","SPEA2","MOEAD","MOEADde"};
	            //String[] creditDefs = new String[]{"OPI","SII","CSI"};
	            futures = new ArrayList<>();
	            
	            for(String alg:algs){
	            	futures.clear();
            		for (int k = 0; k < numberOfSeeds; k++){
            			System.out.println(k);
            			Problem prob = ProblemFactory.getInstance().getProblem(probName);
            			double[] epsilonDouble = new double[prob.getNumberOfObjectives()];
            			 for (int i = 0; i < prob.getNumberOfObjectives(); i++) {
                             epsilonDouble[i] = EpsilonHelper.getEpsilon(prob);
                         }
            			 Properties prop = new Properties();
                         String popSize = "0";
                         int maxEvaluations = 0;
                         if (prob.getName().startsWith("UF")) {
                             maxEvaluations = 300000;
                             if (prob.getNumberOfObjectives() == 2) {
                                 popSize = "600";
                             }
                             if (prob.getNumberOfObjectives() == 3) {
                                 popSize = "1000";
                             }
                         } else if (prob.getName().startsWith("DTLZ") || prob.getName().startsWith("WFG")) {
                             if (prob.getNumberOfObjectives() == 2) {
                                 maxEvaluations = 25000;
                                 popSize = "100";
                             }
                             if (prob.getNumberOfObjectives() == 3) {
                                 maxEvaluations = 30000;
                                 popSize = "105";
                             }
                         }
                         if (maxEvaluations == 0) {
                             throw new IllegalArgumentException("Problem not recognized: " + probName);
                         }

                         prop.put("indicator", "r2");
                         if (prob.getNumberOfObjectives() == 2) {
                             prop.put("r2.numberVectors", "50");
                         } else if (prob.getNumberOfObjectives() == 3) {
                             prop.put("r2.numberVectors", "91");
                         }

                         	 prop.put("populationSize", popSize);
                         	 prop.put("saveFolder", "results2");
                             prop.put("saveIndicators", "true");
                             prop.put("saveFinalPopulation", "true");
                             prop.put("saveOperatorCreditHistory", "true");
                             prop.put("saveOperatorSelectionHistory", "true");
                             prop.put("saveOperatorQualityHistory", "true");
                             prop.put("numberofseeds", k);
                             
                             NondominatedPopulation refSet = ProblemFactory.getInstance().getReferenceSet(probName);

                             TypedProperties typeProp = new TypedProperties(prop);
                             typeProp.setDoubleArray("ArchiveEpsilon", epsilonDouble);
                             TestRun2 test = new TestRun2(path, prob, probName, refSet, typeProp, maxEvaluations, alg);
                             futures.add(pool.submit(test));
            		}
            		for (Future<IHyperHeuristic> run : futures) {
                        try {
                            run.get();
                        } catch (InterruptedException | ExecutionException ex) {
                            Logger.getLogger(HHCreditTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
	            }
		 }
	}
}
