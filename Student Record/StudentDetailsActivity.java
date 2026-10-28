package com.example.studentmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class StudentDetailsActivity extends AppCompatActivity {
    DatabaseHelper db;int id;
    @Override protected void onCreate(Bundle state){
        super.onCreate(state);setContentView(R.layout.activity_student_details);
        db=new DatabaseHelper(this);id=getIntent().getIntExtra("student_id",-1);
        if(id==-1){finish();return;}
        findViewById(R.id.edit).setOnClickListener(v->{
            Intent intent=new Intent(this,AddStudentActivity.class);
            intent.putExtra("student_id",id);startActivity(intent);
        });
        findViewById(R.id.delete).setOnClickListener(v->
            new MaterialAlertDialogBuilder(this)
                .setTitle("Delete student?")
                .setMessage("This record will be permanently deleted.")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Delete",(d,w)->{
                    if(db.delete(id)){Toast.makeText(this,"Student deleted",Toast.LENGTH_SHORT).show();finish();}
                    else Toast.makeText(this,"Delete failed",Toast.LENGTH_SHORT).show();
                }).show()
        );
    }
    @Override protected void onResume(){
        super.onResume();Student s=db.get(id);
        if(s==null){finish();return;}
        ((TextView)findViewById(R.id.details)).setText(
            s.name+"\n"+s.registerNo+"\n\n"+
            "ACADEMIC INFORMATION\nCourse: "+s.course+"\nSemester: "+s.semester+"\n\n"+
            "CONTACT INFORMATION\nEmail: "+s.email+"\nPhone: "+s.phone+"\nAddress: "+s.address);
    }
}
