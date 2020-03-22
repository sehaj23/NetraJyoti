package com.example.netrajyoti;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report extends AppCompatActivity {
    TextView total, present, absent, datee;
    SqliteHelper sql;
    Button done;
    SharedPreferences sharedPreferences;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        total = (TextView) findViewById(R.id.total);
        present = (TextView) findViewById(R.id.present);
        absent = (TextView) findViewById(R.id.absent);
        datee = (TextView) findViewById(R.id.date);
        done = (Button)findViewById(R.id.done);
        sql = new SqliteHelper(this);
        int presentstudent = sql.getpresent();
        int absentstudent = sql.getabsent();
        int totalstudent = sql.totalstudent();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa");
        Date currentTime = Calendar.getInstance().getTime();


        String oldtime = sdf.format(currentTime);

        Toast.makeText(Report.this,oldtime,Toast.LENGTH_LONG).show();

        sharedPreferences = Report.this.getSharedPreferences("MyPrefs",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lasttime", oldtime);
        editor.commit();


        present.setText(String.valueOf(presentstudent));
        absent.setText(String.valueOf(absentstudent));
        total.setText(String.valueOf(totalstudent));
        datee.setText(date);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report.this,Main2Activity.class);
                startActivity(intent);
            }
        });


    }
}
