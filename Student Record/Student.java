package com.example.studentmanagement;
public class Student {
    public final int id, semester;
    public final String name, registerNo, course, email, phone, address;
    public Student(int id,String name,String registerNo,String course,int semester,String email,String phone,String address){
        this.id=id; this.name=name; this.registerNo=registerNo; this.course=course;
        this.semester=semester; this.email=email; this.phone=phone; this.address=address;
    }
}
