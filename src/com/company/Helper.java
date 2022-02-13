package com.company;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Helper {

    private static String path = System.getenv("APPDATA") + "\\save.json";

    public String addLocation(String nameLocation) {
        System.out.println("Provide location latitude");
        Scanner scan = new Scanner(System.in);
        double latitude = scan.nextDouble();
        System.out.println("Provide location longitude");
        double longitude = scan.nextDouble();
        Location location = new Location(nameLocation, latitude, longitude);
        Main.locationList.add(location);
        if(nameLocation.equals("home")){
            Main.home = location;
        }
        if(nameLocation.equals("work")){
            Main.work = location;
        }
        return nameLocation + " added successfully.";
    }

    public static String saveLocations(){
        try{
            File file = new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(locationsToJson());
            fileWriter.close();
            return "Locations have been saved to file.";
        }catch(Exception e){
            return "Couldn't save locations cause: " + e;
        }
    }

    private static String locationsToJson() {
        JSONArray jsonArray = new JSONArray();
        for(Location location: Main.locationList){
            jsonArray.put(location.toJson());
        }
        return jsonArray.toString();
    }

    public static void loadLocations(){
        try{
            File file = new File(path);
            String content = FileUtils.readFileToString(file, "utf-8");
            if(!file.exists()){
                return;
            }
            if(content==null){
                return;
            }
            JSONArray array = new JSONArray(content);
            for(int i=0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                Location location = new Location(
                        obj.getString("name"),
                        obj.getDouble("latitude"),
                        obj.getDouble("longitude"));
                Main.locationList.add(location);
                if(location.name.equals("home")){
                    Main.home = location;
                    Main.isHome = true;
                    continue;
                }
                if(location.name.equals("work")){
                    Main.work = location;
                    Main.isWork = true;
                }
            }
        }catch(Exception ignored){}
    }

}
