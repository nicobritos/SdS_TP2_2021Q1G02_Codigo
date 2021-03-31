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

            try {
                createOutputFile(this.particles, i);
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

    private void deletePastFiles(){
        File file = new File("./ovito.1.txt");
        int i = 2;
        while (file.delete()){
            file = new File("./ovito." + i+ ".txt");
            i+=1;
        }
    }

    private void createOutputFile(List<CellularParticle> particles, int i) throws IOException {

        List<CellularParticle> livingParticles = particles.stream()
                .filter(particle -> State.ALIVE.equals(particle.getState()))
                .collect(Collectors.toList());

        // create new file
        File file = new File("./ovito."+ i + ".txt");
        if (!file.createNewFile()) {
            file.delete();
            file.createNewFile();
        }


        // write on file
        FileWriter myFile = new FileWriter("./ovito."+ i +".txt");
        myFile.write(Long.toString(livingParticles.stream().count()));
        myFile.write('\n');
        myFile.write('\n');

        System.out.println(Long.toString((livingParticles.stream().count())));
        long count = livingParticles.stream().count();
        for(int j=0; j<count ; j++){

            myFile.write(Double.toString(livingParticles.get(j).getRadius()));
            myFile.write('\t');
            myFile.write(Double.toString(livingParticles.get(j).getPosition().getX()));
            myFile.write('\t');
            myFile.write(Double.toString(livingParticles.get(j).getPosition().getY()));
            myFile.write('\n');
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
}
