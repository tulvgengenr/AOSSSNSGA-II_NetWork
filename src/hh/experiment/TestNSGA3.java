package hh.experiment;

import hh.moea.SteadyStateNSGAII;
import hh.problem.rwa.Network1;
import hh.problem.rwa.RWA;
import org.moeaframework.algorithm.MOEAD;
import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.algorithm.ReferencePointNondominatedSortingPopulation;
import org.moeaframework.algorithm.SPEA2;
import org.moeaframework.analysis.collector.InstrumentedAlgorithm;
import org.moeaframework.core.*;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.operator.GAVariation;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.util.TypedProperties;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static hh.experiment.RunSSNSGAII.instrument;

public class TestNSGA3 {
    public static void main(String[] args) {

        // 种群最大数量
        int populationSize = 120;
        int maxEvaluations = 10000;

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
        // 将待解决问题转化为遗传算法的输入格式

        Properties properties = new Properties();
        properties.put("populationSize", populationSize);
        properties.put("saveFolder", "results2");
        properties.put("saveIndicators", "true");
        properties.put("saveFinalPopulation", "true");

        TypedProperties prop = new TypedProperties(properties);

        for(int j=0;j<5;j++){

            for(int i=3;i<4;i++) {

                // 定义遗传算法所有算子，选择算子为“1x”，变异算子为“umIe”
                OperatorFactory of = OperatorFactory.getInstance();
                Initialization initialization;
                NondominatedSortingPopulation population;
                TournamentSelection selection = new TournamentSelection(2,
                        new ChainedComparator(new ParetoDominanceComparator(),
                                new CrowdingComparator()));
                EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(0.5);

                NSGAII nsgaii;

                Network1 network = new Network1(problems[i]);
                RWA problem = new RWA(3, maxEvaluations,network);
                GAVariation ga1 = new GAVariation(of.getVariation("1x", prop, problem), of.getVariation("umIe", prop, problem));

                //String directory = "result1";
                String directory = "temp";
                try {
                    initialization = new RandomInitialization(problem, populationSize);
                    // 这里是修改部分
                    // 将NSGA-II的非支配层换为带有参考点的非支配层
                    // population = new NondominatedSortingPopulation();
                    // numberOfObjectves：目标数，3
                    // divisonsOuter,divisonsinner 不清楚，假设为1
                    population = new ReferencePointNondominatedSortingPopulation(3, 1,1);

                    nsgaii = new NSGAII(problem, population, archive, selection, ga1, initialization);

                    InstrumentedAlgorithm instAlgorithmnsga2op1 = instrument(nsgaii, maxEvaluations, problem);

                    System.out.println("Starting nsgaiii-rwa");
                    try {
                        long t1 = System.currentTimeMillis();
                        while (!instAlgorithmnsga2op1.isTerminated() && (instAlgorithmnsga2op1.getNumberOfEvaluations() < maxEvaluations)) {
                            //System.out.println("*****");
                            instAlgorithmnsga2op1.step();
//                            if (instAlgorithmnsga2op1.getNumberOfEvaluations() % 100 == 0) {
//                                System.out.println(instAlgorithmnsga2op1.getNumberOfEvaluations());
//                            }
                        }
                        long t2 = System.currentTimeMillis();
                        long t = t2 - t1;
                        System.out.println("*******************nsgaiii:   " + t);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    nsgaii.terminate();

                    String filenamenop1 = directory +  File.separator + "nsga3-rwa";

                    if (Boolean.parseBoolean(prop.getString("saveFinalPopulation","true"))) {
                        NondominatedPopulation ndPop = instAlgorithmnsga2op1.getResult();
                        System.out.println(ndPop.size());

                        try {
                            PopulationIO.writeObjectives(new File(filenamenop1 + (i+1) + (j+1) + ".txt"), ndPop);

                        } catch (IOException ex) {
                            Logger.getLogger(TestRun.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
