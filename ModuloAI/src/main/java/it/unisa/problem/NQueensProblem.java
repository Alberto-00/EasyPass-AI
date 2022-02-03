package it.unisa.problem;

import org.uma.jmetal.problem.integerproblem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.integersolution.IntegerSolution;

import java.util.ArrayList;
import java.util.List;

public class NQueensProblem extends AbstractIntegerProblem {

    private final int chessSize; //roomSize

    public NQueensProblem(String name, int chessSize) {
        if (chessSize < 2) {
            throw new IllegalArgumentException("Cannot set chess size to a value lower than 2.");
        }
        this.chessSize = chessSize;

        setName(name);//name of problems
        setNumberOfVariables(chessSize);//number of students

        /* Usiamo due Lista:
         * 1. viene usata per indicare il minimo valore che avranno i geni;
         * 2. viene usata per indicate il massimo valore che avranno i geni
         */
        List<Integer> lowerBounds = new ArrayList<>();
        List<Integer> upperBounds = new ArrayList<>();

        //Tutti i geni hanno lo stesso lower e upper bound quindi li inseriremo con un for
        for (int i = 0; i < chessSize; i++) {
            lowerBounds.add(1);
            upperBounds.add(chessSize);
        }
        setVariableBounds(lowerBounds, upperBounds);
        setNumberOfObjectives(1);//Problema mono-obiettivo
    }

    /*Calcola i conflitti che si avranno*/
    @Override
    public void evaluate(IntegerSolution integerSolution) {
        int conflicts = calculateConflicts(integerSolution.getVariables());
        integerSolution.getObjectives()[0] = conflicts;
    }

    private int calculateConflicts(List<Integer> encoding) {
        int rowConflicts = 0;
        int upperRightConflicts = 0;
        int lowerRightConflicts = 0;
        for (int col = 0; col < chessSize; col++) {
            int row = encoding.get(col);
            for (int rightCol = col + 1; rightCol < chessSize; rightCol++) {
                int rightRow = encoding.get(rightCol);
                if (rightRow == row) {
                    rowConflicts++;
                } else if (rightRow == row - (rightCol - col)) {
                    upperRightConflicts++;
                } else if (rightRow == row + (rightCol - col)) {
                    lowerRightConflicts++;
                }
            }
        }
        return upperRightConflicts + lowerRightConflicts + rowConflicts;
    }

    public int getChessSize() {
        return chessSize;
    }
}
