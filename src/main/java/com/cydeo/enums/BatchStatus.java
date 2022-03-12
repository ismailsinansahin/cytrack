package com.cydeo.enums;

public enum BatchStatus {

    PLANNED(1, "Planned"), INPROGRESS(2, "In Progress"), COMPLETED(3, "Completed");

    private int id;
    private String value;

    BatchStatus(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public int getId(){ return id;}

    public static BatchStatus getWithId(int id){
        switch (id){
            case 1: return BatchStatus.PLANNED;
            case 2: return BatchStatus.INPROGRESS;
            case 3: return BatchStatus.COMPLETED;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
