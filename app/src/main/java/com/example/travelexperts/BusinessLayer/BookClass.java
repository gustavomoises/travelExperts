//Author: Gustavo Lourenco Moises
//Thread Project - Group 1
//OOSD Program Spring 2020
//Date:9/30/2020
//Travel Agency Application
package com.example.travelexperts.BusinessLayer;

import java.io.Serializable;

public class BookClass implements Serializable {
    private String ClassId;
    private String ClassName;
    private String ClassDes;

    //Constructor no arguments
    public BookClass() {
    }

    //Constructor 3 arguments
    public BookClass(String classId, String className, String classDes) {
        ClassId = classId;
        ClassName = className;
        ClassDes = classDes;
    }

    //Getters and Setters
    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassDes() {
        return ClassDes;
    }

    public void setClassDes(String classDes) {
        ClassDes = classDes;
    }

    //toString Method
    @Override
    public String toString() {
        return ClassName;
    }
}
