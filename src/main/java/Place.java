import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {
    @JsonProperty("xid")
    private String xid;
    @JsonProperty("name")
    private String name;
    private Point point;

    public String getName() {
        return name;
    }

    public String getXid() {
        return xid;
    }

    public Point getPoint() {
        return point;
    }
}