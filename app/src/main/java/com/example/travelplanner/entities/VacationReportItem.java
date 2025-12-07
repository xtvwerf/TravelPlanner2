package com.example.travelplanner.entities;

public class VacationReportItem extends ReportItem {

    private final int vacationId;

    public VacationReportItem(Vacation vacation) {
        super(
                vacation.getVacationName(),
                "Hotel: " + vacation.getVacationHotelName(),
                "From " + vacation.getVacationStartDate() + " to " + vacation.getVacationEndDate()
        );
        this.vacationId = vacation.getVacationID();
    }

    @Override
    public String getItemType() {
        return "Vacation";
    }

    public int getVacationId() {
        return vacationId;
    }
}
