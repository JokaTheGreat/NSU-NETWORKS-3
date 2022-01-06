package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TempWrapper {
    @JsonProperty("temp")
    private double temp;

    public double getTemp() {
        return temp;
    }
}
