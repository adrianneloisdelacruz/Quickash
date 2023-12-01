package com.example.quickash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {
    TextInputLayout txtFirstname,txtEmail,txtPassword, txtConPass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebabe;
    ProgressBar progressBar;
    public CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        txtFirstname = findViewById(R.id.txtname);
        txtEmail = findViewById(R.id.txtemail);
        txtPassword = findViewById(R.id.txtpassword);
        txtConPass = findViewById(R.id.txtconfirmpassword);
        progressBar = findViewById(R.id.progressBar2);

        mAuth = FirebaseAuth.getInstance();

        firebabe = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Intent intent = new Intent(RegisterUser.this, Login.class);
                startActivity(intent);
                finish();
                return;
            }
        };
    }

    private boolean validateName() {
        String name = txtFirstname.getEditText().getText().toString().trim();

        if (name.isEmpty()) {
            txtFirstname.setError("Field can't be empty");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnRegister), "Field can't be empty", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            txtFirstname.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String number = txtEmail.getEditText().getText().toString().trim();

        if (number.isEmpty()) {
            txtEmail.setError("Field can't be empty");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnRegister),"Field can't be empty", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        }
        else {
            txtEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = txtPassword.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            txtPassword.setError("Field can't be empty");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnRegister), "Field can't be empty", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else if (txtPassword.getEditText().length() < 8) {
            txtPassword.setError("Password too weak");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnRegister), "Password too weak", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        } else {
            txtPassword.setError(null);
            return true;
        }
    }


    private boolean validateConfirmPassword() {
        String confirmpassword = txtConPass.getEditText().getText().toString().trim();
        String password = txtPassword.getEditText().getText().toString().trim();

        if (confirmpassword.isEmpty()) {
            txtConPass.setError("Field can't be empty");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnRegister),"Field can't be empty", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        } else if (password.isEmpty()) {
            txtPassword.setError("Field can't be empty");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnRegister), "Field can't be empty", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        } else if (!confirmpassword.equals(password)) {
            txtConPass.setError("Password does not match");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.btnRegister), "Password does not match", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        } else {
            txtPassword.equals(txtConPass);
            {
                txtConPass.setError(null);
                return true;
            }
        }
    }

    public void confirmInput(View view) {

        if (!validateEmail() | !validateName() | !validateConfirmPassword() | !validatePassword()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String confirmpassword = txtConPass.getEditText().getText().toString();
        String email = txtEmail.getEditText().getText().toString();

        mAuth.createUserWithEmailAndPassword(email,confirmpassword).addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Register Unsuccessful, Please try again", Toast.LENGTH_LONG).show();
                }else{
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Register Successful, Please check your email for verification", Toast.LENGTH_LONG).show();
                                timer();
                                countDownTimer.start();
                                progressBar.setVisibility(View.VISIBLE);
                                String user_id = mAuth.getCurrentUser().getUid();
                                String name = txtFirstname.getEditText().getText().toString();
                                //String password = _txtpassword.getEditText().getText().toString();
                                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
                                String email = txtEmail.getEditText().getText().toString();
                                String pw = txtConPass.getEditText().getText().toString();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                                //DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Registered").child(user_id);
                                Map newPost = new HashMap();
                                newPost.put("name", name);
                                newPost.put("email", email);
                                newPost.put("password", pw);
                                newPost.put("user_uid", user_id);
                                databaseReference.setValue(newPost);
                                //databaseReference1.setValue(name);
                            }else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void timer() {
        countDownTimer = new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                progressBar.setVisibility(View.INVISIBLE);
                finish();
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_LONG).show();
            }
        };
    }
}