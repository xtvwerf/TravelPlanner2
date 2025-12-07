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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travelplanner.R;
import com.example.travelplanner.database.Repository;
import com.example.travelplanner.entities.Excursion;
import com.example.travelplanner.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    String name;
    String date;
    int excursionID;
    int vacationID;
    EditText editName;
    TextView editDate;
    EditText editNote;
    Repository repository;
    DatePickerDialog.OnDateSetListener datePickerDate;
    final Calendar calendarDate = Calendar.getInstance();
    CheckBox notifyMeExcursionCheckbox;
    Spinner spinner;
    Vacation associatedVacation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(getApplication());
        name = getIntent().getStringExtra("name");
        editName = findViewById(R.id.excursionName);
        editName.setText(name);
        date = getIntent().getStringExtra("date");
        editDate = findViewById(R.id.excursionDate);
        editDate.setText(date);
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        editNote = findViewById(R.id.note);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        notifyMeExcursionCheckbox = findViewById(R.id.notifyMeExcursion);

        ArrayList<Vacation> vacationArrayList = new ArrayList<>();
        vacationArrayList.addAll(repository.getAllVacations());
        ArrayList<Integer> vacationIdList = new ArrayList<>();
        for (Vacation vacation : vacationArrayList) {
            vacationIdList.add(vacation.getVacationID());
        }

        for (Vacation vacation : repository.getAllVacations()) {
            if (vacation.getVacationID() == vacationID) associatedVacation = vacation;
        }

        // design elements that make the application scalable
        ArrayAdapter<Vacation> vacationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacationArrayList);
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(vacationAdapter);
        spinner.setSelection(vacationIdList.indexOf(vacationID));

        // industry-appropriate security features
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editDate.getText().toString();
                if (info.equals("")) info = "11/01/25";
                try {
                    calendarDate.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, datePickerDate, calendarDate.get(Calendar.YEAR), calendarDate.get(Calendar.MONTH), calendarDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        datePickerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarDate.set(Calendar.YEAR, year);
                calendarDate.set(Calendar.MONTH, monthOfYear);
                calendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }
        };


    }
    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDate.setText(sdf.format(calendarDate.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.excursionsave) {
            int vID = -1;
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            Excursion excursion;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            for (Vacation vacation : repository.getAllVacations()) {
                if (vacation.getVacationName().equals(spinner.getSelectedItem().toString())) vID = vacation.getVacationID();
            }
            try {
                Date vacationStartDate = sdf.parse(associatedVacation.getVacationStartDate());
                Date vacationEndDate = sdf.parse(associatedVacation.getVacationEndDate());
                if (myDate.before(vacationStartDate) || myDate.after(vacationEndDate)) {
                    Toast.makeText(ExcursionDetails.this, "Excursion date must be within Vacation start and end dates!", Toast.LENGTH_LONG).show();
                    this.finish();
                } else {
                    if (excursionID == -1) {
                        if (repository.getAllExcursions().isEmpty()) {
                            excursionID = 1;
                        } else {
                            excursionID = repository.getAllExcursions().get(repository.getAllExcursions().size() - 1).getExcursionID() + 1;
                        }
                        if (notifyMeExcursionCheckbox.isChecked()) {
                            try {
                                Long trigger = myDate.getTime();
                                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                                intent.putExtra("key", "Excursion " + name + " has started!");
                                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vID);
                        repository.insert(excursion);
                    } else {
                        if (notifyMeExcursionCheckbox.isChecked()) {
                            try {
                                Long trigger = myDate.getTime();
                                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                                intent.putExtra("key", "Excursion " + name + " has started!");
                                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vID);
                        repository.update(excursion);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            finish();
            return true;
        }

        if (item.getItemId() == R.id.excursiondelete) {
            for (Excursion excursion : repository.getAllExcursions()) {
                if (excursion.getExcursionID() == excursionID) {
                    repository.delete(excursion);
                    Toast.makeText(ExcursionDetails.this, excursion.getExcursionName() + " was deleted", Toast.LENGTH_SHORT).show();
                    ExcursionDetails.this.finish();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
