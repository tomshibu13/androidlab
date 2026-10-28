package com.example.studentmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context c){super(c,"StudentDB",null,1);}
    @Override public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE students(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,register_no TEXT NOT NULL UNIQUE,course TEXT NOT NULL,semester INTEGER NOT NULL,email TEXT,phone TEXT,address TEXT)");
    }
    @Override public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){}
    private ContentValues values(String name,String reg,String course,int sem,String email,String phone,String address){
        ContentValues v=new ContentValues();
        v.put("name",name);v.put("register_no",reg);v.put("course",course);v.put("semester",sem);
        v.put("email",email);v.put("phone",phone);v.put("address",address);
        return v;
    }
    public boolean add(String name,String reg,String course,int sem,String email,String phone,String address){
        return getWritableDatabase().insert("students",null,values(name,reg,course,sem,email,phone,address))!=-1;
    }
    public boolean update(int id,String name,String reg,String course,int sem,String email,String phone,String address){
        return getWritableDatabase().update("students",values(name,reg,course,sem,email,phone,address),"id=?",new String[]{String.valueOf(id)})>0;
    }
    public boolean delete(int id){
        return getWritableDatabase().delete("students","id=?",new String[]{String.valueOf(id)})>0;
    }
    private Student fromCursor(Cursor c){
        return new Student(c.getInt(c.getColumnIndexOrThrow("id")),
            c.getString(c.getColumnIndexOrThrow("name")),c.getString(c.getColumnIndexOrThrow("register_no")),
            c.getString(c.getColumnIndexOrThrow("course")),c.getInt(c.getColumnIndexOrThrow("semester")),
            c.getString(c.getColumnIndexOrThrow("email")),c.getString(c.getColumnIndexOrThrow("phone")),
            c.getString(c.getColumnIndexOrThrow("address")));
    }
    public Student get(int id){
        try(Cursor c=getReadableDatabase().query("students",null,"id=?",new String[]{String.valueOf(id)},null,null,null)){
            return c.moveToFirst()?fromCursor(c):null;
        }
    }
    public ArrayList<Student> search(String query,String course){
        ArrayList<Student> result=new ArrayList<>();
        String sql="SELECT * FROM students WHERE (name LIKE ? OR register_no LIKE ?)"+
            (course.isEmpty()?"":" AND course = ?")+" ORDER BY name COLLATE NOCASE";
        String term="%"+query+"%";
        String[] args=course.isEmpty()?new String[]{term,term}:new String[]{term,term,course};
        try(Cursor c=getReadableDatabase().rawQuery(sql,args)){
            while(c.moveToNext())result.add(fromCursor(c));
        }
        return result;
    }
    public ArrayList<String> courses(){
        ArrayList<String> result=new ArrayList<>();
        try(Cursor c=getReadableDatabase().rawQuery("SELECT DISTINCT course FROM students ORDER BY course COLLATE NOCASE",null)){
            while(c.moveToNext())result.add(c.getString(0));
        }
        return result;
    }
    public int count(){
        try(Cursor c=getReadableDatabase().rawQuery("SELECT COUNT(*) FROM students",null)){
            return c.moveToFirst()?c.getInt(0):0;
        }
    }
    public int courseCount(){return courses().size();}
}
