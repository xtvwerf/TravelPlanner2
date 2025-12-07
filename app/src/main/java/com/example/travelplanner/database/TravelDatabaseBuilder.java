package com.example.travelplanner.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.travelplanner.dao.ExcursionDAO;
import com.example.travelplanner.dao.VacationDAO;
import com.example.travelplanner.entities.Excursion;
import com.example.travelplanner.entities.Vacation;

@Database(entities = {Vacation.class, Excursion.class}, version = 12, exportSchema = false)
public abstract class TravelDatabaseBuilder extends RoomDatabase {
    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();
    private static volatile TravelDatabaseBuilder INSTANCE;

    static TravelDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TravelDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TravelDatabaseBuilder.class, "MyTravelDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
