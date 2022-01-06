import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import deserialize.*;

public class Requester {
    private final String GRAPHHOPPER_API_KEY;
    private final String OPENTRIPMAP_API_KEY;
    private final String OPENWEATHER_API_KEY;

    private static final int RESPONSE_OK = 200;
    private static final double KELVIN_WATER_FREEZE_TEMP = 273.15;
    private static final int SEARCHING_RADIUS = 1000;
    private static final int PLACES_LIMIT = 5;

    private String lang;
    private final HttpClient client = HttpClient.newHttpClient();
    private LocationsWrapper locations = null;
    private Place[] places = null;

    public Requester() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("src/main/resources/api.properties"));

        GRAPHHOPPER_API_KEY = properties.get("graphhopperApiKey").toString();
        OPENTRIPMAP_API_KEY = properties.get("opentripmapApiKey").toString();
        OPENWEATHER_API_KEY = properties.get("openweatherApiKey").toString();
    }

    private String encodeString(String oldString) {
        return oldString.replace(" ", "%20");
    }

    private void setSearchingLanguage(String place) {
        if (Pattern.compile("\s*[a-zA-Z]+\s*[a-zA-Z]*\s*").matcher(place).matches()){
            lang = "en";
        }
        else {
            lang = "ru";
        }
    }

    public CompletableFuture<List<String>> requestLocations(String place) {
        setSearchingLanguage(place);

        String uriForRequest = String.format("https://graphhopper.com/api/1/geocode?q=%s&locale=de&debug=true&key=%s",
                encodeString(place),
                GRAPHHOPPER_API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriForRequest))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == RESPONSE_OK) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            locations = objectMapper.readValue(response.body(), LocationsWrapper.class);
                        }
                        catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }

                    return locations;
                })
                .thenApply(ignored -> {
                    List<String> names = new ArrayList<>();
                    for (Location location : locations.getLocations()) {
                        String name = location.getName();
                        String city = location.getCity();
                        if (city != null) {
                            names.add(String.format("%s, %s", name, city));
                        }
                        else {
                            names.add(name);
                        }
                    }

                    return names;
                });
    }

    public CompletableFuture<List<String>> requestPlaces(int locationIndex) {
        Location location = locations.getLocations().get(locationIndex);
        Point point = location.getPoint();

        String uriForRequest = String.format(Locale.ROOT, "https://api.opentripmap.com/0.1/%s/places/radius?radius=%d&lon=%g&lat=%g&format=json&limit=%d&apikey=%s",
                lang,
                SEARCHING_RADIUS,
                point.getLng(),
                point.getLat(),
                PLACES_LIMIT,
                OPENTRIPMAP_API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriForRequest))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == RESPONSE_OK){
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            places = objectMapper.readValue(response.body(), Place[].class);
                        }
                        catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }

                    return places;
                })
                .thenApply(ignored -> {
                    List<String> placesNames = new ArrayList<>();
                    for(Place place : places) {
                        String name = place.getName();
                        if (!name.isEmpty()) {
                            placesNames.add(Controller.TRIANGLE_DOWN + " " + name);
                        }
                    }
                    if (placesNames.isEmpty()) {
                        placesNames.add("Can't find any interesting places! Sorry!!!");
                    }

                    return placesNames;
                });
    }

    public CompletableFuture<String> requestDescription(int placeIndex) {
        Place place = places[placeIndex];

        String uriForRequest = String.format("https://api.opentripmap.com/0.1/%s/places/xid/%s?apikey=%s",
                lang,
                place.getXid(),
                OPENTRIPMAP_API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriForRequest))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    PlaceInfo placeWithDescription = null;

                    if (response.statusCode() == RESPONSE_OK){
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            placeWithDescription = objectMapper.readValue(response.body(), PlaceInfo.class);
                        }
                        catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }

                    return placeWithDescription;
                })
                .thenApply(placeWithDescription -> {
                    DescrWrapper descrWrapper = placeWithDescription.getDescrWrapper();
                    if (descrWrapper != null) {
                        String description = descrWrapper.getDescr();
                        if (description != null) {
                            return description;
                        }
                    }

                    AlterDescrWrapper alterDescrWrapper = placeWithDescription.getAlterDescrWrapper();
                    if (alterDescrWrapper != null) {
                        String description = alterDescrWrapper.getDescr();
                        if (description != null) {
                            return description;
                        }
                    }

                    return "";
                });
    }

    public CompletableFuture<Double> requestTemp(int locationIndex) {
        Location location = locations.getLocations().get(locationIndex);
        Point point = location.getPoint();

        String uriForRequest = String.format(Locale.ROOT, "https://api.openweathermap.org/data/2.5/weather?lat=%g&lon=%g&appid=%s",
                point.getLat(),
                point.getLng(),
                OPENWEATHER_API_KEY
        );

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uriForRequest))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    Weather weather = null;

                    if (response.statusCode() == RESPONSE_OK) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            weather = objectMapper.readValue(response.body(), Weather.class);
                        }
                        catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }

                    return weather;
                })
                .thenApply(weather -> {
                    TempWrapper tempWrapper = weather.getTempWrapper();
                    return tempWrapper.getTemp() - KELVIN_WATER_FREEZE_TEMP;
                });
    }
}
