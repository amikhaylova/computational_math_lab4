package Utils;

public class Point implements Comparable<Point> {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return ("( " + this.x + " ; " + this.y + " )");
    }

    @Override
    public int compareTo(Point o) {
        Double a = this.getX();
        Double b = o.getX();
        return a.compareTo(b);
    }
}
