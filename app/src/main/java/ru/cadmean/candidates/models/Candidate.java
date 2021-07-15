package ru.cadmean.candidates.models;

import java.util.Map;

public class Candidate {
    public String id;
    public String name;
    public String surname;
    public String middleName;
    public String party;
    public String shortDescription;
    public String description;
    public long year;
    public String placeOfResidence;
    public String work;
    public String workPosition;
    public long district;

    public Candidate() {}

    private static final String JSON_NAME = "name";
    private static final String JSON_SURNAME = "surname";
    private static final String JSON_MIDDLE_NAME = "middleName";
    private static final String JSON_PARTY = "party";
    private static final String JSON_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_YEAR = "year";
    private static final String JSON_PLACE_OF_RESIDENCE = "placeOfResidence";
    private static final String JSON_WORK = "work";
    private static final String JSON_WORK_POSITION = "workPosition";
    private static final String JSON_DISTRICT = "district";

    public Candidate(String id, Map<String, Object> json) {
        this.id = id;
        name = (String)json.get(JSON_NAME);
        surname = (String)json.get(JSON_SURNAME);
        middleName = (String)json.get(JSON_MIDDLE_NAME);
        party = (String)json.get(JSON_PARTY);
        shortDescription = (String)json.get(JSON_SHORT_DESCRIPTION);
        description = (String)json.get(JSON_DESCRIPTION);
        year = (long)json.get(JSON_YEAR);
        placeOfResidence = (String)json.get(JSON_PLACE_OF_RESIDENCE);
        work = (String)json.get(JSON_WORK);
        workPosition = (String)json.get(JSON_WORK_POSITION);
        district = (long)json.get(JSON_DISTRICT);
    }
}
