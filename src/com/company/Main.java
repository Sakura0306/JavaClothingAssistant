package com.company;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static String API_KEY;
    public static Location home;
    public static boolean isHome;
    public static Location work;
    public static boolean isWork;
    public static ArrayList<Location> locationList = new ArrayList<>();
    public static Helper helper = new Helper();

    public static void main(String[] args) {
        API_KEY = args[0];
        Helper.loadLocations();
        displayMenu("Welcome to Clothing assistant");
    }

    public static void displayMenu(String msg){
        hasToHaveMinLocations();
        System.out.println("\n\n");
        System.out.println(msg + "\n");
        System.out.println("1) Add Location");
        System.out.println("2) What should I wear now");
        System.out.println("3) Check the weather at chosen location");
        System.out.println("4) Save Locations");
        System.out.println("5) Exit");
        Scanner scan = new Scanner(System.in);
        int input = scan.nextInt();
        String result;
        switch(input){
            case 1:
                System.out.println("How you want to name your location");
                Scanner scanners = new Scanner(System.in);
                String nameLocation = scanners.nextLine();
                result = helper.addLocation(nameLocation);
                displayMenu(result);
                break;
            case 2:
                result = Forecast.whatWearNow(home);
                displayMenu(result);
                break;
            case 3:
                System.out.println("Provide number of location: ");
                for(int i=0; i<Main.locationList.size(); i++){
                    System.out.println(i+") "+ Main.locationList.get(i).name);
                }
                Scanner scanner = new Scanner(System.in);
                int locationNumber = scanner.nextInt();
                Location chosenLocation = Main.locationList.get(locationNumber);
                System.out.println("At what hour in 24h format: ");
                int hour = scanner.nextInt();
                result = Forecast.checkWeather(chosenLocation, hour);
                displayMenu(result);
                break;
            case 4:
                result = Helper.saveLocations();
                displayMenu(result);
                break;
            case 5:
                System.exit(0);
            default:
                displayMenu("Incorrect option");
        }
    }

    public static void hasToHaveMinLocations(){
        while(locationList.size() < 5){
            if(!isHome){
                System.out.println("First you need to add home location");
                helper.addLocation("home");
                isHome = true;
                continue;
            }
            if(!isWork){
                System.out.println("First you need to add work location");
                helper.addLocation("work");
                isWork = true;
                continue;
            }
            System.out.println("You have to have minimum 5 locations");
            System.out.println("Provide its name: ");
            Scanner scan = new Scanner(System.in);
            String locationName = scan.nextLine();
            helper.addLocation(locationName);
        }
    }
}
