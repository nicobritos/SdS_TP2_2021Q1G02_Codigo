package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.ArrayList;
import java.util.List;

public class MooreNeighborhood {
    private final List<Position> positions;
    private final List<Position> positions3D;

    public MooreNeighborhood(final int r) {
        this.positions = createPositions(r);
        this.positions3D = createPositions3D(r);
    }

    public List<Position> getPositions() {
        return this.positions;
    }

    public List<Position> getPositions3D() {
        return this.positions3D;
    }

    private static List<Position> createPositions(int r) {
        List<Position> positions = new ArrayList<>();

        for (int x = -1 * r; x <= r; x++) {
            for (int y = -1 * r; y <= r; y++) {
                if (!(x == 0 && y == 0)) {
                    positions.add(new Position(x, y));
                }
            }
        }

        return positions;
    }

    private static List<Position> createPositions3D(int r) {
        List<Position> positions = new ArrayList<>();

        for (int x = -1 * r; x <= r; x++) {
            for (int y = -1 * r; y <= r; y++) {
                for (int z = -1 * r; z <= r; z++) {
                    if (!(x == 0 && y == 0 && z == 0)) {
                        positions.add(new Position(x, y, z));
                    }
                }
            }
        }

        return positions;
    }
}
