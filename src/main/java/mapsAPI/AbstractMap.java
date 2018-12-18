package mapsAPI;

import model.Address;

public interface AbstractMap {

    enum Type {
        GOOGLE_MAPS     ("Google Maps"),
        MS_BING_MAPS    ("Microsoft Bing Maps"),
        SEZNAM_MAPS     ("Seznam Maps"),
        ;

        private final String name;
        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    Address process(String address);

    Type getType();
}
