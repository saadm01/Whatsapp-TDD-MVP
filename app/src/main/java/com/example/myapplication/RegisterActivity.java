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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private RegisterPresenter registerPresenter;

    private Button CreateAccountButton;
    private EditText UserEmail, UserPassword;
    private TextView ExistingAccount;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialisePresenter();
        InitialiseFields();

        ExistingAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendUserToLoginPage();
            }
        });

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPresenter.CreateNewAccount(UserEmail.getText().toString(),  UserPassword.getText().toString());
            }
        });
    }

    private void initialisePresenter() {
        registerPresenter = new RegisterPresenter(this);
    }


    private void InitialiseFields() {
        CreateAccountButton = (Button) findViewById(R.id.register_button);
        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        ExistingAccount = (TextView) findViewById(R.id.existing_account);
        loadingBar = new ProgressDialog(this);
    }

    protected void SendUserToLoginPage() {
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
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
    public void ShowPasswrodNotLong() {
        Toast.makeText(this, "Password Not Long Enough", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void loadingBarShow() {
        loadingBar.setTitle("Creating New Account");
        loadingBar.setMessage("Please wait, creating new account");
        loadingBar.setCanceledOnTouchOutside(true);
        loadingBar.show();
    }
    @Override
    public void SendUserToMainPage() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }
    @Override
    public void showSucessMsg() {
        Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void dismissLoadingBar() {
        loadingBar.dismiss();
    }


}
