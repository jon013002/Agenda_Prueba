package com.example.agenda;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class Login extends AppCompatActivity {

    EditText correoLogin, passLogin;
    Button btn_logueo, btn_google;
    TextView usuarioNuevoTXT;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth, auth;
    FirebaseDatabase database;

    //Validar datos
    String correo = "", password = "";

    //Google
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correoLogin = findViewById(R.id.correoLogin);
        passLogin = findViewById(R.id.passLogin);
        btn_logueo = findViewById(R.id.btn_logueo);
        usuarioNuevoTXT = findViewById(R.id.usuarioNuevoTXT);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        // Boton de google
        btn_google = findViewById(R.id.btn_google);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Creando cuenta");
        progressDialog.setMessage("Estamos creando su cuenta");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Boton google onClickListener
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticacionGoogle();
            }
        });


        btn_logueo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

        usuarioNuevoTXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });

    }

    // Google
    int RC_SIGN_IN = 40;

    private void autenticacionGoogle() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount>task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuth(account.getIdToken());

            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();

                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setName(user.getDisplayName());
                            users.setProfile(user.getPhotoUrl().toString());

                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent intent = new Intent(Login.this, MenuPrincipal.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(Login.this, "Error al loguear tu cuenta", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    // Aqui termina Google

    private void validarDatos() {

        correo = correoLogin.getText().toString();
        password = passLogin.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toast.makeText(this, "Correo inválido", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }else{
            loginDeUsuario();
        }
    }

    private void loginDeUsuario() {
        progressDialog.setMessage("Iniciando Sesión...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,password)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(Login.this, MenuPrincipal.class));
                            Toast.makeText(Login.this, "Bienvenido(a): " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, "Verifique si el correo y contraseña con correctos", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Login.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}