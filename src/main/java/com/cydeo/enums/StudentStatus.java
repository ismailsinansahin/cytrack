package com.cydeo.enums;

public enum StudentStatus {

    ACTIVE(1,"Active"), DROPPED(2,"Dropped"), ALUMNI(3, "Alumni"), TRANSFERRED(4,"Transferred");

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
            case 1: return StudentStatus.ACTIVE;
            case 2: return StudentStatus.DROPPED;
            case 3: return StudentStatus.ALUMNI;
            case 4: return StudentStatus.TRANSFERRED;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
