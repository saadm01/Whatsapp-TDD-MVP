package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter loginPresenter;
    private ProgressDialog progressDialog;

    private Button LoginButton, PhoneLoginButton;
    private EditText UserEmail, UserPassword;
    private TextView NewAccount, ForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Show login layout
        setContentView(R.layout.activity_login);


        initialisePresenter();
        InitialiseFields();

        NewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send user to register page
                loginPresenter.sendUserToRegisterPage();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Login button pressed
                loginPresenter.loginUser( UserEmail.getText().toString(),  UserPassword.getText().toString());
            }
        });

        PhoneLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send user to phone login page
                loginPresenter.sendUserToPhoneLoginPage();
            }
        });

    }

    private void initialisePresenter() {
        //Initialise login presenter class
        loginPresenter = new LoginPresenter(this);
    }



    private void InitialiseFields() {
        LoginButton = (Button) findViewById(R.id.login_button);
        PhoneLoginButton = (Button) findViewById(R.id.number_login_button);
        UserEmail = (EditText) findViewById(R.id.login_email);
        UserPassword = (EditText) findViewById(R.id.login_password);
        NewAccount = (TextView) findViewById(R.id.new_account);
        ForgetPassword = (TextView) findViewById(R.id.forget_password);
        progressDialog = new ProgressDialog(this);
    }



    @Override
    public  void SendUserToRegisterPage() {
        Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    @Override
    public  void SendUserToPhoneLoginPage() {
        Intent phoneIntent = new Intent(LoginActivity.this, NumberLoginActivity.class);
        startActivity(phoneIntent);
    }

    @Override
    public void showEnterEmail() {
        Toast.makeText(this, "Please enter email...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showEnterPassword() {
        Toast.makeText(this, "Please enter password...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void progressDialog() {
        progressDialog.setTitle("Sign In");
        progressDialog.setMessage("Please wait, signing in...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }

    @Override
    public void SendUserToMainPage() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    @Override
    public void progressDialogDismiss() {
        progressDialog.dismiss();
    }

    @Override
    public void showSucess() {
        Toast.makeText(this, "Log In Successful...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showErrorMsg(){
        Toast.makeText(this, "Log In Unsuccessful...", Toast.LENGTH_SHORT).show();
    }
}
