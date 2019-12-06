package com.example.safeapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText source_text = (EditText)findViewById(R.id.source);
        String source_value = source_text.getText().toString();

        EditText destination_text = (EditText)findViewById(R.id.destination);
        String destination_value = destination_text.getText().toString();

        Button get_route = (Button) findViewById(R.id.get_route_btn);
        get_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
