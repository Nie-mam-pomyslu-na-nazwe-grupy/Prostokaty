package com.example.draganddrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText editText;
    Button login;

    String playerName="";
    FirebaseDatabase database;
    DatabaseReference playerRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText= findViewById(R.id.editText);
        login= findViewById(R.id.login);

        database = FirebaseDatabase.getInstance();

        //sprawdza czy gracz istnieje
        SharedPreferences preferences=getSharedPreferences("PREFS",0);
        playerName=preferences.getString("playerName","");
        if(!playerName.equals("")){
            playerRef=database.getReference("players/" + playerName);
            addEventListener();
            playerRef.setValue("");
        }

        //logowanie gracza
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerName=editText.getText().toString();
                editText.setText("");
                if(!playerName.equals("")){
                    login.setText("LOGGING IN");
                    login.setEnabled(false);
                    playerRef=database.getReference("players/" + playerName);
                    addEventListener();
                    playerRef.setValue("");
                }
            }
        });
    }

    private void addEventListener() {
        //wczytuje z bazy danych
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //w przypadku sukcesu zapisuje imię gracza i przenosi do nowego ekranu
                if(!playerName.equals("")) {
                    SharedPreferences preferences=getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor= preferences.edit();
                    editor.putString("playerName", playerName);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), Rooms.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //w przeciwnym razie wyświetla error
                login.setText("LOG IN");
                login.setEnabled(true);
                Toast.makeText(Login.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

