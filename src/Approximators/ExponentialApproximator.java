package Approximators;

import Utils.Point;

import java.util.ArrayList;
import java.util.List;

public class ExponentialApproximator extends Approximator {
    private List<Point> transformedList;


    public ExponentialApproximator(List<Point> pointList) {
        super(pointList);
        this.pointList = pointList;
        transformedList = new ArrayList<>();
    }

    @Override
    public void calculateCoefs() {
        for (int i = 0; i < pointList.size(); i++) {
            transformedList.add(new Point(pointList.get(i).getX(), Math.log(pointList.get(i).getY())));
        }
        LinearApproximator approximator = new LinearApproximator(transformedList);
        approximator.calculateCoefs();
        this.a = Math.pow(Math.E, approximator.getB());
        this.b = approximator.getA();
    }

    @Override
    public double getApprFunctionValue(double x) {
        return a * Math.pow(Math.E, b * x);
    }

    @Override
    public String getStringApproximation() {
        return String.format("y = %.4f*e^(%.4f*x)", a, b);
    }
}
