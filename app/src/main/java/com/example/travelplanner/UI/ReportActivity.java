package com.example.travelplanner.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.example.travelplanner.entities.ExcursionReportItem;
import com.example.travelplanner.entities.ReportItem;
import com.example.travelplanner.entities.Vacation;
import com.example.travelplanner.entities.VacationReportItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReportActivity extends AppCompatActivity {

    private Repository repository;
    private ReportAdapter reportAdapter;
    private EditText searchEditText;
    private TextView generatedAtTextView;

    private final List<ReportItem> allReportItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.report_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        searchEditText = findViewById(R.id.editTextSearchVacation);
        generatedAtTextView = findViewById(R.id.textViewGeneratedAt);
        Button searchButton = findViewById(R.id.buttonSearchVacation);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewReport);
        reportAdapter = new ReportAdapter(this);
        recyclerView.setAdapter(reportAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = new Repository(getApplication());

        buildReportItems();
        reportAdapter.setItems(allReportItems);

        updateGeneratedAtTime();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchEditText.getText().toString();
                reportAdapter.filterByVacationName(query);
                updateGeneratedAtTime();
            }
        });
    }

    private void updateGeneratedAtTime() {
        String format = "MM/dd/yy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        String generated = sdf.format(new Date());
        generatedAtTextView.setText("Report generated: " + generated);
    }

    private void buildReportItems() {
        allReportItems.clear();

        List<Vacation> vacations = repository.getAllVacations();
        List<Excursion> excursions = repository.getAllExcursions();

        Map<Integer, Vacation> vacationById = new HashMap<>();
        for (Vacation vacation : vacations) {
            vacationById.put(vacation.getVacationID(), vacation);

            allReportItems.add(new VacationReportItem(vacation));
        }

        for (Excursion excursion : excursions) {
            Vacation parent = vacationById.get(excursion.getVacationID());
            String parentName = parent != null ? parent.getVacationName() : "";
            allReportItems.add(new ExcursionReportItem(excursion, parentName));
        }
    }


    private boolean isCurrentVacation(Vacation vacation, Date today, SimpleDateFormat sdf) {
        try {
            Date start = sdf.parse(vacation.getVacationStartDate());
            Date end = sdf.parse(vacation.getVacationEndDate());
            if (start == null || end == null) {
                return false;
            }
            return !today.before(start) && !today.after(end);
        } catch (ParseException e) {
            return false;
        }
    }
}
