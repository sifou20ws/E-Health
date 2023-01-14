package com.example.dunno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class SignUp_patient extends AppCompatActivity {
    EditText InpName , InpEmail , InpPassword , InpDoctor;
    Button createAccountButton ;
    ProgressDialog loadingBar;

    void id(){
        InpEmail=findViewById(R.id.pat_signUp_email);
        InpPassword=findViewById(R.id.pat_signUp_password);
        InpName=findViewById(R.id.pat_signUp_name);
        InpDoctor=findViewById(R.id.pat_doctor);
        createAccountButton=findViewById(R.id.pat_signUp_create_account);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id();

        createAccountButton.setOnClickListener(v -> createAccount());

    }

    public void createAccount(){
        String name = InpName.getText().toString();
        String email = InpEmail.getText().toString();
        String password = InpPassword.getText().toString();
        String doctor = InpDoctor.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this,"fill the fields", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("create account");
            loadingBar.setMessage("please wait!");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            CheckDocName(name,email,password,doctor);
        }
    }

    void CheckDocName(String patName , String patEmail , String patPassword , String docEmail){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Doctor");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(docEmail).exists()){
                    HashMap<String,Object> userdataMap = new HashMap<>();

                    RootRef.child(docEmail).updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            addPatientToDoc( patName ,  patEmail ,  patPassword ,  docEmail);
                        }else{
                            Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(SignUp_patient.this , "this doctor ''" + docEmail +"''does not exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void addPatientToDoc(String patName , String patEmail , String patPassword , String docEmail){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Doctor").child(docEmail).child("Patients").child(patEmail);

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                HashMap<String,Object> userdataMap = new HashMap<>();
                userdataMap.put("Email",patEmail);
                userdataMap.put("Name",patName);

                RootRef.updateChildren(userdataMap).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        ValidateEmail( patName ,  patEmail ,  patPassword ,  docEmail);

                    }else{
                        Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void ValidateEmail(String patName , String patEmail , String patPassword, String docEmail){
        final DatabaseReference RootReff ;
        RootReff = FirebaseDatabase.getInstance().getReference();

        RootReff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Patient").child(patEmail).exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Email",patEmail);
                    userdataMap.put("Password",patPassword);
                    userdataMap.put("Name",patName);
                    userdataMap.put("Your doctor",docEmail);

                    RootReff.child("Patient").child(patEmail).updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            addMeasurementChild( patName ,  patEmail ,  patPassword ,  docEmail);
                            //Toast.makeText(SignUp_patient.this , "account created" , Toast.LENGTH_SHORT).show();
                            //loadingBar.dismiss();
                        }else{
                            Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });
                }else {
                    Toast.makeText(SignUp_patient.this , "this"+patEmail+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void addMeasurementChild(String name , String email , String password , String docEmail){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Patient").child(email).child("Measurement").exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("1st Measurement","Measurement");

                    RootRef.child("Patient").child(email).child("Measurement").updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            addTemperatureMeasurementChild( name ,  email ,  password , docEmail);
                            //Toast.makeText(SignUpPatient.this , "account created" , Toast.LENGTH_SHORT).show();
                            //loadingBar.dismiss();
                        }else{
                            Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    });
                }else {
                    Toast.makeText(SignUp_patient.this , "this"+email+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void addTemperatureMeasurementChild(String name , String email , String password , String docEmail){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(email).child("Measurement");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Temperature").exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Temperature 1","Temperature");

                    RootRef.child("Temperature").updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            addHeartBeatMeasurementChild( name ,  email ,  password , docEmail);
                            //Toast.makeText(SignUpPatient.this , "account created" , Toast.LENGTH_SHORT).show();
                            //loadingBar.dismiss();

                        }else{
                            Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    });
                }else {
                    Toast.makeText(SignUp_patient.this , "this"+email+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void addHeartBeatMeasurementChild(String name , String email , String password , String docEmail){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(email).child("Measurement");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Heart_Beat").exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Hear beat 1","Heart Beat");

                    RootRef.child("Heart_Beat").updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            addPressureMeasurementChild( name ,  email ,  password , docEmail);
                            //Toast.makeText(SignUpPatient.this , "account created" , Toast.LENGTH_SHORT).show();
                            //loadingBar.dismiss();

                        }else{
                            Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    });
                }else {
                    Toast.makeText(SignUp_patient.this , "this"+email+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void addPressureMeasurementChild(String name , String email , String password , String docEmail){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(email).child("Measurement");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Pressure").exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Pressure 1","Pressure");

                    RootRef.child("Pressure").updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            addGlucoseMeasurementChild( name ,  email ,  password , docEmail);
                            //Toast.makeText(SignUpPatient.this , "account created" , Toast.LENGTH_SHORT).show();
                            //loadingBar.dismiss();

                        }else{
                            Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    });
                }else {
                    Toast.makeText(SignUp_patient.this , "this"+email+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void addGlucoseMeasurementChild(String name , String email , String password , String docEmail){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Patient").child(email).child("Measurement");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Glucose").exists())){

                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put("Glucose 1","Glucose");

                    RootRef.child("Glucose").updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            //addPressureMeasurementChild( name ,  email ,  password , docEmail);
                            Toast.makeText(SignUp_patient.this , "account created" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i = new Intent(SignUp_patient.this, login_patient.class);
                            startActivity(i);

                        }else{
                            Toast.makeText(SignUp_patient.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    });
                }else {
                    Toast.makeText(SignUp_patient.this , "this"+email+" already exist" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}