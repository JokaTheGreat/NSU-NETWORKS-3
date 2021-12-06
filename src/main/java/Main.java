import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Main {
    private static final String FIRST_API_KEY = "";
    private static final String SECOND_API_KEY = ""; //вставьте свои ключи пожалуйста, моих я не дам
    private static final String THIRD_API_KEY = "";

    public static void main(String[] args) throws IOException, InterruptedException {
        String place = "London"; //пробелы придется самому изменить на %20 //percentEscaper//UrlEscaper
        //мб вместо огромной мапы использовать классы джсона
        System.out.println(place);
        getCoords(place);
    }

    private static void getCoords(String place) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://graphhopper.com/api/1/geocode?q=" + place + "&locale=de&debug=true&key=" + FIRST_API_KEY))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        Coords coords = objectMapper.readValue(response.body(), Coords.class);
        List<Hits> hits = coords.getHits();

        hits.forEach((element) -> {
            Point pointForElement = element.getPoint();
            System.out.println(element.getName() + ": " + pointForElement.getLat() + ", " + pointForElement.getLng());
        });

        getPlaces(hits);
    }

    private static void getPlaces(List<Hits> places) throws IOException, InterruptedException {
        List<Place> newPlaces = new ArrayList<Place>();
        HttpClient client = HttpClient.newHttpClient();

        places.forEach((element) -> {
            Point pointForElement = element.getPoint();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://api.opentripmap.com/0.1/en/places/radius?radius=1000&lon=" + pointForElement.getLng() + "&lat=" + pointForElement.getLat() + "&format=json&limit=5&apikey=" + SECOND_API_KEY))
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                ObjectMapper objectMapper = new ObjectMapper();
                Place[] manyPlaces = objectMapper.readValue(response.body(), Place[].class);
                newPlaces.addAll(Arrays.asList(manyPlaces));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        getDescription(newPlaces);
    }

    private static void getDescription(List<Place> places) throws IOException, InterruptedException {
        List<PlaceInfo> newPlaces = new ArrayList<PlaceInfo>();
        HttpClient client = HttpClient.newHttpClient();

        for (Place place : places) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://api.opentripmap.com/0.1/en/places/xid/" + place.getXid() + "?apikey=" + SECOND_API_KEY))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            PlaceInfo newPlace = objectMapper.readValue(response.body(), PlaceInfo.class);
            newPlaces.add(newPlace);

            Thread.sleep(200);
        }
        System.out.println(newPlaces.size());
        getWeather(newPlaces);
    }

    private static void getWeather(List<PlaceInfo> places) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        for (PlaceInfo place : places) {
            Point placeCoords = place.getPoint();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=" + placeCoords.getLat() +"&lon=" + placeCoords.getLng() + "&appid=" + THIRD_API_KEY))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            Weather weather = objectMapper.readValue(response.body(), Weather.class);
            WeatherDescr[] weatherDescr = weather.getWeatherDescr();
            TempWrapper tempWrapper = weather.getTempWrapper();

            System.out.println("name: " + place.getName());

            Address placeAddress = place.getAddress();
            if (placeAddress != null) {
                System.out.println("city: " + placeAddress.getCity());
            }
            else {
                System.out.println("city: ");
            }
            Wikipedia_extracts placeWikiExtracts = place.getWikiExtracts();
            if (placeWikiExtracts != null) {
                System.out.println("descr: " + placeWikiExtracts.getText());
            }
            else {
                System.out.println("descr: ");
            }
            if (weatherDescr != null) {
                for(WeatherDescr someDescr : weatherDescr) {
                    System.out.println("weather: " + someDescr.getDescription());
                }
            }
            if (tempWrapper != null) {
                System.out.printf("temperature: %.2g", tempWrapper.getTemp() - 273.15);
            }
            System.out.println("\n\n\n");
        }
    }
}
