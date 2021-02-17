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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 *Created by Saad Manzoor
 *Integration of Test-Driven Development with the Agile Methodology (Honours Project)
 *Edinburgh Napier University (2019 - 2020)
 */

public class NumberLoginActivity extends AppCompatActivity implements NumberLoginView {

    private NumberLoginPresenter numberLoginPresenter;

    private EditText userNumber;
    private EditText verficationCode;
    private Button verificationButton;
    private Button verifiyNumberButton;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    private String mVerificationId;
    private String phoneNumber;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_login);

        firebaseAuth = FirebaseAuth.getInstance();

        initialisePresenter();
        InitialiseFields();

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                phoneNumber = userNumber.getText().toString();
                numberLoginPresenter.sendVerificationNumber(phoneNumber);


//                if (TextUtils.isEmpty(phoneNumber))
//                {
//                    Toast.makeText(NumberLoginActivity.this, "Please enter your phone number first...", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    progressDialog.setTitle("Phone Verification");
//                    progressDialog.setMessage("Please wait, while we are authenticating using your phone...");
//                    progressDialog.setCanceledOnTouchOutside(false);
//                    progressDialog.show();
//
//                    PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, NumberLoginActivity.this, callbacks);
//                }
            }
        });



        verifiyNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                userNumber.setVisibility(View.INVISIBLE);
                verificationButton.setVisibility(View.INVISIBLE);

                verificationCode = verficationCode.getText().toString();

                numberLoginPresenter.sendVerificationCode(verificationCode);

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                signInWithPhoneAuthCredential(credential);

            }
        });


        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {
                Toast.makeText(NumberLoginActivity.this, "Invalid Phone Number, Please enter correct phone number with your country code...", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

                userNumber.setVisibility(View.VISIBLE);
                verificationButton.setVisibility(View.VISIBLE);
                verficationCode.setVisibility(View.INVISIBLE);
                verifiyNumberButton.setVisibility(View.INVISIBLE);
            }

            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token)
            {
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                forceResendingToken = token;

                Toast.makeText(NumberLoginActivity.this, "Code has been sent, please check and verify...", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                userNumber.setVisibility(View.INVISIBLE);
                verificationButton.setVisibility(View.INVISIBLE);
                verficationCode.setVisibility(View.VISIBLE);
                verifiyNumberButton.setVisibility(View.VISIBLE);
            }
        };
    }

    private void initialisePresenter() {
        numberLoginPresenter = new NumberLoginPresenter(this);
    }

    private void InitialiseFields() {

        verificationButton = (Button) findViewById(R.id.verification_button);
        verifiyNumberButton = (Button) findViewById(R.id.verifiy_number_button);
        verficationCode = (EditText) findViewById(R.id.verification_code);
        userNumber = (EditText) findViewById(R.id.user_number);
        progressDialog = new ProgressDialog(this);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(NumberLoginActivity.this, "Congratulations, you're logged in Successfully.", Toast.LENGTH_SHORT).show();
                            SendUserToMainPage();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(NumberLoginActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    private void SendUserToMainPage() {
        Intent mainIntent = new Intent(NumberLoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void showEnter() {
        Toast.makeText(NumberLoginActivity.this, "Please enter your phone number first...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showProgress() {
        progressDialog.setTitle("Phone Verification");
        progressDialog.setMessage("Please wait, while we are authenticating using your phone...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    @Override
    public void sendNumber() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, NumberLoginActivity.this, callbacks);
    }
    @Override
    public void showEnterCode() {
        Toast.makeText(NumberLoginActivity.this, "Please write verification code first...", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void showDialogForCode() {
        progressDialog.setTitle("Verification Code");
        progressDialog.setMessage("Please wait, while we are verifying verification code...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    @Override
    public void dismiss() {
        progressDialog.dismiss();
    }
}

