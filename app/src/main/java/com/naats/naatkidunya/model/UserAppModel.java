package com.naats.naatkidunya.model;

public class UserAppModel {
    private String Name;
    private String MobileNumber;
    private String City;
    private String Country;
    private String regToken;
    private String message;

    public UserAppModel() {
    }

    public UserAppModel(String name, String mobileNumber, String message) {
        Name = name;
        MobileNumber = mobileNumber;
        this.message = message;
    }

    public UserAppModel(String name, String mobileNumber, String city, String country, String regTokens) {
        Name = name;
        MobileNumber = mobileNumber;
        City = city;
        Country = country;
        regToken = regTokens;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getRegToken() {
        return regToken;
    }

    public void setRegToken(String regToken) {
        this.regToken = regToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
