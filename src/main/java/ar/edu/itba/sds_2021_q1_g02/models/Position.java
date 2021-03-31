package ar.edu.itba.sds_2021_q1_g02.models;

public class Position {
    protected final double x;
    protected final double y;
    protected final double z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(double x, double y) {
        this(x, y, 0);
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public Position copy() {
        return new Position(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [").append((int) this.x).append(",").append((int) this.y).append(",").append((int) this.z).append("] ");
        return sb.toString();
    }
}
