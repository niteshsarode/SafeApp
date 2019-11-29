package com.example.safeapp.data.model;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safeapp.R;


import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText source_text = (EditText)findViewById(R.id.source);
        String source_value = source_text.getText().toString();

        EditText destination_text = (EditText)findViewById(R.id.destination);
        String destination_value = destination_text.getText().toString();

    }
}
