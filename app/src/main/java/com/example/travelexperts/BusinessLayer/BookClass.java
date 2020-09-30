package com.example.travelexperts.BusinessLayer;

public class BookClass {
    private String ClassId;
    private String ClassName;
    private String ClassDes;

    public BookClass() {
    }

    public BookClass(String classId, String className, String classDes) {
        ClassId = classId;
        ClassName = className;
        ClassDes = classDes;
    }

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

    @Override
    public String toString() {
        return ClassName;
    }
}
