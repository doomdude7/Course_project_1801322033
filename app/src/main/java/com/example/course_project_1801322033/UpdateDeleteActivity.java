package com.example.course_project_1801322033;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class UpdateDeleteActivity extends DatabaseActivity {
    protected EditText editName, editPhone, editDescription, editCategory;
    protected Button btnUpdate, btnDelete;
    protected String ID="";
    protected void BackToMain(){
        finishActivity(200);
        Intent i=new Intent(UpdateDeleteActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        editName=findViewById(R.id.editName);
        editPhone=findViewById(R.id.editPhone);
        editDescription=findViewById(R.id.editDescription);
        editCategory=findViewById(R.id.editCategory);
        btnUpdate=findViewById(R.id.btnUpdate);
        btnDelete=findViewById(R.id.btnDelete);
        Bundle b=getIntent().getExtras();
        if(b!=null){
            ID=b.getString("ID");
            editName.setText(b.getString("Name"));
            editPhone.setText(b.getString("Phone"));
            editDescription.setText(b.getString("Description"));
            editCategory.setText(b.getString("Category"));
        }

        btnUpdate.setOnClickListener(view -> {
            try{
                ExecSQL(
                        "UPDATE CONTACTS SET " + "Name = ?, " + "Phone = ?, " + "Description = ?, " +
                                "Category = ? " + "WHERE ID = ?",
                        new Object[]{
                                editName.getText().toString(),
                                editPhone.getText().toString(),
                                editDescription.getText().toString(),
                                editCategory.getText().toString(),
                                ID
                        },
                        ()-> Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "ERROR:"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            BackToMain();
        });

        btnDelete.setOnClickListener(view -> {
            try{
                ExecSQL(
                        "DELETE FROM CONTACTS WHERE ID = ?",
                        new Object[]{ ID},
                        ()-> Toast.makeText(getApplicationContext(), "Deleted!", Toast.LENGTH_LONG).show()

                );
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "ERROR:"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            BackToMain();
        });
    }
}