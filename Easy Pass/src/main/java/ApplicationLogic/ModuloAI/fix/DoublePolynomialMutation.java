package ApplicationLogic.ModuloAI.fix;

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
        double x, y, first_xl, first_xu, second_yl, second_yu;
        boolean flag_x, flag_y;

        for (int i = 0; i < solution.getNumberOfVariables(); i += 2) {
            flag_x = false; flag_y = false;

            if (randomGenerator.getRandomValue() <= probability) {
                x = Math.floor(solution.getVariable(i));
                y = Math.floor(solution.getVariable(i + 1));

                first_xl = solution.getLowerBound(i);
                first_xu = Math.floor(solution.getUpperBound(i));

                second_yl = solution.getLowerBound(i + 1);
                second_yu = Math.floor(solution.getUpperBound(i + 1));

                if (first_xl == first_xu) {
                    x = first_xl ;
                    flag_x = true;
                }

                if (second_yl == second_yu) {
                    y = second_yl;
                    flag_y = true;
                }

                x = performMutation(x, first_xl, first_xu, flag_x);
                solution.setVariable(i, Math.floor(x));

                y = performMutation(y, second_yl, second_yu, flag_y);
                solution.setVariable(i + 1, Math.floor(y));
            }
        }
    }

    private double performMutation(double value, double val_l, double val_u, boolean flag_val) {
        double delta1, delta2, deltaq;
        double rnd, mutPow;
        double xy, val;

        if (!flag_val){
            delta1 = (value - val_l) / (val_u - val_l);
            delta2 = (val_u - value) / (val_u - val_l);
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
            value = value + deltaq * (val_u - val_l);
            value = solutionRepair.repairSolutionVariableValue(value, val_l, val_u);
        }
        return value;
    }
}
