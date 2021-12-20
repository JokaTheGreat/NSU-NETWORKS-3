package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationsWrapper {
    private List<Location> locations;

    public List<Location> getLocations() { return locations; }

    @JsonSetter("hits")
    public void setHits(List<Location> hits) {
        this.locations = hits;
    }
}
