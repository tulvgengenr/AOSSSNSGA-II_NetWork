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
import org.moeaframework.core.Variation;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.core.spi.ProblemFactory;
import org.moeaframework.util.TypedProperties;

import hh.hyperheuristics.IHyperHeuristic;

public class RunBenchmark {
	
	 /**
     * pool of resources
     */
    private static ExecutorService pool;

    /**
     * List of future tasks to perform
     */
    private static ArrayList<Future<IHyperHeuristic>> futures;


	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		String[] problems = new String[]{"DTLZ1_3"};
		//String[] problems = new String[]{"DTLZ1_3","DTLZ2_3","DTLZ3_3","DTLZ4_3","DTLZ5_3","DTLZ6_3","DTLZ7_3"};
		 pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-1);
		for (String problem : problems){
			String path="E:\\exp\\exp res3\\";
			String probName = problem;
            System.out.println(probName);
            int numberOfSeeds = 1;
            //String[] selectors = new String[]{"PM","AP","FRRMAB"};
            String[] selectors = new String[]{"FRRMAB"};
            String[] creditDefs = new String[]{"OPDo","SIDoPF","CSDoPF"};
            //String[] creditDefs = new String[]{"OPI","SII","CSI"};
            futures = new ArrayList<>();
            for (String selector : selectors){
            	for (String credDefStr : creditDefs){
            		futures.clear();
            		for (int k = 0; k < numberOfSeeds; k++){
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
                             prop.put("HH", selector);
                             prop.put("CredDef", credDefStr);
                             
                             prop.put("saveFolder", "results2");
                             prop.put("saveIndicators", "true");
                             prop.put("saveFinalPopulation", "true");
                             prop.put("saveOperatorCreditHistory", "true");
                             prop.put("saveOperatorSelectionHistory", "true");
                             prop.put("saveOperatorQualityHistory", "true");
                             
                             ArrayList<Variation> heuristics = new ArrayList<>();
                             OperatorFactory of = OperatorFactory.getInstance();

                             heuristics.add(of.getVariation("um", prop, prob));
                             heuristics.add(of.getVariation("sbx+pm", prop, prob));
                             heuristics.add(of.getVariation("de+pm", prop, prob));
                             heuristics.add(of.getVariation("pcx+pm", prop, prob));
                             heuristics.add(of.getVariation("undx+pm", prop, prob));
                             heuristics.add(of.getVariation("spx+pm", prop, prob));
                             
                             NondominatedPopulation refSet = ProblemFactory.getInstance().getReferenceSet(probName);

                             TypedProperties typeProp = new TypedProperties(prop);
                                 typeProp.setDoubleArray("ArchiveEpsilon", epsilonDouble);
                                 TestRun test = new TestRun(path, prob, probName,refSet,
                                         typeProp, heuristics, maxEvaluations);
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
		pool.shutdown();
	}

}
