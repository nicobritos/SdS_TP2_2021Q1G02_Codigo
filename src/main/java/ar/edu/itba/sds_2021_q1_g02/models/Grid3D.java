package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.List;

public class Grid3D implements Grid<Particle[][][]> {
    private Particle[][][] grid;

    public Grid3D(final int M) {
        this.grid = new Particle[M][M][M];
    }

    public void populateGrid(final List<? extends Particle> particles) {
        particles.forEach(particle ->
        {
            if (particle.getPosition() != null) {
                grid[(int) particle.getPosition().getX()][(int) particle.getPosition().getY()][(int) particle.getPosition().getZ()] = particle;
            } else {
                return;
            }
        });
    }

    public Particle[][][] getGrid() {
        return this.grid;
    }

    @Override
    public Particle getParticle(Position position) {
        if (position != null) {
            return this.grid[(int) position.getX()][(int) position.getY()][(int) position.getZ()];
        }
        return null;
    }
}
