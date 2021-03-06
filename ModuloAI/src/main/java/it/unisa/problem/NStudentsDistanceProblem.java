package it.unisa.problem;

import it.unisa.fix.DefaultDoubleToIntSolution;
import org.uma.jmetal.problem.doubleproblem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

import java.util.ArrayList;
import java.util.List;

public class NStudentsDistanceProblem extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution>{

    private final int COL, ROW;
    private final OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree ;
    private final NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints ;

    public NStudentsDistanceProblem(String name, int row, int col, int students){
        if ((row < 6 || row > 20) || (col < 6 || col > 20))
            throw new IllegalArgumentException("Cannot set room size to rows and columns lower than 5.");

        if (students == 0 || students > (row * col)/2)
            throw new IllegalArgumentException("No valid number students.");

        this.ROW = row;
        this.COL = col;

        overallConstraintViolationDegree = new OverallConstraintViolation<>() ;
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<>() ;

        setName(name);
        setNumberOfObjectives(1);
        setNumberOfConstraints(1);
        setNumberOfVariables(students);
        setBounds(students);
    }

    //Creazione soluzioni con parte decimale .0
    @Override
    public DoubleSolution createSolution() {
        return new DefaultDoubleToIntSolution(bounds, getNumberOfObjectives(), getNumberOfConstraints());
    }

    //Primo obiettivo
    @Override
    public void evaluate(DoubleSolution solution) {
        int conflicts = calculateConflicts(solution.getVariables());
        solution.getObjectives()[0] = conflicts;

        evaluateConstraints(solution);
    }

    //Calcolo dei vincoli
    @Override
    public void evaluateConstraints(DoubleSolution solution)  {
        double[] constraint = new double[this.getNumberOfConstraints()];
        constraint[0] = 0;
        List<Double> encoding = solution.getVariables();

        for (int i = 0; i < encoding.size() - 3; i += 2){
            double x = Math.floor(encoding.get(i));
            double y = Math.floor(encoding.get(i + 1));

            for (int j = i+2; j < encoding.size(); j += 2) {
                if (x == Math.floor(encoding.get(j)) && y == Math.floor(encoding.get(j+1)))
                    constraint[0] -= 1.0;
            }
        }
        solution.setConstraint(0, constraint[0]);

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

    //Calcolo dei conflitti da minimizzare
    private int calculateConflicts(List<Double> encoding) {
        int conflicts = 0;

        for (int i = 0; i < encoding.size(); i += 2){
            int x = (int) Math.floor(encoding.get(i));
            int y = (int) Math.floor(encoding.get(i + 1));

            for (int j = i + 2; j < encoding.size(); j += 2){
                int xa = (int) Math.floor(encoding.get(j));
                int ya = (int) Math.floor(encoding.get(j + 1));

                if ((xa - 1) >= 0) {
                    if (xa - 1 == x && y == ya)
                        conflicts++;
                } if ((xa + 1) < this.ROW){
                    if (xa + 1 == x && y == ya)
                        conflicts++;
                } if ((ya + 1) < this.COL){
                    if (xa == x && ya + 1 == y)
                        conflicts++;
                } if ((ya - 1) >= 0){
                    if (xa == x && ya - 1 == y)
                        conflicts++;
                }

            }
        }
        return conflicts;
    }

    /*Imposta i lower e gli upper bounds per ogni studente.*/
    private void setBounds(int students){
        List<Double> doubleLowerBounds = new ArrayList<>();
        List<Double> doubleUpperBounds = new ArrayList<>();

        for (int i = 0; i < students; i++) {
            doubleLowerBounds.add(0.0);
            doubleUpperBounds.add(ROW - 0.1);
            doubleLowerBounds.add(0.0);
            doubleUpperBounds.add(COL - 0.1);
        }
        setVariableBounds(doubleLowerBounds, doubleUpperBounds);
    }

    public int getCOL(){
        return COL;
    }

    public int getROW(){
        return ROW;
    }
}
