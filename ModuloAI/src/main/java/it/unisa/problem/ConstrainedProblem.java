package it.unisa.problem;

import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

public interface ConstrainedProblem<S extends Solution<?>> extends Problem<S> {

    /* Getters */
    int getNumberOfConstraints() ;

    /* Methods */
    void evaluateConstraints(S solution) ;
}