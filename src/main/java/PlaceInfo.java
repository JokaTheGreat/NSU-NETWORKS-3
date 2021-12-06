import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceInfo {
    @JsonProperty("name")
    private String name;
    private Address address;
    private Wikipedia_extracts wikipedia_extracts;
    private Point point;

    public Address getAddress() {
        return address;
    }

    public Wikipedia_extracts getWikiExtracts() {
        return wikipedia_extracts;
    }

    public String getName() {
        return name;
    }

    public Point getPoint() {
        return point;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter("wikipedia_extracts")
    public void setWikiExtracts(Wikipedia_extracts wikipedia_extracts) {
        this.wikipedia_extracts = wikipedia_extracts;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Address {
    @JsonProperty("city")
    private String city;

    public String getCity() {
        return city;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Wikipedia_extracts {
    @JsonProperty("text")
    private String text;

    public String getText() {
        return text;
    }
}
