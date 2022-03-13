package com.cydeo.enums;

public enum Gender {

    FEMALE(0,"Female"), MALE(1,"Male");

    private int id;
    private String value;

    Gender(int id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public int getId() { return id; }

    public static Gender getWithId(int id){
        switch (id){
            case 0: return Gender.MALE;
            case 1: return Gender.FEMALE;
            default: throw new IllegalArgumentException("Id[" + id +"] not supported.");
        }
    }

}
