package com.example.miniprojet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miniprojet.MainActivity;
import com.example.miniprojet.R;

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

public class LoginActivity extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private ProgressBar progressBar;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished){}

            @Override
            public void onFinish(){
                LoginActivity.this.setContentView(R.layout.activity_login);

                initLayouts();

                loginBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strUsername = usernameField.getText().toString();
                        String strPassword = passwordField.getText().toString();

                        if (strUsername.isEmpty()) {
                            String loginUserError = getString(R.string.empty_username);
                            Toast.makeText(getApplicationContext(), loginUserError, Toast.LENGTH_LONG).show();
                        } else if (strPassword.isEmpty()) {
                            Toast.makeText(getApplicationContext(), getString(R.string.empty_password), Toast.LENGTH_LONG).show();
                        } else if (strPassword.length() < 5) {
                            Toast.makeText(getApplicationContext(), getString(R.string.invalid_password), Toast.LENGTH_LONG).show();
                        } else {
                            LoginTask loginTask = new LoginTask(LoginActivity.this);
                            loginTask.execute(strUsername, strPassword);
                        }
                    }
                });
            }
        }.start();
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
            String dbURL = "http://192.168.1.3/ExpertMaintenance/login.php";

            try {
                URL url = new URL(dbURL);
                Log.v("dbURL", url.toString());
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
            }
            else if (s.contains("login_success")) {
                displayAlertDialog(getString(R.string.login_success));

                Intent i = new Intent();
                i.setClass(context.getApplicationContext(), MainActivity.class);
                context.startActivity(i);
            }
        }

        private void displayAlertDialog(String string) {
            alertDialog.setMessage(string);
            alertDialog.show();
        }
    }
}