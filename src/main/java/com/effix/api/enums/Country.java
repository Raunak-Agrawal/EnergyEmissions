package com.effix.api.enums;

public enum Country {
    UK("UK"), NETHERLANDS("NETHERLANDS");
    private String country;

    Country(String country) {
        this.country=country;
    }
    public String getCountry(){
        return this.country;
    }
}
