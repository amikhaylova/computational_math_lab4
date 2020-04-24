package Approximators;

import Utils.Point;

import java.util.List;

public class LinearApproximator extends Approximator {

    public LinearApproximator(List<Point> pointList) {
        super(pointList);
    }

    @Override
    public double getApprFunctionValue(double x) {
        return (a * x + b);
    }

    @Override
    public void calculateCoefs() {
        double a;
        double b;
        double sx = 0;
        double sy = 0;
        double sxx = 0;
        double sxy = 0;
        int n = pointList.size();

        for (int i = 0; i < n; i++) {
            sx += pointList.get(i).getX();
            sy += pointList.get(i).getY();
            sxx += Math.pow(pointList.get(i).getX(), 2);
            sxy += pointList.get(i).getX() * pointList.get(i).getY();
        }

        a = (sxy * n - sx * sy) / (sxx * n - sx * sx);
        b = (sxx * sy - sx * sxy) / (sxx * n - sx * sx);

        this.a = a;
        this.b = b;
    }

    @Override
    public String getStringApproximation() {
        if (b > 0) {
            return String.format("y = %.4f*x + %.4f", a, b);
        } else if (b == 0) {
            return String.format("y = %.4f*x ", a);
        } else {
            return String.format("y = %.4f*x - %.4f", a, Math.abs(b));
        }
    }

}
