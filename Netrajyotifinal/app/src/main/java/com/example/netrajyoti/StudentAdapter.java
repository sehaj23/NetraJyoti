package com.example.netrajyoti;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

class StudentAdapter extends ArrayAdapter<Student> {
    Context mCtx;
    int listLayoutRes;
    List<Student> students;
    SQLiteDatabase mDatabase;


    public StudentAdapter(Context mCtx, int listLayoutRes, List<Student> students, SQLiteDatabase mDatabase) {
        super(mCtx, listLayoutRes, students);

        this.mCtx = mCtx;
        this.listLayoutRes = listLayoutRes;
        this.students = students;
        this.mDatabase = mDatabase;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.student_list_view, null);
        Student student = students.get(position);
        TextView textViewName = view.findViewById(R.id.name1);
        TextView textViewroll = view.findViewById(R.id.roll);
        textViewName.setText(student.getUSERNAME());
        textViewroll.setText(student.getID());

        return view;


    }
}
