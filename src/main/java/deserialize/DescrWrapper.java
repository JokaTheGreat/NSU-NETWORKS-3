package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DescrWrapper {
    @JsonProperty("text")
    private String descr;

    public String getDescr() {
        return descr;
    }
}
