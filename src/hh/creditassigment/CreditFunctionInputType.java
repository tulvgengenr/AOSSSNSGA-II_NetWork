/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hh.creditassigment;

/**
 * The enum for the different types of credits used
 *
 * @author nozomihitomi
 */
public enum CreditFunctionInputType {
    /**
     * If the reward is assigned based on the solution quality improvement of the offspring over the parent solution
     */
    OP,
    /**
     * If the reward is assigned based on the quality improvement of the nondominated set with the insertion of a single offspring solution
     */
    SI,
    /**
     * If the reward is assigned based on the contribution of an operator to a nondominated set
     */
    CS
}
