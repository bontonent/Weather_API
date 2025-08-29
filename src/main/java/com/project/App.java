package com.project;

import okhttp3.*;
import com.google.gson.*;

import java.awt.*;
import java.io.IOException;

//import java.util.LinkedList;
//import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class App {
    public static void main(String[] args) {
//        url London "http://api.weatherapi.com/v1/forecast.json?key=d5f374864b0249659a6124128252608&q=London&days=2&aqi=yes&alerts=yes"
//        List<String> city = new LinkedList<>();
//        city.add("chisinau");
//        city.add("madrid");
//        city.add("kyiv");
//        city.add("amsterdam");

        String url = "http://api.weatherapi.com/v1/forecast.json?key=d5f374864b0249659a6124128252608&q=London&days=2&aqi=no&alerts=no";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();

        try (Response response = client.newCall(request).execute()) {
            String body = response.body() != null ? response.body().string() : "{}";
//            JsonElement json = JsonParser.parseString(body);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);
            String to_day = root.path("location").path("localtime").asText().substring(0, 10);
            //work get +1 to don't break down
            // need create cycle
            JsonNode to_forecastday = root.path("forecast").path("forecastday");
//            for(int ion = 1; "True" == "True"; ion++) {
// only tomorrow
            int ion = 1;

            System.out.println(to_forecastday.get(ion).path("date").asText());
            String maxterm_c = to_forecastday.get(ion).path("day").path("maxtemp_c").asText();
            String minterm_c = to_forecastday.get(ion).path("day").path("mintemp_c").asText();
            System.out.println("Max = "+maxterm_c+" Min = "+minterm_c);

            JsonNode to_hour = to_forecastday.get(ion).path("hour");
            for (int i = 0; "True" == "True"; i++) {
                if (i == 0) {
                    System.out.println("i  time" + "  " + "humidity" + " " + "wid_kph" + " " + "wid_dir");
                }
                try {

                    String time = to_hour.get(i).path("time").asText().substring(10);
                    String humidity = to_hour.get(i).path("humidity").asText(); //columns
                    String wid_kph = to_hour.get(i).path("wind_kph").asText(); //columns
                    String wid_dir = to_hour.get(i).path("wind_dir").asText(); //columns
                    System.out.println((i+1)+" "+time + "    " + humidity + "      " + wid_kph + "    " + wid_dir);
                } catch (Exception e) {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
