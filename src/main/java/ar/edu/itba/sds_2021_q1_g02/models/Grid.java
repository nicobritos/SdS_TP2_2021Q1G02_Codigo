package ar.edu.itba.sds_2021_q1_g02.models;

import java.util.List;

public interface Grid<T> {
    void populateGrid(final List<Particle> particles);

    T getGrid();

    Particle getParticle(final Position position);
}
