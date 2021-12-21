package com.app.clonedzoom;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyOTPActivity extends BaseActivity  extends AppCompatActivity {

    private EditText edtInputCode1, edtInputCode2, edtInputCode3, edtInputCode4, edtInputCode5, edtInputCode6;
    private String verificationID;
    private TextView timerOTP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);


        edtInputCode1 = findViewById(R.id.edtInputCode1);
        edtInputCode2 = findViewById(R.id.edtInputCode2);
        edtInputCode3 = findViewById(R.id.edtInputCode3);
        edtInputCode4 = findViewById(R.id.edtInputCode4);
        edtInputCode5 = findViewById(R.id.edtInputCode5);
        edtInputCode6 = findViewById(R.id.edtInputCode6);

        TextView textViewCancel = findViewById(R.id.textViewCancel);

        setupOTPInputs();

        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        timerOTP =  findViewById(R.id.timerOTP);

        verificationID= getIntent().getStringExtra("verificationID");

        textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VerifyOTPActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtInputCode1.getText().toString().trim().isEmpty()
                ||edtInputCode2.getText().toString().trim().isEmpty()
                ||edtInputCode3.getText().toString().trim().isEmpty()
                ||edtInputCode4.getText().toString().trim().isEmpty()
                ||edtInputCode5.getText().toString().trim().isEmpty()
                ||edtInputCode6.getText().toString().trim().isEmpty()){
                    Toast.makeText(VerifyOTPActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    return;

                }
                String code = edtInputCode1.getText().toString()+
                  edtInputCode2.getText().toString()+
                  edtInputCode3.getText().toString()+
                  edtInputCode4.getText().toString()+
                  edtInputCode5.getText().toString()+
                  edtInputCode6.getText().toString();

                if(verificationID!=null){
                    progressBar.setVisibility(View.VISIBLE);
                    btnVerifyOTP.setVisibility(View.INVISIBLE);

                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationID, code);

                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            btnVerifyOTP.setVisibility(View.VISIBLE);

                            if(task.isSuccessful()){
                                 Intent intent = new Intent(VerifyOTPActivity.this, HomeActivity.class);
                                 startActivity(intent);
                                 finish();
                                 overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                            else {
                                Toast.makeText(VerifyOTPActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });





        findViewById(R.id.resendOTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("mobile"),
                        60,
                        TimeUnit.SECONDS,
                        VerifyOTPActivity.this,

                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onCodeSent(@NonNull String newverificationID, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                verificationID = newverificationID;
                                Toast.makeText(VerifyOTPActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                            }
                        }
                );



            }
        });

        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {

                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d", TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));

                timerOTP.setText(sDuration);


            }

            @Override
            public void onFinish() {
                timerOTP.setVisibility(View.GONE);

            }
        }.start();

    }

    private void setupOTPInputs(){
        edtInputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    edtInputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtInputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    edtInputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtInputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    edtInputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtInputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    edtInputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edtInputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().trim().isEmpty()){
                    edtInputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
