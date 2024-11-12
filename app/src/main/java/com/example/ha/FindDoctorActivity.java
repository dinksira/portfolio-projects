package com.example.ha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FindDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CardView familyphysician = findViewById(R.id.familyphy);
        familyphysician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Family Physicians");
            }
        });

        CardView dietitian = findViewById(R.id.Dietician);
        dietitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Dietitian");
            }
        });
        CardView dentist = findViewById(R.id.Dentist);
        dietitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Dentist");
            }
        });
        CardView Surgeon = findViewById(R.id.Surgeon);
        dietitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Surgeon");
            }
        });
        CardView Cardiologist = findViewById(R.id.Cardiologist);
        dietitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Cardiologist");
            }
        });
        CardView neurologist = findViewById(R.id.Neurologist);
        dietitian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDoctorDetailActivity("Neurologist");
            }
        });

    }

    private void startDoctorDetailActivity(String title) {
        Intent intent = new Intent(FindDoctorActivity.this, DoctorDetailActivity.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }
}
