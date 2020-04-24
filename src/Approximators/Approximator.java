package Approximators;

import Exceptions.NotDomainOfFunctionException;
import Utils.Point;

import java.util.List;

public class Approximator {
    List<Point> pointList;
    double a;
    double b;
    double c;

    public Approximator(List<Point> pointList) {
        this.pointList = pointList;
        this.a = 0;
        this.b = 0;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public double getApprFunctionValue(double x) throws NotDomainOfFunctionException {
        return 0.0;
    }


    public int getIndexOfMaxDeviation() throws NotDomainOfFunctionException{
        int index = 0;
        double maxDeviation = 0;
        for (int i = 0; i < pointList.size(); i++) {
            double curDeviation = Math.abs(pointList.get(i).getY() - getApprFunctionValue(pointList.get(i).getX()));
            if (curDeviation > maxDeviation) {
                maxDeviation = curDeviation;
                index = i;
            }
        }
        return index;
    }

    public void calculateCoefs() {

    }

    public String getStringApproximation() {
        return "";
    }

}
