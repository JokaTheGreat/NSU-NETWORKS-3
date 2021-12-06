import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coords {
    private List<Hits> hits;

    public List<Hits> getHits() { return hits; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Hits {
    @JsonProperty("name")
    private String name;
    private Point point;

    public Point getPoint() { return point; }
    public String getName() { return name; }
}

class Point {
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


