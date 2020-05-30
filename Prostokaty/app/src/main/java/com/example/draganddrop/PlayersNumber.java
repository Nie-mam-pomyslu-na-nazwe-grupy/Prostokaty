package com.example.draganddrop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PlayersNumber extends AppCompatActivity {

    private ImageButton graczy2;
    private ImageButton graczy3;
    private ImageButton graczy4;
    int numberOfPlayers;
    public static final String PLAYERS ="com.example.draganddrop.PLAYERS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_number);

        graczy2=(ImageButton) findViewById(R.id.graczy2);
        graczy3=(ImageButton) findViewById(R.id.graczy3);
        graczy4=(ImageButton) findViewById(R.id.graczy4);

        graczy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfPlayers=2;
                openAcitivity(numberOfPlayers);
            }
        });
        graczy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfPlayers=3; openAcitivity(numberOfPlayers);
            }
        });
        graczy4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfPlayers=4; openAcitivity(numberOfPlayers);
            }
        });


    }

    public void openAcitivity(int number) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(PLAYERS,numberOfPlayers);
        startActivity(intent);
    }
}
