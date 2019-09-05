package com.example.karim.muzzafapp.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karim.muzzafapp.MainActivity;
import com.example.karim.muzzafapp.Model.AppLockUser;
import com.example.karim.muzzafapp.Model.User;
import com.example.karim.muzzafapp.R;
import com.example.karim.muzzafapp.shared;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    View registerLayout,loginLayout;
    TextView nameText,emailTxt,passwordText,loginPassword,loginEmail;
    ProgressBar pb;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("AppLockUser");


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallsBack;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcalls;
    private FirebaseAuth mAuth;

    public void Close(View view){
        finish();
    }
    private void __init__(){
        mAuth=FirebaseAuth.getInstance();
        registerLayout=findViewById(R.id.register_layout);
        loginLayout=findViewById(R.id.login_layout);
        nameText=findViewById(R.id.nameText);
        emailTxt=findViewById(R.id.emailTxt);
        passwordText=findViewById(R.id.passordText);
        pb=findViewById(R.id.pb);
        loginPassword=findViewById(R.id.loginPassword);
        loginEmail=findViewById(R.id.loginEmail);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w=getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_register);
        __init__();
    }
    public void viewRegisterClicked(View v){
        loginLayout.setVisibility(View.GONE);
        registerLayout.setVisibility(View.VISIBLE);
    }
    public void viewLoginClicked(View v){
        loginLayout.setVisibility(View.VISIBLE);
        registerLayout.setVisibility(View.GONE);
    }
    public void Registerfunc(View v){
        pb.setVisibility(View.VISIBLE);
        nameText.setEnabled(false);
        passwordText.setEnabled(false);
        emailTxt.setEnabled(false);
        validation();
    }
    private void validation(){
        if(nameText.getText()==null){
            nameText.setError("please enter your name");
        }else if(emailTxt.getText()==null){
            emailTxt.setText("please enter your Email");
        }else if(passwordText.getText()==null){
            passwordText.setError("please enter your password");
        }else{
            shared.Email=emailTxt.getText().toString();
            pushData();
        }
    }
    private void pushData(){
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=(emailTxt.getText().toString());
                String []split=name.split("@");
                if(dataSnapshot.hasChild(split[0])){
                    Toast.makeText(Register.this, "this email has beeen registred before", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(emailTxt.getText().toString(), passwordText.getText().toString())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Error Email", "createUserWithEmail:success");
                                     //   FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Error Email", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(Register.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateUI() {
        AppLockUser user=new AppLockUser();
        user.setName(nameText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        user.setEmail(emailTxt.getText().toString());
        mReference.child(user.getEmail()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
                startActivity(new Intent(Register.this,MainActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage());
            }
        });
    }

    public void login(View v) {
        mAuth.signInWithEmailAndPassword(loginEmail.getText().toString(), loginPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("loginEmail", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(Register.this,MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("loginEmail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
