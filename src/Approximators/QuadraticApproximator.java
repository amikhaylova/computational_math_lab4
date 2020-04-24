package Approximators;

import Utils.Point;

import java.util.List;

public class QuadraticApproximator extends Approximator {

    public QuadraticApproximator(List<Point> pointList) {
        super(pointList);
        this.c = 0;
    }

    @Override
    public double getApprFunctionValue(double x) {
        return (a * Math.pow(x, 2) + b * x + c);
    }

    @Override
    public void calculateCoefs() {
        double a;
        double b;
        double c;
        double x = 0;
        double y = 0;
        double x2 = 0;
        double xy = 0;
        double x3 = 0;
        double x4 = 0;
        double x2y = 0;
        int n = pointList.size();

        for (int i = 0; i < n; i++) {
            x += pointList.get(i).getX();
            y += pointList.get(i).getY();
            x2 += Math.pow(pointList.get(i).getX(), 2);
            xy += pointList.get(i).getX() * pointList.get(i).getY();
            x3 += Math.pow(pointList.get(i).getX(), 3);
            x4 += Math.pow(pointList.get(i).getX(), 4);
            x2y += Math.pow(pointList.get(i).getX(), 2) * pointList.get(i).getY();
            System.out.println("***");
            System.out.println(x + " " + y + " " + x2 + " " + xy + " " + x3 + " " + x4 + " " + x2y + " ");
        }

        c = -(x * x3 * x2y - x * xy * x4 - x2y * Math.pow(x2, 2) + x2 * y * x4 + x2 * x3 * xy - y * Math.pow(x3, 2)) /
                (Math.pow(x, 2) * x4 - 2 * x * x2 * x3 + Math.pow(x2, 3) - x2 * x4 * n + Math.pow(x3, 2) * n);

        b = -(x * x2 * x2y - x * y * x4 - xy * Math.pow(x2, 2) + x2 * y * x3 - x3 * x2y * n + xy * x4 * n)
                / (Math.pow(x, 2) * x4 - 2 * x * x2 * x3 + Math.pow(x2, 3) - x2 * x4 * n + Math.pow(x3, 2) * n);

        a = -(-x2y * Math.pow(x, 2) + x * x2 * xy + x * y * x3 - y * Math.pow(x2, 2) + x2 * x2y * n - xy * x3 * n)
                / (Math.pow(x, 2) * x4 - 2 * x * x2 * x3 + Math.pow(x2, 3) - x2 * x4 * n + Math.pow(x3, 2) * n);

        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String getStringApproximation() {
        if (b >= 0 && c >= 0) {
            return String.format("y = %.4f*x^2 + %.4f*x + %.4f", a, b, c);
        } else if (b < 0 && c < 0) {
            return String.format("y = %.4f*x^2 - %.4f*x - %.4f", a, Math.abs(b), Math.abs(c));
        } else if (b > 0 && c < 0) {
            return String.format("y = %.4f*x^2 + %.4f*x - %.4f", a, b, Math.abs(c));
        } else {
            return String.format("y = %.4f*x^2 - %.4f*x + %.4f", a, Math.abs(b), c);
        }
    }

}
