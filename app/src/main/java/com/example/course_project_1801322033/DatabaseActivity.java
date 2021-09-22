package com.example.course_project_1801322033;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public abstract class DatabaseActivity extends AppCompatActivity {
    protected void SelectSQL(String SelectQ, String[] args, OnSelectSuccess success)
            throws Exception
    {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(
                getFilesDir().getPath()+"/contacts.db", null
        );
        Cursor cursor = db.rawQuery(SelectQ, args);
        while(cursor.moveToNext()){
            String ID=cursor.getString(cursor.getColumnIndex("ID"));
            String Name=cursor.getString(cursor.getColumnIndex("Name"));
            String Phone=cursor.getString(cursor.getColumnIndex("Phone"));
            String Description=cursor.getString(cursor.getColumnIndex("Description"));
            String Category=cursor.getString(cursor.getColumnIndex("Category"));
            success.OnElementSelected(ID, Name, Phone, Description, Category);
        }
        db.close();
    }

    protected void ExecSQL(String SQL, Object[] args, OnQuerySuccess success)
            throws Exception{
        SQLiteDatabase db =
                SQLiteDatabase.openOrCreateDatabase(
                        getFilesDir().getPath()+"/contacts.db", null
                );
        if(args!=null)
            db.execSQL(SQL, args);
        else
            db.execSQL(SQL);
        db.close();
        success.OnSuccess();
    }

    protected void initDB() throws Exception{
        ExecSQL(
                "CREATE TABLE if not exists CONTACTS( " +
                        "ID integer PRIMARY KEY AUTOINCREMENT, " +
                        "Name text not null," + "Phone text not null," +
                        "Description text not null," + "Category text not null," +
                        "unique(Name, Phone)" +
                        ");",
                null,
                ()-> Toast.makeText(getApplicationContext(), "DB init successful",
                        Toast.LENGTH_SHORT).show()

        );
    }

    protected interface OnQuerySuccess{
        public void OnSuccess();
    }

    protected interface OnSelectSuccess{
        public void OnElementSelected(String ID, String Name, String Phone, String Description, String Category);
    }
}
