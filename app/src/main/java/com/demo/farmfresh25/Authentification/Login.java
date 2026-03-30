package com.demo.farmfresh25.Authentification;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.farmfresh25.ForgotPassword;
import com.demo.farmfresh25.Home;
import com.demo.farmfresh25.R;

import com.demo.farmfresh25.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {


//    private FirebaseAuth mAuth;
    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtRegister, txtForgot;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//         Initialize Firebase Auth
         mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.txtRegister);
        txtForgot = findViewById(R.id.txtForgot);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String pass = edtPassword.getText().toString();

            if(email.isEmpty() || pass.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            } else {


                mAuth.signInWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCustomToken:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null && user.isEmailVerified()) {
                                        updateUI(user);

                                    } else {
                                        Toast.makeText(Login.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                                    Toast.makeText(Login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });


                Toast.makeText(this, "Login clicked", Toast.LENGTH_SHORT).show();
                // TODO: connect to API / Firebase / backend
            }
        });
//NumberEmailSent = numberEmailsent +1
//        if(user != null && !user.isEmailVerified()and NumberEmailSent < 2{
        txtRegister.setOnClickListener(v ->
                startActivity(new Intent(this, Register.class)));

//        txtForgot.setOnClickListener(v ->
//                Toast.makeText(this, "Forgot password", Toast.LENGTH_SHORT).show());
    }




    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.isEmailVerified()) {
            updateUI(currentUser);
        }else {

            if (currentUser != null) {
                if (!currentUser.isEmailVerified()) {
                    Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateUI(FirebaseUser currentUser) {

        Toast.makeText(this, "Login sucessfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();

    }



    public void ForgotPass(View view) {

        Intent intent = new Intent(this, ForgotPassword.class);
        startActivity(intent);
        finish();
    }

}