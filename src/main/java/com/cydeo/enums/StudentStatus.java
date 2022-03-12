package com.cydeo.enums;

public enum StudentStatus {

    RETURNING(1,"Returning"), NEW(2,"New"), DROPPED(3,"Dropped"), TRANSFERRED(4,"Transferred");

    private int id;
    private String value;

    StudentStatus(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public int getId() {
        return id;
    }

    public static StudentStatus getWithId(int id){
        switch (id){
            case 1: return StudentStatus.RETURNING;
            case 2: return StudentStatus.NEW;
            case 3: return StudentStatus.DROPPED;
            case 4: return StudentStatus.TRANSFERRED;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
