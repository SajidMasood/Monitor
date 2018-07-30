package com.mr_abdali.monitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //TODO Variables Form XML
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    public static final String EXTRA_MESSAGE = "ParentId";
    private ProgressDialog progressDialog;

    @BindView(R.id.input_email) EditText mEmailText;
    @BindView(R.id.input_password) EditText mPasswordText;
    @BindView(R.id.btn_login) Button mLoginButton;
    @BindView(R.id.link_signup) TextView mSignupLink;
    @BindView(R.id.tvForgotPassword) TextView mForgotPassword;

    //TODO Database variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        //TODO Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //TODO Check User Already Loged In or Not.....
        /*FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null){
            finish();
            startActivity(new Intent(LoginActivity.this, ChildActivity.class));
        }*/

        //TODO Login Button Code
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //TODO signup textview Code
        mSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        //TODO start Forgot Password area...
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });
        //TODO end forgot password area

    }     //TODO onCreate() Method End.......

    //TODO login method Start
    public void login() {
        Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed();
            return;
        }
        mLoginButton.setEnabled(false);
        progressDialog = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        onLoginSuccess();
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //onLoginSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }
    //TODO login method End

    //TODO validate() start
    public boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            mPasswordText.setError("between 6 and 15 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }
        return valid;
    }
    //TODO validate() End here

    //TODO onLoginFailed() start
    public void onLoginFailed() {
        progressDialog.dismiss();
        Toast.makeText(getBaseContext(), "Login failed! Try Again...", Toast.LENGTH_LONG).show();
        mLoginButton.setEnabled(true);
    }
    //TODO onLoginFailed() End here

    //TODO onLoginSuccess() start
    public void onLoginSuccess() {
        mLoginButton.setEnabled(true);
        //TODO Login logic here
        String emailAddress = mEmailText.getText().toString().trim();
        String password = mPasswordText.getText().toString().trim();
        if (!TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(password)){
            mAuth.signInWithEmailAndPassword(emailAddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        Intent intent = new Intent(LoginActivity.this, ChildActivity.class);
                        intent.putExtra(EXTRA_MESSAGE,user_id);
                        finish();
                        startActivity(intent);
                    } else{
                        onLoginFailed();
                    }
                }
            });
        }

    }
    //TODO onLoginSuccess method end here

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
              //  startActivity(new Intent(LoginActivity.this, ChildActivity.class));
                // By default we just finish the Activity and log them in automatically
                //this.finish();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }
}
