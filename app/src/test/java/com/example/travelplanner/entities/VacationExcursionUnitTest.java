package com.example.travelplanner.entities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VacationExcursionUnitTest {

    @Test
    public void vacationWithValidData_isStoredCorrectly() {
        int expectedId = 1;
        String expectedName = "Beach Trip";
        String expectedHotel = "Seaside Hotel";
        String expectedStart = "05/01/25";
        String expectedEnd = "05/07/25";

        Vacation vacation = new Vacation(
                expectedId,
                expectedName,
                expectedHotel,
                expectedStart,
                expectedEnd
        );

        assertEquals(expectedId, vacation.getVacationID());
        assertEquals(expectedName, vacation.getVacationName());
        assertEquals(expectedHotel, vacation.getVacationHotelName());
        assertEquals(expectedStart, vacation.getVacationStartDate());
        assertEquals(expectedEnd, vacation.getVacationEndDate());

        assertEquals(expectedName, vacation.toString());
    }

    @Test
    public void excursionWithValidData_linksToCorrectVacation() {
        int vacationId = 2;
        Vacation vacation = new Vacation(
                vacationId,
                "Mountain Retreat",
                "Lodge on the Hill",
                "06/10/25",
                "06/15/25"
        );

        int expectedExcursionId = 100;
        String expectedExcursionName = "Guided Hike";
        String expectedExcursionDate = "06/11/25";

        Excursion excursion = new Excursion(
                expectedExcursionId,
                expectedExcursionName,
                expectedExcursionDate,
                vacation.getVacationID()
        );

        assertEquals(expectedExcursionId, excursion.getExcursionID());
        assertEquals(expectedExcursionName, excursion.getExcursionName());
        assertEquals(expectedExcursionDate, excursion.getExcursionDate());
        assertEquals(vacationId, excursion.getVacationID());
    }
}
