import filter.AddressFilter;
import mapsAPI.AbstractMap;
import mapsAPI.GoogleMap;
import mapsAPI.MSBingMap;
import mapsAPI.SeznamMap;
import model.APIKeys;
import tools.Args;
import tools.CSVParser;
import tools.YAMLParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Configuration {

    private final List<AbstractMap> maps;
    private final List<String> dataStream;
    private final Map<Class<? extends AbstractMap>, BufferedWriter> writers;

    public static Configuration config(Args args) throws IOException {
        return new Configuration(args);
    }

    private Configuration(Args args) throws IOException {
        Optional<APIKeys> keys = YAMLParser.parse(args.getKeysFile(), APIKeys.class);
        if (!keys.isPresent()) {
            throw new IOException("Cannot read the file with API keys.");
        }

        maps = MapConfig.parse(keys.get(), args.isMsBingMaps(), args.isGoogleMaps(), args.isSeznamMaps());

        writers = new HashMap<>(this.maps.size());

        for (AbstractMap map : maps) {
            String fileName = map.getClass().getSimpleName() + "Output.csv";
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
            writers.put(map.getClass(), writer);
        }

        if (args.getInput() != null) {
            dataStream = CSVParser.read(args.getInput(), AddressFilter.class);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Address: ");
            dataStream =  Arrays.asList(scanner.nextLine());
        }
    }

    public List<AbstractMap> getMaps() {
        return maps;
    }

    public List<String> getDataStream() {
        return dataStream;
    }

    public Map<Class<? extends AbstractMap>, BufferedWriter> getWriters() {
        return writers;
    }

    private static class MapConfig {

        private static final String GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/geocode/json";
        private static final String MSBING_MAPS_API = "http://dev.virtualearth.net/REST/v1/Locations";
        private static final String SEZNAM_MAPS_API = "";

        static List<AbstractMap> parse(APIKeys keys, boolean MSBingMaps, boolean GMaps, boolean SMaps) {
            List<AbstractMap> maps = new ArrayList<>();

            if (MSBingMaps) {
                maps.add(MSBingMap.newInstance(MSBING_MAPS_API, keys.getMsBingMapsAPI()));
            }

            if (GMaps) {
                maps.add(GoogleMap.newInstance(GOOGLE_MAPS_API, keys.getGoogleMapsAPI()));
            }

            if (SMaps) {
                maps.add(SeznamMap.newInstance(SEZNAM_MAPS_API, keys.getSeznamMapsAPI()));
            }

            return maps;
        }

    }

}
