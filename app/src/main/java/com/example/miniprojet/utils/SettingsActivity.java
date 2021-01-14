package com.example.miniprojet.utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.miniprojet.R;
import com.example.miniprojet.activities.MainActivity;
import com.example.miniprojet.models.Employees;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    // Firebase
    private FirebaseDatabase myDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;

    // Layouts
    private AlertDialog alertDialog;
    private EditText nameField, emaiLField, newPasswordField, confirmPasswordField;
    private Button changePasswordBtn;
    private ProgressBar progressBar;
    private TextView settingsInfo;

    // vars
    private final String SETTINGS_INFORMATION = "Change your password and take other actions to add more security to your account";
    private String employeeName, employeeEmail;
    private String newPassword, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Action bar
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // init firebase & layouts
        initFirebase();
        initLayout();

        myRef.child("Employees/" + mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Employees employee = dataSnapshot.getValue(Employees.class);
                assert employee != null;
                employeeName = employee.getPrenom() + " " + employee.getNom().toUpperCase();
                employeeEmail = employee.getEmail();
                nameField.setText(employeeName);
                emaiLField.setText(employeeEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // On button click
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPassword = newPasswordField.getText().toString();
                confirmPassword = confirmPasswordField.getText().toString();

                if (TextUtils.isEmpty(newPassword)) {
                    displayAlertDialog(getString(R.string.empty_password));
                    return;
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    displayAlertDialog(getString(R.string.empty_confirm_password));
                    return;
                } else if (newPassword.length() < 6) {
                    displayAlertDialog(getString(R.string.short_password));
                    return;
                } else if (!newPassword.equals(confirmPassword)) {
                    displayAlertDialog(getString(R.string.invalid_password));
                    return;
                }

                changePasswordBtn.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                current_user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        changePasswordBtn.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            displayAlertDialog(getString(R.string.change_password_successful));
                            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                            finish();
                        } else {
                            displayAlertDialog(getString(R.string.change_password_error));
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
        current_user = mAuth.getCurrentUser();
    }

    // init layouts
    private void initLayout() {
        nameField = findViewById(R.id.stgNameEdit);
        emaiLField = findViewById(R.id.stgEmailEdit);
        settingsInfo = findViewById(R.id.settingsInfos);
        progressBar = findViewById(R.id.stgProgressBar);
        newPasswordField = findViewById(R.id.newPasswordField);
        confirmPasswordField = findViewById(R.id.confirmNewPasswordField);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);

        nameField.setEnabled(false);
        emaiLField.setEnabled(false);
        settingsInfo.setText(SETTINGS_INFORMATION);

        progressBar.setVisibility(View.GONE);
    }

    private void displayAlertDialog(String string) {
        alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
        alertDialog.setTitle("Password change");
        alertDialog.setMessage(string);
        alertDialog.show();
    }
}