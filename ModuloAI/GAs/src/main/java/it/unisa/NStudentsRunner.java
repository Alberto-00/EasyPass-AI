package it.unisa;

import it.unisa.fix.IntegerNPointCrossover;
import it.unisa.problem.NStudentsVisionRangeProblem;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.example.AlgorithmRunner;
import org.uma.jmetal.operator.crossover.CrossoverOperator;
import org.uma.jmetal.operator.mutation.impl.IntegerPolynomialMutation;
import org.uma.jmetal.operator.selection.impl.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
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
        int maxEvaluations = 100000;
        int populationSize = 100;

        Problem<IntegerSolution> problem = new NStudentsVisionRangeProblem("Vision Range Problem", ROW, COL, students);
        BinaryTournamentSelection<IntegerSolution> selection = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());
        CrossoverOperator<IntegerSolution> crossover = new IntegerNPointCrossover(crossoverProbability, 1);
        IntegerPolynomialMutation mutation = new IntegerPolynomialMutation(mutationProbability, 0);

        NSGAII<IntegerSolution> nsgaii = new NSGAIIBuilder<>(problem, crossover, mutation, populationSize)
                .setSelectionOperator(selection)
                .setMaxEvaluations(maxEvaluations)
                .build();

        AlgorithmRunner nsgaiiRunner = new AlgorithmRunner.Executor(nsgaii).execute();
        List<IntegerSolution> bestIndividuals = nsgaii.getResult();

        JMetalLogger.logger.info(String.format("Problem: %s", problem.getName()));
        JMetalLogger.logger.info(String.format("Solutions: \n%s\n", bestIndividuals));
        // TODO Do not return identical solutions
        JMetalLogger.logger.info(String.format("Total execution time: %s ms", nsgaiiRunner.getComputingTime()));
    }
}
