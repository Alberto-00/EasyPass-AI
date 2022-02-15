package it.unisa.fix;

import org.uma.jmetal.operator.mutation.MutationOperator;
import org.uma.jmetal.problem.integerproblem.IntegerProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.solution.util.repairsolution.RepairDoubleSolution;
import org.uma.jmetal.solution.util.repairsolution.impl.RepairDoubleSolutionWithBoundValue;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

public class DoublePolynomialMutation implements MutationOperator<DoubleSolution> {
    private static final double DEFAULT_PROBABILITY = 0.01D;
    private static final double DEFAULT_DISTRIBUTION_INDEX = 20.0D;
    private double distributionIndex;
    private double mutationProbability;
    private final RepairDoubleSolution solutionRepair;
    private final RandomGenerator<Double> randomGenerator;

    /** Constructor */
    public DoublePolynomialMutation() {
        this(DEFAULT_PROBABILITY, DEFAULT_DISTRIBUTION_INDEX) ;
    }

    /** Constructor */
    public DoublePolynomialMutation(IntegerProblem problem, double distributionIndex) {
        this(1.0/problem.getNumberOfVariables(), distributionIndex) ;
    }

    /** Constructor */
    public DoublePolynomialMutation(double mutationProbability, double distributionIndex) {
        this(mutationProbability, distributionIndex, new RepairDoubleSolutionWithBoundValue()) ;
    }

    /** Constructor */
    public DoublePolynomialMutation(double mutationProbability, double distributionIndex,
                                     RepairDoubleSolution solutionRepair) {
        this(mutationProbability, distributionIndex, solutionRepair, () -> JMetalRandom.getInstance().nextDouble());
    }

    public DoublePolynomialMutation(double mutationProbability, double distributionIndex,
                                    RepairDoubleSolution solutionRepair, RandomGenerator<Double> randomGenerator) {
        if (mutationProbability < 0.0D) {
            throw new JMetalException("Mutation probability is negative: " + mutationProbability);
        } else if (distributionIndex < 0.0D) {
            throw new JMetalException("Distribution index is negative: " + distributionIndex);
        } else {
            this.mutationProbability = mutationProbability;
            this.distributionIndex = distributionIndex;
            this.solutionRepair = solutionRepair;
            this.randomGenerator = randomGenerator;
        }
    }

    public double getMutationProbability() {
        return this.mutationProbability;
    }

    public double getDistributionIndex() {
        return this.distributionIndex;
    }

    public void setDistributionIndex(double distributionIndex) {
        this.distributionIndex = distributionIndex;
    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    public DoubleSolution execute(DoubleSolution solution) throws JMetalException {
        if (null == solution) {
            throw new JMetalException("Null parameter");
        } else {
            this.doMutation(this.mutationProbability, solution);
            return solution;
        }
    }

    private void doMutation(double probability, DoubleSolution solution) {
        double rnd, delta1, delta2, mutPow, deltaq;
        double y, yl, yu, val, xy;

        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            if (randomGenerator.getRandomValue() <= probability) {
                y = Math.floor(solution.getVariable(i));
                yl = solution.getLowerBound(i);
                yu = solution.getUpperBound(i);
                if (yl == yu) {
                    y = yl ;
                } else {
                    delta1 = (y - yl) / (yu - yl);
                    delta2 = (yu - y) / (yu - yl);
                    rnd = randomGenerator.getRandomValue();
                    mutPow = 1.0 / (distributionIndex + 1.0);
                    if (rnd <= 0.5) {
                        xy = 1.0 - delta1;
                        val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = Math.pow(val, mutPow) - 1.0;
                    } else {
                        xy = 1.0 - delta2;
                        val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = 1.0 - Math.pow(val, mutPow);
                    }
                    y = y + deltaq * (yu - yl);
                    y = solutionRepair.repairSolutionVariableValue(y, yl, yu);
                }
                solution.setVariable(i, Math.floor(y));
            }
        }

    }
}
