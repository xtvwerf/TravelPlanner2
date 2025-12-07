package com.example.travelplanner.entities;

public class ExcursionReportItem extends ReportItem {

    private final int excursionId;
    private final int vacationId;

    public ExcursionReportItem(Excursion excursion, String parentVacationName) {
        super(
                "Excursion: " + excursion.getExcursionName(),
                "Date: " + excursion.getExcursionDate(),
                "Vacation: " + parentVacationName
        );
        this.excursionId = excursion.getExcursionID();
        this.vacationId = excursion.getVacationID();
    }

    @Override
    public String getItemType() {
        return "Excursion";
    }

    public int getExcursionId() {
        return excursionId;
    }

    public int getVacationId() {
        return vacationId;
    }
}
