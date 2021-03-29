import javafx.util.Pair;
import models.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameOfLife {
    private final int MOORE_NEIGHBORHOOD_RADIUS = 1;
    private List<CellularParticle> particles;
    private int M;

    public GameOfLife(List<CellularParticle> particles, final int M) {
        this.particles = particles;
        this.M = M;
    }

    //MaxIteration como metodo de corte
    public void simulate2D(int maxIterations) {
        Grid grid = new Grid2D(this.M);
        for (int i = 0; i < maxIterations; i++) {
            Map<Particle, State> nextStates = new HashMap<>();
            grid.populateGrid(this.particles);

            for (int x = 0; x < M; x++) {
                for (int y = 0; y < M; y++) {
                    Position currentPosition = new Position(x, y);
                    int neighborsAlive = getTotalNeighborsAlive((Particle[][]) grid.getGrid(), currentPosition);
                    Pair<State, Boolean> stateUpdated =
                            Rules.applyRules(((CellularParticle) grid.getParticle(currentPosition)).getState(),
                                    neighborsAlive);
                    if (stateUpdated.getValue()) {
                        nextStates.put(grid.getParticle(currentPosition), stateUpdated.getKey());
                    }
                }
            }
            for (CellularParticle particle : this.particles) {
                if (nextStates.containsKey(particle)) {
                    updateParticleState(particle, nextStates.get(particle));
                }
            }

            //TODO: Delete when already tested
            int idx = 1;
            System.out.println("------- ITERATION:" + i + "-----------");
            for (CellularParticle particle : this.particles) {
                System.out.print(" [" + particle.getState() + "] ");
                if (idx % 5 == 0) {
                    System.out.println();
                }
                idx++;
            }
        }
    }

    private void updateParticleState(CellularParticle particle, State nextState) {
        particle.setState(nextState);
    }

    private int getTotalNeighborsAlive(Particle[][] grid, Position position) {
        MooreNeighborhood mooreNeighborhood = new MooreNeighborhood(MOORE_NEIGHBORHOOD_RADIUS);
        List<Position> neighborsPositions = mooreNeighborhood.getPositions();
        int neighborsAlive = 0;
        for (Position neighborPosition : neighborsPositions) {
            int neighbor_x = (int) (position.getX() + neighborPosition.getX());
            int neighbor_y = (int) (position.getY() + neighborPosition.getY());
            if ((neighbor_x >= 0) && (neighbor_x < grid.length) && (neighbor_y >= 0) && (neighbor_y < grid.length)) {
                State state = ((CellularParticle) grid[neighbor_x][neighbor_y]).getState();
                if (state.equals(State.ALIVE)) {
                    neighborsAlive++;
                }
            }
        }
        return neighborsAlive;
    }
}
