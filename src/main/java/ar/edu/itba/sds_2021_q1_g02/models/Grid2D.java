package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.List;

public class Grid2D implements Grid<Particle[][]> {
    protected Particle[][] grid;

    public Grid2D(final int M) {
        this.grid = new Particle[M][M];
    }

    public void populateGrid(final List<Particle> particles) {

        particles.forEach(particle -> this.grid[(int) particle.getPosition().getX()][(int) particle.getPosition().getY()] = particle);
    }

    public Particle[][] getGrid() {
        return this.grid;
    }

    @Override
    public Particle getParticle(Position position) {
        return this.grid[(int) position.getX()][(int) position.getY()];
    }
}
