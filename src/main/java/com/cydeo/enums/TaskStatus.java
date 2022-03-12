package com.cydeo.enums;

public enum TaskStatus {

    PLANNED(1,"Planned"), PUBLISHED(2,"Published"), OUT_OF_TIME(3,"Out Of Time");

    private int id;
    private String value;

    TaskStatus(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public int getId() {
        return id;
    }

    public static TaskStatus getWithId(int id){
        switch (id){
            case 1: return TaskStatus.PLANNED;
            case 2: return TaskStatus.PUBLISHED;
            case 3: return TaskStatus.OUT_OF_TIME;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
