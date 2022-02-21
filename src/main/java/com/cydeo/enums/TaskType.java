package com.cydeo.enums;

public enum TaskType {

    RECORDING("Recording"), QUIZ("Quiz"), FLIPGRID("Flipgrid"), REPLIT("Replit"),
    ASSIGNMENT("Assignment"),WEEKLY_MENTOR_MEETING("Weekly Mentor Meeting"),
    ALUMNI_MENTOR_MEETING("Alumni_Mentor_Meeting");

    private String value;

    TaskType(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}