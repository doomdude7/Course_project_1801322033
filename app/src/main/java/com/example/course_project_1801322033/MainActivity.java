package com.example.course_project_1801322033;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends DatabaseActivity {
    protected EditText editName, editPhone, editDescription, editCategory;
    protected Button btnInsert, btnDelete;
    protected ListView contactList;





    protected void FillContactList() throws Exception {
        final ArrayList<String> listResults=new ArrayList<>();
        SelectSQL("SELECT * FROM CONTACTS ORDER BY Name", null,
                new OnSelectSuccess() {
                    @Override
                    public void OnElementSelected(String ID, String Name, String Phone, String Description, String Category) {
                        listResults.add(ID+"\t"+Name+"\t"+Phone+"\t"+Description+"\t"+Category);
                    }
                });
        contactList.clearChoices();
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.activity_contactlist,
                R.id.textView,
                listResults
        );
        contactList.setAdapter(arrayAdapter);
    }
    @Override
    @CallSuper
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            FillContactList();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editName=findViewById(R.id.editName);
        editPhone=findViewById(R.id.editPhone);
        editDescription=findViewById(R.id.editDescription);
        editCategory=findViewById(R.id.editCategory);
        btnInsert=findViewById(R.id.btnInsert);
        contactList=findViewById(R.id.contactList);
        btnInsert.setOnClickListener(view -> {
            try {
                ExecSQL("INSERT INTO CONTACTS(Name, Phone, Description, Category)" +
                                "VALUES(?, ?, ?, ?)",
                        new Object[]{
                                editName.getText().toString(),
                                editPhone.getText().toString(),
                                editDescription.getText().toString(),
                                editCategory.getText().toString()},
                        ()-> Toast.makeText(getApplicationContext(),"Contents inserted into database", Toast.LENGTH_SHORT).show());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            try {
                FillContactList();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();;
            }

        });

        try {
            initDB();
            FillContactList();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "exception: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected="";
                TextView clickedText=view.findViewById(R.id.textView);
                selected=clickedText.getText().toString();
                String[] elements = selected.split("\t");
                String ID=elements[0];
                String Name=elements[1];
                String Phone=elements[2];
                String Description=elements[3];
                String Category=elements[4].trim();

                Intent intent=new Intent(MainActivity.this, UpdateDeleteActivity.class);
                Bundle b=new Bundle();
                b.putString("ID", ID);
                b.putString("Name", Name);
                b.putString("Phone", Phone);
                b.putString("Description", Description);
                b.putString("Category", Category);
                intent.putExtras(b);
                startActivityForResult(intent, 200, b);

            }
        });

    }


}