package com.example.quickash;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenuBank extends AppCompatActivity {
    private TextView tv1, tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_bank);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv1.startAnimation(myanim);
        tv2.startAnimation(myanim);
        final Intent i =new Intent(this,Login.class);
        Thread timer =new Thread(){
            public void run () {
                try {
                    sleep(5000) ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
