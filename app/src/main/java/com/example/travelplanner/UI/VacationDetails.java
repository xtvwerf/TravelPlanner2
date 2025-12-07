package com.example.travelplanner.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplanner.R;
import com.example.travelplanner.database.Repository;
import com.example.travelplanner.entities.Excursion;
import com.example.travelplanner.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    String name;
    String hotel;
    String startDate;
    String endDate;
    int vacationID;
    EditText editName;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    EditText editNote;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    DatePickerDialog.OnDateSetListener datePickerStartDate;
    DatePickerDialog.OnDateSetListener datePickerEndDate;
    final Calendar calendarStart = Calendar.getInstance();
    final Calendar calendarEnd = Calendar.getInstance();
    CheckBox notifyMeVacationCheckbox;
    String extraText = "";
    String extraTitle = "";
    String excursionInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);

        editName = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startDatePicker);
        editEndDate = findViewById(R.id.endDatePicker);
        vacationID = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");
        hotel = getIntent().getStringExtra("hotel");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        editName.setText(name);
        editHotel.setText(hotel);
        editNote = findViewById(R.id.note);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        notifyMeVacationCheckbox = findViewById(R.id.notifyMeVacation);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getAllExcursions()) {
            if (excursion.getVacationID() == vacationID) {
                filteredExcursions.add(excursion);
            }
        }
        excursionAdapter.setExcursions(filteredExcursions);

        // validation functionality
        datePickerStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, monthOfYear);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }
        };

        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editStartDate.getText().toString();
                if (info.isEmpty()) info = "11/01/25";
                try {
                    calendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, datePickerStartDate, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH), calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        datePickerEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, monthOfYear);
                calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelEnd();
            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editEndDate.getText().toString();
                if (info.equals("")) info = "11/01/25";
                try {
                    calendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, datePickerEndDate, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH), calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        extraText += "Vacation: " + name + "\n";
        extraText += "Hotel: " + hotel + "\n";
        extraText += "Start Date: " + startDate + "\n";
        extraText += "End Date: " + endDate + "\n";
        for (Excursion excursion : filteredExcursions) {
            excursionInfo += "Excursion: " + excursion.getExcursionName() + ", Date: " + excursion.getExcursionDate() + "\n";
        }
        extraText += excursionInfo;

    }

    private void updateLabelStart() {
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editStartDate.setText(sdf.format(calendarStart.getTime()));
    }

    private void updateLabelEnd() {
        String format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        editEndDate.setText(sdf.format(calendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.vacationsave) {
            Vacation vacation;
            String dateFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
            if (vacationID == -1) {
                if (repository.getAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getAllVacations().get(repository.getAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                try {
                    Date vacationStartDate = sdf.parse(vacation.getVacationStartDate());
                    Date vacationEndDate = sdf.parse(vacation.getVacationEndDate());
                    if (vacationEndDate.before(vacationStartDate)) {
                        Toast.makeText(VacationDetails.this, "End date cannot be before start date!", Toast.LENGTH_LONG).show();
                        this.finish();
                    } else {
                        if (notifyMeVacationCheckbox.isChecked()) {
                            try {
                                Long trigger = vacationStartDate.getTime();
                                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                                intent.putExtra("key", "Vacation " + vacation.getVacationName() + " has started!");
                                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Long trigger = vacationEndDate.getTime();
                                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                                intent.putExtra("key", "Vacation " + vacation.getVacationName() + " has ended :(");
                                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        repository.insert(vacation);
                        this.finish();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                vacation = new Vacation(vacationID, editName.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                try {
                    Date vacationStartDate = sdf.parse(vacation.getVacationStartDate());
                    Date vacationEndDate = sdf.parse(vacation.getVacationEndDate());
                    if (vacationEndDate.before(vacationStartDate)) {
                        Toast.makeText(VacationDetails.this, "End date cannot be before start date!", Toast.LENGTH_LONG).show();
                        return false;
                    } else {
                        if (notifyMeVacationCheckbox.isChecked()) {
                            try {
                                Long trigger = vacationStartDate.getTime();
                                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                                intent.putExtra("key", vacation.getVacationName() + " has started!");
                                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                Long trigger = vacationEndDate.getTime();
                                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                                intent.putExtra("key", vacation.getVacationName() + " has ended :(");
                                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        repository.update(vacation);
                        this.finish();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (item.getItemId() == R.id.vacationdelete) {
            for (Vacation vacation : repository.getAllVacations()) {
                if (vacation.getVacationID() == vacationID) currentVacation = vacation;
            }
            numExcursions = 0;
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getExcursionID() == vacationID) ++numExcursions;
            }
            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_SHORT).show();
                VacationDetails.this.finish();
            } else {
                Toast.makeText(VacationDetails.this, "Cannot delete vacation with excursions", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        if (item.getItemId() == R.id.shareVacationDetails) {
            extraTitle = "Vacation: " + name;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, extraText);
            sendIntent.putExtra(Intent.EXTRA_TITLE, extraTitle);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }
}
