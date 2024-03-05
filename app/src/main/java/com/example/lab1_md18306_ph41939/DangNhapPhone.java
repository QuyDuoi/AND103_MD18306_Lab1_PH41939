package com.example.lab1_md18306_ph41939;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class DangNhapPhone extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    TextInputLayout tilPhone, tilOTP;
    TextInputEditText edtPhone, edtOTP;
    Button btnLayOTP, btnDangNhap;
    String phoneNumber, maOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_phone);
        mAuth = FirebaseAuth.getInstance();
        tilPhone = findViewById(R.id.tilPhoneNumber);
        tilOTP = findViewById(R.id.tilOTP);
        edtPhone = findViewById(R.id.edtPhoneNumber);
        edtOTP = findViewById(R.id.edtOTP);
        btnDangNhap = findViewById(R.id.btnDangNhapOTP);
        btnLayOTP = findViewById(R.id.btnLayOTP);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                edtOTP.setText(credential.getSmsCode());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = s;
            }
        };

        btnLayOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber = edtPhone.getText().toString().trim();
                boolean check = false;
                if (TextUtils.isEmpty(phoneNumber)) {
                    tilPhone.setError("Vui lòng nhập số điện thoại!");
                    check = true;
                } else if (!TextUtils.isDigitsOnly(phoneNumber)) {
                    tilPhone.setError("Số không đúng định dạng!");
                    check = true;
                } else if (phoneNumber.length() != 10) {
                    tilPhone.setError("Số điện thoại phải có 10 chữ số!");
                    check = true;
                } else if (!phoneNumber.startsWith("0")) {
                    tilPhone.setError("Số điện thoại phải bắt đầu bằng số 0!");
                    check = true;
                } else {
                    tilPhone.setError(null);
                }
                if (check) {
                    return;
                }
                getOTP(phoneNumber);
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maOTP = edtOTP.getText().toString().trim();
                if (TextUtils.isEmpty(maOTP)) {
                    tilOTP.setError("Vui lòng nhập mã OTP!");
                    return;
                } else {
                    tilOTP.setError(null);
                }
                verifyOTP(maOTP);
            }
        });
    }
    private void getOTP (String phone) {
        tilOTP.setVisibility(View.VISIBLE);
        btnDangNhap.setVisibility(View.VISIBLE);
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+84" + phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyOTP (String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential (PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(DangNhapPhone.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = task.getResult().getUser();
                            startActivity(new Intent(DangNhapPhone.this, DangXuat.class));
                        } else {
                            Log.w("Main", "Đăng nhập thất bại", task.getException());
                        }
                    }
                });
    }
}