package com.example.netrajyoti;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TakeAttendance extends AppCompatActivity implements
        SimpleGestureFilter.SimpleGestureListener, TextToSpeech.OnInitListener {
    private SimpleGestureFilter detector;
    SQLiteDatabase mDatabase;
    TextView rollno, username;
    SqliteHelper sql;
    //  List<student> Student;
    Button next, present, absent;
    int x = 1;
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    static int count = 0;
    SharedPreferences sharedPreferences;

    Button ttsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        ttsButton = findViewById(R.id.ttsButton);

        detector = new SimpleGestureFilter(TakeAttendance.this, this);
        username = findViewById(R.id.username);
        rollno = findViewById(R.id.rollno);
        sql = new SqliteHelper(this);
        mDatabase = openOrCreateDatabase(SqliteHelper.DATABASENAME, MODE_PRIVATE, null);
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        fetchnames();

        ttsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speech = username.getText().toString();
                speakWords(speech);
            }
        });


    }

    public void fetchnames() {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM STUDENTS where ID =" + x, null);
        if (cursor.moveToFirst()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            username.setText(name);
            rollno.setText(String.valueOf(id));
            x++;
        }
        cursor.close();
        ttsButton.performClick();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSwipe(int direction) {
        String showToastMessage = "";

        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT:
                swiperight();
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                swipeleft();
                break;
        }
    //    Toast.makeText(this, showToastMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
            // Double Tab
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void swipeleft() {
        String totalstudents = sharedPreferences.getString("totalstudent",null);
        if(count == Integer.parseInt(totalstudents)){
            Toast.makeText(TakeAttendance.this,"ALL STUDENTS ATTENDANCE TAKEN",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TakeAttendance.this,Report.class);
            startActivity(intent);
        }else{
            String roll = rollno.getText().toString();
            sql.markabsentattendance(Integer.parseInt(roll));
            fetchnames();
            count++;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void swiperight() {
        String totalstudents = sharedPreferences.getString("totalstudent", null);

        //         Toast.makeText(fetch.this,totalstudents,Toast.LENGTH_LONG).show();
        if (count == Integer.parseInt(totalstudents)) {
            Toast.makeText(TakeAttendance.this, "ALL STUDENTS ATTENDANCE TAKEN", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TakeAttendance.this,Report.class);
            startActivity(intent);
        } else {
            String roll = rollno.getText().toString();
            sql.markpresentattendance(Integer.parseInt(roll));
            fetchnames();
            count++;
        }
    }

    private void speakWords(String speech) {
        //speak straight away
        //myTTS.setPitch(0.2f);
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.ENGLISH)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.ENGLISH);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            } else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
}
