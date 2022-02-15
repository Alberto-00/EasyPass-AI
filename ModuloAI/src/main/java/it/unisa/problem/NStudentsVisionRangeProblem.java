package it.unisa.problem;

import org.uma.jmetal.solution.doublesolution.DoubleSolution;

import java.util.*;

public class NStudentsVisionRangeProblem extends NStudentsDistanceProblem{

    private final TreeMap<Integer, ArrayList<Integer>> matrixSectors;

    public NStudentsVisionRangeProblem(String name, int row, int col, int students){
        super(name, row, col, students);

        matrixSectors = new TreeMap<>();
        calculateSectors();
        setNumberOfObjectives(2);
    }

    //Secondo obiettivo
    @Override
    public void evaluate(DoubleSolution solution) {
        super.evaluate(solution);

        int visionRange = calculateVisionRange(solution.getVariables());
        solution.getObjectives()[1] = -1.0 * visionRange;
    }

    //Calcolo della funzione di fitness
    private int calculateVisionRange(List<Double> encoding){
        int seatingScore = 0;

        for (int i = 0; i < encoding.size(); i += 2){
            int x = (int) Math.floor(encoding.get(i));
            int y = (int) Math.floor(encoding.get(i + 1));

            seatingScore += calculateSeatingScore(x, y);
        }
        return seatingScore;
    }

    //Divisione dell'aula in 9 settori: ogni settore avrà due coppie di coordinate (che indicano rispettivamente
    // lo spigolo in alto a sinistra, in cui inizia il settore, e lo spigolo in basso a destra, in cui termina il settore) e
    // il punteggio del settore.
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
                matrixSectors.put(countSectors, sectors);
            }
        }
    }


    private int calculateSeatingScore(int row, int col){
        int firstX, firstY;
        int secondX, secondY;
        int score = 0;

        for (int i = 0; i < matrixSectors.size(); i++){
            firstX = matrixSectors.get(i).get(0);
            firstY = matrixSectors.get(i).get(1);

            secondX = matrixSectors.get(i).get(2);
            secondY = matrixSectors.get(i).get(3);

            score = matrixSectors.get(i).get(4);

            if ((row >= firstX && row <= secondX) &&
                    (col >= firstY && col <= secondY))
                return score;
        }
        return score;
    }
}
