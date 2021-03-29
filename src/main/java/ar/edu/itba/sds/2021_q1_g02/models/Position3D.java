package models;

public class Position3D extends Position {
    private final double z;

    public Position3D(Position position2D, double z) {
        super(position2D.getX(), position2D.getY());
        this.z = z;
    }

    public Position3D(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }

    public double getZ() {
        return this.z;
    }

    public Position3D copy() {
        return new Position3D(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [").append((int) this.x).append(",").append((int) this.y).append(",").append((int) this.z).append("] ");
        return sb.toString();
    }
}
