package Approximators;

import Exceptions.NotDomainOfFunctionException;
import Utils.Point;

import java.util.ArrayList;
import java.util.List;

public class PowApproximator extends Approximator {
    private List<Point> transformedList;

    public PowApproximator(List<Point> pointList) {
        super(pointList);
        this.pointList = pointList;
        transformedList = new ArrayList<>();
    }

    @Override
    public void calculateCoefs() {
        for (int i = 0; i < pointList.size(); i++) {
            transformedList.add(new Point(Math.log(pointList.get(i).getX()), Math.log(pointList.get(i).getY())));
        }
        LinearApproximator approximator = new LinearApproximator(transformedList);
        approximator.calculateCoefs();
        a = Math.pow(Math.E, approximator.getB());
        b = approximator.getA();
    }

    @Override
    public double getApprFunctionValue(double x) throws NotDomainOfFunctionException {
        if (x > 0) {
            return a * Math.pow(x, b);
        } else {
            throw new NotDomainOfFunctionException();
        }
    }

    @Override
    public String getStringApproximation() {
        return String.format("y = %.4f*x^(%.4f)", a, b);
    }

}
