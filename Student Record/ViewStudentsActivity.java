package com.example.studentmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;

public class ViewStudentsActivity extends AppCompatActivity {
    DatabaseHelper db;StudentAdapter adapter;TextInputEditText search;Spinner filter;TextView empty;
    @Override protected void onCreate(Bundle state){
        super.onCreate(state);setContentView(R.layout.activity_view_students);
        db=new DatabaseHelper(this);search=findViewById(R.id.search);
        filter=findViewById(R.id.courseFilter);empty=findViewById(R.id.empty);
        RecyclerView list=findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter=new StudentAdapter(s->{
            Intent intent=new Intent(this,StudentDetailsActivity.class);
            intent.putExtra("student_id",s.id);startActivity(intent);
        });list.setAdapter(adapter);
        search.addTextChangedListener(new TextWatcher(){
            public void beforeTextChanged(CharSequence s,int start,int count,int after){}
            public void onTextChanged(CharSequence s,int start,int before,int count){refresh();}
            public void afterTextChanged(Editable s){}
        });
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> parent,View v,int position,long id){refresh();}
            public void onNothingSelected(AdapterView<?> parent){}
        });
        findViewById(R.id.add).setOnClickListener(v->startActivity(new Intent(this,AddStudentActivity.class)));
    }
    @Override protected void onResume(){super.onResume();loadFilters();refresh();}
    private void loadFilters(){
        String selected=filter.getSelectedItem()==null?"All courses":filter.getSelectedItem().toString();
        ArrayList<String> courses=db.courses();courses.add(0,"All courses");
        filter.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,courses));
        int index=courses.indexOf(selected);filter.setSelection(Math.max(0,index));
    }
    private void refresh(){
        String query=search.getText()==null?"":search.getText().toString();
        String selected=filter.getSelectedItem()==null?"All courses":filter.getSelectedItem().toString();
        ArrayList<Student> result=db.search(query,selected.equals("All courses")?"":selected);
        adapter.setStudents(result);
        empty.setVisibility(result.isEmpty()?View.VISIBLE:View.GONE);
    }
}
