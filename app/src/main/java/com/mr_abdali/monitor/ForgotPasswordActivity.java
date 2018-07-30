package com.mr_abdali.monitor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {

    //TODO Variable initialization
    private FirebaseAuth mAuth;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.enter_email) TextView tvReserPassword;
    @BindView(R.id.btn_resetPassword) Button btn_resetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        //TODO TOOLBAR
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TODO
        mAuth = FirebaseAuth.getInstance();


        //TODO Reset Password Button....
        btn_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = tvReserPassword.getText().toString().trim();
                if (userEmail.equals("")){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Registered Email Address!",Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Password Reset Email Sent!",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(),"Error in Sending Password Reset Email!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
