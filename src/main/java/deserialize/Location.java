package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    @JsonProperty("name")
    private String name;
    @JsonProperty("city")
    private String city;
    private Point point;

    public Point getPoint() { return point; }
    public String getName() { return name; }
    public String getCity() { return city; }
}
