package com.example.agenda;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import java.util.concurrent.Executor;

public class Autenticar_Huella extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticar_huella);

        TextView status = findViewById(R.id.estadoAutenticar);
        Button btn = findViewById(R.id.btnAutenticacion);

        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(Autenticar_Huella.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                status.setText("ERROR DE AUTENTICACIÓN");
                Toast.makeText(Autenticar_Huella.this, "ERROR DE AUTENTICACIÓN: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                status.setText("AUTENTICACIÓN SATISFACTORIA");
                Toast.makeText(Autenticar_Huella.this, "AUTENTICACIÓN SATISFACTORIA", Toast.LENGTH_SHORT).show();

                // Redirigir a MainActivity2
                Intent intent = new Intent(Autenticar_Huella.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                status.setText("FALLO AUTENTICACIÓN");
                Toast.makeText(Autenticar_Huella.this, "FALLO AUTENTICACIÓN", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación de Biometrico")
                .setSubtitle("Se usará la autenticación para guardar")
                .setNegativeButtonText("Usa tu contraseña")
                .build();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }
}