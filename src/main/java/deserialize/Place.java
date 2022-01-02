package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {
    @JsonProperty("xid")
    private String xid;
    @JsonProperty("name")
    private String name;

    public String getXid() {
        return xid;
    }

    public String getName() {
        return name;
    }
}
