/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hh.creditassignment.offspringpopulation;

import hh.creditassigment.AbstractRewardDefintion;
import hh.creditassigment.CreditFunctionInputType;
import org.moeaframework.core.Population;
import org.moeaframework.core.Solution;

/**
 * Class defining the inputType for reward definition based on comparing the offspring solution to a population/archive
 * @author nozomihitomi
 */
public abstract class AbstractOffspringPopulation extends AbstractRewardDefintion{

    public AbstractOffspringPopulation(){
        inputType = CreditFunctionInputType.SI;
    }
    
    /**
     * Computes the credit of an offspring solution with respect to some archive
     * @param offspring solution that will receive credits
     * @param population the population to compare the offspring solutions with
     * @return the value of credit to resulting from the solution
     */
    public abstract double compute(Solution offspring, Population population);
}
