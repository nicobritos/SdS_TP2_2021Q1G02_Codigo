package models;

import java.util.List;

public class Grid {
    private Particle[][] grid;

    public Grid(final int M) {
        this.grid = new Particle[M][M];
    }

    public void populateGrid(final List<? extends Particle> particles) {
        particles.forEach(particle -> grid[particle.getPosition().getX().intValue()][particle.getPosition().getY().intValue()] = particle);
    }

    public Particle[][] getGrid() {
        return this.grid;
    }

}
