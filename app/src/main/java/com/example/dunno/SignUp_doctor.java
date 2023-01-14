package com.example.dunno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class SignUp_doctor extends AppCompatActivity {
    EditText InpName, InpPhone , InpEmail , InpPassword;
    Button createAccountButton ;
    ProgressDialog loadingBar;

    void id(){
        InpEmail=findViewById(R.id.doc_signUp_email);
        InpPassword=findViewById(R.id.doc_signUp_password);
        InpName=findViewById(R.id.doc_signUp_name);
        InpPhone=findViewById(R.id.doc_signUp_phone);
        createAccountButton=findViewById(R.id.doc_signUp_create_account);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doctor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id();

        createAccountButton.setOnClickListener(v -> createAccount());

    }

    public void createAccount(){
        String name = InpName.getText().toString();
        String email = InpEmail.getText().toString();
        String password = InpPassword.getText().toString();
        String phone = InpPhone.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(phone)){
            Toast.makeText(this,"fill the fields", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("create account");
            loadingBar.setMessage("please wait!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            ValidateEmail(name,email,password,phone);
        }
    }

    void ValidateEmail(String name_ , String email_ , String password_, String phone_){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Doctor").child(email_).exists())){
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Email",email_);
                    userdataMap.put("Password",password_);
                    userdataMap.put("Name",name_);
                    userdataMap.put("phone",phone_);

                    RootRef.child("Doctor").child(email_).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //Toast.makeText(SignUp_doctor.this , "account created" , Toast.LENGTH_SHORT).show();
                                addPatientChild(email_);
                                loadingBar.dismiss();
                            }else{
                                Toast.makeText(SignUp_doctor.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }else {
                    Toast.makeText(SignUp_doctor.this , "this"+email_+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void addPatientChild(String email){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Doctor").child(email).child("Patients").exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Patient1","patient");

                    RootRef.child("Doctor").child(email).child("Patients").updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(SignUp_doctor.this , "account created" , Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent i = new Intent(SignUp_doctor.this, Login_doctor.class);
                                startActivity(i);

                            }else{
                                Toast.makeText(SignUp_doctor.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }
                    });
                }else {
                    Toast.makeText(SignUp_doctor.this , "this"+email+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}