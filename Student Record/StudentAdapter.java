package com.example.studentmanagement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.Holder>{
    public interface OnStudentClick{void onClick(Student student);}
    private ArrayList<Student> students=new ArrayList<>();
    private final OnStudentClick listener;
    public StudentAdapter(OnStudentClick listener){this.listener=listener;}
    public void setStudents(ArrayList<Student> list){students=list;notifyDataSetChanged();}
    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent,int type){
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student,parent,false));
    }
    @Override public void onBindViewHolder(@NonNull Holder h,int position){
        Student s=students.get(position);
        h.name.setText(s.name);
        h.info.setText(s.registerNo+"  •  "+s.course+"  •  Sem "+s.semester);
        h.avatar.setText(s.name.isEmpty()?"S":s.name.substring(0,1).toUpperCase());
        h.itemView.setOnClickListener(v->listener.onClick(s));
    }
    @Override public int getItemCount(){return students.size();}
    static class Holder extends RecyclerView.ViewHolder{
        TextView name,info,avatar;
        Holder(View v){super(v);name=v.findViewById(R.id.studentName);
            info=v.findViewById(R.id.studentInfo);avatar=v.findViewById(R.id.avatar);}
    }
}
