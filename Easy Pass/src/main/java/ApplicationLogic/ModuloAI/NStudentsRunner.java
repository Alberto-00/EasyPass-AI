package ApplicationLogic.ModuloAI;
import ApplicationLogic.ModuloAI.fix.DoubleNPointCrossover;
import ApplicationLogic.ModuloAI.fix.DoublePolynomialMutation;
import ApplicationLogic.ModuloAI.problem.NStudentsVisionRangeProblem;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.List;

public class NStudentsRunner {

    public static void main(String[] args) {
        if (args.length != 3)
            throw new IllegalArgumentException("No valid argument passed.");

        int ROW = Integer.parseInt(args[0]);
        int COL = Integer.parseInt(args[1]);
        int students = Integer.parseInt(args[2]);

        double crossoverProbability = 0.8;
        double mutationProbability = 0.01;
        int maxEvaluations = 1000000;
        int populationSize = 100;

        Problem<DoubleSolution> problem = new NStudentsVisionRangeProblem("Vision Range Problem", ROW, COL, students);
        BinaryTournamentSelection<DoubleSolution> selection = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());
        CrossoverOperator<DoubleSolution> crossover = new DoubleNPointCrossover(crossoverProbability, 1);
        DoublePolynomialMutation mutation = new DoublePolynomialMutation(mutationProbability, 0);

        NSGAII<DoubleSolution> nsgaii = new NSGAIIBuilder<>(problem, crossover, mutation, populationSize)
                .setSelectionOperator(selection)
                .setMaxEvaluations(maxEvaluations)
                .build();

        AlgorithmRunner nsgaiiRunner = new AlgorithmRunner.Executor(nsgaii).execute();
        List<DoubleSolution> bestIndividuals = nsgaii.getResult();

        for (DoubleSolution solution: nsgaii.getResult()) {
            int i = 0;
            for (double value: solution.getVariables()) {
                solution.setVariable(i, Math.floor(value));
                i++;
            }
        }

        JMetalLogger.logger.info(String.format("Problem: %s", problem.getName()));
        JMetalLogger.logger.info(String.format("Solutions: \n%s\n", bestIndividuals));
        JMetalLogger.logger.info(String.format("Total execution time: %s ms", nsgaiiRunner.getComputingTime()));
    }
}
