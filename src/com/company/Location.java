package com.company;

import org.json.JSONObject;

public class Location {
    public String name;
    public double latitude;
    public double longitude;

    public Location(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        return name + " - (" + latitude + ", " + longitude + ")";
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("latitude", latitude);
        jsonObject.put("longitude", longitude);
        return jsonObject;
    }
}
