package com.example.dunno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import io.paperdb.Paper;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dunno.R;
import com.google.android.material.navigation.NavigationView;

public class Activity1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DeviceInfoModel device ;


    Button hm , hs , sr, htu;
    Animation scaleUp , scaleDown;
    private DrawerLayout drawer;

    public static String EXTRA_ADDRESS = "deviceAddress";
    public String address = null , patient_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_1);

        address  = getIntent().getStringExtra("device");
        //Toast.makeText(this,address,Toast.LENGTH_SHORT).show();

        patient_email  = getIntent().getStringExtra("patient_Email");


        id();
        btnOnClick ();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void id(){
        hm = findViewById(R.id.hm);
        hs = findViewById(R.id.hs);
        sr = findViewById(R.id.sr);
        htu = findViewById(R.id.htu);
        scaleUp = AnimationUtils.loadAnimation(this ,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this ,R.anim.scale_down);
        Paper.init(this);

    }

    @SuppressLint("ClickableViewAccessibility")
    public void btnOnClick (){
        hm.setOnClickListener(v -> {
            if(address == null || address.equals("null")){
                Toast.makeText(Activity1.this,"connect your device",Toast.LENGTH_SHORT).show();
            }else{
                Intent i = new Intent(Activity1.this, health_monitoring.class);
                i.putExtra("device" , String.valueOf(address));
                i.putExtra("patient_Email" , String.valueOf(patient_email));
                startActivity(i);
            }
        });

        hs.setOnClickListener(v -> {
                hs.startAnimation(scaleDown);
                Intent i = new Intent(Activity1.this , Patient_history.class);
                startActivity(i);
        });

        sr.setOnClickListener(v -> {
            Intent i = new Intent(Activity1.this , Patient_send_report.class);
            startActivity(i);
        });

        htu.setOnClickListener(v -> {
            Intent i = new Intent(Activity1.this , Patient_how_to_use.class);
            startActivity(i);
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                Paper.book().destroy();
                Intent i = new Intent(this, Sign_in_up.class);
                startActivity(i);
                break;
            case R.id.nav_setting:
                Intent intent = new Intent(this, setting.class);
                startActivity(intent);
                break;
            case R.id.nav_help:
                Toast.makeText(this,"help",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contact:
                Toast.makeText(this,"contact",Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent p = new Intent(Activity1.this , MainActivity.class);
        p.putExtra("device" ,address );
        startActivity(p);
    }


}