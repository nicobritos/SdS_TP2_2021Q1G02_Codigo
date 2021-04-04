package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.CellularParticle;
import ar.edu.itba.sds_2021_q1_g02.models.Rules;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private static final Rules[] RULES_2D = {
            GameOfLife.defaultRules(),
            new Rules(2, 7, 3, 6),
            new Rules(3, 5, 3, 4)
    };
    private static final Rules[] RULES_3D = {
            GameOfLife.defaultRules(),
            new Rules(6, 14, 12, 13),
            new Rules(10, 20, 15, 17)
    };

    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);

//        System.out.println("Parsing particles");
//        Pair<List<CellularParticle>, Integer> particles = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath(), CommandParser.getInstance().isEnable3D());

//        GameOfLife GOL = new GameOfLife(particles.getKey(), particles.getValue());

        System.out.println("Running simulation");
//        if (CommandParser.getInstance().isEnable3D())
//            GOL.simulate3D(CommandParser.getInstance().getMaxIterations(), RULES_3D[0]);
//        else
//            GOL.simulate2D(CommandParser.getInstance().getMaxIterations(), RULES_2D[0]);

        // 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(10 * 5 * (RULES_2D.length + RULES_3D.length));
        for (int i = 10; i <= 100; i += 10) {
            for (int j = 0; j < 5; j++) {
                simulate2D(executor, latch, i, j);
                simulate3D(executor, latch, i, j);
            }
        }

        do {
            try {
                latch.await();
            } catch (InterruptedException e) {
                // Do nothing
            }
        } while (latch.getCount() > 0);

        executor.shutdown();
    }

    private static void simulate2D(ExecutorService executor, CountDownLatch latch, int i, int j) {
        Pair<List<CellularParticle>, Integer> particles2D;
        try {
            particles2D = ParticleParser.parseParticles(
                    "./sample_inputs/input_2d_" + i + "_" + j + ".txt",
                    false
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int rule = 0; rule < RULES_2D.length; rule++) {
            int finalRule = rule;

            executor.execute(() -> {
                String prefix = "ovito_particles_" + i + "_" + j + "_R" + finalRule + "_2D";

                GameOfLife GOL = new GameOfLife(particles2D.getKey(), particles2D.getValue());
                GOL.simulate2D(
                        200,
                        RULES_2D[finalRule],
                        "ovito_particles_" + i + "_" + j + "_R" + finalRule + "_2D"
                );

                System.out.println("Finished simulation: " + prefix);

                latch.countDown();
            });
        }
    }

    private static void simulate3D(ExecutorService executor, CountDownLatch latch, int i, int j) {
        Pair<List<CellularParticle>, Integer> particles3D;
        try {
            particles3D = ParticleParser.parseParticles(
                    "./sample_inputs/input_3d_" + i + "_" + j + ".txt",
                    true
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int rule = 0; rule < RULES_3D.length; rule++) {
            int finalRule = rule;

            executor.execute(() -> {
                String prefix = "ovito_particles_" + i + "_" + j + "_R" + finalRule + "_3D";
                GameOfLife GOL = new GameOfLife(particles3D.getKey(), particles3D.getValue());
                GOL.simulate3D(
                        50,
                        RULES_3D[finalRule],
                        prefix
                );

                System.out.println("Finished simulation: " + prefix);

                latch.countDown();
            });
        }
    }
}
