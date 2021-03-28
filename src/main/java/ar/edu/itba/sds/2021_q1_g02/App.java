import models.CellularParticle;
import models.Position;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        List<CellularParticle> particles = Arrays.asList(
                new CellularParticle(1, 1, 1, false),
                new CellularParticle(2, 1, 1, false),
                new CellularParticle(3, 1, 1, false),
                new CellularParticle(4, 1, 1, false),
                new CellularParticle(5, 1, 1, false),
                new CellularParticle(6, 1, 1, false),
                new CellularParticle(7, 1, 1, true),
                new CellularParticle(8, 1, 1, false),
                new CellularParticle(9, 1, 1, true),
                new CellularParticle(10, 1, 1, false),
                new CellularParticle(11, 1, 1, false),
                new CellularParticle(12, 1, 1, true),
                new CellularParticle(13, 1, 1, true),
                new CellularParticle(14, 1, 1, true),
                new CellularParticle(15, 1, 1, false),
                new CellularParticle(16, 1, 1, true),
                new CellularParticle(17, 1, 1, true),
                new CellularParticle(18, 1, 1, false),
                new CellularParticle(19, 1, 1, false),
                new CellularParticle(20, 1, 1, true),
                new CellularParticle(21, 1, 1, false),
                new CellularParticle(22, 1, 1, false),
                new CellularParticle(23, 1, 1, false),
                new CellularParticle(24, 1, 1, false),
                new CellularParticle(25, 1, 1, false)
        );
        int i = 0;
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                particles.get(i).setPosition(new Position(x, y));
                i++;
            }
        }
        int idx = 1;
        for (CellularParticle particle : particles) {
            System.out.print(" [" + particle.getState() + "] ");
            if (idx % 5 == 0) {
                System.out.println();
            }
            idx++;
        }
        GameOfLife GOL = new GameOfLife(particles, 5);
        GOL.simulate(10);
    }
}


