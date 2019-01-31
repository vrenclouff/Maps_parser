package core;

import tools.Args;

import java.io.BufferedWriter;

public class Main {

    public static void main(String[] argv) {
        try {
            Args args = Args.parse(argv);
            Configuration config = Configuration.config(args);

            Processing.run(
                    config.getDataStream(),
                    config.getMaps(),
                    config.getWriters()
            );

            for (BufferedWriter item : config.getWriters().values()) {
                item.close();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
