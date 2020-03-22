package com.example.netrajyoti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Date;
import java.util.Locale;

public class SqliteHelper extends SQLiteOpenHelper {
    SQLiteDatabase mDatabase;
    MainActivity main;

    public static final String DATABASENAME = "ATTENDANCE";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENT = "STUDENTS";
    public static final String KEY_ID = "ID";
    public static final String KEY_PK = "PK";

    public static final String KEY_USERNAME = "USERNAME";

    public static final String TABLE_ATTENDANCE = "DAILY_ATTENDANCE";
    public static final String KEY_DATE = "DATEE";
    public static final String KEY_ROLLNO = "ID";
    public static final String KEY_PRESENCE = "PRESENCE";



    public static final String SQL_TABLE_ATTENDANCE =" CREATE TABLE " + TABLE_ATTENDANCE
            + " ( "
            + KEY_PK+ " INTEGER PRIMARY KEY, "
            + KEY_DATE+ " TEXT , "
            + KEY_ROLLNO + " INTEGER, "
            + KEY_PRESENCE + " TEXT, "
            + " FOREIGN KEY ("+KEY_ROLLNO+") REFERENCES "+TABLE_STUDENT+"("+KEY_ID+"));";





    //COLUMN user name

    public static final String SQL_TABLE_STUDENT = " CREATE TABLE " + TABLE_STUDENT
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USERNAME + " TEXT "
            + " ) ";




    public SqliteHelper(Context context) {
        super(context, DATABASENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TABLE_STUDENT);
        db.execSQL(SQL_TABLE_ATTENDANCE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addstudent(Student student){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME,student.USERNAME);

        db.insert(TABLE_STUDENT,null,values);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void markpresentattendance(int rollno){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();









        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // int finaldate = Integer.parseInt(date);
        String present = "1";

        values.put(KEY_DATE,date);
        values.put(KEY_ROLLNO,rollno);
        values.put(KEY_PRESENCE,present);


        db.insert(TABLE_ATTENDANCE,null,values);





    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void markabsentattendance(int rollno){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();









        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        // int finaldate = Integer.parseInt(date);
        String absent = "0";

        values.put(KEY_DATE,date);
        values.put(KEY_ROLLNO,rollno);
        values.put(KEY_PRESENCE,absent);


        db.insert(TABLE_ATTENDANCE,null,values);



    }
    public void deleteStudent(int rollno){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM STUDENTS WHERE ID = " + rollno);

    }
    public void deleteallStudent(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE  FROM STUDENTS");

    }
    public void updatestudent(int rollno,String username){
        SQLiteDatabase db = getWritableDatabase();
        //   db.execSQL("UPDATE STUDENTS SET USERNAME = "+ username +" WHERE ID = " +rollno );
        ContentValues cv = new ContentValues();
        //   cv.put("ID",rollno); //These Fields should be your String values of actual column names
        cv.put("USERNAME",username);
        db.update(TABLE_STUDENT,cv,"ID= "+rollno,null);



    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  int getpresent(){
        //  long tasklist_Id = 0;
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM DAILY_ATTENDANCE WHERE PRESENCE = '1' AND DATEE = '"+date+"';" ,null);
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();


        db.close();
        return count;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public  int getabsent(){
        //  long tasklist_Id = 0;
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM DAILY_ATTENDANCE WHERE PRESENCE = '0' AND DATEE = '"+date+"';"  ,null);
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();


        db.close();
        return count;
    }
    public int totalstudent(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM STUDENTS;"  ,null);
        int count = 0;
        if(null != cursor)
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();


        db.close();
        return count;






    }
}
