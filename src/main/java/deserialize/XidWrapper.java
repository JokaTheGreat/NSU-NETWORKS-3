package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class XidWrapper {
    @JsonProperty("xid")
    private String xid;

    public String getXid() {
        return xid;
    }
}
