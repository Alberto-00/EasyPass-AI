package it.unisa.problem;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

public class NStudentsDistanceProblem extends AbstractIntegerProblem implements ConstrainedProblem<IntegerSolution>{

    private final int COL, ROW;
    private int[][] roomSize;
    private final OverallConstraintViolation<IntegerSolution> overallConstraintViolationDegree ;
    private final NumberOfViolatedConstraints<IntegerSolution> numberOfViolatedConstraints ;

    public NStudentsDistanceProblem(String name, int row, int col, int students){
        if ((row < 6 || row > 20) || (col < 6 || col > 20))
            throw new IllegalArgumentException("Cannot set room size to rows and columns lower than 5.");

        if (students == 0 || students > (row * col)/2)
            throw new IllegalArgumentException("No valid number students.");

        this.ROW = row;
        this.COL = col;
        this.roomSize = new int[ROW][COL];

        overallConstraintViolationDegree = new OverallConstraintViolation<>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>() ;

        setName(name);
        setNumberOfObjectives(1);
        setNumberOfConstraints(1);
        setNumberOfVariables(students);

        List<Integer> lowerBounds = new ArrayList<>();
        List<Integer> upperBounds = new ArrayList<>();
        for (int i = 0; i < students; i++) {
            lowerBounds.add(0);
            upperBounds.add(ROW - 1);
            lowerBounds.add(0);
            upperBounds.add(COL - 1);
        }
        setVariableBounds(lowerBounds, upperBounds);
    }

    //Funzione di minimizzazzione e vincoli
    @Override
    public void evaluate(IntegerSolution integerSolution) {
        int conflicts = calculateConflicts(integerSolution.getVariables());
        integerSolution.getObjectives()[0] = conflicts;

        //evaluateConstraints(integerSolution);
    }

    private int calculateConflicts(List<Integer> encoding) {
        int conflicts = 0;
        this.roomSize = new int[ROW][COL];

        for (int i = 0; i < encoding.size(); i += 2){
            int x = encoding.get(i);
            int y = encoding.get(i + 1);

            if (roomSize[x][y] == 1)
                conflicts+=1000;
            else roomSize[x][y] = 1;
        }
        for (int row = 0; row < this.ROW; row++){
            for (int col = 0; col < this.COL; col++){
                if (roomSize[row][col] == 1){
                    if ((row - 1) >= 0) {
                        if (roomSize[row - 1][col] == 1)
                            conflicts++;
                    } if ((row + 1) < this.ROW){
                        if (roomSize[row + 1][col] == 1)
                            conflicts++;
                    } if ((col + 1) < this.COL){
                        if (roomSize[row][col + 1] == 1)
                            conflicts++;
                    } if ((col - 1) >= 0){
                        if (roomSize[row][col - 1] == 1)
                            conflicts++;
                    }
                }
            }
        }
        return conflicts;
    }

    @Override
    public void evaluateConstraints(IntegerSolution solution)  {
        double[] constraint = new double[this.getNumberOfConstraints()];
        List<Integer> encoding = solution.getVariables();
        constraint[0] = 0;
        boolean flag = false;

        for (int i = 0; i < encoding.size() - 3; i += 2){
            int x = encoding.get(i);
            int y = encoding.get(i + 1);
            for (int j = i+2; j < encoding.size(); j+=2) {
                if (x == encoding.get(j) && y == encoding.get(j+1)){
                    constraint[0] = -1.0;
                    flag = true;
                    break;
                }
            }
            /*if (roomSize[x][y] == 1) {
                constraint[j] = -1.0;
                break;
            }*/
            if (flag)
                break;
        }
        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;

        for (int i = 0; i < this.getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
            }
        }
        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);
    }

    public int getCOL(){
        return COL;
    }

    public int getROW(){
        return ROW;
    }

    public int[][] getRoomSize() {
        return roomSize;
    }
}
