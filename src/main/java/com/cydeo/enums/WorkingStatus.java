package com.cydeo.enums;

public enum WorkingStatus {

    FULLTIME("Full Time"), PARTTIME("Part Time"), NOTWORKING("Not Working");

    private String value;

    WorkingStatus(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
