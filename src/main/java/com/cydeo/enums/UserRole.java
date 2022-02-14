package com.cydeo.enums;

public enum UserRole {

    ADMIN("Admin"), INSTRUCTOR("Instructor"), CYDEO_MENTOR("Cydeo Mentor"),
    ALUMNI_MENTOR("Alumni Mentor"), STUDENT("Student");

    private String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
