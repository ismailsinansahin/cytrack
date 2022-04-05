package com.cydeo.enums;

public enum BatchStatus {

    NOBATCH(1,"No-Batch"), PLANNED(2, "Planned"), INPROGRESS(3, "In Progress"), COMPLETED(4, "Completed");

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
            case 1: return BatchStatus.NOBATCH;
            case 2: return BatchStatus.PLANNED;
            case 3: return BatchStatus.INPROGRESS;
            case 4: return BatchStatus.COMPLETED;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
