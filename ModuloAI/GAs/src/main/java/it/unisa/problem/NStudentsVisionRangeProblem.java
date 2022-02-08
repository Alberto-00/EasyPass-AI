package it.unisa.problem;

import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.List;

public class NStudentsVisionRangeProblem extends NStudentsDistanceProblem{

    public NStudentsVisionRangeProblem(String name, int row, int col, int students){
        super(name, row, col, students);
        setNumberOfObjectives(2);
    }

    @Override
    public void evaluate(IntegerSolution integerSolution) {
        super.evaluate(integerSolution);
        int visionRange = calculateVisionRange(integerSolution.getVariables());
        integerSolution.getObjectives()[1] = -1.0 * visionRange;
    }

    private int calculateVisionRange(List<Integer> encoding){
        return 0;
    }
}
