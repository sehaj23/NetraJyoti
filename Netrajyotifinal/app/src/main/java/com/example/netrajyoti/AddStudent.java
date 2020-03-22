package com.example.netrajyoti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddStudent extends AppCompatActivity {
    EditText name;
    TextView add;
    Button submit;
    SqliteHelper sql;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        name= (EditText)findViewById(R.id.name);
        add = (TextView)findViewById(R.id.add);
        submit=(Button)findViewById(R.id.done) ;
        sql = new SqliteHelper(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString();
                sql.addstudent(new Student(null,Name));
                Toast.makeText(AddStudent.this,"NAME INSERTED",Toast.LENGTH_SHORT).show();
                name.setText("");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddStudent.this,StudentList.class);
                startActivity(intent);

            }
        });
    }
}
