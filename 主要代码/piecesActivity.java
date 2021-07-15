package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class piecesActivity extends AppCompatActivity {

    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pieces);

        Intent i = getIntent();
        token = i.getStringExtra("token");

        loadpieces();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        loadpieces();
    }

    public  void  loadpieces(){

        Context that = this;
        AndroidNetworking.get("https://myhouse.enovations.fr/rooms")
                .addHeaders("Authorization", "Bearer " + token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray rooms = response.getJSONArray("rooms");

                            ArrayList<Room> roomList = new ArrayList<>();

                            for(int iRoom = 0; iRoom < rooms.length(); ++iRoom)
                            {

                                final JSONObject room = rooms.getJSONObject(iRoom);

                                roomList.add(new Room(
                                        room.getInt("id"),
                                        room.getString("name"),
                                        room.getString("picture")
                                ));
                            }

                            RoomAdapter adapter = new RoomAdapter(
                                    that,
                                    R.layout.activity_room,
                                    R.id.picture_lst_text_room,
                                    roomList
                            );
                            ListView listView = findViewById(R.id.lstNewRoom);
                            listView.setAdapter(adapter);
                            //Spinner spinner = findViewById(R.id.spin_room1);
                            //spinner.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                {
                                    Room room_info = (Room)listView.getItemAtPosition(position);
                                    //String text = listView.getItemAtPosition(position).toString();
                                    String room_name=room_info.getName();
                                    int room_id =room_info.getId();
                                    //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                    Intent i = getIntent();
                                    token = i.getStringExtra("token");
                                    Intent intent = new Intent(that,intheroomActivity.class);
                                    intent.putExtra("token", token);
                                    intent.putExtra("nameRoom",room_name);
                                    intent.putExtra("idRoom",""+room_id);
                                    startActivity(intent);

                                }

                            });
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast toast = Toast.makeText(that,anError.getMessage(),Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

    }
    public void add(View view){
        Intent i = new Intent(this,AddRoomActivity.class);
        i.putExtra("token", token);
        startActivity(i);
    }
    public void delete(View view){
        Intent i = new Intent(this,DeleteRoomActivity.class);
        i.putExtra("token", token);
        startActivity(i);
    }

}