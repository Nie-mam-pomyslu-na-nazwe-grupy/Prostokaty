package com.example.draganddrop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

    public class DiceRoller extends AppCompatActivity {
        private static final Random RANDOM = new Random();
        private ImageView imageViewDie1, imageViewDie2;

        private Button rollDice;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
//wszystko z numerem 1 to kostka lewa, 2 kostka prawa
            rollDice = (Button) findViewById(R.id.rollDice);
            imageViewDie1 = (ImageView) findViewById(R.id.image_view_die_1);
            imageViewDie2 = (ImageView) findViewById(R.id.image_view_die_2);


            rollDice.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {//na podstawie wylosowanej liczby ustawia obraz kostki
                    int dievalue1 = randomDiceValue();
                    int dievalue2 = randomDiceValue();

                    int res1 = getResources().getIdentifier("dice" + dievalue1, "drawable", "com.example.draganddrop");
                    int res2 = getResources().getIdentifier("dice" + dievalue2, "drawable", "com.example.draganddrop");

                    imageViewDie1.setImageResource(res1);
                    imageViewDie2.setImageResource(res2);
                }
            });
        }

        public static int randomDiceValue() {//losuje wartośc kości
            return RANDOM.nextInt(6) + 1;

        }


    }
