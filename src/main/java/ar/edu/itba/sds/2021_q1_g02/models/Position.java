package models;

public class Position {
    private final Double x;
    private final Double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Double getX() {
        return this.x;
    }

    public Double getY() {
        return this.y;
    }

    public Position copy() {
        return new Position(this.x, this.y);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [").append(this.x.intValue()).append(",").append(this.y.intValue()).append("] ");
        return sb.toString();
    }
}
