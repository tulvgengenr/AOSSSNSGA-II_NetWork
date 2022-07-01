//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.moeaframework.problem.DTLZ;

import org.moeaframework.core.PRNG;
import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.EncodingUtils;
import org.moeaframework.core.variable.RealVariable;

/**
 * DTLZ6 test problem copied from jMetal but modified to be compatible with
 * MOEAframework
 *
 * @author SEAK2
 */
public class DTLZ6 extends DTLZ {

    /**
     * Constructs a DTLZ6 test problem with the specified number of objectives.
     * This is equivalent to calling {@code new DTLZ6(numberOfObjectives+9,
     * numberOfObjectives)}
     *
     * @param numberOfObjectives the number of objectives for this problem
     */
    public DTLZ6(int numberOfObjectives) {
        this(numberOfObjectives + 9, numberOfObjectives);
    }

    /**
     * Constructs a DTLZ6 test problem with the specified number of variables
     * and objectives.
     *
     * @param numberOfVariables the number of variables for this problem
     * @param numberOfObjectives the number of objectives for this problem
     */
    public DTLZ6(int numberOfVariables, int numberOfObjectives) {
        super(numberOfVariables, numberOfObjectives);
    }

    @Override
    public void evaluate(Solution solution) {
        double[] theta = new double[numberOfObjectives - 1];

        double[] f = new double[numberOfObjectives];
        double[] x = EncodingUtils.getReal(solution);

        int k = numberOfVariables - numberOfObjectives + 1;

        double g = 0.0;
        for (int i = numberOfVariables - k; i < numberOfVariables; i++) {
            g += java.lang.Math.pow(x[i], 0.1);
        }

        double t = java.lang.Math.PI / (4.0 * (1.0 + g));
        theta[0] = x[0] * java.lang.Math.PI / 2;
        for (int i = 1; i < (numberOfObjectives - 1); i++) {
            theta[i] = t * (1.0 + 2.0 * g * x[i]);
        }

        for (int i = 0; i < numberOfObjectives; i++) {
            f[i] = 1.0 + g;

            for (int j = 0; j < numberOfObjectives - (i + 1); j++) {
                f[i] *= java.lang.Math.cos(theta[j]);
            }
            if (i != 0) {
                int aux = numberOfObjectives - (i + 1);
                f[i] *= java.lang.Math.sin(theta[aux]);
            }

            solution.setObjective(i, f[i]);
        }
    }

    @Override
    public Solution generate() {
        Solution solution = newSolution();

        for (int i = 0; i < numberOfObjectives - 1; i++) {
            ((RealVariable) solution.getVariable(i)).setValue(PRNG.nextDouble());
        }

        for (int i = numberOfObjectives - 1; i < numberOfVariables; i++) {
            ((RealVariable) solution.getVariable(i)).setValue(0);
        }

        evaluate(solution);

        return solution;
    }
}
