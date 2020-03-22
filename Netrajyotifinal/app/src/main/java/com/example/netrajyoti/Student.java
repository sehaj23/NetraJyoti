package com.example.netrajyoti;

public class Student {
    public String ID;
    public String USERNAME;



    public Student(String ID,String USERNAME){
        this.ID = ID;
        this.USERNAME = USERNAME;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public String getID() {
        return ID;
    }
}
