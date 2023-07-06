package com.example.notesharingminiprojectapp;

public class Subjects {
    private int imageProfile;
    private String subjectName,subjectCode;

    public Subjects(int imageProfile, String subjectName, String subjectCode) {
        this.imageProfile = imageProfile;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    public int getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(int imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
