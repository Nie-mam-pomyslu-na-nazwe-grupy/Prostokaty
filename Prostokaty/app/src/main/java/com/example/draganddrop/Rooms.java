package com.example.draganddrop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Rooms extends AppCompatActivity {

    ListView RoomList;
    Button CreateRoom;

    List<String> roomsList;

    String playerName ="";
    String roomName="";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
//przypisuje nazwę gracza do nazwy pokoju
        database=FirebaseDatabase.getInstance();
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        playerName=preferences.getString("playerName","");
        roomName=playerName;

        RoomList=findViewById(R.id.RoomList);
        CreateRoom=findViewById(R.id.CreateRoom);

        //lista istniejących pokoi
        roomsList=new ArrayList<>();
        CreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create room and set yourself as a player1
                CreateRoom.setText("CREATING ROOM");
                CreateRoom.setEnabled(false);
                roomName=playerName;
                roomRef=database.getReference("rooms/" + roomName +"/player1");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });

        RoomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //join existing room and add as player2
                roomName=roomsList.get(position);
                roomRef=database.getReference("rooms/" + roomName +"/player2");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });
        //pokazuje czy są nowe pokoje dostępne
        addRoomsEventListener();


    }
    private void addRoomEventListener() {
        //wczytuje z bazy danych
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //dołacz do pokoju
                CreateRoom.setText("CREATE ROOM");
                CreateRoom.setEnabled(true);
                Intent intent=new Intent(getApplicationContext(),Main2Activity.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error
                CreateRoom.setText("CREATE ROOM");
                CreateRoom.setEnabled(true);
                Toast.makeText(Rooms.this,"Error!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void addRoomsEventListener() {
        roomsRef=database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //pokazuje listę pokoi
                roomsList.clear();
                Iterable<DataSnapshot> rooms=dataSnapshot.getChildren();
                for (DataSnapshot snapshot : rooms) {
                    roomsList.add(snapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Rooms.this, android.R.layout.simple_list_item_1, roomsList);
                    RoomList.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //error-nic się nie dzieje
            }
        });

    }
}



