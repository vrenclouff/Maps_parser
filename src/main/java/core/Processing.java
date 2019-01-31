package core;

import mapsAPI.AbstractMap;
import model.Address;
import model.BasicAddress;
import tools.CSVParser;

import java.io.BufferedWriter;
import java.util.List;
import java.util.Map;

public class Processing {

    public static void run(List<String> dataStream, List<AbstractMap> mapsAPI, Map<Class<? extends AbstractMap>, BufferedWriter> writers) {
        Statistics statistics = Statistics.create(AbstractMap.Type.values().length);
        mapsAPI.forEach(api -> {
            CSVParser.writeHeader(BasicAddress.class, writers.get(api.getClass()));
            dataStream.forEach(source -> {
                long startTime = System.currentTimeMillis();
                Address result = api.process(source);
                long estimatedTime = System.currentTimeMillis() - startTime;
                statistics.process(source, result, api.getType(), estimatedTime);
                CSVParser.write(result, writers.get(api.getClass()));
            });
            statistics.print(api.getType());
        });
    }
}
