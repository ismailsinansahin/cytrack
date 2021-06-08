package com.cydeo.enums;

public enum BatchStatus {

    PLANNED("Planned"), INPROGRESS("In Progress"), COMPLETED("Completed");

    private String value;

    BatchStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
