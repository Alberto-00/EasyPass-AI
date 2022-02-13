package it.unisa.fix;

import org.apache.commons.lang3.ArrayUtils;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.checking.Check;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DoubleNPointCrossover implements CrossoverOperator<DoubleSolution> {
    private final JMetalRandom randomNumberGenerator = JMetalRandom.getInstance();
    private final double probability;
    private final int crossovers;

    public DoubleNPointCrossover(double probability, int crossovers) {
        if (probability < 0.0) throw new JMetalException("Probability can't be negative");
        if (crossovers < 1) throw new JMetalException("Number of crossovers is less than one");
        this.probability = probability;
        this.crossovers = crossovers;
    }

    public DoubleNPointCrossover(int crossovers) {
        this.crossovers = crossovers;
        this.probability = 1.0;
    }

    @Override
    public double getCrossoverProbability() {
        return probability;
    }

    @Override
    public List<DoubleSolution> execute(List<DoubleSolution> s) {
        Check.that(
                getNumberOfRequiredParents() == s.size(),
                "Point Crossover requires + "
                        + getNumberOfRequiredParents()
                        + " parents, but got "
                        + s.size());

        if (randomNumberGenerator.nextDouble() < probability) {
            return doCrossover(s);
        } else {
            return s;
        }
    }

    private List<DoubleSolution> doCrossover(List<DoubleSolution> s) {
        DoubleSolution mom = s.get(0);
        DoubleSolution dad = s.get(1);

        Check.that(
                mom.getVariables().size() == dad.getVariables().size(),
                "The 2 parents doesn't have the same number of variables");
        Check.that(
                mom.getVariables().size() >= crossovers,
                "The number of crossovers is higher than the number of variables");

        int[] crossoverPoints = new int[crossovers];
        for (int i = 0; i < crossoverPoints.length; i++) {
            crossoverPoints[i] = randomNumberGenerator.nextInt(0, mom.getVariables().size() - 1);
        }
        DoubleSolution girl = (DoubleSolution) mom.copy();
        DoubleSolution boy = (DoubleSolution) dad.copy();
        boolean swap = false;

        for (int i = 0; i < mom.getVariables().size(); i++) {
            if (swap) {
                boy.getVariables().set(i, mom.getVariables().get(i));
                girl.getVariables().set(i, dad.getVariables().get(i));
            }

            if (ArrayUtils.contains(crossoverPoints, i)) {
                swap = !swap;
            }
        }
        List<DoubleSolution> result = new ArrayList<>();
        result.add(girl);
        result.add(boy);
        return result;
    }

    @Override
    public int getNumberOfRequiredParents() {
        return 2;
    }

    @Override
    public int getNumberOfGeneratedChildren() {
        return 2;
    }
}