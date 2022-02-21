package com.cydeo.enums;

public enum StudentStatus {

    RETURNING("Returning"), NEW("New");

    private String value;

    StudentStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}