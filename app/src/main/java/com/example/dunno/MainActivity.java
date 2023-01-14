package com.example.dunno;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dunno.R;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    String address ;
    void id (){
        logo = findViewById(R.id.logo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        address  = getIntent().getStringExtra("device");
        //Toast.makeText(this,address,Toast.LENGTH_SHORT).show();

        id ();

        Intent i = new Intent(MainActivity.this, Sign_in_up.class);
        startActivity(i);

        Animation fadeout = new AlphaAnimation(1.00f, 0.00f);
        fadeout.setDuration(2500);
        Animation fading = new AlphaAnimation(0.00f, 1.00f);
        fading.setDuration(3000);

        logo.startAnimation(fading);

        fadeout.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            public void onAnimationEnd(Animation animation) {
                logo.setVisibility(View.GONE);
                Intent i = new Intent(MainActivity.this, Sign_in_up.class);
                i.putExtra("device" , String.valueOf(address));
                startActivity(i);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logo.startAnimation(fadeout);
            }
        });

    }
    int count=0;
    @Override
    public void onBackPressed() {
        count++;
        if(count ==1){
            Toast.makeText(MainActivity.this,"press twice",Toast.LENGTH_SHORT).show();
        }
        if(count ==2)
            super.onBackPressed();
    }
}