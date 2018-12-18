package model;

public interface Address {

    static Address empty() {
        return new BasicAddress();
    }

    static Builder create() {
        return new AddressBasicBuilder(new BasicAddress());
    }

    String getCountry();
    String getRegion();
    String getCity();
    String getZipCode();
    String getStreet();
    String getHouseNumber();
    Float getLatitude();
    Float getLongitude();
    String getFull();

    interface Builder {

        Builder setFullAddress(String fullAddress);

        Builder setCountry(String country);
        Builder setRegion(String region);
        Builder setCity(String city);
        Builder setZipCode(String zipCode);
        Builder setStreet(String street);
        Builder setHouseNumber(String houseNumber);
        Builder setLatitude(float latitude);
        Builder setLongitude(float longitude);

        Address get();
    }

    class AddressBasicBuilder implements Builder {
        private BasicAddress address;

        private AddressBasicBuilder(BasicAddress address) {
            this.address = address;
        }


        @Override
        public Builder setFullAddress(String fullAddress) {
            address.setFull(fullAddress);
            return this;
        }

        @Override
        public Builder setCountry(String country) {
            address.setCountry(country);
            return this;
        }

        @Override
        public Builder setRegion(String region) {
            address.setRegion(region);
            return this;
        }

        @Override
        public Builder setCity(String city) {
            address.setCity(city);
            return this;
        }

        @Override
        public Builder setZipCode(String zipCode) {
            address.setZipCode(zipCode);
            return this;
        }

        @Override
        public Builder setStreet(String street) {
            address.setStreet(street);
            return this;
        }

        @Override
        public Builder setHouseNumber(String houseNumber) {
            address.setHouseNumber(houseNumber);
            return this;
        }

        @Override
        public Builder setLatitude(float latitude) {
            address.setLatitude(latitude);
            return this;
        }

        @Override
        public Builder setLongitude(float longitude) {
            address.setLongitude(longitude);
            return this;
        }

        @Override
        public Address get() {
            return address;
        }
    }
}

