package deserialize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    private TempWrapper tempWrapper;

    public TempWrapper getTempWrapper() {
        return tempWrapper;
    }

    @JsonSetter("main")
    public void setTempWrapper(TempWrapper tempWrapper) {
        this.tempWrapper = tempWrapper;
    }
}
