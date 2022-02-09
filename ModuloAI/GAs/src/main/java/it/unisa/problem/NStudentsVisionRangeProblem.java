package it.unisa.problem;

import it.unisa.utils.SectorsComparator;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.*;

public class NStudentsVisionRangeProblem extends NStudentsDistanceProblem{

    private final TreeMap<String, ArrayList<Integer>> matrixSectors;

    public NStudentsVisionRangeProblem(String name, int row, int col, int students){
        super(name, row, col, students);
        matrixSectors = new TreeMap<>(new SectorsComparator());
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
        int x = 0, y = 0;

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
        int countSectors = 0;
        int rowDivision;
        int colDivision;
        int[] score = {3, 4, 3, 2, 3, 2, 1, 2, 1};

        if (super.getROW() % 3 != 0)
            rowDivision = (super.getROW() / 3) + 1;
        else rowDivision = (super.getROW() / 3);

        if (super.getCOL() % 3 != 0)
            colDivision = (super.getCOL() / 3) + 1;
        else colDivision = (super.getCOL() / 3);

        for (int row = 0; row < super.getROW(); row += rowDivision){
            for (int col = 0; col < super.getCOL(); col += colDivision, countSectors++){
                ArrayList<Integer> sectors = new ArrayList<>();
                sectors.add(row); sectors.add(col);

                for (int i = rowDivision - 1; i >= 0; i--){
                    if (row + i < super.getROW()){
                        sectors.add(row + i);
                        break;
                    }
                }
                for (int i = colDivision - 1; i >= 0; i--){
                    if (col + i < super.getCOL()){
                        sectors.add(col + i);
                        break;
                    }
                }
                //Assegnazione Punteggio
                sectors.add(score[countSectors]);
                matrixSectors.put("Sectors" + countSectors, sectors);
            }
        }
       System.out.println(matrixSectors);
    }
}
