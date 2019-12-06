package com.example.safeapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String url = "http://10.218.110.74:5000/users/login?user="+usernameEditText.getText().toString()+"&pwd="+passwordEditText.getText().toString();
                try {
                    String response = new LoginUrl().execute(url).get();
                    if (response == "login_success") {
                        Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(v.getContext(), "User not registered!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                String url = "http://10.218.110.74:5000/users/register?user="+usernameEditText.getText().toString()+"&pwd="+passwordEditText.getText().toString();
                try {
                    String response = new LoginUrl().execute(url).get();
                    if (response == "register_success") {
                        Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(v.getContext(), "Registration Failed!!!", Toast.LENGTH_SHORT).show();
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
