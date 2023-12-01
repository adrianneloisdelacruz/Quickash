package com.example.quickash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private FirebaseAuth.AuthStateListener authStateListener;
    public CountDownTimer countDownTimer;
    String username, pwd;
    private FirebaseAuth mAuth;
    TextInputLayout email1, password1;
    TextView forgotpass, regbtn;
    Button loginbtn;
    ProgressBar pgbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        email1 = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        forgotpass = findViewById(R.id.forgotpass);
        regbtn = findViewById(R.id.btnregister);
        loginbtn = findViewById(R.id.loginbtn);
        pgbar = findViewById(R.id.progressBar);


//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                if (firebaseUser != null){
//                    Intent HomePage = new Intent(Login.this, BankMenu.class);
//                    startActivity(HomePage);
//                    finish();
//                }else{
////                    Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int3 = new Intent(Login.this, RegisterUser.class);
                startActivity(int3);
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int3 = new Intent(Login.this, ForgotPass.class);
                startActivity(int3);
            }
        });
    }

    private boolean validateBankNo () {
        String number = email1.getEditText().getText().toString().trim();

        if (number.isEmpty()) {
            email1.setError("Field can't be empty");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.loginbtn), "Please input your email or Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            email1.setError(null);
            return true;
        }
    }

    private boolean validatePassword () {
        String password = password1.getEditText().getText().toString().trim();

        if (password.isEmpty()) {
            password1.setError("Field can't be empty");
            Snackbar snackbar = Snackbar.make(findViewById(R.id.loginbtn), "Please input your email or Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;
        } else {
            password1.setError(null);
            return true;
        }
    }

    public void confirmInput (View view) {
        if (!validateBankNo() | !validatePassword()) {
            return;
        }
        pgbar.setVisibility(View.VISIBLE);
        final String email = email1.getEditText().getText().toString();
        final String password = password1.getEditText().getText().toString();
        insertdata(password, email);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username=dataSnapshot.child("Admin").child("username").getValue(String.class);
                pwd=dataSnapshot.child("Admin").child("password").getValue(String.class);
                if (email.equals(username)&&password.equals(pwd)) {
//                    timer();
//                    countDownTimer.start();
//                    Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_LONG).show();
//                    pgbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void insertdata(final String password, final String email) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                            timer1();
                            countDownTimer.start();
                            pgbar.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Welcome to Quickash", Toast.LENGTH_LONG).show();
                        }else{
                            pgbar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Please verify your email", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(authStateListener);
//    }

    private void timer() {
        countDownTimer = new CountDownTimer(3000, 1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                pgbar.setVisibility(View.INVISIBLE);
                Intent HomePage = new Intent(Login.this, BankMenu.class);
                startActivity(HomePage);
                finish();
            }
        };
    }
    private void timer1() {
        countDownTimer = new CountDownTimer(3000, 1000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent HomePage = new Intent(Login.this, BankMenu.class);
                pgbar.setVisibility(View.INVISIBLE);
                startActivity(HomePage);
                finish();
            }
        };
    }
}
