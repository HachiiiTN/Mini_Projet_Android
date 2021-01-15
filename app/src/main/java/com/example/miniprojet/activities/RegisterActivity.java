package com.example.miniprojet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.models.Employees;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // layouts
    private AlertDialog alertDialog;
    private EditText nomField, prenomField, emailField, passwordField, confirmPasswordField;
    private Button registerBtn;
    private TextView login;

    // vars
    private String nom, prenom, email, pwd, confPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // init firebase & layouts
        initFirebase();
        initLayouts();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nom = nomField.getText().toString();
                prenom = prenomField.getText().toString();
                email = emailField.getText().toString();
                pwd = passwordField.getText().toString();
                confPwd = confirmPasswordField.getText().toString();

                if (TextUtils.isEmpty(nom)) {
                    displayAlertDialog(getString(R.string.empty_nom));
                    return;
                } else if (TextUtils.isEmpty(prenom)) {
                    displayAlertDialog(getString(R.string.empty_prenom));
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    displayAlertDialog(getString(R.string.empty_email));
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    displayAlertDialog(getString(R.string.empty_password));
                    return;
                } else if (TextUtils.isEmpty(confPwd)) {
                    displayAlertDialog(getString(R.string.empty_confirm_password));
                    return;
                } else if (pwd.length() < 6) {
                    displayAlertDialog(getString(R.string.short_password));
                    return;
                } else if (!pwd.equals(confPwd)) {
                    displayAlertDialog(getString(R.string.invalid_password));
                    return;
                }

                registerBtn.setClickable(false);

                mAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(RegisterActivity.this, " " + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                if (!task.isSuccessful()) {
                                    registerBtn.setClickable(true);
                                    Toast.makeText(RegisterActivity.this, "Error ! " + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Employees employee = new Employees(mAuth.getUid(), true, nom.toUpperCase(), prenom, email);
                                    myRef = myRef.child("Employees").child(mAuth.getUid());
                                    myRef.setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(RegisterActivity.this, "You have successfully signed up", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            }
                        });
            }
        });
    }

    // init firebase
    private void initFirebase() {
        myDatabase = FirebaseDatabase.getInstance();
        myRef = myDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // get Firebase auth instance
        if (currentUser != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }

    private void initLayouts() {
        nomField = findViewById(R.id.rgNomEdit);
        prenomField = findViewById(R.id.rgPrenomEdit);
        emailField = findViewById(R.id.rgEmailEdit);
        passwordField = findViewById(R.id.rgPasswordEdit);
        confirmPasswordField = findViewById(R.id.rgConfirmPasswordEdit);
        registerBtn = findViewById(R.id.rgRegisterBtn);
        login = findViewById(R.id.returnLogin);
    }

    private void displayAlertDialog(String string) {
        alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
        alertDialog.setTitle("Create new account");
        alertDialog.setMessage(string);
        alertDialog.show();
    }
}