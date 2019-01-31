package tools;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class Args {

    public static Args parse(String[] argv) throws Exception {
        Args args = new Args();
        JCommander.newBuilder().addObject(args).build().parse(argv);

        if (!Paths.get(args.getKeysFile()).toFile().exists()) {
            throw new FileNotFoundException("core.Configuration file wan't found.");
        }

        if (args.getInput() != null && !Paths.get(args.getInput()).toFile().exists()) {
            throw new FileNotFoundException("Input file wan't found.");
        }

        return args;
    }

    @Parameter(names = "-f", description = "Input file")
    private String input;

    @Parameter(names = "-g", description = "Google maps")
    private boolean googleMaps = false;

    @Parameter(names = "-s", description = "Seznam maps")
    private boolean seznamMaps = false;

    @Parameter(names = "-b", description = "MS Bing maps")
    private boolean msBingMaps = false;

    @Parameter(names = "-key", description = "Keys config file for APIs", required = true)
    private String keysFile;

    public String getInput() {
        return input;
    }

    public boolean isGoogleMaps() {
        return googleMaps;
    }

    public boolean isSeznamMaps() {
        return seznamMaps;
    }

    public boolean isMsBingMaps() {
        return msBingMaps;
    }

    public String getKeysFile() {
        return keysFile;
    }
}
