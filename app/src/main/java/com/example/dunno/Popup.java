package com.example.dunno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Popup extends AppCompatActivity {
    TextView text , value;
    String arduinoMsg ,patientEmail , arduinoText , arduinoValue;
    String tempurature ="tm";
    String heartBeat ="hb";
    String glucose ="gl";
    String bloodPressure ="bp";
    ImageView temp , bp , gl , hb ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        //text = findViewById(R.id.text);
        value = findViewById(R.id.value);
        temp = findViewById(R.id.temp_image);
        bp = findViewById(R.id.bp_image);
        gl = findViewById(R.id.gl_image);
        hb = findViewById(R.id.hb_image);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.55) , (int)(height*.4));

        arduinoMsg = getIntent().getStringExtra("arduinoMsg");
        arduinoText = getIntent().getStringExtra("arduinoText");
        arduinoValue = getIntent().getStringExtra("arduinoValue");
        patientEmail = getIntent().getStringExtra("patient_Email");

        if (arduinoText.equals(tempurature)){
            bp.setAlpha((float) 0.00001);
            gl.setAlpha((float) 0.00001);
            hb.setAlpha((float) 0.00001);

            //text.setText("your temperature is :");
            arduinoValue = arduinoValue.replaceAll("(\\r|\\n)", " °C");
            value.setText(arduinoValue);
        }else if (arduinoText.equals(heartBeat)){
            bp.setAlpha((float) 0.00001);
            gl.setAlpha((float) 0.00001);
            temp.setAlpha((float) 0.00001);

            //text.setText("your heartBeat is :");
            arduinoValue = arduinoValue.replaceAll("(\\r|\\n)", " Bpm");
            value.setText(arduinoValue);
        }else if (arduinoText.equals(glucose)){
            bp.setAlpha((float) 0.00001);
            temp.setAlpha((float) 0.00001);
            hb.setAlpha((float) 0.00001);
            //text.setText("your glucose is :");
            arduinoValue = arduinoValue.replaceAll("(\\r|\\n)", " mg/l");
            value.setText(arduinoValue);
        }else if (arduinoText.equals(bloodPressure)){
            temp.setAlpha((float) 0.00001);
            gl.setAlpha((float) 0.00001);
            hb.setAlpha((float) 0.00001);
            //text.setText("your bloodPressure is :");
            arduinoValue = arduinoValue.replaceAll("(\\r|\\n)", " bp");
            value.setText(arduinoValue);
        }

    }

}