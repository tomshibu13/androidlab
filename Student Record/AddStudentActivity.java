package com.example.studentmanagement;
import android.os.Bundle;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddStudentActivity extends AppCompatActivity {
    TextInputEditText name,reg,course,semester,email,phone,address;
    DatabaseHelper db;int id=-1;
    @Override protected void onCreate(Bundle state){
        super.onCreate(state);setContentView(R.layout.activity_add_student);
        db=new DatabaseHelper(this);
        name=findViewById(R.id.name);reg=findViewById(R.id.reg);
        course=findViewById(R.id.course);semester=findViewById(R.id.semester);
        email=findViewById(R.id.email);phone=findViewById(R.id.phone);address=findViewById(R.id.address);
        id=getIntent().getIntExtra("student_id",-1);
        if(id!=-1){
            Student s=db.get(id);
            if(s==null){finish();return;}
            ((TextView)findViewById(R.id.formTitle)).setText("Edit Student");
            name.setText(s.name);reg.setText(s.registerNo);course.setText(s.course);
            semester.setText(String.valueOf(s.semester));email.setText(s.email);
            phone.setText(s.phone);address.setText(s.address);
        }
        findViewById(R.id.save).setOnClickListener(v->save());
    }
    private String value(TextInputEditText e){return e.getText()==null?"":e.getText().toString().trim();}
    private void save(){
        String n=value(name),r=value(reg),c=value(course),e=value(email),p=value(phone),a=value(address),sem=value(semester);
        if(n.isEmpty()){name.setError("Required");return;}
        if(r.isEmpty()){reg.setError("Required");return;}
        if(c.isEmpty()){course.setError("Required");return;}
        int s;
        try{s=Integer.parseInt(sem);}catch(NumberFormatException ex){semester.setError("Enter a semester");return;}
        if(s<1||s>12){semester.setError("Use a semester from 1 to 12");return;}
        if(!e.isEmpty()&&!Patterns.EMAIL_ADDRESS.matcher(e).matches()){email.setError("Invalid email");return;}
        boolean ok;
        try{
            ok=id==-1?db.add(n,r,c,s,e,p,a):db.update(id,n,r,c,s,e,p,a);
        }catch(SQLiteConstraintException ex){ok=false;}
        if(ok){Toast.makeText(this,"Student saved",Toast.LENGTH_SHORT).show();finish();}
        else{reg.setError("Register number may already exist");}
    }
}
