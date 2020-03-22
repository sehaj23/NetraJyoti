package com.example.netrajyoti;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StudentList extends AppCompatActivity {
   List<Student> students;
    SQLiteDatabase mDatabase;
    ListView listView;
    StudentAdapter adapter;
    SqliteHelper sql;
    String student_id;
    TextView clear;
    int listPosition;
    AdapterView.AdapterContextMenuInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        listView = (ListView)findViewById(R.id.list);
        students = new ArrayList<>();
        sql = new SqliteHelper(this);
        clear = (TextView) findViewById(R.id.clear);

        mDatabase = openOrCreateDatabase(SqliteHelper.DATABASENAME, MODE_PRIVATE, null);
        shownamesfromdatabase();
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sql.deleteallStudent();
                shownamesfromdatabase();
            }
        });

    }

    private void shownamesfromdatabase() {
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM STUDENTS", null);
        if (cursor.moveToFirst()) {
            //looping through all the records
            do {
                //pushing each record in the current booking list
                students.add(new Student(
                        cursor.getString(0),
                        cursor.getString(1)
                        //   cursorEmployees.getString(4)

                ));
            } while (cursor.moveToNext());
        }
        //closing the cursor
        cursor.close();

        //creating the adapter object
        adapter = new StudentAdapter(this, R.layout.student_list_view, students, mDatabase);

        //adding the adapter to listview
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuinf = new MenuInflater(this);
        menuinf.inflate(R.menu.context_menu,menu);
        if (menu != null) {
            menu.setHeaderTitle("Select The Action");
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){





            case R.id.edit:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

                listPosition = info.position;
                student_id = adapter.getItem(listPosition).getID();//list
                LayoutInflater myLayout = LayoutInflater.from(StudentList.this);
                View dialogView = myLayout.inflate(R.layout.edit_input, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StudentList.this);
                alertDialogBuilder.setView(dialogView);
                alertDialogBuilder.setTitle("ENTER NAME");
                alertDialogBuilder.create();
                final EditText input = (EditText) dialogView.findViewById(R.id.input);
                alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String namee = input.getText().toString();

                        sql.updatestudent(Integer.parseInt(student_id), namee);
                        Intent intent1 = new Intent(StudentList.this, StudentList.class);
                        startActivity(intent1);
                    }
                });
                alertDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.show();

                break;
            case R.id.delete:
                info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                int listPositionn = info.position;
                final String student_idd = adapter.getItem(listPositionn).getID();//list


                AlertDialog.Builder alertDialogBuilderr = new AlertDialog.Builder(StudentList.this);
               // alertDialogBuilder.setView(dialogView);
                AlertDialog.Builder build = alertDialogBuilderr.setTitle("ARE YOUR SURE");
                alertDialogBuilderr.create();
              //  final EditText input = (EditText) dialogView.findViewById(R.id.input);
                alertDialogBuilderr.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  String namee = input.getText().toString();
                        sql.deleteStudent(Integer.parseInt(student_idd));



                        Intent intent1 = new Intent(StudentList.this, StudentList.class);
                        startActivity(intent1);
                    }
                });
                alertDialogBuilderr.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilderr.show();


                break;


//                int listPosition = info.position;
//                listView.get(listPosition).getTitle();

        }
        return super.onContextItemSelected(item);

    }

}

