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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_forgot_password);

                        }

    public void resetLink(View view) {

        try {
            TextInputEditText emailpasswod = findViewById(R.id.emailpasswodedit);
            String email = emailpasswod.getText().toString();
            FirebaseAuth auth = FirebaseAuth.getInstance();

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, " Password reset email sent to "+email, Toast.LENGTH_SHORT).show();
                        }else  {
                            Toast.makeText(this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }

        });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }



}