package deserialize;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {
    @JsonProperty("lat")
    private double lat;
    @JsonAlias({"lng", "lon"})
    private double lng;

    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }
}