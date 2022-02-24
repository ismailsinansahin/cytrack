package com.cydeo.enums;

public enum TaskStatus {

    PLANNED("Planned"), PUBLISHED("Published"), OUT_OF_TIME("Out Of Time");

    private String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
