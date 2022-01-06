package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceInfo {
    @JsonProperty("name")
    private String name;
    private DescrWrapper descrWrapper;
    @JsonProperty("info")
    private AlterDescrWrapper alterDescrWrapper;

    public DescrWrapper getDescrWrapper() {
        return descrWrapper;
    }

    public AlterDescrWrapper getAlterDescrWrapper() {
        return alterDescrWrapper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter("wikipedia_extracts")
    public void setDescrWrapper(DescrWrapper descrWrapper) {
        this.descrWrapper = descrWrapper;
    }
}
