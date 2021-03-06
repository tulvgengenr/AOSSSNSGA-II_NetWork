/* Copyright 2009-2016 David Hadka
 *
 * This file is part of the MOEA Framework.
 *
 * The MOEA Framework is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * The MOEA Framework is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MOEA Framework.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.moeaframework.algorithm;

import java.util.Properties;
import hh.moea.R2MOEA;
import hh.moea.SteadyStateIBEA;
import hh.moea.SteadyStateNSGAII;

import org.apache.commons.math3.util.CombinatoricsUtils;
import org.moeaframework.algorithm.pso.OMOPSO;
import org.moeaframework.algorithm.pso.SMPSO;
import org.moeaframework.analysis.sensitivity.EpsilonHelper;
import org.moeaframework.core.Algorithm;
import org.moeaframework.core.EpsilonBoxDominanceArchive;
import org.moeaframework.core.FitnessEvaluator;
import org.moeaframework.core.FrameworkException;
import org.moeaframework.core.Initialization;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.NondominatedSortingPopulation;
import org.moeaframework.core.PRNG;
import org.moeaframework.core.Population;
import org.moeaframework.core.Problem;
import org.moeaframework.core.Selection;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.Variation;
import org.moeaframework.core.comparator.AggregateConstraintComparator;
import org.moeaframework.core.comparator.ChainedComparator;
import org.moeaframework.core.comparator.CrowdingComparator;
import org.moeaframework.core.comparator.DominanceComparator;
import org.moeaframework.core.comparator.ParetoDominanceComparator;
import org.moeaframework.core.fitness.AdditiveEpsilonIndicatorFitnessEvaluator;
import org.moeaframework.core.fitness.HypervolumeContributionFitnessEvaluator;
import org.moeaframework.core.fitness.HypervolumeFitnessEvaluator;
import org.moeaframework.core.fitness.IndicatorFitnessEvaluator;
import org.moeaframework.core.fitness.R2FitnessEvaluator;
import org.moeaframework.core.operator.RandomInitialization;
import org.moeaframework.core.operator.TournamentSelection;
import org.moeaframework.core.operator.UniformSelection;
import org.moeaframework.core.operator.real.DifferentialEvolution;
import org.moeaframework.core.operator.real.DifferentialEvolutionSelection;
import org.moeaframework.core.operator.real.UM;
import org.moeaframework.core.spi.AlgorithmProvider;
import org.moeaframework.core.spi.OperatorFactory;
import org.moeaframework.core.spi.ProviderLookupException;
import org.moeaframework.core.spi.ProviderNotFoundException;
import org.moeaframework.core.variable.RealVariable;
import org.moeaframework.util.TypedProperties;

/**
 * A provider of standard algorithms. The following table contains all
 * available algorithms and the customizable properties.  These properties are
 * tailored for real-valued operators.  If using a different representation,
 * see {@link OperatorFactory} for the appropriate parameters.  See the user
 * manual for a more detailed description of the algorithms and parameters.
 * <p>
 * <table width="100%" border="1" cellpadding="3" cellspacing="0">
 *   <tr class="TableHeadingColor">
 *     <th width="10%" align="left">Name</th>
 *     <th width="10%" align="left">Type</th>
 *     <th width="80%" align="left">Properties</th>
 *   </tr>
 *   <tr>
 *     <td>CMA-ES</td>
 *     <td>Real</td>
 *     <td>{@code lambda, cc, cs, damps, ccov, ccovsep, sigma,
 *         diagonalIterations, indicator, initialSearchPoint}</td>
 *   </tr>
 *   <tr>
 *     <td>DBEA</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, divisions, sbx.rate, sbx.distributionIndex,
 *         pm.rate, pm.distributionIndex} (for the two-layer approach, replace
 *         {@code divisions} by {@code divisionsOuter} and
 *         {@code divisionsInner})</td>
 *   </tr>
 *   <tr>
 *     <td>eMOEA</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, epsilon, sbx.rate,
 *         sbx.distributionIndex, pm.rate, pm.distributionIndex}</td>
 *   </tr>
 *   <tr>
 *     <td>eNSGAII</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, epsilon, sbx.rate,
 *         sbx.distributionIndex, pm.rate, pm.distributionIndex, 
 *         injectionRate, windowSize, maxWindowSize, minimumPopulationSize,
 *         maximumPopulationSize}</td>
 *   </tr>
 *   <tr>
 *     <td>GDE3</td>
 *     <td>Real</td>
 *     <td>{@code populationSize, de.crossoverRate, de.stepSize}</td>
 *   </tr>
 *   <tr>
 *     <td>IBEA</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, sbx.rate, sbx.distributionIndex, pm.rate,
 *         pm.distributionIndex, indicator}</td>
 *   </tr>
 *   <tr>
 *     <td>MOEAD</td>
 *     <td>Real</td>
 *     <td>{@code populationSize, de.crossoverRate, de.stepSize, pm.rate,
 *         pm.distributionIndex, neighborhoodSize, delta, eta, 
 *         updateUtility}</td>
 *   </tr>
 *   <tr>
 *     <td>NSGAII</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, sbx.rate, sbx.distributionIndex,
 *         pm.rate, pm.distributionIndex}</td>
 *   </tr>
 *   
 *   <tr>
 *     <td>NSGAIII</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, divisions, sbx.rate, sbx.distributionIndex,
 *         pm.rate, pm.distributionIndex} (for the two-layer approach, replace
 *         {@code divisions} by {@code divisionsOuter} and
 *         {@code divisionsInner})</td>
 *   </tr>
 *   <tr>
 *     <td>OMOPSO</td>
 *     <td>Real</td>
 *     <td>{@code populationSize, archiveSize, maxEvaluations,
 *         mutationProbability, perturbationIndex, epsilon}</td>
 *   </tr>
 *   <tr>
 *     <td>PAES</td>
 *     <td>Any</td>
 *     <td>{@code archiveSize, bisections, pm.rate, pm.distributionIndex}</td>
 *   </tr>
 *   <tr>
 *     <td>PESA2</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, archiveSize, bisections, sbx.rate,
 *         sbx.distributionIndex, pm.rate, pm.distributionIndex}</td>
 *   </tr>
 *   <tr>
 *     <td>Random</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, epsilon}</td>
 *   </tr>
 *   <tr>
 *     <td>SMPSO</td>
 *     <td>Real</td>
 *     <td>{@code populationSize, archiveSize, pm.rate,
 *         pm.distributionIndex}</td>
 *   </tr>
 *   <tr>
 *     <td>SMS-EMOA</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, offset, sbx.rate, sbx.distributionIndex,
 *         pm.rate, pm.distributionIndex}</td>
 *   </tr>
 *   <tr>
 *     <td>SPEA2</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, offspringSize, k, sbx.rate,
 *         sbx.distributionIndex, pm.rate, pm.distributionIndex}</td>
 *   </tr>
 *   <tr>
 *     <td>VEGA</td>
 *     <td>Any</td>
 *     <td>{@code populationSize, sbx.rate, sbx.distributionIndex, pm.rate,
 *         pm.distributionIndex}</td>
 *   </tr>
 * </table>
 */
public class StandardAlgorithms extends AlgorithmProvider {

	/**
	 * Constructs the standard algorithm provider.
	 */
	public StandardAlgorithms() {
		super();
	}

	@Override
	public Algorithm getAlgorithm(String name, Properties properties,
			Problem problem) {
		TypedProperties typedProperties = new TypedProperties(properties);

		try {
			if (name.equalsIgnoreCase("MOEAD") ||
					name.equalsIgnoreCase("MOEA/D")) {
				return newMOEAD(typedProperties, problem);
			} else if (name.equalsIgnoreCase("GDE3")) {
				return newGDE3(typedProperties, problem);
			} else if (name.equalsIgnoreCase("NSGAII") ||
					name.equalsIgnoreCase("NSGA-II") ||
					name.equalsIgnoreCase("NSGA2")) {
				return newNSGAII(typedProperties, problem);
                        } else if (name.equalsIgnoreCase("SSNSGAII")) {
				return newSteadyStateNSGAII(typedProperties, problem);
			} else if (name.equalsIgnoreCase("NSGAIII") ||
					name.equalsIgnoreCase("NSGA-III") ||
					name.equalsIgnoreCase("NSGA3")) {
				return newNSGAIII(typedProperties, problem);
			} else if (name.equalsIgnoreCase("eNSGAII") ||
					name.equalsIgnoreCase("e-NSGA-II") ||
					name.equalsIgnoreCase("eNSGA2")) {
				return neweNSGAII(typedProperties, problem);
			} else if (name.equalsIgnoreCase("eMOEA")) {
				return neweMOEA(typedProperties, problem);
			} else if (name.equalsIgnoreCase("CMA-ES") ||
					name.equalsIgnoreCase("CMAES") ||
					name.equalsIgnoreCase("MO-CMA-ES")) {
				return newCMAES(typedProperties, problem);
			} else if (name.equalsIgnoreCase("SPEA2")) {
				return newSPEA2(typedProperties, problem);
			} else if (name.equalsIgnoreCase("PAES")) {
				return newPAES(typedProperties, problem);
			} else if (name.equalsIgnoreCase("PESA2")) {
				return newPESA2(typedProperties, problem);
			} else if (name.equalsIgnoreCase("OMOPSO")) {
				return newOMOPSO(typedProperties, problem);
			} else if (name.equalsIgnoreCase("SMPSO")) {
				return newSMPSO(typedProperties, problem);
			} else if (name.equalsIgnoreCase("IBEA")) {
				return newIBEA(typedProperties, problem);
                        } else if (name.equalsIgnoreCase("SSIBEA")) {
                                return newSteadyStateIBEA(typedProperties, problem);
                        } else if (name.equalsIgnoreCase("R2MOEA")){
                                return newR2MOEA(typedProperties, problem);
			} else if (name.equalsIgnoreCase("SMSEMOA") ||
					name.equalsIgnoreCase("SMS-EMOA")) {
				return newSMSEMOA(typedProperties, problem);
                        }else if (name.equalsIgnoreCase("R2EMOA")) {
                                return newR2EMOA(typedProperties, problem);
			} else if (name.equalsIgnoreCase("VEGA")) {
				return newVEGA(typedProperties, problem);
			} else if (name.equalsIgnoreCase("DBEA") ||
					name.equalsIgnoreCase("I-DBEA")) {
				return newDBEA(typedProperties, problem);
			} else if (name.equalsIgnoreCase("Random")) {
				return newRandomSearch(typedProperties, problem);
			} else {
				return null;
			}
		} catch (FrameworkException e) {
			throw new ProviderNotFoundException(name, e);
		}
	}
	
	/**
	 * Returns {@code true} if all decision variables are assignment-compatible
	 * with the specified type; {@code false} otherwise.
	 * 
	 * @param type the type of decision variable
	 * @param problem the problem
	 * @return {@code true} if all decision variables are assignment-compatible
	 *         with the specified type; {@code false} otherwise
	 */
	private boolean checkType(Class<? extends Variable> type, Problem problem) {
		Solution solution = problem.newSolution();
		
		for (int i=0; i<solution.getNumberOfVariables(); i++) {
			if (!type.isInstance(solution.getVariable(i))) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Returns a new {@link eMOEA} instance.
	 * 
	 * @param properties the properties for customizing the new {@code eMOEA}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code eMOEA} instance
	 */
	private Algorithm neweMOEA(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Population population = new Population();

		DominanceComparator comparator = new ParetoDominanceComparator();

		EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(
				properties.getDoubleArray("epsilon", 
						new double[] { EpsilonHelper.getEpsilon(problem) }));

		final TournamentSelection selection = new TournamentSelection(
				2, comparator);
		
		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		EpsilonMOEA emoea = new EpsilonMOEA(problem, population, archive,
				selection, variation, initialization, comparator);

		return emoea;
	}

	/**
	 * Returns a new {@link NSGAII} instance.
	 * 
	 * @param properties the properties for customizing the new {@code NSGAII}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code NSGAII} instance
	 */
	private Algorithm newNSGAII(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		NondominatedSortingPopulation population = 
				new NondominatedSortingPopulation();

		TournamentSelection selection = new TournamentSelection(2, 
				new ChainedComparator(
						new ParetoDominanceComparator(),
						new CrowdingComparator()));

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		return new NSGAII(problem, population, null, selection, variation,
				initialization);
	}
           /**
	 * Returns a new {@link NSGAII} instance.
	 * 
	 * @param properties the properties for customizing the new {@code NSGAII}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code NSGAII} instance
	 */
	private Algorithm newSteadyStateNSGAII(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		NondominatedSortingPopulation population = 
				new NondominatedSortingPopulation();

		TournamentSelection selection = new TournamentSelection(2, 
				new ChainedComparator(
						new ParetoDominanceComparator(),
						new CrowdingComparator()));

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		return new SteadyStateNSGAII(problem, population, null, selection, variation,
				initialization);
	}
        
	
	/**
	 * Returns a new {@link NSGAIII} instance.
	 * 
	 * @param properties the properties for customizing the new {@code NSGAIII}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code NSGAIII} instance
	 */
	private Algorithm newNSGAIII(TypedProperties properties, Problem problem) {
		int divisionsOuter = 4;
		int divisionsInner = 0;
		
		if (properties.contains("divisionsOuter") && properties.contains("divisionsInner")) {
			divisionsOuter = (int)properties.getDouble("divisionsOuter", 4);
			divisionsInner = (int)properties.getDouble("divisionsInner", 0);
		} else if (properties.contains("divisions")){
			divisionsOuter = (int)properties.getDouble("divisions", 4);
		} else if (problem.getNumberOfObjectives() == 1) {
			divisionsOuter = 100;
		} else if (problem.getNumberOfObjectives() == 2) {
			divisionsOuter = 99;
		} else if (problem.getNumberOfObjectives() == 3) {
			divisionsOuter = 12;
		} else if (problem.getNumberOfObjectives() == 4) {
			divisionsOuter = 8;
		} else if (problem.getNumberOfObjectives() == 5) {
			divisionsOuter = 6;
		} else if (problem.getNumberOfObjectives() == 6) {
			divisionsOuter = 5;
		} else if (problem.getNumberOfObjectives() == 7) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 8) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 9) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 10) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else {
			divisionsOuter = 2;
			divisionsInner = 1;
		}
		
		int populationSize;
		
		if (properties.contains("populationSize")) {
			populationSize = (int)properties.getDouble("populationSize", 100);
		} else {
			// compute number of reference points
			populationSize = (int)(CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsOuter - 1, divisionsOuter) +
					(divisionsInner == 0 ? 0 : CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsInner - 1, divisionsInner)));

			// round up to a multiple of 4
			populationSize = (int)Math.ceil(populationSize / 4d) * 4;
		}
		
		Initialization initialization = new RandomInitialization(problem,
				populationSize);
		
		ReferencePointNondominatedSortingPopulation population = new ReferencePointNondominatedSortingPopulation(
				problem.getNumberOfObjectives(), divisionsOuter, divisionsInner);

		Selection selection = null;
		
		if (problem.getNumberOfConstraints() == 0) {
			selection = new Selection() {
	
				@Override
				public Solution[] select(int arity, Population population) {
					Solution[] result = new Solution[arity];
					
					for (int i = 0; i < arity; i++) {
						result[i] = population.get(PRNG.nextInt(population.size()));
					}
					
					return result;
				}
				
			};
		} else {
			selection = new TournamentSelection(2, new ChainedComparator(
					new AggregateConstraintComparator(),
					new DominanceComparator() {

						@Override
						public int compare(Solution solution1, Solution solution2) {
							return PRNG.nextBoolean() ? -1 : 1;
						}
						
					}));
		}

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		return new NSGAII(problem, population, null, selection, variation,
				initialization);
	}

	/**
	 * Returns a new {@link MOEAD} instance.  Only real encodings are supported.
	 * 
	 * @param properties the properties for customizing the new {@code MOEAD}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code MOEAD} instance
	 * @throws FrameworkException if the decision variables are not real valued
	 */
	private Algorithm newMOEAD(TypedProperties properties, Problem problem) {
		if (!checkType(RealVariable.class, problem)) {
			throw new FrameworkException("unsupported decision variable type");
		}
		
		int populationSize = (int)properties.getDouble("populationSize", 100);
		
		//enforce population size lower bound
		if (populationSize < problem.getNumberOfObjectives()) {
			System.err.println("increasing MOEA/D population size");
			populationSize = problem.getNumberOfObjectives();
		}

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		String operator = properties.getString("operator", "de+pm");
		Variation variation = OperatorFactory.getInstance().getVariation(
				operator, properties, problem);
		
		int neighborhoodSize = 20;
		int eta = 2;
		
		if (properties.contains("neighborhoodSize")) {
			neighborhoodSize = (int)properties.getDouble("neighborhoodSize", 20);
		}
		
		if (properties.contains("eta")) {
			eta = (int)properties.getDouble("eta", 2);
		}

		MOEAD algorithm = new MOEAD(
				problem,
				neighborhoodSize,
				initialization,
				variation,
				properties.getDouble("delta", 0.9),
				eta,
				(int)properties.getDouble("updateUtility", -1));

		return algorithm;
	}

	/**
	 * Returns a new {@link GDE3} instance.  Only real encodings are supported.
	 * 
	 * @param properties the properties for customizing the new {@code GDE3}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code GDE3} instance
	 * @throws FrameworkException if the decision variables are not real valued
	 */
	private Algorithm newGDE3(TypedProperties properties, Problem problem) {
		if (!checkType(RealVariable.class, problem)) {
			throw new FrameworkException("unsupported decision variable type");
		}
		
		int populationSize = (int)properties.getDouble("populationSize", 100);
		
		DominanceComparator comparator = new ParetoDominanceComparator();

		NondominatedSortingPopulation population = 
				new NondominatedSortingPopulation(comparator);

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		DifferentialEvolutionSelection selection = 
				new DifferentialEvolutionSelection();

		DifferentialEvolution variation = (DifferentialEvolution)OperatorFactory
				.getInstance().getVariation("de", properties, problem);

		return new GDE3(problem, population, comparator, selection, variation,
				initialization);
	}

	/**
	 * Returns a new {@link eNSGAII} instance.
	 * 
	 * @param properties the properties for customizing the new {@code eNSGAII}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code eNSGAII} instance
	 */
	private Algorithm neweNSGAII(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		NondominatedSortingPopulation population = 
				new NondominatedSortingPopulation(
						new ParetoDominanceComparator());

		EpsilonBoxDominanceArchive archive = new EpsilonBoxDominanceArchive(
				properties.getDoubleArray("epsilon", 
						new double[] { EpsilonHelper.getEpsilon(problem) }));

		TournamentSelection selection = new TournamentSelection(2, 
				new ChainedComparator(
						new ParetoDominanceComparator(),
						new CrowdingComparator()));

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		NSGAII nsgaii = new NSGAII(problem, population, archive, selection,
				variation, initialization);

		AdaptiveTimeContinuation algorithm = new AdaptiveTimeContinuation(
				nsgaii,
				properties.getInt("windowSize", 100),
				Math.max(properties.getInt("windowSize", 100),
						 properties.getInt("maxWindowSize", 100)),
				1.0 / properties.getDouble("injectionRate", 0.25),
				properties.getInt("minimumPopulationSize", 100),
				properties.getInt("maximumPopulationSize", 10000),
				new UniformSelection(),
				new UM(1.0));

		return algorithm;
	}
	
	/**
	 * Returns a new {@link CMAES} instance.
	 * 
	 * @param properties the properties for customizing the new {@code CMAES}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code CMAES} instance
	 */
	private Algorithm newCMAES(TypedProperties properties, Problem problem) {
		if (!checkType(RealVariable.class, problem)) {
			throw new FrameworkException("unsupported decision variable type");
		}
		
		int lambda = (int)properties.getDouble("lambda", 100);
		double cc = properties.getDouble("cc", -1.0);
		double cs = properties.getDouble("cs", -1.0);
		double damps = properties.getDouble("damps", -1.0);
		double ccov = properties.getDouble("ccov", -1.0);
		double ccovsep = properties.getDouble("ccovsep", -1.0);
		double sigma = properties.getDouble("sigma", -1.0);
		int diagonalIterations = (int)properties.getDouble("diagonalIterations", 0);
		String indicator = properties.getString("indicator", "crowding");
		double[] initialSearchPoint = properties.getDoubleArray("initialSearchPoint", null);
		NondominatedPopulation archive = null;
		FitnessEvaluator fitnessEvaluator = null;
		
		if (problem.getNumberOfObjectives() == 1) {
			archive = new NondominatedPopulation();
		} else {
			archive = new EpsilonBoxDominanceArchive(
					properties.getDoubleArray("epsilon", 
							new double[] { EpsilonHelper.getEpsilon(problem) }));
		}
		
		if ("hypervolume".equals(indicator)) {
			fitnessEvaluator = new HypervolumeFitnessEvaluator(problem);
		} else if ("epsilon".equals(indicator)) {
			fitnessEvaluator = new AdditiveEpsilonIndicatorFitnessEvaluator(problem);
		}
		
		CMAES cmaes = new CMAES(problem, lambda, fitnessEvaluator, archive,
				initialSearchPoint, false, cc, cs, damps, ccov, ccovsep, sigma,
				diagonalIterations);

		return cmaes;
	}
	
	/**
	 * Returns a new {@link SPEA2} instance.
	 * 
	 * @param properties the properties for customizing the new {@code SPEA2}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code SPEA2} instance
	 */
	private Algorithm newSPEA2(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);
		int offspringSize = (int)properties.getDouble("offspringSize", 100);
		int k = (int)properties.getDouble("k", 1);
		
		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		return new SPEA2(problem, initialization, variation, offspringSize, k);
	}
	
	/**
	 * Returns a new {@link PAES} instance.
	 * 
	 * @param properties the properties for customizing the new {@code PAES}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code PAES} instance
	 */
	private Algorithm newPAES(TypedProperties properties, Problem problem) {
		int archiveSize = (int)properties.getDouble("archiveSize", 100);
		int bisections = (int)properties.getDouble("bisections", 8);

		Variation variation = OperatorFactory.getInstance().getVariation(
				OperatorFactory.getInstance().getDefaultMutation(problem), 
				properties,
				problem);

		return new PAES(problem, variation, bisections, archiveSize);
	}
	
	/**
	 * Returns a new {@link PESA2} instance.
	 * 
	 * @param properties the properties for customizing the new {@code PESA2}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code PESA2} instance
	 */
	private Algorithm newPESA2(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);
		int archiveSize = (int)properties.getDouble("archiveSize", 100);
		int bisections = (int)properties.getDouble("bisections", 8);
		
		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		return new PESA2(problem, variation, initialization, bisections, archiveSize);
	}
	
	/**
	 * Returns a new {@link OMOPSO} instance.
	 * 
	 * @param properties the properties for customizing the new {@code OMOPSO}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code OMOPSO} instance
	 */
	private Algorithm newOMOPSO(TypedProperties properties, Problem problem) {
		if (!checkType(RealVariable.class, problem)) {
			throw new FrameworkException("unsupported decision variable type");
		}
		
		int populationSize = (int)properties.getDouble("populationSize", 100);
		int archiveSize = (int)properties.getDouble("archiveSize", 100);
		int maxIterations = (int)properties.getDouble("maxEvaluations", 25000) /
				populationSize;
		double mutationProbability = properties.getDouble("mutationProbability",
				1.0 / problem.getNumberOfVariables());
		double perturbationIndex = properties.getDouble("perturbationIndex",
				0.5);
		double[] epsilon = properties.getDoubleArray("epsilon",
				new double[] { EpsilonHelper.getEpsilon(problem) });
		
		return new OMOPSO(problem, populationSize, archiveSize,
				epsilon, mutationProbability, perturbationIndex, maxIterations);
	}
	
	/**
	 * Returns a new {@link SMPSO} instance.
	 * 
	 * @param properties the properties for customizing the new {@code SMPSO}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code SMPSO} instance
	 */
	private Algorithm newSMPSO(TypedProperties properties, Problem problem) {
		if (!checkType(RealVariable.class, problem)) {
			throw new FrameworkException("unsupported decision variable type");
		}
		
		int populationSize = (int)properties.getDouble("populationSize", 100);
		int archiveSize = (int)properties.getDouble("archiveSize", 100);
		double mutationProbability = properties.getDouble("pm.rate",
				1.0 / problem.getNumberOfVariables());
		double distributionIndex = properties.getDouble("pm.distributionIndex",
				20.0);
		
		return new SMPSO(problem, populationSize, archiveSize,
				mutationProbability, distributionIndex);
	}
	
	/**
	 * Returns a new {@link IBEA} instance.
	 * 
	 * @param properties the properties for customizing the new {@code IBEA}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code IBEA} instance
	 */
	private Algorithm newIBEA(TypedProperties properties, Problem problem) {
		if (problem.getNumberOfConstraints() > 0) {
			throw new ProviderNotFoundException("IBEA", 
					new ProviderLookupException("constraints not supported"));
		}
		
		int populationSize = (int)properties.getDouble("populationSize", 100);
		String indicator = properties.getString("indicator", "hypervolume");
		IndicatorFitnessEvaluator fitnessEvaluator = null;

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);
		
		if ("hypervolume".equals(indicator)) {
			fitnessEvaluator = new HypervolumeFitnessEvaluator(problem);
		} else if ("epsilon".equals(indicator)) {
			fitnessEvaluator = new AdditiveEpsilonIndicatorFitnessEvaluator(
					problem);
                } else if ("r2".equals(indicator)) {
                        int numVectors =  properties.getInt("r2.numberVectors", 100);
			fitnessEvaluator = new R2FitnessEvaluator(problem, numVectors, 1);
		} else {
			throw new IllegalArgumentException("invalid indicator: " +
					indicator);
		}

		return new IBEA(problem, null, initialization, variation,
				fitnessEvaluator);
	}
        
        private Algorithm newSteadyStateIBEA(TypedProperties properties, Problem problem) {
        int populationSize = (int)properties.getDouble("populationSize", 100);
    
        Initialization initialization = new RandomInitialization(problem,
				populationSize);
        
        Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);
        
        HypervolumeFitnessEvaluator fitnessEvaluator = new HypervolumeFitnessEvaluator(problem);
        
        return new SteadyStateIBEA(problem, null, initialization, variation, fitnessEvaluator);
    }
        
        private Algorithm newR2MOEA(TypedProperties properties, Problem problem) {
        int populationSize = (int)properties.getDouble("populationSize", 100);
        
        int numberOffspring = (int)properties.getDouble("numberOffspring", 1);
        
        int numVecs = (int)properties.getDouble("numberVectors", 100);

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		return new R2MOEA(problem, numberOffspring, numVecs, null,initialization, variation);
    }
	
	/**
	 * Returns a new {@link SMSEMOA} instance.
	 * 
	 * @param properties the properties for customizing the new {@code SMSEMOA}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code SMSEMOA} instance
	 */
	private Algorithm newSMSEMOA(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);
		double offset = properties.getDouble("offset", 100.0);
		String indicator = properties.getString("indicator", "hypervolume");
		FitnessEvaluator fitnessEvaluator = null;
		
		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);
		
		if ("hypervolume".equals(indicator)) {
			fitnessEvaluator = new HypervolumeContributionFitnessEvaluator(
					problem, offset);
		}

		return new SMSEMOA(problem, initialization, variation,
				fitnessEvaluator);
	}
        
         private Algorithm newR2EMOA(TypedProperties properties, Problem problem) {
        int populationSize = (int)properties.getDouble("populationSize", 100);
        
        double offset = properties.getDouble("R2offset", 1);
        
        int numVecs = (int)properties.getProperties().get("r2.numberVectors");

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);

		return new SMSEMOA(problem, initialization, variation, new R2FitnessEvaluator(problem, numVecs, offset));
    }

	
	/**
	 * Returns a new {@link VEGA} instance.
	 * 
	 * @param properties the properties for customizing the new {@code VEGA}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code VEGA} instance
	 */
	private Algorithm newVEGA(TypedProperties properties, Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);

		Initialization initialization = new RandomInitialization(problem,
				populationSize);

		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);
		return new VEGA(problem, new Population(), null, initialization,
				variation);
	}
	
	/**
	 * Returns a new {@link DBEA} instance.
	 * 
	 * @param properties the properties for customizing the new {@code DBEA}
	 *        instance
	 * @param problem the problem
	 * @return a new {@code DBEA} instance
	 */
	private Algorithm newDBEA(TypedProperties properties, Problem problem) {
		int divisionsOuter = 4;
		int divisionsInner = 0;
		
		if (properties.contains("divisionsOuter") && properties.contains("divisionsInner")) {
			divisionsOuter = (int)properties.getDouble("divisionsOuter", 4);
			divisionsInner = (int)properties.getDouble("divisionsInner", 0);
		} else if (properties.contains("divisions")){
			divisionsOuter = (int)properties.getDouble("divisions", 4);
		} else if (problem.getNumberOfObjectives() == 1) {
			divisionsOuter = 100;
		} else if (problem.getNumberOfObjectives() == 2) {
			divisionsOuter = 99;
		} else if (problem.getNumberOfObjectives() == 3) {
			divisionsOuter = 12;
		} else if (problem.getNumberOfObjectives() == 4) {
			divisionsOuter = 8;
		} else if (problem.getNumberOfObjectives() == 5) {
			divisionsOuter = 6;
		} else if (problem.getNumberOfObjectives() == 6) {
			divisionsOuter = 5;
		} else if (problem.getNumberOfObjectives() == 7) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 8) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 9) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else if (problem.getNumberOfObjectives() == 10) {
			divisionsOuter = 3;
			divisionsInner = 2;
		} else {
			divisionsOuter = 2;
			divisionsInner = 1;
		}
		
		int populationSize;
		
		if (properties.contains("populationSize")) {
			populationSize = (int)properties.getDouble("populationSize", 100);
		} else {
			// compute number of reference points
			populationSize = (int)(CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsOuter - 1, divisionsOuter) +
					(divisionsInner == 0 ? 0 : CombinatoricsUtils.binomialCoefficient(problem.getNumberOfObjectives() + divisionsInner - 1, divisionsInner)));
		}
		
		Initialization initialization = new RandomInitialization(problem,
				populationSize);
		
		Variation variation = OperatorFactory.getInstance().getVariation(null, 
				properties, problem);
		
		return new DBEA(problem, initialization,
				variation, divisionsOuter, divisionsInner);
	}
	
	/**
	 * Returns a new {@link RandomSearch} instance.
	 * 
	 * @param properties the properties for customizing the new
	 *        {@code RandomSearch} instance
	 * @param problem the problem
	 * @return a new {@code RandomSearch} instance
	 */
	private Algorithm newRandomSearch(TypedProperties properties, 
			Problem problem) {
		int populationSize = (int)properties.getDouble("populationSize", 100);
		
		Initialization generator = new RandomInitialization(problem,
				populationSize);
		
		NondominatedPopulation archive = null;
		
		if (properties.contains("epsilon")) {
			archive = new EpsilonBoxDominanceArchive(
					properties.getDoubleArray("epsilon", new double[] {
							EpsilonHelper.getEpsilon(problem) }));
		} else {
			archive = new NondominatedPopulation();
		}
		
		return new RandomSearch(problem, generator, archive);
	}

}
