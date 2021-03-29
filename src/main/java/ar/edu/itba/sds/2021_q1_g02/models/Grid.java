package models;

import java.util.Collection;
import java.util.List;

public interface Grid<T> {
    void populateGrid(final List<Particle> particles);

    T getGrid();

    Particle getParticle(final Position position);
}
