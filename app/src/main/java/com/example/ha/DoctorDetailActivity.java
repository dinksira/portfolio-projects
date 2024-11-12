package com.example.ha;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

import java.util.ArrayList;
import java.util.HashMap;


public class DoctorDetailActivity extends AppCompatActivity {
    private String[][] doctor_details1={
        {"Doctor name: Dinksira", "Hospital Address: Hawassa", "Exp:5yrs", "Mobile No:0912816","600"},
        {"Doctor name: Elsa", "Hospital Address: Adama", "Exp:4yrs", "Mobile No:0912478","600"}
    };
    private String[][] doctor_details2={
            {"Doctor name: Dinksira", "Hospital Address: Hawassa", "Exp:5yrs", "Mobile No:0912816","600"},
            {"Doctor name: Elsa", "Hospital Address: Adama", "Exp:4yrs", "Mobile No:0912478","600"}
    };
    private String[][] doctor_details3={
            {"Doctor name: Dinksira", "Hospital Address: Hawassa", "Exp:5yrs", "Mobile No:0912816","600"},
            {"Doctor name: Elsa", "Hospital Address: Adama", "Exp:4yrs", "Mobile No:0912478","600"}
    };
    private String[][] doctor_details4={
            {"Doctor name: Dinksira", "Hospital Address: Hawassa", "Exp:5yrs", "Mobile No:0912816","600"},
            {"Doctor name: Elsa", "Hospital Address: Adama", "Exp:4yrs", "Mobile No:0912478","600"}
    };
    private String[][] doctor_details5={
            {"Doctor name: Dinksira", "Hospital Address: Hawassa", "Exp:5yrs", "Mobile No:0912816","600"},
            {"Doctor name: Elsa", "Hospital Address: Adama", "Exp:4yrs", "Mobile No:0912478","600"}
    };
    private String[][] doctor_details6={
            {"Doctor name: Dinksira", "Hospital Address: Hawassa", "Exp:5yrs", "Mobile No:0912816","600"},
            {"Doctor name: Elsa", "Hospital Address: Adama", "Exp:4yrs", "Mobile No:0912478","600"}
    };
    TextView tv;
    Button btn;
    String[][] doctor_details={};
    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv=findViewById(R.id.healthadv);
        btn=findViewById(R.id.button);
        Intent it=getIntent();
        String title=it.getStringExtra("title");
        tv.setText(title);
        if(title.compareTo("Family Physician")==0)
            doctor_details=doctor_details1;
        else
        if(title.compareTo("Dietitian")==0)
            doctor_details=doctor_details2;
        else
        if(title.compareTo("Dentist")==0)
            doctor_details=doctor_details3;
        else
        if(title.compareTo("Surgeon")==0)
            doctor_details=doctor_details4;
        else
        if(title.compareTo("Cardiologist")==0)
            doctor_details=doctor_details5;
        else
            doctor_details=doctor_details6;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorDetailActivity.this, FindDoctorActivity.class));
            }
        });
        list=new ArrayList();
        for(int i=0; i<doctor_details.length; i++)
        {
            item=new HashMap<String, String>();
            item.put("line1",doctor_details[i][0]);
            item.put("line2",doctor_details[i][1]);
            item.put("line3",doctor_details[i][2]);
            item.put("line4",doctor_details[i][3]);
            item.put("line5","Cons Fee"+doctor_details[i][4]+"/-");
            list.add(item);
        }
        sa=new SimpleAdapter(this, list, R.layout.multi_line, new String[]{"line1","line2","line3","line4","line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e}
        );
        ListView lst=findViewById(R.id.listv);
        lst.setAdapter(sa);
    }
}