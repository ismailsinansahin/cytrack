package com.cydeo.enums;

public enum Status {

    RETURNING("Returning"), NEW("New");

    private String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
