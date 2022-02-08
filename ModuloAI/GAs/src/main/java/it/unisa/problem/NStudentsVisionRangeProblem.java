package it.unisa.problem;

import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.*;

public class NStudentsVisionRangeProblem extends NStudentsDistanceProblem{

    private final Hashtable<String, ArrayList<Integer>> matrixSectors;

    public NStudentsVisionRangeProblem(String name, int row, int col, int students){
        super(name, row, col, students);
        matrixSectors = new Hashtable<>();
        calculateSectors();
        setNumberOfObjectives(2);
    }

    @Override
    public void evaluate(IntegerSolution integerSolution) {
        super.evaluate(integerSolution);
        int visionRange = calculateVisionRange(integerSolution.getVariables());
        integerSolution.getObjectives()[1] = -1.0 * visionRange;
    }

    private int calculateVisionRange(List<Integer> encoding){
        int[][] roomSize = super.getRoomSize();
        int seatingScore = 0;
        int x = 0,y = 0;

        for (int i = 0; i < encoding.size(); i += 2){
            x = encoding.get(i);
            y = encoding.get(i + 1);
            roomSize[x][y] = 1;
        }

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (roomSize[i][j] == 1) {
                    seatingScore += 1;
                }
            }

        }

        return seatingScore;
    }

    private void calculateSectors(){
        int rowDivision = super.getROW()/3;
        int colDivision = super.getCOL()/3;
        //da implementare considerando le approssimazioni
        int rowCounter = 0;

        for (int i = 0; i < 9; i++){
            ArrayList<Integer> sector = new ArrayList<Integer>();
            int module = i % 3;
            sector.add(rowDivision * rowCounter);
            if (module != 0){
                sector.add((colDivision * module)+1);
            } else {
                sector.add(colDivision * module);
            }

            sector.add(rowDivision * (rowCounter+1));
            sector.add(colDivision * (module+1));

            if(module == 1){
                sector.add(4 - rowCounter);
            } else {
                sector.add(3 - rowCounter);
                if (module == 2)
                    rowCounter++;
            }
            matrixSectors.put("sector"+i, sector);
        }
    }
}
