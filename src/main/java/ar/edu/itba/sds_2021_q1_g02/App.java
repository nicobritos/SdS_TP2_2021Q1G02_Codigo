package ar.edu.itba.sds_2021_q1_g02;

import ar.edu.itba.sds_2021_q1_g02.models.CellularParticle;
import ar.edu.itba.sds_2021_q1_g02.models.Position;

import java.util.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors


public class App {
    public static void main(String[] args) {
        List<CellularParticle> particles = new ArrayList<CellularParticle>();
        System.out.println(args[0]);
        int i = 0;
        try {
            File myObj = new File(args[0]);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] info = data.split("\t");
                CellularParticle particle = new CellularParticle(i, Double.parseDouble(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[4]) == 1 ? true : false);
                particles.add(particle);
                particles.get(i).setPosition(new Position(Integer.parseInt(info[2]), Integer.parseInt(info[3])));
                i++;

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

//        List<CellularParticle> particles = Arrays.asList(
//                new CellularParticle(1, 1, 1, false),
//                new CellularParticle(2, 1, 1, false),
//                new CellularParticle(3, 1, 1, false),
//                new CellularParticle(4, 1, 1, false),
//                new CellularParticle(5, 1, 1, false),
//                new CellularParticle(6, 1, 1, false),
//                new CellularParticle(7, 1, 1, true),
//                new CellularParticle(8, 1, 1, false),
//                new CellularParticle(9, 1, 1, true),
//                new CellularParticle(10, 1, 1, false),
//                new CellularParticle(11, 1, 1, false),
//                new CellularParticle(12, 1, 1, true),
//                new CellularParticle(13, 1, 1, true),
//                new CellularParticle(14, 1, 1, true),
//                new CellularParticle(15, 1, 1, false),
//                new CellularParticle(16, 1, 1, true),
//                new CellularParticle(17, 1, 1, true),
//                new CellularParticle(18, 1, 1, false),
//                new CellularParticle(19, 1, 1, false),
//                new CellularParticle(20, 1, 1, true),
//                new CellularParticle(21, 1, 1, false),
//                new CellularParticle(22, 1, 1, false),
//                new CellularParticle(23, 1, 1, false),
//                new CellularParticle(24, 1, 1, false),
//                new CellularParticle(25, 1, 1, false)
//        );
//        int i = 0;
//        for (int x = 0; x < 5; x++) {
//            for (int y = 0; y < 5; y++) {
//                particles.get(i).setPosition(new Position(x, y));
//                i++;
//            }
//        }
//        int idx = 1;
//        for (CellularParticle particle : particles) {
//            System.out.print(" [" + particle.getState() + "] ");
//            if (idx % 110 == 0) {
//                System.out.println();
//            }
//            idx++;
//        }
        GameOfLife GOL = new GameOfLife(particles, 110);
        GOL.simulate2D(10);
    }

}


