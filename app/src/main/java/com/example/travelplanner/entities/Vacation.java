package com.example.travelplanner.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vacations")
public class Vacation {
    @PrimaryKey(autoGenerate = true)
    private int vacationID;
    private String vacationName;
    private String vacationHotelName;
    private String vacationStartDate;
    private String vacationEndDate;

    // B2
    public Vacation(int vacationID, String vacationName, String vacationHotelName, String vacationStartDate, String vacationEndDate) {
        this.vacationID = vacationID;
        this.vacationName = vacationName;
        this.vacationHotelName = vacationHotelName;
        this.vacationStartDate = vacationStartDate;
        this.vacationEndDate = vacationEndDate;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getVacationName() {
        return vacationName;
    }

    public void setVacationName(String vacationName) {
        this.vacationName = vacationName;
    }

    public String getVacationHotelName() {
        return vacationHotelName;
    }

    public void setVacationHotelName(String vacationHotelName) {
        this.vacationHotelName = vacationHotelName;
    }

    public String getVacationStartDate() {
        return vacationStartDate;
    }

    public void setVacationStartDate(String vacationStartDate) {
        this.vacationStartDate = vacationStartDate;
    }

    public String getVacationEndDate() {
        return vacationEndDate;
    }

    public void setVacationEndDate(String vacationEndDate) {
        this.vacationEndDate = vacationEndDate;
    }

    public String toString() {
        return vacationName;
    }

}
