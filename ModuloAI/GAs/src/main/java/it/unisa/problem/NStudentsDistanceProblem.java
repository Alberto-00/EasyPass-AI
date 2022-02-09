package it.unisa.problem;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.ArrayList;
import java.util.List;

public class NStudentsDistanceProblem extends AbstractIntegerProblem {

    private final int COL, ROW;
    private final int[][] roomSize;

    public NStudentsDistanceProblem(String name, int row, int col, int students){
        if ((row < 6 || row > 20) || (col < 6 || col > 20))
            throw new IllegalArgumentException("Cannot set room size to rows and columns lower than 5.");

        if (students == 0 || students > (row * col)/2)
            throw new IllegalArgumentException("No valid number students.");

        this.ROW = row;
        this.COL = col;
        this.roomSize = new int[ROW][COL];

        setName(name);
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
        setNumberOfObjectives(1);
    }

    //Funzione di minimizzazzione
    @Override
    public void evaluate(IntegerSolution integerSolution) {
        int conflicts = calculateConflicts(integerSolution.getVariables());
        integerSolution.getObjectives()[0] = conflicts;
    }

    private int calculateConflicts(List<Integer> encoding) {
        int conflicts = 0;

        for (int i = 0; i < encoding.size(); i += 2){
            int x = encoding.get(i);
            int y = encoding.get(i + 1);
            if (roomSize[x][y] == 1)
                conflicts++;
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
