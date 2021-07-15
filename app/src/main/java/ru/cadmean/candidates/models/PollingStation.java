package ru.cadmean.candidates.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class PollingStation {
    public String id;
    public String address;
    public String name;
    public String description;
    public GeoPoint coordinates;
    public long district;

    public PollingStation() {}

    private static final String JSON_ADDRESS = "address";
    private static final String JSON_NAME = "name";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_COORDINATES = "coordinates";
    private static final String JSON_DISTRICT = "district";

    public PollingStation(String id, Map<String, Object> data) {
        this.id = id;
        address = (String)data.get(JSON_ADDRESS);
        name = (String)data.get(JSON_NAME);
        description = (String)data.get(JSON_DESCRIPTION);
        coordinates = (GeoPoint)data.get(JSON_COORDINATES);
        district = (long)data.get(JSON_DISTRICT);
    }
}
