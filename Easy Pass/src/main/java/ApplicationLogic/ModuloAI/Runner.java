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

public class Runner {

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
        List<DoubleSolution> prova = nsgaii.getResult();

        for (DoubleSolution solution: prova) {
            int i = 0;
            for (double value: solution.getVariables()) {
                solution.setVariable(i, Math.floor(value));
                i++;
            }
        }

        List<DoubleSolution> bestIndividuals = nsgaii.getResult();
        List<Integer> bestSolution = new ArrayList<>();
        DoubleSolution doubleBestSolution = Runner.getBestSolution(bestIndividuals);

        for (double value: doubleBestSolution.getVariables()) {
            bestSolution.add((int) Math.floor(value));
        }

        JMetalLogger.logger.info(String.format("Problem: %s", problem.getName()));
        JMetalLogger.logger.info(String.format("Solutions: \n%s\n", bestIndividuals));
        JMetalLogger.logger.info(String.format("Total execution time: %s ms", nsgaiiRunner.getComputingTime()));
        System.out.println(bestSolution);
    }

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