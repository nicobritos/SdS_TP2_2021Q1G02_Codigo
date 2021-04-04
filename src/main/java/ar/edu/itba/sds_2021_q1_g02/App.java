package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.CellularParticle;
import ar.edu.itba.sds_2021_q1_g02.models.Rules;
import ar.edu.itba.sds_2021_q1_g02.parsers.CommandParser;
import ar.edu.itba.sds_2021_q1_g02.parsers.ParticleParser;
import javafx.util.Pair;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.*;

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

        System.out.println("Parsing particles");
        Pair<List<CellularParticle>, Integer> particles = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath(), CommandParser.getInstance().isEnable3D());

        GameOfLife GOL = new GameOfLife(particles.getKey(), particles.getValue());

        System.out.println("Running simulation");
        if (CommandParser.getInstance().isEnable3D())
            GOL.simulate3D(CommandParser.getInstance().getMaxIterations(), RULES_3D[0]);
        else
            GOL.simulate2D(CommandParser.getInstance().getMaxIterations(), RULES_2D[0]);
    }
}


