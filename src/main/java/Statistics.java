import mapsAPI.AbstractMap;
import model.Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {

    private final List<Map<String, Object>> data;

    public static Statistics create(int size) {
        return new Statistics(size);
    }

    private Statistics(int size) {
        this.data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            this.data.add(emptyDataSheet());
        }
    }

    private Map<String, Object> emptyDataSheet() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("requestTime", (long)0);
        map.put("requestCount", (long)0);
        map.put("score", (long)0);

        return map;
    }

    public void process(String source, Address address, AbstractMap.Type mapType, long requestTime) {

        Map<String, Object> sts = data.get(mapType.ordinal());

        sts.merge("requestTime", requestTime, (a, b) -> (Long)a + (Long)b);
        sts.merge("requestCount", (long)1, (a, b) -> (Long)a + (Long)b);


        String normalizedSource = normalize(source);

        long score = 0;

        score = isNotEmpty(address.getCountry()) ? setScoreLevel(0, score) : score;
        score = isNotEmpty(address.getRegion()) && normalizedSource.contains(normalize(address.getRegion())) ? setScoreLevel(1, score) : score;

        score = address.getLatitude() != null && address.getLongitude() != null ? setScoreLevel(3, score) : score;
        score = isNotEmpty(address.getCity()) && normalizedSource.contains(normalize(address.getCity())) ? setScoreLevel(4, score) : score;
        score = isNotEmpty(address.getZipCode()) ? setScoreLevel(5, score) : score;

        score = isNotEmpty(address.getStreet()) && normalizedSource.contains(normalize(address.getStreet())) ? setScoreLevel(9, score) : score;
        score = isNotEmpty(address.getHouseNumber()) && normalizedSource.contains(normalize(address.getHouseNumber())) ? setScoreLevel(10, score) : score;


        sts.merge("score", score, (a, b) -> (Long)a + (Long)b);
    }

    private long setScoreLevel(int level, long score) {
        return score | (1 << level);
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    private String normalize(String string) {
        return string.replaceAll(" ", "").toLowerCase();
    }

    public void print(AbstractMap.Type mapType) {
        System.out.println("---- " + mapType.getName() + " ------");
        Map<String, Object> sts = data.get(mapType.ordinal());

        long count = (Long)sts.get("requestCount");
        System.out.println(String.format("processed address: %s", count));

        long avgReqTime = (Long)sts.get("requestTime") / count;
        System.out.println(String.format("average request time: %s ms", avgReqTime));

        long score = (Long)sts.get("score") / count;
        System.out.println(String.format("score: %s", score));
    }
}
