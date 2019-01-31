package core;

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

        map.put("requestTime", new long[3]);
        map.put("requestCount", new long[3]);
        map.put("score", (long)0);

        return map;
    }

    public void process(String source, Address address, AbstractMap.Type mapType, long requestTime) {

        Map<String, Object> sts = data.get(mapType.ordinal());


        ((long[])sts.get("requestTime"))[address.getStatus().ordinal()] += requestTime;
        ((long[])sts.get("requestCount"))[address.getStatus().ordinal()] += 1;


        if (!address.getStatus().equals(Address.Status.SUCCESS)) return;

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

        int successIndex = Address.Status.SUCCESS.ordinal();

        long[] counts = (long[])sts.get("requestCount");
        System.out.println("processed address:");

        for (Address.Status status : Address.Status.values()) {
            System.out.println(String.format("\t%s: %s", status.name(), counts[status.ordinal()]));
        }


        long avgReqTime = ((long[])sts.get("requestTime"))[successIndex] / counts[successIndex];
        System.out.println(String.format("average request time: %s ms", avgReqTime));

        long score = (Long)sts.get("score") / counts[successIndex];
        System.out.println(String.format("score: %s", score));
    }
}
