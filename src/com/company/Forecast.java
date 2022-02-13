package com.company;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Forecast {
    private static String API_URL = "https://api.weatherapi.com/v1/forecast.json?key=";

    public Location location;

    public Forecast(Location location){
        this.location = location;
    }

    public static String whatWearNow(Location location){
        try {
            JSONObject response = getWeatherAPIResponse(location);
            int raining = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
                    .getJSONObject("day").getInt("daily_will_it_rain");
            int snowing = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
                    .getJSONObject("day").getInt("daily_will_it_snow");
            double temp = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
                    .getJSONObject("day").getInt("avgtemp_c");

            boolean isRaining = raining == 1;
            boolean isSnowing = snowing == 1;
            boolean warm = temp > 18;
            return whatToWear(isRaining, isSnowing, warm);
        }catch(Exception e){
            return "Something went wrong try again later " + e;
        }
    }

    private static String whatToWear(boolean isRaining, boolean isSnowing, boolean warm) {

        if(isRaining && !warm){ // raining and cold
            return "Take into consideration to wear: jeans, jacket, umbrella, scarf";
        }
        if(isRaining){ // raining and warm
            return "Take into consideration to wear: umbrella, shorts, sweater, boots";
        }
        if(warm){ // warm and no rain
            return "Take into consideration to wear: cap, shoes, tank top, heels, sunglasses, blouse, slippers";
        }
        if(isSnowing){ // snowing
            return "Take into consideration to wear: coat, gloves, socks";
        }
        //it's cold
        return "Take into consideration to wear: jacket, scarf, gloves, dress, stockings";
    }

    public static String checkWeather(Location location, int hour){
        try {
            JSONObject response = getWeatherAPIResponse(location, hour);
            int isRaining = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
                    .getJSONObject("day").getInt("daily_will_it_rain");
            int isSnowing = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
                    .getJSONObject("day").getInt("daily_will_it_snow");
            double temp = response.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0)
                    .getJSONObject("day").getInt("avgtemp_c");
            String result = "Temperature is " + temp;
            if (isRaining == 1) {
                result += ". It will rain.";
            }
            if (isSnowing == 1) {
                result += ". It will Snow.";
            }
            return result;
        }catch(Exception e){
            return "Something went wrong try again later " + e;
        }
    }

    private static JSONObject getWeatherAPIResponse(Location location) throws Exception {
        String url = API_URL
                +Main.API_KEY
                +"&q="+location.latitude
                +","+location.longitude;
        return apiConnection(url);
    }

    private static JSONObject getWeatherAPIResponse(Location location, int hour) throws Exception {
        String url = API_URL
                +Main.API_KEY
                +"&q="+location.latitude
                +","+location.longitude
                +"&hour="+hour;
        return apiConnection(url);
    }

    private static JSONObject apiConnection(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        if (!(responseCode == 200)) {
            throw new Exception("Something went wrong try again. ");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        if (response.isEmpty()
                || response.toString().equals("{\"data\":[[]]}")
                || response.toString().equals("{}")) {
            throw new Exception("API responded with empty data. ");
        }
        return new JSONObject(response.toString());
    }
}
