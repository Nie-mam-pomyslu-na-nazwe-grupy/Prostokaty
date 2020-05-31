package com.example.draganddrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class StartMenu extends AppCompatActivity {
private ImageButton graLokalna;
private ImageButton graOnline;
private ImageButton instrukcje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        graLokalna=(ImageButton) findViewById(R.id.graLokalna);
        graOnline=(ImageButton) findViewById(R.id.graOnline);
        instrukcje=(ImageButton) findViewById(R.id.instrukcje);
        graLokalna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayersNumber();
            }
        });

        graOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        instrukcje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstrukcje();
            }
        });

    }

    private void openInstrukcje() {
        Intent intent = new Intent(this, Instrukcje.class);
        startActivity(intent);
    }

    private void openLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    private void openPlayersNumber() {
        Intent intent = new Intent(this, PlayersNumber.class);
        startActivity(intent);
    }


}
