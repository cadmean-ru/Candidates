package ru.cadmean.candidates.Utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.util.List;

public class District {
    private int number;
    private List<LatLng> coordinates;

    public District(int number, List<LatLng> coordinates) {
        this.number = number;
        this.coordinates = coordinates;
    }

    public int getNumber() {
        return number;
    }

    public List<LatLng> getCoordinates() {
        return coordinates;
    }

    public boolean checkCoordinates(LatLng coord) {
        return PolyUtil.containsLocation(coord, coordinates, true);
    }

    @Override
    public String toString() {
        return "District{" +
                "number=" + number +
                ", coordinates=" + coordinates +
                '}';
    }
}
