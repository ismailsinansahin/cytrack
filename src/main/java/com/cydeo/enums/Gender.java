package com.cydeo.enums;

public enum Gender {

    FEMALE("Female"), MALE("Male");

    private String value;

    Gender(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
