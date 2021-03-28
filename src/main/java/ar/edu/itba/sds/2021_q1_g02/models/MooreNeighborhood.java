package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MooreNeighborhood {

    private int r;

    public MooreNeighborhood(final int r) {
        this.r = r;
    }

    public int getRadio() {
        return this.r;
    }

    public List<Position> getPositions() {
        List<Position> moore_positions = new ArrayList<>();
        for (int x = -1 * this.r; x <= this.r; x++) {
            for (int y = -1 * this.r; y <= this.r; y++) {
                if (!(x == 0 && y == 0)) {
                    moore_positions.add(new Position(x, y));
                }
            }
        }
        return moore_positions;
    }
}
