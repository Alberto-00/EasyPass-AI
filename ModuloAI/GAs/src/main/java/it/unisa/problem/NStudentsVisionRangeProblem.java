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
        for (int row = 0; row < super.getROW(); row += 3){
            for (int col = 0; col < super.getCOL(); col += 3, countSectors++){
                ArrayList<Integer> sectors = new ArrayList<>();
                sectors.add(row); sectors.add(col);

                if (row + 2 < super.getROW()){
                    sectors.add(row + 2);
                } else if (row + 1 < super.getROW()){
                    sectors.add(row + 1);
                } else sectors.add(row);

                if (col + 2 < super.getCOL()){
                    sectors.add(col + 2);
                } else if (col + 1 < super.getCOL()) {
                    sectors.add(col + 1);
                } else sectors.add(col);

                sectors.add(33);
                matrixSectors.put("Sectors" + countSectors, sectors);
            }
        }
        System.out.println(matrixSectors);
    }

   /* private void calculateSectors(){
        int rowDivision = super.getROW()/3 - 1;
        int colDivision = super.getCOL()/3 - 1;
        //da implementare considerando le approssimazioni
        int rowCounter = 0;

        //Divisione in settori
        for (int i = 0; i < 9; i++){
            int module = i % 3;
            ArrayList<Integer> sector = new ArrayList<>();
            sector.add(rowDivision * rowCounter);

            if (module != 0)
                sector.add((colDivision * module) + 1);
            else
                sector.add(0);

            sector.add(rowDivision * (rowCounter + 1));
            sector.add(colDivision * (module + 1));

            //Assegnazione del Punteggio
            if(module == 1){
                sector.add(4 - rowCounter);
            } else {
                sector.add(3 - rowCounter);
                if (module == 2)
                    rowCounter++;
            }
            matrixSectors.put("sector"+i, sector);
        }
        System.out.println(matrixSectors);
    }*/
}
