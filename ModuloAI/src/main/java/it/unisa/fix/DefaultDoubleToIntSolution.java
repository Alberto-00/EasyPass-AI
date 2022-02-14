package it.unisa.fix;

import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.uma.jmetal.solution.doublesolution.DoubleSolution;
import org.uma.jmetal.solution.doublesolution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

public class DefaultDoubleToIntSolution extends DefaultDoubleSolution {

        protected List<Pair<Double, Double>> bounds;

        public DefaultDoubleToIntSolution(List<Pair<Double, Double>> bounds, int numberOfObjectives, int numberOfConstraints) {
            super(bounds, numberOfObjectives, numberOfConstraints);
            this.bounds = bounds;

            for(int i = 0; i < bounds.size(); ++i) {
                this.setVariable(i, Math.floor(JMetalRandom.getInstance().nextDouble((Double)((Pair)bounds.get(i)).getLeft(), (Double)((Pair)bounds.get(i)).getRight())));
            }

        }

        public DefaultDoubleToIntSolution(List<Pair<Double, Double>> bounds, int numberOfObjectives) {
            this(bounds, numberOfObjectives, 0);
        }

    public DefaultDoubleToIntSolution(DefaultDoubleSolution solution) {
        super(solution);

    }

    public Double getLowerBound(int index) {
            return (Double)((Pair)this.bounds.get(index)).getLeft();
        }

        public Double getUpperBound(int index) {
            return (Double)((Pair)this.bounds.get(index)).getRight();
        }

        public org.uma.jmetal.solution.doublesolution.impl.DefaultDoubleSolution copy() {
            return new org.uma.jmetal.solution.doublesolution.impl.DefaultDoubleSolution(this);
        }


}
