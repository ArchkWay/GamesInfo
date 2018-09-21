package com.example.archek.gamesinfo.network;

public class GbObjectResponse {

    private String name;
    private String deck;
    private Image image;
    private String guid;
    private String description;
    private String location_country;
    private String location_city;


    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDeck() {
        return deck;
    }

    public String getGuid() {
        return guid;
    }
    public String getDescription() {
        return description;
    }
    public String getCountry() {
        return location_country;
    }
    public String getCity() {
        return location_city;
    }


    public static class Image{
        private String smallUrl;

        public String getSmallUrl() {
            return smallUrl;
        }
    }

}
