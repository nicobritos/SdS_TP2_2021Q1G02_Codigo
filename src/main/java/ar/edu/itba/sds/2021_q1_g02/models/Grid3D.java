package models;

import java.util.Collection;
import java.util.List;

public class Grid3D implements Grid<Particle[][][]> {
    private Particle[][][] grid;

    public Grid3D(final int M) {
        this.grid = new Particle[M][M][M];
    }

    public void populateGrid(final List<Particle> particles) {
        particles.forEach(particle ->
        {
            if (particle.getPosition() instanceof Position3D) {
                grid[(int) particle.getPosition().getX()][(int) particle.getPosition().getY()][(int) ((Position3D) particle.getPosition()).getZ()] = particle;
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
        if (position instanceof Position3D) {
            return this.grid[(int) position.getX()][(int) position.getY()][(int) ((Position3D) position).getZ()];
        }
        return null;
    }
}
