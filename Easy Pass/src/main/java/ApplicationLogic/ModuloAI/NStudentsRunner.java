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

import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmo genetico NSGAII: dispone gli studenti rispettando
 * le norme di sicurezza anti-Covid ma li organizza anche in
 * modo tale che ognuno di essi ha una buona visuale per la lavagna.
 */
public class NStudentsRunner {

    public static List<Integer> Algorithm(String[] args) {
        if (args.length != 3)
            throw new IllegalArgumentException("No valid argument passed.");

        int ROW = Integer.parseInt(args[0]);
        int COL = Integer.parseInt(args[1]);
        int students = Integer.parseInt(args[2]);

        double crossoverProbability = 0.8;
        double mutationProbability = 0.01;
        int maxEvaluations = 100000;
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
        List<Integer> bestSolution = new ArrayList<>();
        DoubleSolution doubleBestSolution = NStudentsRunner.getBestSolution(bestIndividuals);

        for (double value: doubleBestSolution.getVariables()) {
            bestSolution.add((int) value);
        }

        JMetalLogger.logger.info(String.format("Problem: %s", problem.getName()));
        JMetalLogger.logger.info(String.format("Solutions: \n%s\n", bestIndividuals));
        JMetalLogger.logger.info(String.format("Total execution time: %s ms", nsgaiiRunner.getComputingTime()));
        return bestSolution;
    }

    /*Ritorna la migliore soluzione tra quelle analizzate*/
    private static DoubleSolution getBestSolution(List<DoubleSolution> bestIndividuals){
        DoubleSolution doubleBestSolution = bestIndividuals.get(0);

        for (int i = 1; i < bestIndividuals.size() - 1; i++){
            if (doubleBestSolution.getObjective(0) > bestIndividuals.get(i).getObjective(0)){
                doubleBestSolution = bestIndividuals.get(i);
            } else if (doubleBestSolution.getObjective(0) == bestIndividuals.get(i).getObjective(0)){
                if (doubleBestSolution.getObjective(1) > bestIndividuals.get(i).getObjective(1))
                    doubleBestSolution = bestIndividuals.get(i);
            }
        }
        return doubleBestSolution;
    }
}