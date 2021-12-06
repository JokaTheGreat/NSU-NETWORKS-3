import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {
    private WeatherDescr[] weatherDescr;
    private TempWrapper tempWrapper;

    public WeatherDescr[] getWeatherDescr() {
        return weatherDescr;
    }

    public TempWrapper getTempWrapper() {
        return tempWrapper;
    }

    @JsonSetter("weather")
    public void setWeatherDescr(WeatherDescr[] weatherDescr) {
        this.weatherDescr = weatherDescr;
    }

    @JsonSetter("main")
    public void setTempWrapper(TempWrapper tempWrapper) {
        this.tempWrapper = tempWrapper;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherDescr {
    @JsonProperty("description")
    private String description;

    public String getDescription() {
        return description;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TempWrapper {
    @JsonProperty("temp")
    private double temp;

    public double getTemp() {
        return temp;
    }
}
