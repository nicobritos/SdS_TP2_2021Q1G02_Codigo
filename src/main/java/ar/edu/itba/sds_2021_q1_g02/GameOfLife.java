package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        Grid2D grid = new Grid2D(this.M);
        for (int i = 0; i < maxIterations; i++) {
            Map<Particle, State> nextStates = new HashMap<>();
            grid.populateGrid(this.particles);

            for (int x = 0; x < M; x++) {
                for (int y = 0; y < M; y++) {
                    Position currentPosition = new Position(x, y);
                    int neighborsAlive = getTotalNeighborsAlive(grid.getGrid(), currentPosition);
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

            try {
                createOutputFile(this.particles, i, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO: Delete when already tested
            int idx = 1;
//            System.out.println("------- ITERATION:" + i + "-----------");
//            for (CellularParticle particle : this.particles) {
//                System.out.print(" [" + particle.getState() + "] ");
//                if (idx % 5 == 0) {
//                    System.out.println();
//                }
//                idx++;
//            }
        }
    }

    //MaxIteration como metodo de corte
    public void simulate3D(int maxIterations) {
        Grid3D grid = new Grid3D(this.M);
        for (int i = 0; i < maxIterations; i++) {
            Map<Particle, State> nextStates = new HashMap<>();
            grid.populateGrid(this.particles);

            for (int x = 0; x < M; x++) {
                for (int y = 0; y < M; y++) {
                    for (int z = 0; z < M; z++) {
                        Position currentPosition = new Position(x, y, z);
                        int neighborsAlive = this.getTotalNeighborsAlive(grid.getGrid(), currentPosition);
                        Pair<State, Boolean> stateUpdated =
                                Rules.applyRules(((CellularParticle) grid.getParticle(currentPosition)).getState(),
                                        neighborsAlive);
                        if (stateUpdated.getValue()) {
                            nextStates.put(grid.getParticle(currentPosition), stateUpdated.getKey());
                        }
                    }
                }
            }
            for (CellularParticle particle : this.particles) {
                if (nextStates.containsKey(particle)) {
                    updateParticleState(particle, nextStates.get(particle));
                }
            }

            try {
                createOutputFile(this.particles, i, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO: Delete when already tested
            int idx = 1;
//            System.out.println("------- ITERATION:" + i + "-----------");
//            for (CellularParticle particle : this.particles) {
//                System.out.print(" [" + particle.getState() + "] ");
//                if (idx % 5 == 0) {
//                    System.out.println();
//                }
//                idx++;
//            }
        }
    }

    private void createOutputFile(List<CellularParticle> particles, int i, boolean includeZ) throws IOException {
        List<CellularParticle> livingParticles = particles.stream()
                .filter(particle -> State.ALIVE.equals(particle.getState()))
                .collect(Collectors.toList());

        // create new file
        String filePath = "./ovito." + (includeZ ? "3d." : "2d.") + i + ".txt";
        File file = new File(filePath);
        if (!file.createNewFile()) {
            file.delete();
            file.createNewFile();
        }


        // write on file
        FileWriter myFile = new FileWriter(filePath);
        myFile.write(Long.toString(livingParticles.stream().count()));
        myFile.write('\n');
        myFile.write('\n');

        long count = livingParticles.stream().count();
        for(int j=0; j<count ; j++){
            myFile.write(Double.toString(livingParticles.get(j).getRadius()));
            myFile.write('\t');
            myFile.write(Double.toString(livingParticles.get(j).getPosition().getX()));
            myFile.write('\t');
            myFile.write(Double.toString(livingParticles.get(j).getPosition().getY()));
            myFile.write('\n');

            if (includeZ) {
                myFile.write(Double.toString(livingParticles.get(j).getPosition().getZ()));
                myFile.write('\n');
            }
        }


        myFile.close();
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

    private int getTotalNeighborsAlive(Particle[][][] grid, Position position) {
        MooreNeighborhood mooreNeighborhood = new MooreNeighborhood(MOORE_NEIGHBORHOOD_RADIUS);
        List<Position> neighborsPositions = mooreNeighborhood.getPositions3D();

        int neighborsAlive = 0;
        for (Position neighborPosition : neighborsPositions) {
            int neighbor_x = (int) (position.getX() + neighborPosition.getX());
            int neighbor_y = (int) (position.getY() + neighborPosition.getY());
            int neighbor_z = (int) (position.getZ() + neighborPosition.getZ());

            if (
                    (neighbor_x >= 0) && (neighbor_x < grid.length)
                    && (neighbor_y >= 0) && (neighbor_y < grid.length)
                    && (neighbor_z >= 0) && (neighbor_z < grid.length)
            ) {
                State state = ((CellularParticle) grid[neighbor_x][neighbor_y][neighbor_z]).getState();
                if (state.equals(State.ALIVE)) {
                    neighborsAlive++;
                }
            }
        }
        return neighborsAlive;
    }
}
