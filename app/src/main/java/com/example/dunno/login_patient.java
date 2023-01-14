package com.example.dunno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dunno.Model.patient;
import com.example.dunno.Prevelent.Prevelent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_patient extends AppCompatActivity {
    EditText InpEmail , InpPassword;
    Button login , signup;
    ProgressDialog loadingBar;
    CheckBox rememberMeBoxPat ;

    String parentDBname="Patient";
    private String TAG = "login_patient";
    void id(){
        InpEmail=findViewById(R.id.pat_login_email);
        InpPassword=findViewById(R.id.pat_login_password);
        login=findViewById(R.id.pat_login);
        signup=findViewById(R.id.pat_sign_up_link);
        loadingBar = new ProgressDialog(this);
        rememberMeBoxPat=findViewById(R.id.remember_me_box_pat);
        Paper.init(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_patient);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id();

        login.setOnClickListener(v -> LoginUser());

        signup.setOnClickListener(v -> {
            Intent i = new Intent(login_patient.this, SignUp_patient.class);
            startActivity(i);
        });
    }

    private void LoginUser(){
        String email = InpEmail.getText().toString();
        String password = InpPassword.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"fill the fields", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("create account");
            loadingBar.setMessage("please wait!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(email,password);
        }
    }
    private void AllowAccessToAccount(String email_ , String password_){
        if (rememberMeBoxPat.isChecked()){
            Paper.book().write(Prevelent.userEmailKey,email_ );
            Paper.book().write(Prevelent.userPasswordKey,password_ );

        }

        final DatabaseReference RootReff;
        RootReff = FirebaseDatabase.getInstance().getReference().child("Patient");

        RootReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(email_).exists()){
                    String name = snapshot.child(email_).child("Name").getValue().toString();
                    String email = snapshot.child(email_).child("Email").getValue().toString();
                    String pass = snapshot.child(email_).child("Password").getValue().toString();

                    //Doctor DoctorData = snapshot.child("Doctor").child(email__).getValue(Doctor.class);;

                    if(email_.equals(email)){
                        if (password_.equals(pass)){
                            Toast.makeText(login_patient.this , "logeed in successfully ..." , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i = new Intent(login_patient.this, Activity1.class);
                            i.putExtra("patient_Email",email_);
                            startActivity(i);
                        }else{
                            Toast.makeText(login_patient.this , "Wrong password" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }else{
                    Toast.makeText(login_patient.this , "account dsnt exict" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}