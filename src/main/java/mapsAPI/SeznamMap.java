package mapsAPI;

import model.Address;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SeznamMap implements AbstractMap {

    public static SeznamMap newInstance(String baseURL, String key) {
        return new SeznamMap();
    }


    @Override
    public Address process(String address) {
        // TODO Seznam maps doesn't have a public REST API.
        throw new NotImplementedException();
    }

    @Override
    public Type getType() {
        return Type.SEZNAM_MAPS;
    }
}
