package hh.experiment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.moeaframework.Instrumenter;
import org.moeaframework.algorithm.MOEAD;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.SPEA2;
import org.moeaframework.analysis.collector.Accumulator;
import org.moeaframework.analysis.collector.InstrumentedAlgorithm;
import org.moeaframework.analysis.sensitivity.EpsilonHelper;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.PopulationIO;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.indicator.InvertedGenerationalDistance;
import org.moeaframework.core.indicator.jmetal.FastHypervolume;
import org.moeaframework.core.operator.GAVariation;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.util.TypedProperties;

import hh.IO.IOCreditHistory;
import hh.IO.IOQualityHistory;
import hh.IO.IOSelectionHistory;


public class TestRun2 implements Callable{
	protected TypedProperties properties;
    protected Problem problem;
    protected String probName;
    protected String path;
    //private ICreditAssignment creditDef;
    protected double[] epsilonDouble;
    protected int maxEvaluations;
    //private final Collection<Variation> heuristics;
    protected final NondominatedPopulation referenceSet;
    protected Solution refPointObj;
    String alg;
    
    public TestRun2(String path, Problem problem, String probName, NondominatedPopulation referenceSet, TypedProperties properties,
            int maxEvaluations,String alg) {
    	
        this.alg = alg;
        this.properties = properties;
        this.problem = problem;
        this.epsilonDouble = properties.getDoubleArray("ArchiveEpsilon",
                new double[]{EpsilonHelper.getEpsilon(problem)});
        this.probName = probName;
        this.maxEvaluations = maxEvaluations;
        this.path = path;

        this.referenceSet = referenceSet;
    }
    
    private NSGAII newNSGAII() {
        int populationSize = (int) properties.getDouble("populationSize", 600);

        Initialization initialization = new RandomInitialization(problem,
                populationSize);

        NondominatedSortingPopulation population = new NondominatedSortingPopulation();

        TournamentSelection selection = new TournamentSelection(2,
                new ChainedComparator(new ParetoDominanceComparator(),
                        new CrowdingComparator()));
        OperatorFactory of = OperatorFactory.getInstance();
        EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.5);
        GAVariation ga = new GAVariation(of.getVariation("1x", properties, problem), of.getVariation("umIe", properties, problem));
        
        NSGAII nsgaii = new NSGAII(problem, population, archive, selection, ga, initialization);
        return nsgaii;
    }
    
    private SPEA2 newSPEA2() {
        int populationSize = (int) properties.getDouble("populationSize", 600);

        Initialization initialization = new RandomInitialization(problem,
                populationSize);

        //NondominatedSortingPopulation population = new NondominatedSortingPopulation();

//        TournamentSelection selection = new TournamentSelection(2,
//                new ChainedComparator(new ParetoDominanceComparator(),
//                        new CrowdingComparator()));
        OperatorFactory of = OperatorFactory.getInstance();
        //EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.5);
        GAVariation ga = new GAVariation(of.getVariation("1x", properties, problem), of.getVariation("umIe", properties, problem));
        
        SPEA2 spea2 = new SPEA2(problem, initialization, ga, 10, 1);
        return spea2;
    }
    
    private MOEAD newMOEAD() {
    	  int populationSize = (int) properties.getDouble("populationSize", 600);

          Initialization initialization = new RandomInitialization(problem,
                  populationSize);

          int neighborhoodSize = properties.getInt("neighborhood", 20);

          double delta = properties.getDouble("delta", 0.9);

          double eta = properties.getDouble("eta", 2.0);

          int updateUtility = properties.getInt("updateUtility", 50);
          
          OperatorFactory of = OperatorFactory.getInstance();
          GAVariation ga = new GAVariation(of.getVariation("1x", properties, problem), of.getVariation("umIe", properties, problem));
        
          MOEAD moead = new MOEAD(problem, neighborhoodSize, initialization, ga, delta, eta);
        
        return moead;
    }
    
    private MOEAD newMOEADde() {
  	  int populationSize = (int) properties.getDouble("populationSize", 600);

        Initialization initialization = new RandomInitialization(problem,
                populationSize);

        int neighborhoodSize = properties.getInt("neighborhood", 20);

        double delta = properties.getDouble("delta", 0.9);

        double eta = properties.getDouble("eta", 2.0);

        int updateUtility = properties.getInt("updateUtility", 50);
        
        OperatorFactory of = OperatorFactory.getInstance();
        //GAVariation ga = new GAVariation(of.getVariation("1x", properties, problem), of.getVariation("umIe", properties, problem));
      
        MOEAD moead = new MOEAD(problem, neighborhoodSize, initialization, of.getVariation("de", properties, problem), delta, eta);
      
      return moead;
  }
    
    public Algorithm call() throws Exception {
        Algorithm hh;
        System.out.println(properties.getDouble("numberofseeds", 1));
        int x=1/0;
        if (alg.equalsIgnoreCase("nsgaii")) {
            hh = newNSGAII();
        } else if (alg.equalsIgnoreCase("spea2")) {
            hh = newSPEA2();
        } else if (alg.equalsIgnoreCase("moead")) {
            hh = newMOEAD();
        } else if (alg.equalsIgnoreCase("moeadde")) {
            hh = newMOEADde();
        } else {
            throw new IllegalArgumentException("Credit fitness type " + alg + "not recognized");
        }

        InstrumentedAlgorithm instAlgorithm = instrument(hh);

        // run the executor using the listener to collect results
        System.out.println("Starting "  + " on " + probName + " with pop size: " + properties.getDouble("populationSize", 600));
        long startTime = System.currentTimeMillis();
        try {
            while (!instAlgorithm.isTerminated() && (instAlgorithm.getNumberOfEvaluations() < maxEvaluations)) {
                instAlgorithm.step();
                if (instAlgorithm.getNumberOfEvaluations() % 1000 == 0) {
                    System.out.println(instAlgorithm.getNumberOfEvaluations());
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
        }

        hh.terminate();
        long finishTime = System.currentTimeMillis();
        System.out.println("Done with optimization. Execution time: " + ((finishTime - startTime) / 1000) + "s");

        //hh.setName(String.valueOf(System.nanoTime()));
        
        
        String filename = path + File.separator + properties.getProperties().getProperty("saveFolder") + File.separator + alg + " "+properties.getDouble("numberofseeds", 1);

        saveIndicatorValues(instAlgorithm, filename);

        if (Boolean.parseBoolean(properties.getProperties().getProperty("saveFinalPopulation"))) {
            NondominatedPopulation ndPop = instAlgorithm.getResult();
            try {
                PopulationIO.writeObjectives(new File(filename + ".NDpop"), ndPop);
            } catch (IOException ex) {
                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

   
        return hh;
    }

    /**
     * Attaches the collectors to the instrumented algorithm
     *
     * @param alg
     * @return
     */
    protected InstrumentedAlgorithm instrument(Algorithm alg) {
        refPointObj = problem.newSolution();
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            refPointObj.setObjective(i, 1.1);
        }

        Instrumenter instrumenter = new Instrumenter().withFrequency(maxEvaluations / 100)
                .withProblem(probName)
                .attachAdditiveEpsilonIndicatorCollector()
                .attachGenerationalDistanceCollector()
                .attachInvertedGenerationalDistanceCollector()
                .attachHypervolumeJmetalCollector(refPointObj)
                .withEpsilon(epsilonDouble)
                .withReferenceSet(referenceSet)
                .attachElapsedTimeCollector();

        return instrumenter.instrument(alg);
    }

    /**
     * Saves the indicator values collected during the run.
     *
     * @param instAlgorithm
     * @param filename
     */
    protected void saveIndicatorValues(InstrumentedAlgorithm instAlgorithm, String filename) {
        Accumulator accum = instAlgorithm.getAccumulator();
        if (Boolean.parseBoolean(properties.getProperties().getProperty("saveIndicators"))) {
            File results = new File(filename + ".res");
            System.out.println("Saving results");

            try (FileWriter writer = new FileWriter(results)) {
                Set<String> keys = accum.keySet();
                Iterator<String> keyIter = keys.iterator();
                while (keyIter.hasNext()) {
                    String key = keyIter.next();
                    int dataSize = accum.size(key);
                    writer.append(key).append(",");
                    for (int i = 0; i < dataSize; i++) {
                        writer.append(accum.get(key, i).toString());
                        if (i + 1 < dataSize) {
                            writer.append(",");
                        }
                    }
                    writer.append("\n");
                }

                //also record the final HV
                NondominatedPopulation ndPop = instAlgorithm.getResult();
                FastHypervolume fHV = new FastHypervolume(problem, referenceSet, refPointObj);
                double hv = fHV.evaluate(ndPop);
                writer.append("Final HV, " + hv + "\n");

                //also record the final IGD
                InvertedGenerationalDistance igd = new InvertedGenerationalDistance(problem, referenceSet);
                double figd = igd.evaluate(ndPop);
                writer.append("Final IGD, " + figd + "\n");

                writer.flush();
            } catch (Exception ex) {
                Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
