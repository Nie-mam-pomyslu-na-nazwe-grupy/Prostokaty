package com.example.dice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final Random RANDOM=new Random();
    private ImageView imageViewDie1,imageViewDie2;
    private Button RollDice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewDie1 = findViewById(R.id.image_view_die1);
        imageViewDie2 = findViewById(R.id.image_view_die2);
        RollDice=findViewById(R.id.roll);

        RollDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value1 = randomDiceValue();
                int value2 = randomDiceValue();

                int res1 = getResources().getIdentifier("dice" + value1, "drawable", "com.example.dice");
                int res2 = getResources().getIdentifier("dice" + value2, "drawable", "com.example.dice");

                imageViewDie1.setImageResource(res1);
                imageViewDie2.setImageResource(res2);
                }

            });
        }

    private static int randomDiceValue() {
        return RANDOM.nextInt(6)+1;
    }
    }


