package com.mr_abdali.monitor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {

    //TODO Variables Form XML
    private static final String TAG = "SignupActivity";
    private ProgressDialog progressDialog;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    //TODO for Firebase Database connection
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        //TODO Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //TODO Signup button clicked Start
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        //TODO Signup button clicked End

        //TODO loginlink textview button clicked Start
        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        //TODO loginlink textview button clicked End
    }


    //TODO signup() method code Start
    public void signup() {
        Log.d(TAG, "Signup");
        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);
        progressDialog = new ProgressDialog(SignupActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();
        onSignupSuccess();
        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        //onSignupSuccess();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }
    //TODO signup() method code End


    // TODO validate() success process Start
    public boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        if (name.isEmpty() || name.length() < 5) {
            _nameText.setError("at least 5 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            _passwordText.setError("between 6 and 15 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 6 || reEnterPassword.length() > 15 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }
        return valid;
    }
    // TODO validate() success process End

    // TODO onSignupFailed() success process Start
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }
    // TODO onSignupFailed() success process End


    // TODO onSignupSuccess() success process to goes next layout..Start
    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        // TODO signup success process for Firebase Database
        final String userName = _nameText.getText().toString().trim();
        final String emailAddress = _emailText.getText().toString().trim();
        final String password = _passwordText.getText().toString().trim();
        //TODO Database
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(emailAddress) && !TextUtils.isEmpty(password)){
            mAuth.createUserWithEmailAndPassword(emailAddress,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = mDatabase.child(user_id);
                        current_user.child("Name").setValue(userName);
                        current_user.child("Email").setValue(emailAddress);
                        current_user.child("Password").setValue(password);
                        Toast.makeText(SignupActivity.this,"Account Create Successfully",Toast.LENGTH_SHORT).show();
                        // TODO pass through sign In login Activity
                        finish();
                        startActivity(new Intent(SignupActivity.this, ChildActivity.class));
                        //finish();
                    } else {
                        progressDialog.dismiss();
                        Log.e("ERROR",task.getException().toString());
                        Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        finish();
                    }
                    //finish();
                }
            });
        }
    }
    // TODO onSignupSuccess() success process to goes next layout..End

}
