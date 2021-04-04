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
    public static void main(String[] args) throws ParseException, IOException {
        CommandParser.getInstance().parse(args);

        System.out.println("Parsing particles");
        Pair<List<CellularParticle>, Integer> particles = ParticleParser.parseParticles(CommandParser.getInstance().getInputPath(), CommandParser.getInstance().isEnable3D());

        GameOfLife GOL = new GameOfLife(particles.getKey(), particles.getValue());

        System.out.println("Running simulation");
        if (CommandParser.getInstance().isEnable3D())
            GOL.simulate3D(CommandParser.getInstance().getMaxIterations(), GameOfLife.defaultRules());
        else
            GOL.simulate2D(CommandParser.getInstance().getMaxIterations(), new Rules(5, 6, 4, 6));
    }
}


