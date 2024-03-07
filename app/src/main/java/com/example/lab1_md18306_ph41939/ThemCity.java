package com.example.lab1_md18306_ph41939;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThemCity extends AppCompatActivity {
    TextInputEditText edtCityName, edtState, edtCountry, edtPopulation, edtRegions;
    TextInputLayout tilCityName, tilState, tilCountry, tilPopulation, tilRegions;
    CheckBox chkCapital;
    Button btnCancel, btnAddCity;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_city);
        edtCityName = findViewById(R.id.edtCityName);
        edtState = findViewById(R.id.edtState);
        edtCountry = findViewById(R.id.edtCountry);
        edtPopulation = findViewById(R.id.edtPopulation);
        edtRegions = findViewById(R.id.edtRegions);
        tilCityName = findViewById(R.id.tilCityName);
        tilState = findViewById(R.id.tilState);
        tilCountry = findViewById(R.id.tilCountry);
        tilPopulation = findViewById(R.id.tilPopulation);
        tilRegions = findViewById(R.id.tilRegions);
        chkCapital = findViewById(R.id.chkCapital);
        btnCancel = findViewById(R.id.btnCancle);
        btnAddCity = findViewById(R.id.btnAddCity);
        db = FirebaseFirestore.getInstance();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ThemCity.this, MainActivity.class));
                finish();
            }
        });
        btnAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
    }
    private void validateForm() {
        String cityName = edtCityName.getText().toString().trim();
        String state = edtState.getText().toString().trim();
        String country = edtCountry.getText().toString().trim();
        String populationStr = edtPopulation.getText().toString().trim();
        String regionsStr = edtRegions.getText().toString().trim();

        boolean check = true;

        if (TextUtils.isEmpty(cityName)) {
            tilCityName.setError("Please enter city name");
            check = false;
        } else {
            tilCityName.setError(null);
        }

        if (TextUtils.isEmpty(state)) {
            tilState.setError("Please enter state");
            check = false;
        } else {
            tilState.setError(null);
        }

        if (TextUtils.isEmpty(country)) {
            tilCountry.setError("Please enter country");
            check = false;
        } else {
            tilCountry.setError(null);
        }

        if (TextUtils.isEmpty(populationStr)) {
            tilPopulation.setError("Please enter population");
            check = false;
        } else {
            try {
                int population = Integer.parseInt(populationStr);
                if (population <= 0) {
                    tilPopulation.setError("Population must be a positive number");
                    check = false;
                } else {
                    tilPopulation.setError(null);
                }
            } catch (NumberFormatException e) {
                tilPopulation.setError("Invalid population format");
                check = false;
            }
        }

        if (TextUtils.isEmpty(regionsStr)) {
            tilRegions.setError("Please enter regions");
            check = false;
        } else {
            tilRegions.setError(null);
        }

        if (check) {
            String ma = randomMa();
            CollectionReference cities = db.collection("cities");

            Map<String, Object> data = new HashMap<>();
            data.put("name", cityName);
            data.put("state", state);
            data.put("country", country);
            data.put("capital", chkCapital.isChecked());
            data.put("population", Integer.parseInt(populationStr));
            data.put("regions", Arrays.asList(regionsStr.split(",")));
            cities.document(ma).set(data);
            Toast.makeText(ThemCity.this, "Add New City Succes", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ThemCity.this, MainActivity.class));
            finish();
        }
    }

    public static String randomMa() {
        Random random = new Random();
        char tu1 = (char) (random.nextInt(26) + 'A');
        char tu2 = (char) (random.nextInt(26) + 'A');
        return String.valueOf(tu1) + String.valueOf(tu2);
    }

}