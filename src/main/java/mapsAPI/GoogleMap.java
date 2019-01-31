package mapsAPI;

import model.Address;
import org.json.JSONArray;
import org.json.JSONObject;
import tools.HTTPRequest;
import tools.URLBuilder;

import java.util.Optional;

public class GoogleMap implements AbstractMap {

    private final String url;

    public static GoogleMap newInstance(String baseURL, String key) {
        return new GoogleMap(baseURL, key);
    }

    private GoogleMap(String baseURL, String key) {
        this.url = baseURL + "?key=" + key;
    }

    @Override
    public Address process(String address) {

        Optional<String> response = HTTPRequest.GET(URLBuilder.create(url)
                    .addParam("address", address)
                    .addParam("language", "cs")
                .build());

        if (!response.isPresent()) {
            return Address.empty();
        }

        return parseJSON(new JSONObject(response.get()));
    }

    @Override
    public Type getType() {
        return Type.GOOGLE_MAPS;
    }

    private Address parseJSON(JSONObject rootObject) {

        String status = rootObject.getString("status");

        if (status.equals("OVER_QUERY_LIMIT")) {
            return Address.create().setStatus(Address.Status.EXPIRED).get();
        }

        if (!status.equals("OK")) {
            return Address.empty();
        }

        JSONArray results = rootObject.getJSONArray("results");

        if (results.isEmpty()) {
            return Address.empty();
        }

        JSONObject result = results.getJSONObject(0);

        if (result.isEmpty()) {
            return Address.empty();
        }

        Address.Builder builder = Address.create();

        builder.setFullAddress(result.getString("formatted_address"));

        JSONArray components = result.getJSONArray("address_components");
        if (!components.isEmpty()) {
            for (Object cmp : components) {
                String item = ((JSONObject)cmp).getString("long_name");
                JSONArray types = ((JSONObject)cmp).getJSONArray("types");
                for (Object tp : types) {
                    String type = (String)tp;
                    switch (type) {
                        case "street_number":               builder.setHouseNumber(item);   break;
                        case "route":                       builder.setStreet(item);        break;
                        case "administrative_area_level_2": builder.setRegion(item);        break;
                        case "country":                     builder.setCountry(item);       break;
                        case "postal_code":                 builder.setZipCode(item);       break;
                        case "locality":                    builder.setCity(item);          break;
                    }
                }
            }
        }

        JSONObject geometry = result.getJSONObject("geometry");
        if (!geometry.isEmpty()) {
            JSONObject location = geometry.getJSONObject("location");
            if (!location.isEmpty()) {
                builder.setLatitude(location.getFloat("lat"));
                builder.setLongitude(location.getFloat("lng"));
            }
        }

        return builder.get();
    }
}
