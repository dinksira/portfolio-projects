package com.example.ha;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RegisterActivity extends AppCompatActivity {
    Spinner spinner;
    Spinner spin2;
    String [ ] sex={"male", "female"};
    String [ ] age={"12", "13","14", "15","16", "17","18", "19","20", "21","22", "23","24", "25","26", "27","28", "29","30", "31","32", "33","34", "35","36", "36"};
    EditText frname, laname, email, pass, passconf;
    Button btn;
    TextView tv;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        frname = findViewById(R.id.editTextUsername2);
        laname = findViewById(R.id.editTextUsername3);
        email = findViewById(R.id.editTextTextEmailAddress);
        pass = findViewById(R.id.editTextPassword4);
        passconf = findViewById(R.id.editTextPassword2);
        btn = findViewById(R.id.buttonReg);
        tv = findViewById(R.id.textViewback);
        spinner=  findViewById(R.id.spinner);
        spin2=  findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, sex);
        adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value=parent.getItemAtPosition(position).toString();
                Toast.makeText(RegisterActivity.this, value, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected (AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> adapterAge = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_spinner_item, age);
        adapterAge.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(adapterAge);
        spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value=parent.getItemAtPosition(position).toString();
                Toast.makeText(RegisterActivity.this, value, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected (AdapterView<?> parent) {
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode=FirebaseDatabase.getInstance();
                reference=rootNode.getReference("users");
                String first = frname.getText().toString();
                String last = laname.getText().toString();
                String maill = email.getText().toString();
                String passs = pass.getText().toString();
                String confirm = passconf.getText().toString();
                String ageValue = spin2.getSelectedItem().toString();
                String sexValue = spinner.getSelectedItem().toString();
                Database db = new Database(getApplicationContext());
                if (first.length() == 0 || last.length() == 0 || maill.length() == 0 || passs.length() == 0 || confirm.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Fill all details", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (passs.compareTo(confirm) == 0) {
                        if(isValid(passs))
                        {
                            db.register(first, last, maill,ageValue, sexValue, passs);
                            Toast.makeText(getApplicationContext(), "Record Inserted", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Password must contain at least 8 characters", Toast.LENGTH_SHORT).show();
                        }}
                    else {
                        Toast.makeText(getApplicationContext(), "Password didn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
    public static boolean isValid(String passwordhere) {
        int f1 = 0, f2 = 0, f3 = 0;
        if (passwordhere.length() < 8) {
            return false;
        } else {
            for (int p = 0; p < passwordhere.length(); p++) {
                if (Character.isLetter(passwordhere.charAt(p))) {
                    f1 = 1;
                    break; // No need to continue the loop once a letter is found
                }
            }
            for (int r = 0; r < passwordhere.length(); r++) {
                if (Character.isDigit(passwordhere.charAt(r))) {
                    f2 = 1;
                    break; // No need to continue the loop once a digit is found
                }
            }
            for (int s = 0; s < passwordhere.length(); s++) {
                char c = passwordhere.charAt(s);
                if ((c >= 33 && c <= 47) || (c >= 58 && c <= 64) || (c >= 91 && c <= 96) || (c >= 123 && c <= 126)) {
                    f3 = 1;
                    break; // No need to continue the loop once a special character is found
                }
            }
            return (f1 == 1 && f2 == 1 && f3 == 1);
        }
    }
    private void syncDataWithFirebase() {
        if (isNetworkAvailable()) {
            Database localDatabase = new Database(this);
            localDatabase.syncWithFirebase();
            Toast.makeText(this, "Data synchronized with Firebase", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}