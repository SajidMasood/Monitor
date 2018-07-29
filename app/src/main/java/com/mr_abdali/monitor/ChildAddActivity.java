package com.mr_abdali.monitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Mr-Abdali on 7/27/2018.
 */

public class ChildAddActivity extends AppCompatActivity {

    private EditText childname,childage,childemail,password,confirmedpassword;
    private RadioButton male1,female;
    //private ProgressBar progressBar1;
    private Button btn_add_child;
    private String gender;
    private DatabaseReference mDatabase;
    private FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.child_add);

        mauth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("childlist");

        childname=(EditText)findViewById(R.id.input_name);
        childage=(EditText)findViewById(R.id.input_age);
        childemail=(EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        confirmedpassword=(EditText)findViewById(R.id.input_reEnterPassword);
        //  progressBar1=(ProgressBar)findViewById(R.id.progressBar1);
        btn_add_child=(Button)findViewById(R.id.btn_add_child);
        //  male1=(RadioButton)findViewById(R.id.male);
        // female=(RadioButton)findViewById(R.id.female);


        btn_add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String parentId=getIntent().getStringExtra("ParentId");
                final String childName = childname.getText().toString().trim();
                final String childAge = childage.getText().toString().trim();
                final String childEmail = childemail.getText().toString().trim();
                final String pass= password.getText().toString().trim();
                //    final Boolean male=male1.isChecked();
                //   final Boolean fem=female.isChecked();


                if (TextUtils.isEmpty(childName))
                {
                    Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(childAge))
                {
                    Toast.makeText(getApplicationContext(), "Enter Age!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(childEmail))
                {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isemailvalid(childEmail))
                {
                    Toast.makeText(getApplicationContext(), "Invalid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)|| !isPasswordValid(pass))
                {
                    Toast.makeText(getApplicationContext(), "Enter password or password not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 6)
                {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                final ProgressDialog progressDialog = new ProgressDialog(ChildAddActivity.this,
                        R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.show();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                //  onLoginSuccess();
                                progressDialog.dismiss();
                            }
                        }, 2000);
                //        progressBar1.setVisibility(View.VISIBLE);
                mauth.createUserWithEmailAndPassword(childEmail, pass)
                        .addOnCompleteListener(ChildAddActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Toast.makeText(childaddActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                                if(task.isSuccessful())
                                {
                                    String user_id = mauth.getCurrentUser().getUid();
                                    DatabaseReference current_user = mDatabase.child(parentId).child(user_id);
                                    current_user.child("ChildName").setValue(childName);
                                    current_user.child("ChildEmail").setValue(childEmail);
                                    current_user.child("password").setValue(pass);
                                    current_user.child("Age").setValue(childAge);
                                    Toast.makeText(ChildAddActivity.this,"Child Successfully Added",Toast.LENGTH_SHORT).show();
                                    Intent intent =new Intent(ChildAddActivity.this,ChildActivity.class);
                                    //finish();
                                    startActivity(intent);
                                }
                                //                      progressBar1.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                else if (!task.isSuccessful()) {
                                    Toast.makeText(ChildAddActivity.this, "Authentication failed. try Again" ,
                                            Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(ChildAddActivity.this, ChildActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }
    private boolean isPasswordValid(String password)
    {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        String ConfirmedPassword=confirmedpassword.getText().toString();
        return ConfirmedPassword.equals(password);
    }
    private Boolean isemailvalid(String email)
    {
        return email.contains("@")&& email.contains(".com");
    }

}

