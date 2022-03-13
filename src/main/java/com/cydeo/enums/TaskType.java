package com.cydeo.enums;

public enum TaskType {

    RECORDING(1,"Recording"), QUIZ(2,"Quiz"), FLIPGRID(3,"Flipgrid"), REPLIT(4,"Replit"),
    ASSIGNMENT(5,"Assignment"), WEEKLY_MENTOR_MEETING(6,"Weekly Mentor Meeting"),
    ALUMNI_MENTOR_MEETING(7,"Alumni Mentor Meeting"), ASSESSMENT(8,"Assessment");

    private int id;
    private String value;

    TaskType(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public int getId() {
        return id;
    }

    public static TaskType getWithId(int id){
        switch (id){
            case 1: return TaskType.RECORDING;
            case 2: return TaskType.QUIZ;
            case 3: return TaskType.FLIPGRID;
            case 4: return TaskType.REPLIT;
            case 5: return TaskType.ASSIGNMENT;
            case 6: return TaskType.WEEKLY_MENTOR_MEETING;
            case 7: return TaskType.ALUMNI_MENTOR_MEETING;
            case 8: return TaskType.ASSESSMENT;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
