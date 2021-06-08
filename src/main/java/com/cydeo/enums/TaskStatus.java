package com.cydeo.enums;

public enum TaskStatus {

    PLANNED("Planned"), INPROGRESS("In Progress"), COMPLETED("Completed");

    private String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}

