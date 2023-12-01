package com.example.quickash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Buttonadd extends AppCompatActivity {
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttonadd);
        close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Buttonadd.this, BankMenu.class);
                startActivity(intent1);
            }
        });
    }

    public void  buttonDepositClick(View view){
        EditText editTextDepositAmount = (EditText)findViewById(R.id.amount);
        int depositAmount = 0;

        try {
            depositAmount = Integer.parseInt(editTextDepositAmount.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Please enter a valid amount. ", Toast.LENGTH_LONG).show();
            return;
        }
            Intent intent = getIntent();
            intent.putExtra("amount", depositAmount);
            setResult(RESULT_OK, intent);
            finish();
    }
}

