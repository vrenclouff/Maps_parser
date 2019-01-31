package model;

public interface Address {

    enum Status {
        SUCCESS, EXPIRED, ERROR,
    }

    static Address empty() {
        BasicAddress address = new BasicAddress();
        address.setStatus(Status.ERROR);
        return address;
    }

    static Builder create() {
        BasicAddress address = new BasicAddress();
        address.setStatus(Status.SUCCESS);
        return new AddressBasicBuilder(address);
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
    Status getStatus();

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
        Builder setStatus(Status status);

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
        public Builder setStatus(Status status) {
            address.setStatus(status);
            return this;
        }

        @Override
        public Address get() {
            return address;
        }
    }
}

