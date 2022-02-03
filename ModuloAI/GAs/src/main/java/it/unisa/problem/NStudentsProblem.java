package it.unisa.problem;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.ArrayList;
import java.util.List;

public class NStudentsProblem extends AbstractIntegerProblem {

    private final int[][] roomSize;

    public NStudentsProblem(String name, int[][] roomSize, int students){
        if (roomSize.length < 5 || roomSize[0].length < 5)
            throw new IllegalArgumentException("Cannot set room size to rows and columns lower than 5.");

        this.roomSize = new int[roomSize.length][roomSize[0].length];

        setName(name);
        setNumberOfVariables(students);

        List<Integer> lowerBounds = new ArrayList<>();
        List<Integer> upperBounds = new ArrayList<>();
        for (int i = 0; i < students; i++) {
            lowerBounds.add(1);
            upperBounds.add(roomSize[0].length);
        }
        setVariableBounds(lowerBounds, upperBounds);
        setNumberOfObjectives(1);
    }

    @Override
    public void evaluate(IntegerSolution integerSolution) {
        int conflicts = calculateConflicts(integerSolution.getVariables());
        integerSolution.getObjectives()[0] = conflicts;
    }

    private int calculateConflicts(List<Integer> encoding) {

    }

    public int[][] getRoomSize(){
        return roomSize;
    }
}
