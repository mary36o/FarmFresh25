package com.demo.farmfresh25;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


                        }




    public void resetLink(View view) {

            EditText emailpasswod = findViewById(R.id.emailpasswod);
            String email = emailpasswod.getText().toString();
            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, " Password reset email sent", Toast.LENGTH_SHORT).show();
                        }if (task.isSuccessful()) {
                            Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }

        });

    }



}