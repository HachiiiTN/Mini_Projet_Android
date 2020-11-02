package com.example.miniprojet.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miniprojet.R;
import com.example.miniprojet.managers.SessionManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static com.example.miniprojet.managers.DatabaseManager.LOGGED_USER;
import static com.example.miniprojet.managers.DatabaseManager.LOGIN_URL;

public class LoginActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    private EditText usernameField, passwordField;
    private ProgressBar progressBar;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        initLayouts();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername = usernameField.getText().toString();
                String strPassword = passwordField.getText().toString();

                if (strUsername.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.empty_username), Toast.LENGTH_LONG).show();
                } else if (strPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getString(R.string.empty_password), Toast.LENGTH_LONG).show();
                } else if (strPassword.length() < 5) {
                    Toast.makeText(LoginActivity.this, getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
                } else {
                    LoginTask loginTask = new LoginTask(LoginActivity.this);
                    loginTask.execute(strUsername, strPassword);
                    LOGGED_USER = strUsername;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initLayouts() {
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        progressBar = findViewById(R.id.loginProgressBar);
        loginBtn = findViewById(R.id.loginBtn);

        progressBar.setVisibility(View.GONE);
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private Context context;
        private AlertDialog alertDialog;

        public LoginTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Etat de connexion");

            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String user = strings[0], pass = strings[1];

            try {
                URL url = new URL(LOGIN_URL);
                Log.v("LOGIN_URL", url.toString());

                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");
                http.setDoInput(true);
                http.setDoOutput(true);
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, "UTF-8"));

                String data = URLEncoder.encode("user", "UTF-8") + "=" +
                        URLEncoder.encode(user, "UTF-8") + "&&" +
                        URLEncoder.encode("pass", "UTF-8") + "=" +
                        URLEncoder.encode(pass, "UTF-8");

                writer.write(data);
                writer.flush();
                writer.close();
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, "UTF-8"));
                String ligne = "";
                while ((ligne = reader.readLine()) != null) {
                    result += ligne;
                }
                reader.close();
                ips.close();
                http.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressBar.setVisibility(View.GONE);

            if (s.contains("server_failed")) {
                displayAlertDialog(getString(R.string.server_failed));
            } else if (s.contains("login_failed")) {
                displayAlertDialog(getString(R.string.login_failed));
            } else if (s.contains("login_success")) {
                sessionManager.setLogin(true);
                displayAlertDialog(getString(R.string.login_success));

                Intent i = new Intent();
                i.setClass(context.getApplicationContext(), MainActivity.class);
                context.startActivity(i);
            } else {
                displayAlertDialog(getString(R.string.time_out));
            }
        }

        private void displayAlertDialog(String string) {
            alertDialog.setMessage(string);
            alertDialog.show();
        }
    }
}