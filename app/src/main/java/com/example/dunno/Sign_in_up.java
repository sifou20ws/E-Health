package com.example.dunno;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dunno.Prevelent.Prevelent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class Sign_in_up extends AppCompatActivity {
    Button signInDoc , signInPat , signUpDoc , signUpPat;
    ProgressDialog loadingBar;
    void id(){
        signInDoc=findViewById(R.id.doc_logIn_first);
        signInPat=findViewById(R.id.pat_logIn_first);
        signUpDoc=findViewById(R.id.doc_signUp_first);
        signUpPat=findViewById(R.id.pat_signUp_first);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_up);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        id();

        String UserEmailKey=Paper.book().read(Prevelent.userEmailKey);
        String UserPasswordKey=Paper.book().read(Prevelent.userPasswordKey);

        if (UserEmailKey!="" && UserPasswordKey!=""){
            if (!TextUtils.isEmpty(UserEmailKey) && !TextUtils.isEmpty(UserPasswordKey)){
                AllowAccess(UserEmailKey,UserPasswordKey);

                loadingBar.setTitle("Already logged in");
                loadingBar.setMessage("please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

        signInDoc.setOnClickListener(v -> {
            Intent i = new Intent(Sign_in_up.this, Login_doctor.class);
            startActivity(i);
        });
        signInPat.setOnClickListener(v -> {
            Intent ii = new Intent(Sign_in_up.this, login_patient.class);
            startActivity(ii);
        });
        signUpDoc.setOnClickListener(v -> {
            Intent iii = new Intent(Sign_in_up.this, SignUp_doctor.class);
            startActivity(iii);
        });
        signUpPat.setOnClickListener(v -> {
            Intent iiii = new Intent(Sign_in_up.this, SignUp_patient.class);
            startActivity(iiii);
        });

    }

    private void AllowAccess(String email_ , String password_){
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
                            Toast.makeText(Sign_in_up.this , "logeed in successfully ..." , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent i = new Intent(Sign_in_up.this, Activity1.class);
                            i.putExtra("patient_Email",email_);
                            startActivity(i);
                        }else{
                            Toast.makeText(Sign_in_up.this , "Wrong password" , Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }else{
                    Toast.makeText(Sign_in_up.this , "account dsnt exict" , Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

