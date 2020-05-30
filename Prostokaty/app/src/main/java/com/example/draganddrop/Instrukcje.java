package com.example.draganddrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Instrukcje extends AppCompatActivity {

    ImageButton returnButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrukcje);
        returnButton=(ImageButton) findViewById(R.id.returnButton);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAcitivity();
            }
        });


    }

    private void openAcitivity() {
        Intent intent = new Intent(this, StartMenu.class);
        startActivity(intent);
    }
}
