package com.example.studentmanagement;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    @Override protected void onCreate(Bundle state){
        super.onCreate(state);setContentView(R.layout.activity_main);
        db=new DatabaseHelper(this);
        findViewById(R.id.btnAdd).setOnClickListener(v->startActivity(new Intent(this,AddStudentActivity.class)));
        findViewById(R.id.btnView).setOnClickListener(v->startActivity(new Intent(this,ViewStudentsActivity.class)));
    }
    @Override protected void onResume(){
        super.onResume();
        ((TextView)findViewById(R.id.txtCount)).setText(String.valueOf(db.count()));
        ((TextView)findViewById(R.id.txtCourses)).setText(db.courseCount()+" courses");
        LinearLayout container=findViewById(R.id.recentContainer);
        container.removeAllViews();
        ArrayList<Student> all=db.search("","");
        if(all.isEmpty()){
            TextView empty=new TextView(this);empty.setText("No students yet. Add your first student!");
            empty.setTextSize(15);empty.setPadding(10,12,10,12);container.addView(empty);
        }else{
            // IDs increase as students are added; display the newest first.
            all.sort((a,b)->Integer.compare(b.id,a.id));
            for(int i=0;i<Math.min(5,all.size());i++){
                Student s=all.get(i);
                TextView row=new TextView(this);
                row.setText(s.name+"\n"+s.registerNo+"  •  "+s.course);
                row.setTextSize(16);row.setPadding(18,16,18,16);
                row.setBackgroundColor(0xFFFFFFFF);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(-1,-2);
                params.bottomMargin=10;container.addView(row,params);
                row.setOnClickListener(v->{
                    Intent intent=new Intent(this,StudentDetailsActivity.class);
                    intent.putExtra("student_id",s.id);startActivity(intent);
                });
            }
        }
    }
}
