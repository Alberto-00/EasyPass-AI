package it.unisa;
import it.unisa.fix.DoubleNPointCrossover;
import it.unisa.fix.DoublePolynomialMutation;
import it.unisa.problem.NStudentsVisionRangeProblem;
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

/**
 * Algoritmo genetico NSGAII: dispone gli studenti rispettando
 * le norme di sicurezza anti-Covid ma li organizza anche in
 * modo tale che ognuno di essi ha una buona visuale per la lavagna.
 */
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

        printSolution(problem, bestIndividuals, nsgaiiRunner, populationSize);
    }

    private static void printSolution(Problem<DoubleSolution> problem, List<DoubleSolution> bestIndividuals,
                                      AlgorithmRunner nsgaiiRunner, int populationSize){
        DoubleSolution doubleBestSolution = NStudentsRunner.getBestSolution(bestIndividuals);
        StringBuilder solution = new StringBuilder();

        for (int i = 0; i < doubleBestSolution.getVariables().size(); i++) {
            double value = doubleBestSolution.getVariable(i);
            int intValue = (int) value;
            solution.append(intValue).append(" ");
        }

        JMetalLogger.logger.info(String.format("Problem: %s", problem.getName()));
        JMetalLogger.logger.info(String.format("Solutions: \n%s\n", bestIndividuals));
        JMetalLogger.logger.info(String.format("Best Solution: %s", doubleBestSolution));
        JMetalLogger.logger.info(String.format("Total execution time: %s ms", nsgaiiRunner.getComputingTime()));

        System.out.println("\n'" + problem.getName() + "' INFO Best Solution:\n");
        System.out.println("Population Size: " +  populationSize +
                "\nStudents Size: " + problem.getNumberOfVariables() +
                "\nComputing Time: " + nsgaiiRunner.getComputingTime() + " ms" +
                "\nSolution: " + solution +
                "\nObjectives: " + doubleBestSolution.getObjectives()[0] + " conflicts\t" + doubleBestSolution.getObjectives()[1] + " score" +
                "\nConstraints: " + doubleBestSolution.getConstraints()[0] +
                String.format("\nTotal execution time: %s ms", nsgaiiRunner.getComputingTime()));
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