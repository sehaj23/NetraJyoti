package com.example.netrajyoti;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.widget.Toast;
import com.example.netrajyoti.SimpleGestureFilter.SimpleGestureListener;

import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends AppCompatActivity implements
        SimpleGestureListener{
    private SimpleGestureFilter detector;
    SharedPreferences sharedPreferences;
    SqliteHelper sql;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        sql = new SqliteHelper(this);

        // Detect touched area
        detector = new SimpleGestureFilter(Main2Activity.this, this);
        int totalstudent = sql.totalstudent();
        sharedPreferences = Main2Activity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("totalstudent", String.valueOf(totalstudent));
        editor.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSwipe(int direction) {

        //Detect the swipe gestures and display toast
        String showToastMessage = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_DOWN:

                swipedown();
                break;
            case SimpleGestureFilter.SWIPE_UP:

                swipeup();
                break;

        }
     //   Toast.makeText(this, showToastMessage, Toast.LENGTH_SHORT).show();
    }


    //Toast shown when double tapped on screen
    @Override
    public void onDoubleTap() {
        Toast.makeText(this, "You have Double Tapped.", Toast.LENGTH_SHORT)
                .show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void swipeup(){
        String lasttime = sharedPreferences.getString("lasttime", null);

        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss aa");

        Date currentTime = Calendar.getInstance().getTime();
        Intent intent = new Intent(Main2Activity.this, TakeAttendance.class);
        startActivity(intent);
        finish();

    }
    public void swipedown(){
        Intent intent = new Intent(Main2Activity.this,AddStudent.class);
        startActivity(intent);
        finish();
    }
}