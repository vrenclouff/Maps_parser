package mapsAPI;

import model.Address;
import org.json.JSONArray;
import org.json.JSONObject;
import tools.HTTPRequest;
import tools.URLBuilder;

import java.util.Optional;

public class MSBingMap implements AbstractMap {

    private final String url;

    public static MSBingMap newInstance(String baseURL, String key) {
        return new MSBingMap(baseURL, key);
    }

    private MSBingMap(String baseURL, String key) {
        this.url = baseURL+"?&key=" + key;
    }

    @Override
    public Address process(String address) {

        Optional<String> response = HTTPRequest.GET(URLBuilder.create(url)
                    .addParam("query", address)
                    .addParam("setLang", "cs")
                .build());

        if (!response.isPresent()) {
            return Address.empty();
        }

        return parseJSON(new JSONObject(response.get()));
    }

    @Override
    public Type getType() {
        return Type.MS_BING_MAPS;
    }

    private Address parseJSON(JSONObject rootObject) {

        String status = rootObject.getString("statusDescription");

        if (!status.equals("OK")) {
            return Address.empty();
        }

        JSONArray results = rootObject.getJSONArray("resourceSets");

        if (results.isEmpty()) {
            return Address.empty();
        }

        JSONObject result = results.getJSONObject(0);

        if (result.isEmpty()) {
            return Address.empty();
        }

        JSONArray resources = result.getJSONArray("resources");

        if (resources.isEmpty()) {
            return Address.empty();
        }

        JSONObject components = resources.getJSONObject(0);

        if (components.isEmpty()) {
            return Address.empty();
        }

        Address.Builder builder = Address.create();

        if (!components.isNull("address")) {
            JSONObject address = components.getJSONObject("address");

            if (!address.isNull("formattedAddress")) {
                builder.setFullAddress(address.getString("formattedAddress"));
            }
            if (!address.isNull("countryRegion")) {
                builder.setCountry(address.getString("countryRegion"));
            }
            if (!address.isNull("locality")) {
                builder.setCity(address.getString("locality"));
            }
            if (!address.isNull("adminDistrict2")) {
                builder.setRegion(address.getString("adminDistrict2"));
            }
            if (!address.isNull("addressLine")) {
                builder.setStreet(address.getString("addressLine"));
            }
            if (!address.isNull("postalCode")) {
                builder.setZipCode(address.getString("postalCode"));
            }
        }

        if (!components.isNull("point")) {
            JSONObject geometry = components.getJSONObject("point");
            if (!geometry.isEmpty() && !geometry.isNull("coordinates")) {
                JSONArray coordinates = geometry.getJSONArray("coordinates");
                if (!components.isEmpty()) {
                    builder.setLatitude(coordinates.getFloat(0));
                    builder.setLongitude(coordinates.getFloat(1));
                }
            }
        }


        return builder.get();
    }
}
