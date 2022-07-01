/* Copyright 2009-2015 David Hadka
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
package org.moeaframework.core.operator.Integer;

import java.io.Serializable;
import org.moeaframework.core.ParallelPRNG;
import org.moeaframework.core.Solution;
import org.moeaframework.core.Variable;
import org.moeaframework.core.Variation;
import org.moeaframework.core.variable.RealVariable;

import hh.problem.rwa.IntegerVariable;

/**
 * Uniform mutation (UM) operator.  Each decision variable is mutated by
 * selecting a new value within its bounds uniformly at random.  The figure
 * below depicts the offspring distribution.
 * <p>
 * <img src="doc-files/UM-1.png" alt="Example UM operator distribution" />
 * <p>
 * It is recommended each decision variable is mutated with a probability of
 * {@code 1 / L}, where {@code L} is the number of decision variables.  This
 * results in one mutation per offspring on average.
 * <p>
 * This operator is type-safe.
 */
public class RandOpInteger implements Variation,Serializable{
    private static final long serialVersionUID = 6269864515240886525L;
        
        /**
         * parallel purpose random generator
         */
        private final ParallelPRNG pprng;
        
	/**
	 * Constructs a random search operator.
	 * 
	 */
	public RandOpInteger() {
		super();
        this.pprng = new ParallelPRNG();
	}

	@Override
	public Solution[] evolve(Solution[] parents) {
		Solution result = parents[0].copy();

		for (int i = 0; i < result.getNumberOfVariables(); i++) {
			Variable variable = result.getVariable(i);

			if ((variable instanceof IntegerVariable)) {
				evolve((IntegerVariable)variable);
			}
		}

		return new Solution[] { result };
	}

	/**
	 * Mutates the specified variable using uniform mutation.
	 * 
	 * @param variable the variable to be mutated
	 */
	public void evolve(IntegerVariable variable) {
		variable.setValue(pprng.nextInt(variable.getLowerBound(), variable
				.getUpperBound()));
	}

	@Override
	public int getArity() {
		return 1;
	}

    @Override
    public String toString() {
//        return "UM{" + "prob=" + probability + '}';
        return "RandOpInteger";
    }

}
