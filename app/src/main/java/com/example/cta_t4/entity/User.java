package com.example.cta_t4.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class User implements Parcelable{
    private Integer userId;
    private String name;
    private String surname;
    private String email;
    private Date dob;
    private Double height;
    private Double weight;
    private String gender;
    private String address;
    private Integer postcode;
    private Integer levelOfActivity;
    private Integer stepsPerMile;

    public User(Parcel in){
        userId = in.readInt();
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        dob = (Date) in.readSerializable();
        height = in.readDouble();
        weight = in.readDouble();
        gender = in.readString();
        address = in.readString();
        postcode = in.readInt();
        levelOfActivity = in.readInt();
        stepsPerMile = in.readInt();

    }

    public User(Integer userId, String name, String surname, String email, Date dob, Double height, Double weight, String gender, String address, Integer postcode, Integer levelOfActivity, Integer stepsPerMile) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepsPerMile = stepsPerMile;
    }

    public User(String name, String surname, String email, Date dob, Double height, Double weight, String gender, String address, Integer postcode, Integer levelOfActivity, Integer stepsPerMile) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.levelOfActivity = levelOfActivity;
        this.stepsPerMile = stepsPerMile;
    }

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(userId);
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(email);
        parcel.writeSerializable(dob);
        parcel.writeDouble(height);
        parcel.writeDouble(weight);
        parcel.writeString(gender);
        parcel.writeString(address);
        parcel.writeInt(postcode);
        parcel.writeInt(levelOfActivity);
        parcel.writeInt(stepsPerMile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public Integer getLevelOfActivity() {
        return levelOfActivity;
    }

    public void setLevelOfActivity(Integer levelOfActivity) {
        this.levelOfActivity = levelOfActivity;
    }

    public Integer getStepsPerMile() {
        return stepsPerMile;
    }

    public void setStepsPerMile(Integer stepsPerMile) {
        this.stepsPerMile = stepsPerMile;
    }

}
