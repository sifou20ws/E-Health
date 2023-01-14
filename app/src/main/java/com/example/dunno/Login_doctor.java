package com.example.dunno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dunno.Model.Doctor;
import com.example.dunno.Prevelent.Prevelent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_doctor extends AppCompatActivity {
    EditText email , password;
    Button login  , signup;
    TextView dontHaveAccount;
    ProgressDialog loadingBar;
    CheckBox rememberMeBox ;

    void id(){
        email=findViewById(R.id.doc_login_email);
        password=findViewById(R.id.doc_login_password);
        login=findViewById(R.id.doc_login);
        signup=findViewById(R.id.doctor_signUp_link);
        loadingBar = new ProgressDialog(this);
        rememberMeBox=findViewById(R.id.remember_me_box);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_doctor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_doctor.this, SignUp_doctor.class);
                startActivity(i);
            }
        });
    }

    public void LoginUser(){
        String email_ = email.getText().toString();
        String password_ = password.getText().toString();
        if (TextUtils.isEmpty(email_) || TextUtils.isEmpty(password_)){
            Toast.makeText(this,"fill the fields", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("create account");
            loadingBar.setMessage("please wait!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(email_ , password_);
        }
    }

    private void AllowAccessToAccount(String email__, String password__){


        final DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.child(email__).exists()){
                    Toast.makeText(Login_doctor.this , "account dsnt exict" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }else{
                    String name = snapshot.child(email__).child("Name").getValue().toString();
                    String email = snapshot.child(email__).child("Email").getValue().toString();
                    String pass = snapshot.child(email__).child("Password").getValue().toString();

                    //Doctor DoctorData = snapshot.child("Doctor").child(email__).getValue(Doctor.class);;

                    if(email.equals(email__)){
                        if (pass.equals(password__)){
                            Toast.makeText(Login_doctor.this , "logeed in successfully ..." , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i = new Intent(Login_doctor.this, Doctor_Dashboared.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(Login_doctor.this , "Wrong password" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}