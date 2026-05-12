package com.demo.farmfresh25.Authentification;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.demo.farmfresh25.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    TextInputEditText edtName, edtEmail, edtPassword, edtConfirmPassword;
    MaterialButton btnRegister;
    TextView txtLogin;

    CheckBox checkBox;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


//        Initialize Firebase Auth
                mAuth = FirebaseAuth.getInstance();

        // Connect XML views
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        checkBox = findViewById(R.id.checkBox);

        btnRegister = findViewById(R.id.btnRegister);
        txtLogin = findViewById(R.id.txtLogin);

        // Register Button Click
        btnRegister.setOnClickListener(v -> {


                    String name = edtName.getText().toString().trim();
                    String email = edtEmail.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    String confirmPassword = edtConfirmPassword.getText().toString().trim();
                    boolean isChecked = checkBox.isChecked();


                    if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

                        Toast.makeText(Register.this,
                                "Please fill all fields", Toast.LENGTH_SHORT).show();

                    } else if (!password.equals(confirmPassword)) {

                        Toast.makeText(Register.this,
                                "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else {

                       if(!isChecked){
                           Toast.makeText(this, "please accept the terms and conditions", Toast.LENGTH_SHORT).show();
                           return;
                       }


                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithCustomToken:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            String uid = user.getUid();

                                            saveUserData(uid, name ,confirmPassword);
//                                    updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                                            Toast.makeText(Register.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                        }

//                Toast.makeText(Register.this,"Registration Successful", Toast.LENGTH_SHORT).show();
                                        // Go to Login Screen
//                Intent intent = new Intent(Register.this, Login.class);
//                startActivity(intent);
//                finish();
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });



        // Go to Login Page
        txtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
}

public void policyTerms(View v){

    showPolicyDialog();


}
    private void saveUserData(String uid, String name, String cPass){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("cpass", cPass);

        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(unused -> {
                    SendmailVerification();
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SendmailVerification(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null && !user.isEmailVerified()){
            user.sendEmailVerification()
                    .addOnCompleteListener(task ->  {
                        if (task.isSuccessful()){

                            Toast.makeText(Register.this, "Verification email sent to", Toast.LENGTH_SHORT).show();
                        } else{

                            Toast.makeText(Register.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }



    private void showPolicyDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_policy, null);

        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // user must accept
        dialog.show();


        Button btnAccept = view.findViewById(R.id.btnAccept);

        btnAccept.setOnClickListener(v -> {


            Toast.makeText(this, "accept the policy", Toast.LENGTH_SHORT).show();

            checkBox.setChecked(true);
            dialog.dismiss();
        });



    }


    ClickableSpan clickableSpan = new ClickableSpan(){
        @Override
        public void onClick(@NonNull View widget) {
            Toast.makeText(Register.this, "Terms and Conditions", Toast.LENGTH_SHORT).show();
        }
    };

//
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            if(!checkBox.isChecked()){
//            Toast.makeText(this, "please accept the terms and conditions", Toast.LENGTH_SHORT).show();
//    }

//}



}



