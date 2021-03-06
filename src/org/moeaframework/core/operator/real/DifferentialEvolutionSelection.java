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
package org.moeaframework.core.operator.real;

import org.moeaframework.core.ParallelPRNG;
import org.moeaframework.core.Population;
import org.moeaframework.core.Selection;
import org.moeaframework.core.Solution;

/**
 * Selection operator to be used in conjunction with the
 * {@link DifferentialEvolution} operator. The {@code select} method returns the
 * solutions {@code [currentIndex, r1, r2, ...]}, where {@code currentIndex} is
 * set using the {@link #setCurrentIndex(int)} method and {@code r1, r2, ...}
 * are randomly selected solutions. The returned solutions are guaranteed to
 * have unique indices in the population, but the solutions themselves may not
 * be unique if the population contains duplicate solutions.
 */
public class DifferentialEvolutionSelection implements Selection {

	/**
	 * The current index.
	 */
	private int currentIndex;
        
        /**
         * parallel purpose random generator
         */
        private final ParallelPRNG pprng;

	/**
	 * Constructs a differential evolution selection operator.
	 */
	public DifferentialEvolutionSelection() {
		super();
                this.pprng = new ParallelPRNG();
	}

	/**
	 * Sets the current index, which is the index of the first solution returned
	 * by the {@code select} method.
	 * 
	 * @param currentIndex the current index
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	@Override
	public Solution[] select(int arity, Population population) {
		if (population.size() < arity) {
			throw new IllegalArgumentException("population too small");
		}

		int[] indices = new int[arity];
		indices[0] = currentIndex;

		for (int i = 1; i < arity; i++) {
			boolean isDuplicate;

			do {
				isDuplicate = false;
				indices[i] = pprng.nextInt(population.size());

				for (int j = 0; j < i; j++) {
					if (indices[i] == indices[j]) {
						isDuplicate = true;
						break;
					}
				}
			} while (isDuplicate);
		}

		Solution[] result = new Solution[arity];
		for (int i = 0; i < arity; i++) {
			result[i] = population.get(indices[i]);
		}

		return result;
	}

}
