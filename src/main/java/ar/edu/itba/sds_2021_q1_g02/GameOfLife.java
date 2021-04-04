package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class GameOfLife {
    private static final int MOORE_NEIGHBORHOOD_RADIUS = 1;
    private static final MooreNeighborhood MOORE_NEIGHBORHOOD = new MooreNeighborhood(MOORE_NEIGHBORHOOD_RADIUS);
    private static final Rules DEFAULT_RULES = new Rules(1, 4, 3, 3);

    private List<CellularParticle> particles;
    private int M;

    public GameOfLife(List<CellularParticle> particles, final int M) {
        this.particles = particles;
        this.M = M;
    }

    //MaxIteration como metodo de corte
    public void simulate2D(int maxIterations, Rules rules) {
        Grid2D grid = new Grid2D(this.M);

        try {
            create2DOutputFile(this.particles, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < maxIterations && !areCuttingMethodsApplied(true); i++) {
            System.out.println("Started iteration: " + i);

            Map<CellularParticle, State> nextStates = new HashMap<>();
            grid.populateGrid(this.particles);

            for (int x = 0; x < M; x++) {
                for (int y = 0; y < M; y++) {
                    Position currentPosition = new Position(x, y);
                    int neighborsAlive = getTotalNeighborsAlive(grid.getGrid(), currentPosition);
                    Pair<State, Boolean> stateUpdated =
                            rules.applyRules(((CellularParticle) grid.getParticle(currentPosition)).getState(),
                                    neighborsAlive);
                    if (stateUpdated.getValue()) {
                        nextStates.put((CellularParticle) grid.getParticle(currentPosition), stateUpdated.getKey());
                    }
                }
            }
            for (Entry<CellularParticle, State> entry : nextStates.entrySet()) {
                updateParticleState(entry.getKey(), entry.getValue());
            }

            try {
                create2DOutputFile(this.particles, i);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Finished iteration: " + i);
        }
    }

    //MaxIteration como metodo de corte
    public void simulate3D(int maxIterations, Rules rules) {
        Grid3D grid = new Grid3D(this.M);

        try {
            create3DOutputFile(this.particles, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < maxIterations && !areCuttingMethodsApplied(false); i++) {
            System.out.println("Started iteration: " + i);

            Map<CellularParticle, State> nextStates = new HashMap<>();
            grid.populateGrid(this.particles);

            for (int x = 0; x < M; x++) {
                for (int y = 0; y < M; y++) {
                    for (int z = 0; z < M; z++) {
                        Position currentPosition = new Position(x, y, z);
                        int neighborsAlive = this.getTotalNeighborsAlive(grid.getGrid(), currentPosition);
                        Pair<State, Boolean> stateUpdated =
                                rules.applyRules(((CellularParticle) grid.getParticle(currentPosition)).getState(),
                                        neighborsAlive);
                        if (stateUpdated.getValue()) {
                            nextStates.put((CellularParticle) grid.getParticle(currentPosition), stateUpdated.getKey());
                        }
                    }
                }
            }
            for (Entry<CellularParticle, State> entry : nextStates.entrySet()) {
                updateParticleState(entry.getKey(), entry.getValue());
            }

            try {
                create3DOutputFile(this.particles, i);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Finished iteration: " + i);
        }
    }

    private void create2DOutputFile(List<CellularParticle> particles, int i) throws IOException {
        // create new file
        String filePath = "./ovito.2d." + i + ".txt";
        File file = new File(filePath);
        if (!file.createNewFile()) {
            file.delete();
            file.createNewFile();
        }

        long count = particles.size();
        // write on file
        FileWriter myFile = new FileWriter(filePath);
        myFile.write(Long.toString(count));
        myFile.write('\n');
        myFile.write('\n');

        Map<Integer, Map<Integer, CellularParticle>> xyParticleMap = new HashMap<>();
        for (CellularParticle particle : particles) {
            Map<Integer, CellularParticle> yParticleMap =
                    xyParticleMap.computeIfAbsent((int) particle.getPosition().getX(), integer -> new HashMap<>());

            yParticleMap.put((int) particle.getPosition().getY(), particle);
        }

        for (int x = 0; x < this.M; x++) {
            Map<Integer, CellularParticle> yParticleMap = xyParticleMap.get(x);

            for (int y = 0; y < this.M; y++) {
                CellularParticle particle = yParticleMap.get(y);

                myFile.write(Integer.toString(particle.getId()));
                myFile.write('\t');
                myFile.write(Double.toString(particle.getRadius()));
                myFile.write('\t');
                myFile.write(Double.toString(particle.getPosition().getX()));
                myFile.write('\t');
                myFile.write(Double.toString(particle.getPosition().getY()));
                myFile.write('\t');

                myFile.write(particle.getState().equals(State.ALIVE) ? "0" : "1");

                myFile.write('\n');
            }
        }


        myFile.close();
    }

    private void create3DOutputFile(List<CellularParticle> particles, int i) throws IOException {
        // create new file
        String filePath = "./ovito.3d." + i + ".txt";
        File file = new File(filePath);
        if (!file.createNewFile()) {
            file.delete();
            file.createNewFile();
        }

        long count = particles.stream().count();
        // write on file
        FileWriter myFile = new FileWriter(filePath);
        myFile.write(Long.toString(count));
        myFile.write('\n');
        myFile.write('\n');

        Map<Integer, Map<Integer, Map<Integer, CellularParticle>>> xyzParticleMap = new HashMap<>();
        for (CellularParticle particle : particles) {
            Map<Integer, Map<Integer, CellularParticle>> yzParticleMap =
                    xyzParticleMap.computeIfAbsent((int) particle.getPosition().getX(), integer -> new HashMap<>());
            Map<Integer, CellularParticle> zParticleMap =
                    yzParticleMap.computeIfAbsent((int) particle.getPosition().getY(), integer -> new HashMap<>());

            zParticleMap.put((int) particle.getPosition().getZ(), particle);
        }

        for (int x = 0; x < this.M; x++) {
            Map<Integer, Map<Integer, CellularParticle>> yzParticleMap = xyzParticleMap.get(x);

            for (int y = 0; y < this.M; y++) {
                Map<Integer, CellularParticle> zParticleMap = yzParticleMap.get(y);

                for (int z = 0; z < this.M; z++) {
                    CellularParticle particle = zParticleMap.get(z);

                    myFile.write(Integer.toString(particle.getId()));
                    myFile.write('\t');
                    myFile.write(Double.toString(particle.getRadius()));
                    myFile.write('\t');
                    myFile.write(Double.toString(particle.getPosition().getX()));
                    myFile.write('\t');
                    myFile.write(Double.toString(particle.getPosition().getY()));
                    myFile.write('\t');
                    myFile.write(Double.toString(particle.getPosition().getZ()));
                    myFile.write('\t');

                    myFile.write(particle.getState().equals(State.ALIVE) ? "0" : "1");


                    myFile.write('\n');
                }
            }
        }


        myFile.close();
    }

    private void updateParticleState(CellularParticle particle, State nextState) {
        particle.setState(nextState);
    }

    private int getTotalNeighborsAlive(Particle[][] grid, Position position) {
        List<Position> neighborsPositions = GameOfLife.MOORE_NEIGHBORHOOD.getPositions();
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
        List<Position> neighborsPositions = GameOfLife.MOORE_NEIGHBORHOOD.getPositions3D();

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

    private boolean areCuttingMethodsApplied(boolean is2Dsimulation) {
        for (CellularParticle particle : this.particles) {
            if ((particle.getPosition().getX() == 0) || (particle.getPosition().getX() == this.M) || (particle.getPosition().getY() == 0) || (particle.getPosition().getY() == this.M)) {
                if (particle.getState().equals(State.ALIVE))
                    return true;
            }
            if (!is2Dsimulation) {
                if ((particle.getPosition().getZ() == 0) || (particle.getPosition().getZ() == this.M)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Rules defaultRules() {
        return DEFAULT_RULES;
    }
}
