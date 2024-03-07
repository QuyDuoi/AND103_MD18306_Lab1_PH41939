package com.example.lab1_md18306_ph41939;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DangKy extends AppCompatActivity {
    TextInputEditText edtEmail, edtPass, edtRePass, edtFullName, edtPhone;
    TextInputLayout tilEmail, tilPass, tilRePass, tilFullName, tilPhone;
    Button btnDangKy;
    TextView backDangNhap;
    private FirebaseAuth mAuth;
    String email, pass, rePass, name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        edtRePass = findViewById(R.id.edtRePass);
        edtFullName = findViewById(R.id.edtFullName);
        edtPhone = findViewById(R.id.edtPhone);
        tilEmail = findViewById(R.id.tilEmail);
        tilPass = findViewById(R.id.tilPass);
        tilRePass = findViewById(R.id.tilRePass);
        tilFullName = findViewById(R.id.tilFullName);
        tilPhone = findViewById(R.id.tilPhone);
        btnDangKy = findViewById(R.id.btnDangKy);
        backDangNhap = findViewById(R.id.back_dangNhap);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
        backDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DangKy.this, DangNhap.class));
            }
        });
    }
    private void validateForm() {
        email = edtEmail.getText().toString().trim();
        pass = edtPass.getText().toString().trim();
        rePass = edtRePass.getText().toString().trim();
        name = edtFullName.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();

        boolean check = false;

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Vui lòng nhập Email!");
            check = true;
        } else if (!checkEmail(email)) {
            tilEmail.setError("Email không đúng định dạng!");
            check = true;
        } else {
            tilEmail.setError(null);
        }

        if (TextUtils.isEmpty(pass)) {
            tilPass.setError("Vui lòng nhập mật khẩu!");
            check = true;
        } else if (pass.length() < 6) {
            tilPass.setError("Mật khẩu phải nhiều hơn 6 ký tự!");
            check = true;
        } else if (!checkPass(pass)) {
            tilPass.setError("Mật khẩu không đúng định dạng!");
            check = true;
        } else {
            tilPass.setError(null);
        }

        if (TextUtils.isEmpty(rePass)) {
            tilRePass.setError("Vui lòng nhập lại mật khẩu!");
            check = true;
        } else if (!rePass.equals(pass)) {
            tilRePass.setError("Mật khẩu nhập lại không khớp!");
            check = true;
        } else {
            tilRePass.setError(null);
        }

        if (TextUtils.isEmpty(name)) {
            tilFullName.setError("Vui lòng nhập họ tên!");
            check = true;
        } else {
            tilFullName.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            tilPhone.setError("Vui lòng nhập số điện thoại!");
            check = true;
        } else if (!TextUtils.isDigitsOnly(phone)) {
            tilPhone.setError("Số không đúng định dạng!");
            check = true;
        } else {
            tilPhone.setError(null);
        }

        if (check) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DangKy.this, DangNhap.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", pass);

                    startActivity(intent);
                    finish();
                } else {
                    Log.w("Main", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(DangKy.this, "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkEmail (String email) {
        String check = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(check);
    }
    private boolean checkPass (String password) {
        String check = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$";
        return password.matches(check);
    }
}