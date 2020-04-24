package Approximators;

import Exceptions.NotDomainOfFunctionException;
import Utils.Point;

import java.util.ArrayList;
import java.util.List;

public class LogApproximator extends Approximator {
    private List<Point> transformedList;


    public LogApproximator(List<Point> pointList) {
        super(pointList);
        this.pointList = pointList;
        transformedList = new ArrayList<>();
    }

    @Override
    public void calculateCoefs() {
        for (int i = 0; i < pointList.size(); i++) {
            transformedList.add(new Point(Math.log(pointList.get(i).getX()), pointList.get(i).getY()));
        }
        LinearApproximator approximator = new LinearApproximator(transformedList);
        approximator.calculateCoefs();
        this.a = approximator.getA();
        this.b = approximator.getB();
    }

    @Override
    public double getApprFunctionValue(double x) throws NotDomainOfFunctionException {
        if (x > 0) {
            return a * Math.log(x) + b;
        } else {
            throw new NotDomainOfFunctionException();
        }
    }

    @Override
    public String getStringApproximation() {
        if (b > 0) {
            return String.format("y = %.4f*ln(x) + %.4f", a, b);
        } else if (b == 0) {
            return String.format("y = %.4f*ln(x)", a);
        } else {
            return String.format("y = %.4f*ln(x) - %.4f", a, Math.abs(b));
        }
    }
}
