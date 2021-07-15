package com.alisa.projet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

public class DeleteRoomActivity extends AppCompatActivity {

    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_room);

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

                            Spinner spinner = findViewById(R.id.spin_room_delete);
                            spinner.setAdapter(adapter);
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
    public void delete(View view){

        Spinner id_room = findViewById(R.id.spin_room_delete);

        Intent i = getIntent();
        String token = i.getStringExtra("token");

        Context that = this;

        Room selectedRoom = (Room)id_room.getSelectedItem();

        AndroidNetworking.post("https://myhouse.enovations.fr/room-delete")
                .addHeaders("Authorization", "Bearer " + token)
                .addBodyParameter("idRoom","" + selectedRoom.getId())
                .build()
                .getAsOkHttpResponse(new OkHttpResponseListener() {
                    @Override
                    public void onResponse(Response response) {
                        String message = "";
                        switch(response.code()){
                            case 200:
                                message = " requête correcte :-)";
                                Intent i = new Intent(that, piecesActivity.class);
                                i.putExtra("token",token);
                                startActivity(i);
                                break;
                            case 400:
                                message = "requête incorrecte";
                                break;
                            case 401:
                                message = " Accès non autorisé";
                                break;
                            default:
                                try {
                                    throw new IllegalAccessException("Unexpected value: " + response.code());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                        }

                        Toast toast = Toast.makeText(that,message,Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });

    }
}