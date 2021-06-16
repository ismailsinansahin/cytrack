package com.cydeo.enums;

public enum Country {

    USA("USA"), UK("UK"), GERMANY("Germany"), SPAIN("Spain"), TURKEY("Turkey"),
    FRANCE("France"), NETHERLAND("Netherland"), ITALY("Italy"), GREECE("Greece");

    private String value;

    Country(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
