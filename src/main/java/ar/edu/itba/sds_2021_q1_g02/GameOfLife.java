package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.*;
import javafx.util.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class GameOfLife {
    private static final int MOORE_NEIGHBORHOOD_RADIUS = 1;
    private static final MooreNeighborhood MOORE_NEIGHBORHOOD = new MooreNeighborhood(MOORE_NEIGHBORHOOD_RADIUS);
    private static final Rules DEFAULT_RULES = new Rules(1, 4, 3, 3);

    private final List<CellularParticle> particles;
    private final int M;
    private final double center;
    private final double maxDistance;

    public GameOfLife(List<CellularParticle> particles, final int M) {
        this.particles = particles;
        this.M = M;
        this.center = M / 2.0;
        this.maxDistance = Math.sqrt(
                Math.pow(this.center, 2) +
                Math.pow(this.center, 2) +
                Math.pow(this.center, 2)
        );
    }

    //MaxIteration como metodo de corte
    public void simulate2D(int maxIterations, Rules rules, String fileNamePrefix) {
        Grid2D grid = new Grid2D(this.M);

        List<CellularParticle> particles = new ArrayList<>(this.particles.size());
        for (CellularParticle particle : this.particles) {
            particles.add(particle.copy());
        }
        grid.populateGrid(particles);

        try {
            this.create2DOutputFile(particles, 0, fileNamePrefix);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long alive = particles.stream().filter(cellularParticle -> cellularParticle.getState().equals(State.ALIVE)).count();
        for (int i = 1; i < maxIterations && !this.areCuttingMethodsApplied(true, particles); i++) {
            Map<CellularParticle, State> nextStates = new HashMap<>();

            for (int x = 0; x < this.M; x++) {
                for (int y = 0; y < this.M; y++) {
                    Position currentPosition = new Position(x, y);
                    int neighborsAlive = this.getTotalNeighborsAlive(grid.getGrid(), currentPosition);
                    Pair<State, Boolean> stateUpdated =
                            rules.applyRules(((CellularParticle) grid.getParticle(currentPosition)).getState(),
                                    neighborsAlive);
                    if (stateUpdated.getValue()) {
                        nextStates.put((CellularParticle) grid.getParticle(currentPosition), stateUpdated.getKey());
                    }
                }
            }
            for (Entry<CellularParticle, State> entry : nextStates.entrySet()) {
                alive += this.updateParticleState(entry.getKey(), entry.getValue());
            }

            try {
                this.create2DOutputFile(particles, i, fileNamePrefix);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (alive == 0 && rules.getSolitudeDeathLimit() > 0) {
                // Nothing else to do, all particles are dead
                break;
            }
        }
    }

    //MaxIteration como metodo de corte
    public void simulate3D(int maxIterations, Rules rules, String fileNamePrefix) {
        Grid3D grid = new Grid3D(this.M);

        List<CellularParticle> particles = new ArrayList<>(this.particles.size());
        for (CellularParticle particle : this.particles) {
            particles.add(particle.copy());
        }
        grid.populateGrid(particles);

        try {
            this.create3DOutputFile(particles, 0, fileNamePrefix);
        } catch (IOException e) {
            e.printStackTrace();
        }

        long alive = particles.stream().filter(cellularParticle -> cellularParticle.getState().equals(State.ALIVE)).count();
        for (int i = 1; i < maxIterations && !this.areCuttingMethodsApplied(false, particles); i++) {
            Map<CellularParticle, State> nextStates = new HashMap<>();

            for (int x = 0; x < this.M; x++) {
                for (int y = 0; y < this.M; y++) {
                    for (int z = 0; z < this.M; z++) {
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
                alive += this.updateParticleState(entry.getKey(), entry.getValue());
            }

            try {
                this.create3DOutputFile(particles, i, fileNamePrefix);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (alive == 0 && rules.getSolitudeDeathLimit() > 0) {
                // Nothing else to do, all particles are dead
                break;
            }
        }
    }

    private void create2DOutputFile(List<CellularParticle> particles, int i, String fileNamePrefix) throws IOException {
        // create new file
        String filePath = "./sample_outputs/" + fileNamePrefix + "." + i + ".xyz";
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
        myFile.write("Properties=id:R:1:radius:R:1:pos:R:2:transparency:R:1:color:R:3\n");

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
                myFile.write('\t');

                Color particleColor;
                if (particle.getState().equals(State.ALIVE))
                    particleColor = this.getParticleColor(particle, true);
                else
                    particleColor = new Color(0, 0, 0);
                myFile.write(Double.toString(particleColor.getRed()));
                myFile.write('\t');
                myFile.write(Double.toString(particleColor.getGreen()));
                myFile.write('\t');
                myFile.write(Double.toString(particleColor.getBlue()));

                myFile.write('\n');
            }
        }

        myFile.close();
    }

    private void create3DOutputFile(List<CellularParticle> particles, int i, String fileNamePrefix) throws IOException {
        // create new file
        String filePath = "./sample_outputs/" + fileNamePrefix + "." + i + ".xyz";
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
        myFile.write("Properties=id:R:1:radius:R:1:pos:R:3:transparency:R:1:color:R:3\n");

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

                    Color particleColor;
                    if (particle.getState().equals(State.ALIVE))
                        particleColor = this.getParticleColor(particle, false);
                    else
                        particleColor = new Color(0, 0, 0);
                    myFile.write(Double.toString(particleColor.getRed()));
                    myFile.write('\t');
                    myFile.write(Double.toString(particleColor.getGreen()));
                    myFile.write('\t');
                    myFile.write(Double.toString(particleColor.getBlue()));


                    myFile.write('\n');
                }
            }
        }

        myFile.close();
    }

    private int updateParticleState(CellularParticle particle, State nextState) {
        particle.setState(nextState);

        if (nextState.equals(State.ALIVE))
            return 1;
        return -1;
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

    private Color getParticleColor(Particle particle, boolean is2DSimulation) {
        double rawDistance =
                Math.pow(particle.getPosition().getX() - this.center, 2) +
                Math.pow(particle.getPosition().getY() - this.center, 2);
        if (!is2DSimulation) {
            rawDistance += Math.pow(particle.getPosition().getZ() - this.center, 2);
        }

        double distance = Math.sqrt(rawDistance);

        double colorCutoff = this.maxDistance / 2;
        if (distance < colorCutoff) {
            return new Color(1 - (distance / colorCutoff), (distance / colorCutoff), 0);
        } else {
            distance = distance - colorCutoff;
            return new Color((distance / colorCutoff), 1 - (distance / colorCutoff), 0);
        }
    }

    private boolean areCuttingMethodsApplied(boolean is2DSimulation, List<CellularParticle> particles) {
        for (CellularParticle particle : particles) {
            if ((particle.getPosition().getX() == 0) || (particle.getPosition().getX() == this.M - 1) || (particle.getPosition().getY() == 0) || (particle.getPosition().getY() == this.M - 1)) {
                if (particle.getState().equals(State.ALIVE))
                    return true;
            }
            if (!is2DSimulation) {
                if ((particle.getPosition().getZ() == 0) || (particle.getPosition().getZ() == this.M - 1)) {
                    if (particle.getState().equals(State.ALIVE))
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
